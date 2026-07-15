# ADR-0002: Relational database — PostgreSQL 16+

**Status:** Accepted (signed off 2026-07-15)
**Drivers:** §28 (deployment profiles), NFR-015 (portability), §31 (tenant isolation), §39, NFR-004

## Context

§35 permits "PostgreSQL or Azure SQL". Unlike ADR-0001, this is not a close call — one
requirement settles it.

## Decision

**PostgreSQL 16+** for all profiles, everywhere, with no per-profile variation.

## Rationale

1. **§28 requires sovereign cloud and private cloud / customer-subscription deployment.**
   Azure SQL exists only on Azure. Choosing it would mean either abandoning two of five
   deployment profiles or maintaining a second database backend — i.e. two products.
   NFR-015 ("containerised services and IaC for supported cloud/private profiles") makes
   portability a hard constraint, not a preference. **This alone decides it.**
2. **Row-Level Security is the isolation primitive ADR-0003 needs**, and PostgreSQL's RLS
   (with `FORCE ROW LEVEL SECURITY`, policies on roles without `BYPASSRLS`) is mature and
   well-understood.
3. **Declarative partitioning** by `(tenant, period)` serves NFR-004's billions of rows and
   §39's partitioning rule; also enables the time-series decision (ADR-0010) without a second
   datastore.
4. **`numeric`** gives exact decimal storage (ADR-0006). Non-negotiable.
5. Runs identically in every cell (ADR-0003) — managed (RDS/Azure Flexible/Cloud SQL) or
   customer-operated. Same schema, same migrations, same behaviour.

## Consequences

**Positive:** one backend for five profiles; no cloud lock-in; strong RLS + partitioning;
lower licence cost at 10k tenants; TimescaleDB stays available as a future option.

**Negative:**
- No built-in system-versioned temporal tables (Azure SQL has them). **Acceptable** — we model
  versions and revisions *explicitly* (BR-004/BR-005/BR-014 require domain-meaningful
  versioning with reasons and approvals, which system-versioning wouldn't give us anyway).
- Connection management needs care: PgBouncer in **transaction pooling** mode, which
  constrains RLS to `SET LOCAL` inside a transaction (see ADR-0003).
- You own more DB operations than a fully-managed Azure SQL would give.

## Alternatives considered

| Option | Why not |
|---|---|
| Azure SQL | Kills sovereign + private cloud profiles (§28). Fatal. |
| Both (abstraction layer) | Two backends = two test matrices, lowest-common-denominator SQL, and the RLS/partitioning strategies diverge. This is how teams lose a year. |
| Oracle / SQL Server on-prem | Licence cost at 10k tenants; no portability advantage over Postgres. |

## Confidence

**High.** This is the most clear-cut decision in the set.
