package com.esg360.web.idempotency;

import java.time.Duration;
import java.util.List;
import java.util.UUID;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.esg360.web.error.ConflictException;

/**
 * Idempotency-Key handling for create/import endpoints (spec 37, ADR-0014, BR-011, COL-002).
 * Stores the original response so a replay returns the same outcome — not merely "don't duplicate".
 *
 * <p>The store is keyed by {@code (tenant_id, key, endpoint)}; a reuse of the same key with a
 * different payload (request hash) is a 409 {@link ConflictException}. Tenant isolation is enforced
 * both by RLS on the table and by explicit {@code tenant_id} predicates. Because the app role is
 * subject to RLS (no BYPASSRLS), each transaction sets {@code app.tenant_id} before touching the
 * table — otherwise the policy would reject every read and write.
 *
 * <p>Usage: call {@link #begin} — on {@code Proceed}, execute the work and then {@link #complete};
 * on {@code Replay}, return the stored response. Rows are pruned by a maintenance job (item 0.6).
 */
@Service
public class IdempotencyService {

    private static final Duration TTL = Duration.ofHours(24);

    private final JdbcTemplate jdbc;

    public IdempotencyService(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public IdempotentOutcome begin(UUID tenantId, String key, String endpoint, String requestHash) {
        setTenantContext(tenantId);
        IdempotentOutcome existing = inspectExisting(tenantId, key, endpoint, requestHash);
        if (existing != null) {
            return existing;
        }
        // ON CONFLICT DO NOTHING resolves races without aborting the transaction: a concurrent
        // inserter either commits (we see 0 rows, then re-inspect the now-visible row) or aborts
        // (we win). A raw INSERT would hit 23505 and poison the transaction (25P02) on the loser.
        int inserted = jdbc.update(
                """
                insert into api.idempotency_key
                  (tenant_id, idem_key, endpoint, request_hash, expires_at)
                values (?, ?, ?, ?, now() + (? * interval '1 second'))
                on conflict (tenant_id, idem_key, endpoint) do nothing
                """,
                tenantId,
                key,
                endpoint,
                requestHash,
                TTL.toSeconds());
        if (inserted == 1) {
            return new IdempotentOutcome.Proceed();
        }
        // Lost the race — a concurrent request reserved this key first. Re-inspect: replay if it
        // completed, 409 on a different payload, or 409 "in progress" if still running.
        IdempotentOutcome afterRace = inspectExisting(tenantId, key, endpoint, requestHash);
        if (afterRace != null) {
            return afterRace;
        }
        throw new ConflictException("Idempotent request is already in progress");
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void complete(UUID tenantId, String key, String endpoint, int status, String body) {
        setTenantContext(tenantId);
        int updated = jdbc.update(
                """
                update api.idempotency_key
                   set response_status = ?, response_body = ?
                 where tenant_id = ? and idem_key = ? and endpoint = ?
                """,
                status,
                body,
                tenantId,
                key,
                endpoint);
        if (updated != 1) {
            throw new IllegalStateException("Idempotency reservation to complete was not found");
        }
    }

    /** Sets the per-transaction tenant context the RLS policy keys off (ADR-0003). */
    private void setTenantContext(UUID tenantId) {
        jdbc.queryForObject("select set_config('app.tenant_id', ?, true)", String.class, tenantId.toString());
    }

    private IdempotentOutcome inspectExisting(UUID tenantId, String key, String endpoint, String requestHash) {
        List<Row> rows = jdbc.query(
                """
                select request_hash, response_status, response_body
                  from api.idempotency_key
                 where tenant_id = ? and idem_key = ? and endpoint = ?
                """,
                (rs, n) -> new Row(
                        rs.getString("request_hash"),
                        (Integer) rs.getObject("response_status"),
                        rs.getString("response_body")),
                tenantId,
                key,
                endpoint);
        if (rows.isEmpty()) {
            return null;
        }
        Row row = rows.get(0);
        if (!row.requestHash().equals(requestHash)) {
            throw new ConflictException("Idempotency-Key reused with a different request payload");
        }
        if (row.responseStatus() == null) {
            throw new ConflictException("Idempotent request is already in progress");
        }
        return new IdempotentOutcome.Replay(row.responseStatus(), row.responseBody());
    }

    private record Row(String requestHash, Integer responseStatus, String responseBody) {}
}
