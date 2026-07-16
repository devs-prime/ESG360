package com.esg360.web.tenancy;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

/**
 * The tenant the current thread is acting for (ADR-0003, SEC-001, BR SEC-001).
 *
 * <p>This is the single source of the tenant identifier for the data layer.
 * {@link TenantAwareTransactionManager} reads it at the start of every transaction and binds it to
 * the {@code app.tenant_id} setting the RLS policies key off. Nothing else needs to remember.
 *
 * <p><strong>The value here may only ever originate from a validated token</strong> (spec 31, §37,
 * ARCHITECTURE.md §6: "Any code path that reads a tenant identifier from a request body, query
 * string, or header is a defect"). {@link TenantContextFilter} is the only production writer, and it
 * reads the JWT's tenant claim after Spring Security has verified the signature. {@link #runAs} is
 * the narrow exception, for background work that has no request — a job acting deliberately on a
 * known tenant's behalf.
 *
 * <p>Absent context is not an error here; it is simply "no tenant", which the transaction manager
 * turns into an empty setting and RLS turns into "no rows". Failing closed is the whole point: a
 * bug that loses the context denies access, it does not widen it.
 */
public final class TenantContext {

    private static final ThreadLocal<UUID> CURRENT = new ThreadLocal<>();

    private TenantContext() {}

    /** The current tenant, or empty when this thread is not acting for one. */
    public static Optional<UUID> current() {
        return Optional.ofNullable(CURRENT.get());
    }

    /**
     * Binds a tenant to this thread. Production callers are {@link TenantContextFilter} (from a
     * validated JWT) and {@link #runAs}. Every caller must {@link #clear()} in a finally block —
     * threads are pooled, and a leaked context is a cross-tenant read on the next request.
     */
    public static void set(UUID tenantId) {
        CURRENT.set(tenantId);
    }

    public static void clear() {
        CURRENT.remove();
    }

    /**
     * Runs {@code body} as {@code tenantId}, restoring the previous context afterwards. For
     * background work with no request context (the outbox relay, scheduled maintenance). Restores
     * rather than clears so nesting cannot silently widen an outer scope.
     */
    public static <T> T runAs(UUID tenantId, Supplier<T> body) {
        UUID previous = CURRENT.get();
        CURRENT.set(tenantId);
        try {
            return body.get();
        } finally {
            if (previous == null) {
                CURRENT.remove();
            } else {
                CURRENT.set(previous);
            }
        }
    }
}
