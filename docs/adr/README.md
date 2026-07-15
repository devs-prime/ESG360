# Architecture Decision Records

Format: lightweight MADR. One decision per file. Numbered, immutable once accepted.

**Superseding, not editing:** if a decision changes, write a new ADR that supersedes the old one
and mark the old one `Superseded by ADR-XXXX`. Never rewrite history — the reasoning of a past
decision is evidence, and this product's whole premise is that evidence is preserved.

Read `../ARCHITECTURE.md` first — it's the narrative these decisions hang on.

| ADR | Decision | Status | Confidence |
|---|---|---|---|
| [0001](0001-backend-platform.md) | ~~Backend platform — .NET 8 LTS~~ | ⛔ Superseded by 0018 | — |
| [0002](0002-relational-database.md) | Relational database — PostgreSQL 16+ | Proposed | **High** |
| [0003](0003-tenancy-and-cells.md) | Tenancy — shared schema + RLS + cells | Proposed | **High** |
| [0004](0004-modular-monolith.md) | Modular monolith, enforced boundaries | Proposed | **High** |
| [0005](0005-calculation-engine.md) | Calculation engine — manifest hash, AST, inline lineage | Proposed | **High** |
| [0006](0006-precision-and-rounding.md) | Precision — decimal, strings over JSON, pinned rounding | Proposed | **High** |
| [0007](0007-audit-log.md) | Audit log — append-only + hash chain + WORM | Proposed | High |
| [0008](0008-transactional-outbox.md) | Transactional outbox | Proposed | **High** |
| [0009](0009-workflow-engine.md) | ~~Workflow — state machine, defer BPMN~~ | ⛔ Superseded by 0019 | — |
| [0010](0010-time-series.md) | Time-series — native partitioning, defer TSDB | Proposed | Medium |
| [0011](0011-analytics.md) | Analytics — replicas + matviews; benchmarking separate | Proposed | High |
| [0012](0012-evidence-storage.md) | Evidence — content-addressed, quarantine, object-lock | Proposed | High |
| [0013](0013-ai-containment.md) | AI — gateway sidecar, untrusted content, human review | Proposed | **High** |
| [0014](0014-api-contract.md) | API — contract-first OpenAPI, idempotency, cursors | Proposed | High |
| [0015](0015-frontend.md) | Frontend — Next.js, separate supplier portal | Proposed | Medium |
| [0016](0016-deployment-and-residency.md) | Deployment — cells, residency by placement | Proposed (runtime amended by 0020) | High |
| [0017](0017-toolchain.md) | Release 0 toolchain — GH Actions, Terraform, Flyway, jOOQ, Testcontainers | Proposed | High |
| [0018](0018-backend-platform-revised.md) | **Backend — Java 21 + Spring Boot 3 + Flowable** *(supersedes 0001)* | Proposed | Medium-High |
| [0019](0019-workflow-revised.md) | **Workflow — Flowable embedded** *(supersedes 0009)* | Proposed | **High** |
| [0020](0020-runtime-platform-revised.md) | **Runtime — managed containers, defer K8s** *(amends 0016)* | Proposed | High |

## Decisions needing your sign-off before Release 0 scaffolding

- **ADR-0018** (Java 21 + Spring Boot + Flowable) — the stack call. Read ADR-0001 first for what
  it reversed and why.
- **ADR-0002** (PostgreSQL) — clear-cut, but confirm you accept the sovereign/private-cloud
  reasoning that drives it.
- **ADR-0017** (toolchain) — mostly mechanical; the jOOQ-over-JPA call is the one to argue about.
- **ADR-0020** (defer Kubernetes) + **`../../V1-SCOPE.md`** — both follow from team shape.

## Review schedule

- **ADR-0010 (time-series)** — review when meter ingest volumes are real, ~Release 1.
- **ADR-0011 (lakehouse)** — review when benchmarking is contracted, ~Release 4.
- **ADR-0020 (Kubernetes)** — review at ~5 cells, or first private/sovereign customer, or first SRE hire.
- **ADR-0017 (broker)** — review when the first service is extracted from the monolith.

*ADR-0009's "review at end of R0" is discharged — superseded by 0019.*
