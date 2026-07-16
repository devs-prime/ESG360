package com.esg360.app.identity;

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
import org.springframework.context.annotation.Import;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.esg360.app.Esg360Application;
import com.esg360.app.testsupport.TestIdentityConfig;

/**
 * The structural guards of the identity schema (module 03), proven against real PostgreSQL.
 *
 * <p>Everything asserted here is enforced by the database rather than by service code. That is the
 * point: each of these rules — no self-approval of an SoD exception, no self-certification of one's
 * own access, no duplicate grant, no hard-deleted user — is one forgotten call away from being
 * silently unenforced if it lives only in a service. A CHECK constraint cannot be forgotten.
 *
 * <p>These are schema tests. The behaviour on top (evaluating whether a given assignment conflicts,
 * launching a campaign, expiring uncertified items) arrives with the identity service, and SEC-002's
 * acceptance test lands there.
 */
@SpringBootTest(classes = Esg360Application.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Import(TestIdentityConfig.class)
@Testcontainers
class IdentitySchemaIT {

    private static final UUID SYSTEM = new UUID(0L, 0L);
    private static final String CHECK_VIOLATION = "23514";
    private static final String UNIQUE_VIOLATION = "23505";
    private static final String INSUFFICIENT_PRIVILEGE = "42501";

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
    void allIdentityBusinessTablesForceRowLevelSecurity() throws SQLException {
        String[] tables = {
            "app_user",
            "role",
            "role_permission",
            "role_assignment",
            "sod_rule",
            "sod_exception",
            "session_policy",
            "access_review_campaign",
            "access_review_item",
        };
        try (Connection c = dataSource.getConnection()) {
            for (String table : tables) {
                assertThat(forceRls(c, "identity", table))
                        .as("FORCE ROW LEVEL SECURITY on identity.%s", table)
                        .isTrue();
            }
        }
    }

    @Test
    void permissionCatalogueIsGlobalReferenceDataAndReadOnlyToTheApp() throws SQLException {
        // The one identity table without tenant_id, deliberately: permission codes are defined by
        // the code that checks them, so there is no tenant data to isolate. The guard is that
        // tenants cannot invent permissions — no INSERT for the app role.
        UUID tenant = createTenant("perm-tenant");
        assertThatThrownBy(() -> asApp(tenant, c -> {
                    exec(c, "insert into identity.permission (code, description) values ('pwn', 'granted')");
                    return null;
                }))
                .isInstanceOf(SQLException.class)
                .satisfies(e -> assertThat(((SQLException) e).getSQLState()).isEqualTo(INSUFFICIENT_PRIVILEGE));

        long visible = asApp(tenant, c -> {
            try (Statement s = c.createStatement();
                    ResultSet rs = s.executeQuery("select count(*) from identity.permission")) {
                rs.next();
                return rs.getLong(1);
            }
        });
        assertThat(visible).as("catalogue is readable by every tenant").isPositive();
    }

    @Test
    void sodExceptionCannotBeSelfApproved() throws SQLException {
        UUID tenant = createTenant("sod-self");
        UUID preparer = asApp(tenant, c -> insertUser(c, tenant, "preparer@x.test"));
        UUID ruleId = seedConflictRule(tenant);

        // The requester approving their own exception is not an exception, it is a bypass.
        assertThatThrownBy(() -> asApp(tenant, c -> {
                    insertException(c, tenant, ruleId, preparer, preparer, preparer);
                    return null;
                }))
                .isInstanceOf(SQLException.class)
                .satisfies(e -> assertThat(((SQLException) e).getSQLState()).isEqualTo(CHECK_VIOLATION));
    }

    @Test
    void sodExceptionApprovedBySomeoneElseIsAccepted() throws SQLException {
        UUID tenant = createTenant("sod-ok");
        UUID preparer = asApp(tenant, c -> insertUser(c, tenant, "preparer2@x.test"));
        UUID approver = asApp(tenant, c -> insertUser(c, tenant, "approver2@x.test"));
        UUID ruleId = seedConflictRule(tenant);

        // The negative test above only means something if the positive path works.
        UUID exceptionId = asApp(tenant, c -> insertException(c, tenant, ruleId, preparer, preparer, approver));
        assertThat(exceptionId).isNotNull();
    }

    @Test
    void sodRulePairMustBeCanonicallyOrdered() throws SQLException {
        UUID tenant = createTenant("sod-canon");
        UUID roleA = asApp(tenant, c -> insertRole(c, tenant, "preparer"));
        UUID roleB = asApp(tenant, c -> insertRole(c, tenant, "approver"));

        // Reversed pair: without the canonical-order check, {A,B} and {B,A} both insert and the
        // unique constraint stops preventing duplicates — enforcement silently half-disappears.
        // greatest/least are computed by PostgreSQL: Java's UUID.compareTo is signed and does not
        // match PostgreSQL's unsigned byte ordering.
        assertThatThrownBy(() -> asApp(tenant, c -> {
                    try (PreparedStatement ps = c.prepareStatement(
                            """
                            insert into identity.sod_rule
                              (tenant_id, role_a_id, role_b_id, conflict_type, description, created_by, updated_by)
                            values (?, greatest(?::uuid, ?::uuid), least(?::uuid, ?::uuid),
                                    'submitter_approver', 'reversed', ?, ?)
                            """)) {
                        ps.setObject(1, tenant);
                        ps.setObject(2, roleA);
                        ps.setObject(3, roleB);
                        ps.setObject(4, roleA);
                        ps.setObject(5, roleB);
                        ps.setObject(6, SYSTEM);
                        ps.setObject(7, SYSTEM);
                        ps.executeUpdate();
                    }
                    return null;
                }))
                .isInstanceOf(SQLException.class)
                .satisfies(e -> assertThat(((SQLException) e).getSQLState()).isEqualTo(CHECK_VIOLATION));
    }

    @Test
    void duplicateTenantWideRoleAssignmentIsRejected() throws SQLException {
        UUID tenant = createTenant("dup-grant");
        UUID user = asApp(tenant, c -> insertUser(c, tenant, "dup@x.test"));
        UUID role = asApp(tenant, c -> insertRole(c, tenant, "viewer"));

        asApp(tenant, c -> insertAssignment(c, tenant, user, role));

        // Both rows have scope_node_id NULL. Under PostgreSQL's default NULL handling those are
        // "distinct" and this second insert would succeed — UNIQUE ... NULLS NOT DISTINCT is what
        // makes the duplicate impossible.
        assertThatThrownBy(() -> asApp(tenant, c -> insertAssignment(c, tenant, user, role)))
                .isInstanceOf(SQLException.class)
                .satisfies(e -> assertThat(((SQLException) e).getSQLState()).isEqualTo(UNIQUE_VIOLATION));
    }

    @Test
    void accessReviewItemCannotBeSelfCertified() throws SQLException {
        UUID tenant = createTenant("review-self");
        UUID subject = asApp(tenant, c -> insertUser(c, tenant, "subject@x.test"));
        UUID role = asApp(tenant, c -> insertRole(c, tenant, "reviewed-role"));
        UUID assignment = asApp(tenant, c -> insertAssignment(c, tenant, subject, role));
        UUID campaign = asApp(tenant, c -> insertCampaign(c, tenant, "Q3 review"));

        // Reviewing your own access is not a review (req 3-010).
        assertThatThrownBy(() -> asApp(tenant, c -> {
                    insertReviewItem(c, tenant, campaign, assignment, subject, subject);
                    return null;
                }))
                .isInstanceOf(SQLException.class)
                .satisfies(e -> assertThat(((SQLException) e).getSQLState()).isEqualTo(CHECK_VIOLATION));
    }

    @Test
    void identityRowsAreIsolatedBetweenTenants() throws SQLException {
        UUID tenantA = createTenant("iso-A");
        UUID tenantB = createTenant("iso-B");
        asApp(tenantA, c -> insertUser(c, tenantA, "a@x.test"));
        asApp(tenantB, c -> insertUser(c, tenantB, "b1@x.test"));
        asApp(tenantB, c -> insertUser(c, tenantB, "b2@x.test"));

        assertThat(asApp(tenantA, IdentitySchemaIT::countUsers)).isEqualTo(1L);
        assertThat(asApp(tenantB, IdentitySchemaIT::countUsers)).isEqualTo(2L);
    }

    @Test
    void usersAreDeactivatedNeverDeleted() throws SQLException {
        UUID tenant = createTenant("no-delete");
        asApp(tenant, c -> insertUser(c, tenant, "keeper@x.test"));

        // "Deleted user accounts retain attribution on historical records" (spec 03). The app role
        // simply has no DELETE, so the rule holds even against application code that tries.
        assertThatThrownBy(() -> asApp(tenant, c -> {
                    exec(c, "delete from identity.app_user");
                    return null;
                }))
                .isInstanceOf(SQLException.class)
                .satisfies(e -> assertThat(((SQLException) e).getSQLState()).isEqualTo(INSUFFICIENT_PRIVILEGE));

        long remaining = asApp(tenant, IdentitySchemaIT::countUsers);
        assertThat(remaining).isEqualTo(1L);
    }

    @Test
    void emailIsUniquePerTenantCaseInsensitively() throws SQLException {
        UUID tenant = createTenant("email-ci");
        asApp(tenant, c -> insertUser(c, tenant, "Ada@x.test"));

        // Case-varying duplicates would collide at login-time matching, so the index is on lower(email).
        assertThatThrownBy(() -> asApp(tenant, c -> insertUser(c, tenant, "ada@x.test")))
                .isInstanceOf(SQLException.class)
                .satisfies(e -> assertThat(((SQLException) e).getSQLState()).isEqualTo(UNIQUE_VIOLATION));
    }

    // --- helpers ---------------------------------------------------------------

    /** Seeds two roles plus the canonical conflict rule between them. */
    private UUID seedConflictRule(UUID tenant) throws SQLException {
        UUID roleA = asApp(tenant, c -> insertRole(c, tenant, "preparer-" + UUID.randomUUID()));
        UUID roleB = asApp(tenant, c -> insertRole(c, tenant, "approver-" + UUID.randomUUID()));
        return asApp(tenant, c -> {
            try (PreparedStatement ps = c.prepareStatement(
                    """
                    insert into identity.sod_rule
                      (tenant_id, role_a_id, role_b_id, conflict_type, description, created_by, updated_by)
                    values (?, least(?::uuid, ?::uuid), greatest(?::uuid, ?::uuid),
                            'submitter_approver', 'preparer cannot approve own submission', ?, ?)
                    returning id
                    """)) {
                ps.setObject(1, tenant);
                ps.setObject(2, roleA);
                ps.setObject(3, roleB);
                ps.setObject(4, roleA);
                ps.setObject(5, roleB);
                ps.setObject(6, SYSTEM);
                ps.setObject(7, SYSTEM);
                try (ResultSet rs = ps.executeQuery()) {
                    rs.next();
                    return rs.getObject(1, UUID.class);
                }
            }
        });
    }

    private static long countUsers(Connection c) throws SQLException {
        try (Statement s = c.createStatement();
                ResultSet rs = s.executeQuery("select count(*) from identity.app_user")) {
            rs.next();
            return rs.getLong(1);
        }
    }

    private static UUID insertUser(Connection c, UUID tenant, String email) throws SQLException {
        try (PreparedStatement ps = c.prepareStatement(
                """
                insert into identity.app_user
                  (tenant_id, issuer, external_subject, email, display_name, created_by, updated_by)
                values (?, 'https://idp.test', ?, ?, ?, ?, ?) returning id
                """)) {
            ps.setObject(1, tenant);
            ps.setString(2, UUID.randomUUID().toString());
            ps.setString(3, email);
            ps.setString(4, email);
            ps.setObject(5, SYSTEM);
            ps.setObject(6, SYSTEM);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getObject(1, UUID.class);
            }
        }
    }

    private static UUID insertRole(Connection c, UUID tenant, String code) throws SQLException {
        try (PreparedStatement ps = c.prepareStatement(
                """
                insert into identity.role (tenant_id, code, name, created_by, updated_by)
                values (?, ?, ?, ?, ?) returning id
                """)) {
            ps.setObject(1, tenant);
            ps.setString(2, code);
            ps.setString(3, code);
            ps.setObject(4, SYSTEM);
            ps.setObject(5, SYSTEM);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getObject(1, UUID.class);
            }
        }
    }

    private static UUID insertAssignment(Connection c, UUID tenant, UUID user, UUID role) throws SQLException {
        try (PreparedStatement ps = c.prepareStatement(
                """
                insert into identity.role_assignment
                  (tenant_id, user_id, role_id, effective_from, created_by, updated_by)
                values (?, ?, ?, current_date, ?, ?) returning id
                """)) {
            ps.setObject(1, tenant);
            ps.setObject(2, user);
            ps.setObject(3, role);
            ps.setObject(4, SYSTEM);
            ps.setObject(5, SYSTEM);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getObject(1, UUID.class);
            }
        }
    }

    private static UUID insertException(
            Connection c, UUID tenant, UUID ruleId, UUID user, UUID requestedBy, UUID approvedBy) throws SQLException {
        try (PreparedStatement ps = c.prepareStatement(
                """
                insert into identity.sod_exception
                  (tenant_id, sod_rule_id, user_id, reason, status, requested_by, approved_by, approved_at,
                   created_by, updated_by)
                values (?, ?, ?, 'documented business need', 'approved', ?, ?, now(), ?, ?) returning id
                """)) {
            ps.setObject(1, tenant);
            ps.setObject(2, ruleId);
            ps.setObject(3, user);
            ps.setObject(4, requestedBy);
            ps.setObject(5, approvedBy);
            ps.setObject(6, SYSTEM);
            ps.setObject(7, SYSTEM);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getObject(1, UUID.class);
            }
        }
    }

    private static UUID insertCampaign(Connection c, UUID tenant, String name) throws SQLException {
        try (PreparedStatement ps = c.prepareStatement(
                """
                insert into identity.access_review_campaign (tenant_id, name, created_by, updated_by)
                values (?, ?, ?, ?) returning id
                """)) {
            ps.setObject(1, tenant);
            ps.setString(2, name);
            ps.setObject(3, SYSTEM);
            ps.setObject(4, SYSTEM);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getObject(1, UUID.class);
            }
        }
    }

    private static UUID insertReviewItem(
            Connection c, UUID tenant, UUID campaign, UUID assignment, UUID subject, UUID reviewer)
            throws SQLException {
        try (PreparedStatement ps = c.prepareStatement(
                """
                insert into identity.access_review_item
                  (tenant_id, campaign_id, role_assignment_id, subject_user_id, reviewer_user_id,
                   created_by, updated_by)
                values (?, ?, ?, ?, ?, ?, ?) returning id
                """)) {
            ps.setObject(1, tenant);
            ps.setObject(2, campaign);
            ps.setObject(3, assignment);
            ps.setObject(4, subject);
            ps.setObject(5, reviewer);
            ps.setObject(6, SYSTEM);
            ps.setObject(7, SYSTEM);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getObject(1, UUID.class);
            }
        }
    }

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

    /** Runs work as the runtime role with a per-transaction tenant context, mirroring production. */
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
                        "insert into shared.tenant (name, created_by, updated_by) values (?, ?, ?) returning id")) {
            ps.setString(1, name);
            ps.setObject(2, SYSTEM);
            ps.setObject(3, SYSTEM);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getObject(1, UUID.class);
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
            // the assertion under test owns the failure; rollback noise must not mask it
        }
    }

    private static void cleanup(Connection c) {
        try {
            c.rollback();
            exec(c, "reset role");
            c.setAutoCommit(true);
        } catch (SQLException ignored) {
            // connection is discarded by the pool either way
        }
    }
}
