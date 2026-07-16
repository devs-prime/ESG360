package com.esg360.app.tenancy;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import javax.sql.DataSource;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.support.TransactionTemplate;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import com.esg360.app.Esg360Application;
import com.esg360.app.testsupport.TestIdentityConfig;
import com.esg360.web.tenancy.TenantAwareTransactionManager;
import com.esg360.web.tenancy.TenantContext;

/**
 * Proves that {@link TenantAwareTransactionManager} — the production mechanism — actually binds the
 * tenant to PostgreSQL's RLS, and that the binding cannot leak (ADR-0003, SEC-001).
 *
 * <p>This is distinct from {@code SharedDataModelIT}, which sets {@code app.tenant_id} by hand and
 * therefore proves the <em>schema's</em> policies work. It says nothing about whether the
 * application sets that value correctly, which is the half an attacker actually meets. Here nothing
 * issues {@code set_config} by hand: the transaction manager does it or the test fails.
 *
 * <p>Two details make this test mean what it claims:
 *
 * <ul>
 *   <li><strong>A non-superuser connection.</strong> Testcontainers' default user is a superuser,
 *       and superusers bypass RLS entirely — even {@code FORCE ROW LEVEL SECURITY}. A test written
 *       over the injected {@link DataSource} would pass while proving nothing. This suite runs a
 *       second pool that enters {@code esg360_app} on connect, mirroring the deployed app.
 *   <li><strong>A pool of exactly one connection.</strong> Every transaction therefore reuses the
 *       same physical connection, which is the condition under which a session-scoped {@code SET}
 *       leaks one tenant's context into the next request. That is the failure ADR-0003 forbids, and
 *       {@link #tenantContextDoesNotSurviveIntoTheNextTransaction()} is what makes its absence a
 *       tested fact rather than a code comment.
 * </ul>
 *
 * <p>Scope: this covers the tenant-context mechanism, which is a <em>prerequisite</em> for SEC-001,
 * not the whole of it. SEC-001 as written in spec 03 is an API-level test ("call tenant B's endpoint
 * with tenant A's token... request is denied and a security event is logged") and needs endpoints
 * and the audit service, neither of which exists yet. It lands when they do.
 */
