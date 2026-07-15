package com.esg360.app;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.DriverManager;

import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

/**
 * Proves the test infrastructure this architecture depends on: integration tests run against a
 * real PostgreSQL 16, never H2/in-memory (ADR-0017 — RLS cannot be tested in-memory).
 *
 * <p>Runs in `mvn verify` via failsafe; requires Docker. Skip locally with {@code -DskipITs}.
 */
@Testcontainers
class PostgresContainerIT {

    @Container
    private static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>("postgres:16-alpine");

    @Test
    void realPostgres16IsAvailableForTests() throws Exception {
        try (var connection = DriverManager.getConnection(
                        POSTGRES.getJdbcUrl(), POSTGRES.getUsername(), POSTGRES.getPassword());
                var statement = connection.createStatement();
                var resultSet = statement.executeQuery("select current_setting('server_version_num')::int")) {
            assertTrue(resultSet.next());
            int versionNum = resultSet.getInt(1);
            assertTrue(versionNum >= 160000, "PostgreSQL 16+ required (ADR-0002), got " + versionNum);
        }
    }

    @Test
    void numericArithmeticIsExact() throws Exception {
        // the database-side counterpart of QuantityTest.addsExactly (BR-002)
        try (var connection = DriverManager.getConnection(
                        POSTGRES.getJdbcUrl(), POSTGRES.getUsername(), POSTGRES.getPassword());
                var statement = connection.createStatement();
                var resultSet = statement.executeQuery("select 0.1::numeric + 0.2::numeric")) {
            assertTrue(resultSet.next());
            assertEquals(0, resultSet.getBigDecimal(1).compareTo(new java.math.BigDecimal("0.3")));
        }
    }
}
