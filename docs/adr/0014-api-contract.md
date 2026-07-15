# ADR-0014: API — contract-first OpenAPI, idempotency, cursor pagination

**Status:** Proposed
**Drivers:** §37, §30, NFR-016, BR-011, COL-002, INT-002

## Context

§37 mandates `/api/v1`, semantic versioning, OAuth2 scopes with tenant claims, Idempotency-Key
on create/import, Problem Details errors, cursor pagination, ETag/If-Match, rate limits, and a
published OpenAPI with a generated SDK option.

## Decision

1. **Contract-first.** The OpenAPI document is the source of truth; server contracts and client
   SDKs are generated from it; CI fails on drift between spec and implementation.
2. **Decimals serialize as strings** (ADR-0006) — non-negotiable.
3. **Idempotency**: `(tenant_id, key, endpoint, request_hash)` → stored response, with TTL.
   Replay returns the **original** response (COL-002). A same-key-different-payload request is a
   `409`.
4. **Cursor pagination** on all list endpoints — opaque, stable cursors. Never offset.
5. **ETag / If-Match** for optimistic concurrency, backed by `row_version` (§39).
6. **Problem Details** (RFC 7807) with code, message, field errors, correlation ID, retryability.
7. **Tenant from the validated token only** (ADR-0003).

## Rationale

**Contract-first over code-first.** §37 requires a *published* OpenAPI with examples and SDKs —
that makes the contract a product surface, not a byproduct. Code-first generation lets the
contract drift silently with each refactor; contract-first plus a CI conformance check makes
breaking changes visible at review time, which is what NFR-016's backward-compatibility promise
actually requires.

**Idempotency stores the response, not just the key.** BR-011 and COL-002 require that a replay
return the original outcome — not merely "don't duplicate". Storing only the key gives you an
ambiguous second response; storing the response snapshot makes retries genuinely safe, which is
what §30's at-least-once integration patterns depend on.

**Cursor, not offset.** Offset pagination over high-volume, concurrently-written tables (activity
records, audit events) both degrades — `OFFSET 100000` scans 100k rows — and skips/duplicates rows
as data shifts. For an auditable export, silently skipping rows is a correctness defect.

## Consequences

**Positive:** no contract drift; SDKs free; safe retries; stable pagination; consistent errors;
correlation IDs threaded through to §44's observability.

**Negative:**
- Contract-first slows early iteration — you edit the spec before the code. Correct trade for a
  published, versioned API with external consumers.
- The idempotency store is another high-churn table: partition and TTL-prune it.
- Cursors can't jump to page N. Product must accept this; it's the right constraint.
- Decimals-as-strings surprises consumers — document prominently in the spec and SDK docs.
