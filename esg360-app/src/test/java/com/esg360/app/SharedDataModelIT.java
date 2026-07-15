package com.esg360.app;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;
import javax.sql.DataSource;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

/**
 * The item 0.2 acceptance gate: boot the app against a real PostgreSQL 16, let Flyway apply the
 * migration baseline, then prove the tenancy/audit invariants that everything else depends on
 * (ADR-0003, ADR-0007). This is the SEC-001 seed suite — it reruns on every merge.
 *
 * <p>Flyway/Spring connect as the container superuser (the owner/migration role). Runtime access
 * is exercised by SET ROLE esg360_app, which mirrors how the deployed app connects: no superuser,
 * no BYPASSRLS, subject to RLS.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Testcontainers
class SharedDataModelIT {

    private static final UUID SYSTEM = new UUID(0L, 0L);

    @Container
    private static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>("postgres:16-alpine");

    @DynamicPropertySource
    static void datasource(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRES::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRES::getUsername);
        registry.add("spring.datasource.password", POSTGRES::getPassword);
    }

    @Autowired
    private DataSource dataSource;

    @Test
    void migrationBaselineApplied() throws SQLException {
        try (Connection c = dataSource.getConnection();
                Statement s = c.createStatement();
                ResultSet rs = s.executeQuery(
                        "select count(*) from flyway_schema_history where success and version is not null")) {
            rs.next();
            assertThat(rs.getInt(1)).isGreaterThanOrEqualTo(4);
        }
    }

    @Test
    void appRoleHasNoBypassRlsAndIsNotSuperuser() throws SQLException {
        try (Connection c = dataSource.getConnection();
                Statement s = c.createStatement();
                ResultSet rs =
                        s.executeQuery("select rolsuper, rolbypassrls from pg_roles where rolname = 'esg360_app'")) {
            assertThat(rs.next()).isTrue();
            assertThat(rs.getBoolean("rolsuper"))
                    .as("esg360_app must not be superuser")
                    .isFalse();
            assertThat(rs.getBoolean("rolbypassrls"))
                    .as("esg360_app must not have BYPASSRLS (ADR-0003)")
                    .isFalse();
        }
    }

    @Test
    void allBusinessTablesForceRowLevelSecurity() throws SQLException {
        String[][] relations = {
            {"shared", "organisation_node"},
            {"shared", "reporting_period"},
            {"shared", "unit"},
            {"shared", "metric_definition"},
            {"audit", "audit_event"},
            {"messaging", "outbox"},
        };
        try (Connection c = dataSource.getConnection()) {
            for (String[] rel : relations) {
                assertThat(forceRls(c, rel[0], rel[1]))
                        .as("FORCE ROW LEVEL SECURITY on %s.%s", rel[0], rel[1])
                        .isTrue();
            }
        }
    }

    @Test
    void tenantsCannotSeeEachOthersRows() throws SQLException {
        UUID tenantA = createTenant("Tenant A");
        UUID tenantB = createTenant("Tenant B");

        UUID nodeA = asApp(tenantA, c -> insertOrgNode(c, tenantA, "Site A"));
        asApp(tenantB, c -> insertOrgNode(c, tenantB, "Site B"));

        // In tenant A's context, only A's row is visible...
        long visibleToA = asApp(tenantA, SharedDataModelIT::countOrgNodes);
        assertThat(visibleToA).isEqualTo(1L);

        // ...and A's own row is the one it sees.
        boolean seesOwnNode = asApp(tenantA, c -> orgNodeExists(c, nodeA));
        assertThat(seesOwnNode).isTrue();

        long visibleToB = asApp(tenantB, SharedDataModelIT::countOrgNodes);
        assertThat(visibleToB).isEqualTo(1L);
    }

    @Test
    void withoutTenantContextNoRowsAreVisible() throws SQLException {
        UUID tenant = createTenant("Context-less");
        asApp(tenant, c -> insertOrgNode(c, tenant, "Node"));

        try (Connection c = dataSource.getConnection()) {
            c.setAutoCommit(false);
            try {
                exec(c, "set role esg360_app");
                // no set_config('app.tenant_id', ...) — policy sees NULL, denies all rows
                assertThat(countOrgNodes(c)).isZero();
            } finally {
                cleanup(c);
            }
        }
    }

    @Test
    void crossTenantParentReferenceIsStructurallyImpossible() throws SQLException {
        UUID tenantA = createTenant("Tenant A cross");
        UUID tenantB = createTenant("Tenant B cross");
        UUID nodeB = asApp(tenantB, c -> insertOrgNode(c, tenantB, "B root"));

        // Insert into tenant A a child whose parent belongs to tenant B: the composite FK
        // (tenant_id, parent_id) -> (tenant_id, id) has no matching (A, nodeB) row.
        assertThatThrownBy(() -> asApp(tenantA, c -> {
                    try (PreparedStatement ps = c.prepareStatement(
                            """
                            insert into shared.organisation_node
                              (tenant_id, node_type, name, parent_id, effective_from, created_by, updated_by)
                            values (?, 'site', 'orphan', ?, current_date, ?, ?)
                            """)) {
                        ps.setObject(1, tenantA);
                        ps.setObject(2, nodeB);
                        ps.setObject(3, SYSTEM);
                        ps.setObject(4, SYSTEM);
                        ps.executeUpdate();
                    }
                    return null;
                }))
                .isInstanceOf(SQLException.class)
                .satisfies(e -> assertThat(((SQLException) e).getSQLState()).isEqualTo("23503"));
    }

    @Test
    void auditEventIsAppendOnlyForTheAppRole() throws SQLException {
        UUID tenant = createTenant("Audit tenant");

        // insert is allowed
        asApp(tenant, c -> {
            try (PreparedStatement ps = c.prepareStatement(
                    """
                    insert into audit.audit_event (tenant_id, action, object_type, object_id, result)
                    values (?, 'CREATE', 'organisation_node', ?, 'SUCCESS')
                    """)) {
                ps.setObject(1, tenant);
                ps.setObject(2, UUID.randomUUID());
                ps.executeUpdate();
            }
            return null;
        });

        // update and delete are denied at the privilege layer (42501), not by app code
        assertThatThrownBy(() -> asApp(tenant, c -> {
                    exec(c, "update audit.audit_event set reason = 'tamper'");
                    return null;
                }))
                .isInstanceOf(SQLException.class)
                .satisfies(e -> assertThat(((SQLException) e).getSQLState()).isEqualTo("42501"));

        assertThatThrownBy(() -> asApp(tenant, c -> {
                    exec(c, "delete from audit.audit_event");
                    return null;
                }))
                .isInstanceOf(SQLException.class)
                .satisfies(e -> assertThat(((SQLException) e).getSQLState()).isEqualTo("42501"));
    }

    @Test
    void partitionsCannotBeReadDirectly() throws SQLException {
        UUID tenant = createTenant("Partition-direct");
        // The RLS policy lives on the parent; access must flow through it. Leaf partitions
        // carry no grant, so naming them directly is denied (42501) — closing the
        // direct-partition cross-tenant read hole rather than relying on leaf policies alone.
        assertThatThrownBy(() -> asApp(tenant, c -> {
                    exec(c, "select count(*) from audit.audit_event_default");
                    return null;
                }))
                .isInstanceOf(SQLException.class)
                .satisfies(e -> assertThat(((SQLException) e).getSQLState()).isEqualTo("42501"));

        assertThatThrownBy(() -> asApp(tenant, c -> {
                    exec(c, "select count(*) from messaging.outbox_default");
                    return null;
                }))
                .isInstanceOf(SQLException.class)
                .satisfies(e -> assertThat(((SQLException) e).getSQLState()).isEqualTo("42501"));
    }

    @Test
    void cannotWriteRowTaggedWithAnotherTenant() throws SQLException {
        UUID tenantA = createTenant("Write-guard A");
        UUID tenantB = createTenant("Write-guard B");
        // In tenant A's context, tag a new row tenant_id = B: the RLS WITH CHECK denies it (42501).
        assertThatThrownBy(() -> asApp(tenantA, c -> insertOrgNode(c, tenantB, "smuggled")))
                .isInstanceOf(SQLException.class)
                .satisfies(e -> assertThat(((SQLException) e).getSQLState()).isEqualTo("42501"));
    }

    // --- helpers ---------------------------------------------------------------

    private static boolean forceRls(Connection c, String schema, String table) throws SQLException {
        try (PreparedStatement ps = c.prepareStatement(
                "select relforcerowsecurity from pg_class join pg_namespace on relnamespace = pg_namespace.oid "
                        + "where nspname = ? and relname = ?")) {
            ps.setString(1, schema);
            ps.setString(2, table);
            try (ResultSet rs = ps.executeQuery()) {
                assertThat(rs.next()).as("relation %s.%s exists", schema, table).isTrue();
                return rs.getBoolean(1);
            }
        }
    }

    @FunctionalInterface
    private interface AppWork<T> {
        T run(Connection connection) throws SQLException;
    }

    /** Runs work as the runtime role with a per-transaction tenant context (SET LOCAL). */
    private <T> T asApp(UUID tenantId, AppWork<T> work) throws SQLException {
        try (Connection c = dataSource.getConnection()) {
            c.setAutoCommit(false);
            boolean committed = false;
            try {
                exec(c, "set role esg360_app");
                try (PreparedStatement ps = c.prepareStatement("select set_config('app.tenant_id', ?, true)")) {
                    ps.setString(1, tenantId.toString());
                    ps.execute();
                }
                T result = work.run(c);
                c.commit();
                committed = true;
                return result;
            } finally {
                if (!committed) {
                    safeRollback(c);
                }
                cleanup(c);
            }
        }
    }

    private UUID createTenant(String name) throws SQLException {
        try (Connection c = dataSource.getConnection();
                PreparedStatement ps = c.prepareStatement(
                        """
                        insert into shared.tenant (name, status, created_by, updated_by)
                        values (?, 'active', ?, ?) returning id
                        """)) {
            ps.setString(1, name);
            ps.setObject(2, SYSTEM);
            ps.setObject(3, SYSTEM);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getObject(1, UUID.class);
            }
        }
    }

    private UUID insertOrgNode(Connection c, UUID tenantId, String name) throws SQLException {
        try (PreparedStatement ps = c.prepareStatement(
                """
                insert into shared.organisation_node
                  (tenant_id, node_type, name, effective_from, created_by, updated_by)
                values (?, 'site', ?, current_date, ?, ?) returning id
                """)) {
            ps.setObject(1, tenantId);
            ps.setString(2, name);
            ps.setObject(3, SYSTEM);
            ps.setObject(4, SYSTEM);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getObject(1, UUID.class);
            }
        }
    }

    private static long countOrgNodes(Connection c) throws SQLException {
        try (Statement s = c.createStatement();
                ResultSet rs = s.executeQuery("select count(*) from shared.organisation_node")) {
            rs.next();
            return rs.getLong(1);
        }
    }

    private static boolean orgNodeExists(Connection c, UUID id) throws SQLException {
        try (PreparedStatement ps = c.prepareStatement("select 1 from shared.organisation_node where id = ?")) {
            ps.setObject(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    private static void exec(Connection c, String sql) throws SQLException {
        try (Statement s = c.createStatement()) {
            s.execute(sql);
        }
    }

    private static void safeRollback(Connection c) {
        try {
            c.rollback();
        } catch (SQLException ignored) {
            // connection is being discarded anyway
        }
    }

    /** Reset the session role so the pooled physical connection returns clean. */
    private static void cleanup(Connection c) {
        try {
            exec(c, "reset role");
            c.setAutoCommit(true);
        } catch (SQLException ignored) {
            // connection is being discarded anyway
        }
    }
}
