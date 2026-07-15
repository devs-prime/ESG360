# ADR-0011: Analytics — read replicas + matviews; defer lakehouse; benchmarking is a separate product

**Status:** Proposed
**Drivers:** §29 (lakehouse bronze/silver/gold), §19 (dashboards, benchmarking), NFR-002, BR-007, ADR-0003

## Context

§29 describes an analytics lakehouse with bronze/silver/gold separation. §19 requires dashboards
(R1) and cross-tenant benchmarking (R4). ADR-0003 makes cross-tenant queries structurally
impossible.

## Decision

1. **Dashboards (R1): read replicas + materialized views**, within the cell. No lakehouse.
2. **Defer the lakehouse** until an evolution trigger fires.
3. **Cross-tenant benchmarking (§19, R4) is a separate, opt-in, anonymized pipeline** — never a
   query against production data.

## Rationale

**On deferring the lakehouse.** Building bronze/silver/gold before there are users is the
canonical premature-platform mistake: months of pipeline for dashboards a materialized view
serves in milliseconds. Worse, a lakehouse spanning cells **contradicts §4/§28's residency
model** — you'd be copying sovereign tenants' data into a central store, which is precisely
what a sovereign profile exists to prevent. A per-cell lakehouse, meanwhile, is mostly cost with
little benefit at per-cell volumes.

**On benchmarking — the important part.** ADR-0003 means a benchmarking query cannot see other
tenants. This is *correct*, not an obstacle to route around. Benchmarking therefore requires:
- **Opt-in** per tenant, with contractual consent
- **Aggregate-only** export from each cell (never row-level)
- **k-anonymity thresholds** — consistent with BR-007's privacy-threshold philosophy and
  SOC-002's suppression test; a "benchmark" over three peers de-anonymises them
- A **separate store** outside every production cell, with its own DPIA
- Sovereign/private tenants **excluded by default**

**The failure mode to prevent:** an engineer under deadline adds a `BYPASSRLS` query "just for
benchmarking". That single line dismantles the isolation guarantee the whole architecture is
built on, and would be a Critical defect (§50). It must be architecturally impossible: the
benchmarking store lives outside the cell and the app role cannot bypass RLS (ADR-0003).

## Consequences

**Positive:** R1 dashboards ship fast; residency preserved; no premature data platform; privacy
posture defensible to a regulator.

**Negative:**
- Complex analytical queries on replicas need care (indexing, matview refresh scheduling).
- Matview refresh lag is a real, visible consistency artifact — document the freshness budget
  per dashboard.
- Benchmarking becomes a distinct workstream with its own consent, legal and privacy design.
  That is the honest cost of getting it right, and it is much cheaper than retrofitting.

## Evolution trigger

Introduce a per-cell lakehouse when **any** of:
- Analytical load measurably degrades OLTP p95 despite replicas (NFR-002)
- Matview refresh windows exceed the acceptable freshness budget
- Customers demand raw governed dataset access for their own BI (§26's "BI and regulator systems")
