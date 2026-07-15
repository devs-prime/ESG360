# ADR-0003: Tenancy — shared schema + RLS, deployed as cells

**Status:** Proposed
**Drivers:** NFR-004 (10k tenants), §28 (profiles), §31 (isolation), BR SEC-001, §50 (Critical)

## Context

10,000 tenants, 100,000 users, five deployment profiles ranging from fully shared to
fully private. Tenant isolation failure is a Critical defect with zero tolerance (§50).

## Decision

1. **Shared schema, `tenant_id` on every business table, PostgreSQL RLS** — the only model
   that scales to 10k.
2. **Composite PK `(tenant_id, id)`; every FK includes `tenant_id`** — cross-tenant references
   are structurally unrepresentable, not merely validated.
3. **`id` = UUIDv7** (time-ordered) for index locality on high-volume tables.
4. **Physical isolation is a deployment topology decision (cells), not a code decision.**
   Standard tenants are pooled ~250–500 per cell; dedicated/sovereign/private tenants get a
   cell of one. Identical schema and binary in all cases.

## Rationale — why the alternatives are arithmetically dead

| Model | At 10,000 tenants |
|---|---|
| Schema-per-tenant | 10k schemas × ~100 tables = **~1M tables**. Catalogue bloat, `pg_dump` collapse, and every migration runs 10,000 times. Dead. |
| Database-per-tenant | 10k databases: connection pool explosion, 10k migration targets, unusable ops. Dead as a *default*. |
| **Shared + RLS + cells** | 20–40 cells × ~400 tenants. Each cell is an ordinary, boring, operable Postgres. **Viable.** |

Cells resolve the tension the other models can't: shared economics for the many, physical
isolation for the few, **without forking the product**. Dedicated/sovereign/private stop being
special cases and become a cell with one tenant. Residency (NFR-008) becomes routing.
Tenant upgrade (shared → dedicated) becomes a data migration, not a rewrite.

## Implementation rules

- App DB role: **no `BYPASSRLS`**. Separate migration role.
- `ALTER TABLE x FORCE ROW LEVEL SECURITY` (owner is subject too).
- Policy: `USING (tenant_id = current_setting('app.tenant_id')::uuid)`.
- Set with **`SET LOCAL app.tenant_id`** inside the transaction — required for PgBouncer
  transaction pooling. Never `SET` (session-scoped; leaks across pooled clients).
- Tenant resolved **only** from the validated token (§37). Reading tenant from body/query/header
  is a defect, caught by architecture test.
- **Isolation tests (SEC-001, SUP-001) run on every module merge**, not per release. Add an
  endpoint fuzzer that replays every route with a foreign tenant's IDs.

## Consequences

**Positive:** one codebase for five profiles; blast radius bounded to a cell; per-cell canary
(§43); residency without application awareness; migration path between tiers.

**Negative — accept explicitly:**
- **Cross-tenant queries are impossible by construction.** Benchmarking (§19, R4) therefore
  requires a separate anonymized aggregate pipeline (ADR-0011). This is correct but must be
  planned, never retrofitted.
- A tenant directory + routing layer is new infrastructure to own.
- Cell capacity planning and rebalancing become real operational disciplines.
- Composite keys add modest schema verbosity. Worth it.
