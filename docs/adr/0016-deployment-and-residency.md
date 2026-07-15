# ADR-0016: Deployment — Kubernetes + IaC, cell-based residency and progressive delivery

**Status:** Proposed — **runtime clause amended by [ADR-0020](0020-runtime-platform-revised.md)**
> Kubernetes is deferred in favour of managed containers. Cells, residency-by-placement,
> canary and DR discipline in this ADR all stand unchanged.
**Drivers:** §28, §34, §43, NFR-001, NFR-005, NFR-006, NFR-008, NFR-015

## Context

Five deployment profiles, 99.9% availability, RPO ≤15 min, RTO ≤4 h, data residency, progressive
delivery with feature flags/canary or blue-green, and containerised + IaC portability.

## Decision

1. **Kubernetes + infrastructure-as-code**, one identical cell definition for every profile.
2. **Residency is cell placement.** A tenant's region/jurisdiction is determined by which cell it
   lives in. **No application code is jurisdiction-aware.**
3. **Multi-zone within a cell** where the region supports it (§34) → NFR-001.
4. **Progressive delivery by cell**: canary to one cell → observability gates → proceed (§43).
5. **Per-cell PITR + streaming replica** → NFR-005/006. **Quarterly restore tests, annual DR
   exercise** (§34) — scheduled and evidenced, not aspirational.
6. **Recovery order fixed** (§34): identity/tenant → transactional ESG records → evidence →
   workflows → integrations → analytics → AI.
7. **Migrations backward-compatible**, rehearsed against representative volumes (§43 point 40).

## Rationale

**Residency as placement is the whole trick.** The alternative — jurisdiction logic inside the
application — means every feature carries residency risk forever, and every new regulation is a
code change across 25 modules. Placement means residency is proven by *topology*: a sovereign
tenant's data is in a sovereign cell because there is no other cell it can be in. That is an
argument you can make to a regulator, and it is enforced by infrastructure rather than by a
developer remembering a rule.

**Canary-by-cell is what makes 99.9% survivable.** With 20–40 cells, a bad release is caught on
cell 1 affecting ~400 tenants rather than 10,000. Combined with backward-compatible migrations,
rollback stays possible — a migration that drops a column makes rollback impossible no matter how
good the canary is.

**Restore tests are the only proof of RPO/RTO.** An untested backup is a hypothesis. §34 requires
quarterly restore tests; treat a missed one as a release blocker, because NFR-005/006 are
contractual claims.

## Consequences

**Positive:** one artifact, five profiles; residency provable by topology; bounded blast radius;
credible DR; per-cell rollback.

**Negative:**
- **Fleet management is a real discipline.** 20–40+ cells means IaC, cell provisioning,
  rebalancing, fleet-wide version skew, and per-cell observability. Budget for platform
  engineering (§52's Platform capability) from Release 0 — this is not something to add later.
- Backward-compatible migrations require the expand/contract discipline on every schema change.
  Slower per change; the only thing that makes rollback real.
- Progressive rollout means multiple versions live simultaneously → API and event contracts must
  tolerate skew (NFR-016).
- A tenant directory + routing layer is new infrastructure to own and make highly available. It
  is, notably, the one genuinely global component in the design — and therefore a single point of
  failure that needs care.
