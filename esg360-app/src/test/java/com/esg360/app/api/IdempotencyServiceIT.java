package com.esg360.app.api;

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

import com.esg360.app.Esg360Application;
import com.esg360.web.error.ConflictException;
import com.esg360.web.idempotency.IdempotencyService;
import com.esg360.web.idempotency.IdempotentOutcome;

/** Idempotency store semantics against real PostgreSQL: replay, payload-change conflict, key independence (COL-002). */
@SpringBootTest(classes = Esg360Application.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Testcontainers
class IdempotencyServiceIT {

    private static final UUID SYSTEM = new UUID(0L, 0L);
    private static final String ENDPOINT = "POST /submissions";

    @Container
    private static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>("postgres:16-alpine");

    @DynamicPropertySource
    static void datasource(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRES::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRES::getUsername);
        registry.add("spring.datasource.password", POSTGRES::getPassword);
    }

    @Autowired
    private IdempotencyService idempotency;

    @Autowired
    private DataSource dataSource;

    @Test
    void replaysStoredResponseAndRejectsPayloadChange() throws SQLException {
        UUID tenant = createTenant("idem-a");

        assertThat(idempotency.begin(tenant, "k1", ENDPOINT, "hashA")).isInstanceOf(IdempotentOutcome.Proceed.class);
        idempotency.complete(tenant, "k1", ENDPOINT, 201, "{\"id\":\"abc\"}");

        IdempotentOutcome replay = idempotency.begin(tenant, "k1", ENDPOINT, "hashA");
        assertThat(replay).isInstanceOf(IdempotentOutcome.Replay.class);
        IdempotentOutcome.Replay stored = (IdempotentOutcome.Replay) replay;
        assertThat(stored.status()).isEqualTo(201);
        assertThat(stored.body()).contains("\"id\":\"abc\"");

        assertThatThrownBy(() -> idempotency.begin(tenant, "k1", ENDPOINT, "hashB"))
                .isInstanceOf(ConflictException.class);
    }

    @Test
    void distinctKeysAndTenantsAreIndependent() throws SQLException {
        UUID tenantA = createTenant("idem-b");
        UUID tenantB = createTenant("idem-c");

        assertThat(idempotency.begin(tenantA, "same-key", ENDPOINT, "h")).isInstanceOf(IdempotentOutcome.Proceed.class);
        // same key under a different tenant is a distinct entry
        assertThat(idempotency.begin(tenantB, "same-key", ENDPOINT, "h")).isInstanceOf(IdempotentOutcome.Proceed.class);
        // a different key under the same tenant is independent too
        assertThat(idempotency.begin(tenantA, "other-key", ENDPOINT, "h"))
                .isInstanceOf(IdempotentOutcome.Proceed.class);
    }

    @Test
    void idempotencyTableIsTenantIsolatedUnderRls() throws SQLException {
        UUID tenant = createTenant("idem-rls");
        // As the non-superuser app role WITH a tenant context, the row is writable and visible...
        asApp(tenant, connection -> {
            try (PreparedStatement ps = connection.prepareStatement(
                    """
                    insert into api.idempotency_key
                      (tenant_id, idem_key, endpoint, request_hash, expires_at)
                    values (?, 'k', 'e', 'h', now() + interval '1 hour')
                    """)) {
                ps.setObject(1, tenant);
                ps.executeUpdate();
            }
            assertThat(countKeys(connection)).isEqualTo(1L);
        });
        // ...but the same role WITHOUT a tenant context sees nothing — which is exactly why the
        // service sets app.tenant_id on every transaction (without it, RLS rejects all access).
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false);
            try {
                exec(connection, "set role esg360_app");
                assertThat(countKeys(connection)).isZero();
            } finally {
                resetRole(connection);
            }
        }
    }

    private void asApp(UUID tenantId, AppWork work) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false);
            boolean committed = false;
            try {
                exec(connection, "set role esg360_app");
                try (PreparedStatement ps =
                        connection.prepareStatement("select set_config('app.tenant_id', ?, true)")) {
                    ps.setString(1, tenantId.toString());
                    ps.execute();
                }
                work.run(connection);
                connection.commit();
                committed = true;
            } finally {
                if (!committed) {
                    try {
                        connection.rollback();
                    } catch (SQLException ignored) {
                        // connection discarded on close
                    }
                }
                resetRole(connection);
            }
        }
    }

    private static long countKeys(Connection connection) throws SQLException {
        try (Statement s = connection.createStatement();
                ResultSet rs = s.executeQuery("select count(*) from api.idempotency_key")) {
            rs.next();
            return rs.getLong(1);
        }
    }

    private static void exec(Connection connection, String sql) throws SQLException {
        try (Statement s = connection.createStatement()) {
            s.execute(sql);
        }
    }

    private static void resetRole(Connection connection) {
        try {
            exec(connection, "reset role");
            connection.setAutoCommit(true);
        } catch (SQLException ignored) {
            // connection discarded on close
        }
    }

    @FunctionalInterface
    private interface AppWork {
        void run(Connection connection) throws SQLException;
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
}
