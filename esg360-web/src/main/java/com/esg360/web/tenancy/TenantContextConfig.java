package com.esg360.web.tenancy;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Replaces Boot's auto-configured transaction manager with the tenant-aware one, so that <em>every
 * transaction in the application</em> — not merely the ones whose author remembered — binds
 * {@code app.tenant_id} for RLS (ADR-0003).
 *
 * <p>Boot's {@code DataSourceTransactionManagerAutoConfiguration} backs off when a
 * {@code TransactionManager} bean already exists, so declaring this one is sufficient. Making this
 * the default rather than an opt-in is the point: an opt-in tenant filter is one forgotten
 * annotation away from a cross-tenant read, and SEC-001 is Critical severity with zero tolerance.
 */
@Configuration
public class TenantContextConfig {

    @Bean
    public TenantAwareTransactionManager transactionManager(DataSource dataSource) {
        return new TenantAwareTransactionManager(dataSource);
    }
}