@SpringBootTest(classes = Esg360Application.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Import(TestIdentityConfig.class)
@Testcontainers
class TenantContextIT {

    private static final UUID SYSTEM = new UUID(0L, 0L);
    private static final String COUNT_NODES = "select count(*) from shared.organisation_node";

    @Container
    private static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>("postgres:16-alpine");

    @DynamicPropertySource
    static void datasource(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRES::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRES::getUsername);
        registry.add("spring.datasource.password", POSTGRES::getPassword);
    }

    /** Superuser/owner: Flyway ran with this. Used only to seed rows past RLS. */
    @Autowired
    private DataSource ownerDataSource;

    private HikariDataSource appDataSource;
    private TransactionTemplate tx;
    private JdbcTemplate jdbc;

    @BeforeEach
    void setUp() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(POSTGRES.getJdbcUrl());
        config.setUsername(POSTGRES.getUsername());
        config.setPassword(POSTGRES.getPassword());
        // Enter the runtime role on connect: no superuser, no BYPASSRLS, so policies bite.
        // Production reaches the same state via a LOGIN role that inherits esg360_app.
        config.setConnectionInitSql("set role esg360_app");
        // Exactly one physical connection, so "reused connection" is guaranteed, not hoped for.
        config.setMaximumPoolSize(1);
        appDataSource = new HikariDataSource(config);
        tx = new TransactionTemplate(new TenantAwareTransactionManager(appDataSource));
        jdbc = new JdbcTemplate(appDataSource);
    }

    @AfterEach
    void tearDown() {
        TenantContext.clear();
        if (appDataSource != null) {
            appDataSource.close();
        }
    }

    @Test
    void bindsTheTenantContextSoOnlyThatTenantsRowsAreVisible() throws SQLException {
        UUID tenantA = createTenant("ctx-A");
        UUID tenantB = createTenant("ctx-B");
        seedNode(tenantA, "A site");
        seedNode(tenantB, "B site 1");
        seedNode(tenantB, "B site 2");

        TenantContext.set(tenantA);
        assertThat(countVisibleNodes())
                .as("tenant A sees exactly its own row, though three exist")
                .isEqualTo(1L);

        TenantContext.set(tenantB);
        assertThat(countVisibleNodes())
                .as("tenant B sees exactly its own two rows")
                .isEqualTo(2L);
    }

    @Test
    void aGuessedIdFromAnotherTenantResolvesToNothing() throws SQLException {
        UUID tenantA = createTenant("bola-A");
        UUID tenantB = createTenant("bola-B");
        UUID nodeB = seedNode(tenantB, "B private");

        // The attacker knows the exact id — the object-level check must not depend on ids being
        // unguessable (SEC-901 seed). RLS answers "no rows", so the API layer above cannot leak
        // existence through a 403-vs-404 distinction it never learns about.
        TenantContext.set(tenantA);
        Long found = tx.execute(status ->
                jdbc.queryForObject("select count(*) from shared.organisation_node where id = ?", Long.class, nodeB));
        assertThat(found).isZero();
    }

    @Test
    void withoutTenantContextATransactionSeesNothing() throws SQLException {
        UUID tenant = createTenant("fail-closed");
        seedNode(tenant, "node");

        // Fail closed: losing the context must deny, never widen. A filter bug, a background
        // thread, a forgotten runAs — all land here, and all must see zero.
        TenantContext.clear();
        assertThat(countVisibleNodes()).isZero();
    }

    @Test
    void tenantContextDoesNotSurviveIntoTheNextTransaction() throws SQLException {
        UUID tenantA = createTenant("leak-A");
        seedNode(tenantA, "A node");

        TenantContext.set(tenantA);
        assertThat(countVisibleNodes()).isEqualTo(1L);

        // Same physical connection (pool size 1), next transaction, no context. A session-scoped
        // SET would still be in force here and this would return 1 — the exact cross-tenant read
        // ADR-0003 cites for PgBouncer transaction pooling. SET LOCAL dies with its transaction.
        TenantContext.clear();
        assertThat(countVisibleNodes())
                .as("app.tenant_id must not outlive the transaction that set it")
                .isZero();
    }

    @Test
    void writingIntoAnotherTenantIsRejected() throws SQLException {
        UUID tenantA = createTenant("write-A");
        UUID tenantB = createTenant("write-B");

        // Acting as A, claim tenant B in the payload. The policy's WITH CHECK refuses it, so the
        // "tenant from the request body" defect cannot be reached even if a controller regressed.
        TenantContext.set(tenantA);
        assertThatThrownBy(() -> tx.execute(status -> jdbc.update(
                        """
                        insert into shared.organisation_node
                          (tenant_id, node_type, name, effective_from, created_by, updated_by)
                        values (?, 'site', 'smuggled', current_date, ?, ?)
                        """,
                        tenantB,
                        SYSTEM,
                        SYSTEM)))
                .isInstanceOf(DataAccessException.class);
    }

    /**
     * Counts organisation nodes visible in one transaction of the tenant-aware manager — i.e. what
     * RLS lets the current {@link TenantContext} see. Runs no {@code set_config} of its own: if the
     * transaction manager stops binding the tenant, these numbers change and the tests fail.
     */
    private long countVisibleNodes() {
        Long count = tx.execute(status -> jdbc.queryForObject(COUNT_NODES, Long.class));
        return count == null ? -1L : count;
    }

    private UUID createTenant(String name) throws SQLException {
        try (Connection c = ownerDataSource.getConnection();
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

    private UUID seedNode(UUID tenantId, String name) throws SQLException {
        try (Connection c = ownerDataSource.getConnection();
                PreparedStatement ps = c.prepareStatement(
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
}
