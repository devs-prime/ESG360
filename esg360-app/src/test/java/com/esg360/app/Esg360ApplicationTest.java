package com.esg360.app;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Fast unit-level smoke: the web/application context assembles without a database. Database and
 * migration wiring is exercised by {@link SharedDataModelIT} against a real PostgreSQL container;
 * excluding the datasource/Flyway auto-configuration here keeps this test runnable with {@code
 * -DskipITs} and no Docker.
 */
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        properties = "spring.autoconfigure.exclude="
                + "org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration,"
                + "org.springframework.boot.autoconfigure.jdbc.JdbcTemplateAutoConfiguration,"
                + "org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration,"
                + "org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration")
class Esg360ApplicationTest {

    @Test
    void contextLoads() {
        // failure here means bean wiring is broken; the assertion is the successful startup
    }
}
