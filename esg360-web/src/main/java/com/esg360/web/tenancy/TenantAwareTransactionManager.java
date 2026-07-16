package com.esg360.web.tenancy;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;
import javax.sql.DataSource;

import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.transaction.TransactionDefinition;

/**
 * Binds {@link TenantContext} to the database session at the start of every transaction, so that
 * PostgreSQL's row-level security policies (ADR-0003) apply to every statement without any query
 * having to remember a {@code tenant_id} predicate.
 *
 * <p>This is the mechanism SEC-001 rests on, and it is deliberately the only place that sets the
 * GUC. Three details are load-bearing:
 *
 * <ul>
 *   <li><strong>Transaction-scoped, not session-scoped.</strong> {@code set_config(..., true)} is
 *       the callable form of {@code SET LOCAL}; the value dies with the transaction. ADR-0003 is
 *       explicit that a plain session {@code SET} is forbidden — under PgBouncer transaction
 *       pooling the connection returns to the pool still carrying the last tenant, and the next
 *       borrower reads as them. That is the cross-tenant leak, delivered by the pooler.
 *   <li><strong>Bound as a parameter.</strong> {@code SET LOCAL} takes no bind parameters, which is
 *       exactly why naive implementations interpolate the tenant into the SQL string.
 *       {@code set_config()} is an ordinary function call and takes one (SEC-900).
 *   <li><strong>Fails closed.</strong> No context binds the empty string, and the policy's {@code
 *       nullif(current_setting('app.tenant_id', true), '')::uuid} makes that NULL, so
 *       {@code tenant_id = NULL} is never true and the transaction sees nothing. A lost context
 *       therefore denies rather than exposes.
 * </ul>
 *
 * <p>The hook is {@code prepareTransactionalConnection}, which Spring calls inside {@code doBegin}
 * after {@code setAutoCommit(false)} — i.e. already within the transaction the setting must be
 * local to. Extending {@link JdbcTransactionManager} rather than {@code DataSourceTransactionManager}
 * keeps Boot's SQL exception translation.
 */
public class TenantAwareTransactionManager extends JdbcTransactionManager {

    private static final String BIND_TENANT = "select set_config('app.tenant_id', ?, true)";

    public TenantAwareTransactionManager(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    protected void prepareTransactionalConnection(Connection con, TransactionDefinition definition)
            throws SQLException {
        super.prepareTransactionalConnection(con, definition);
        String tenantId = TenantContext.current().map(UUID::toString).orElse("");
        try (PreparedStatement statement = con.prepareStatement(BIND_TENANT)) {
            statement.setString(1, tenantId);
            statement.execute();
        }
    }
}
