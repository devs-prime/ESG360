# ESG360 Build Progress

Statuses: `—` not started · `PLANNED` plan approved · `WIP` · `TESTS` acceptance tests passing · `DONE` reviewed & merged

Update this file at the end of every Claude Code session. Deviations from spec get a note.

> **Scope:** `V1-SCOPE.md` cuts v1 to R0 + R1's carbon core. R2–R4 below stay in the plan but
> are **not** v1 work. Don't start them.

## Release 0 — Foundation (build first, in this order)

| # | Module | Spec | Status | Notes |
|---|--------|------|--------|-------|
| 0.1 | Repo scaffolding, CI, lint, test runner, IaC skeleton | specs/technical/43 | TESTS | 2026-07-15: Maven multi-module (shared-kernel / app / architecture-tests) + `Quantity` + ArchUnit float-ban & boundary rules + Spotless/Checkstyle + GH Actions CI (build, CodeQL, SBOM, Dependabot) + Terraform skeleton (cloud open). **DB is locally hosted** (docker-compose Postgres 16; Testcontainers for ITs) — no cloud until first-customer signal. Deviations: Spring Boot 3.5.3 (plan said 3.3.x; 3.3 EOL). Local dev machine lacks a Linux-container Docker daemon → ITs verified in CI; install Docker Desktop (WSL2) locally when convenient. PR pending review. |
| 0.2 | Shared data model + migrations baseline | specs/architecture/33, technical/39 | TESTS | 2026-07-15: Flyway baseline V001–V004 — `shared.uuidv7()`, `touch_row_version`, `apply_tenant_rls` helper; `esg360_app` role (no BYPASSRLS/DDL/DELETE); shared kernel (tenant, organisation_node, reporting_period, unit, metric_definition) with composite `(tenant_id,id)` PKs + tenant-inclusive FKs + FORCE RLS; append-only `audit.audit_event` + `messaging.outbox` (partitioned, default partition). RLS/isolation/append-only proven by SharedDataModelIT (SEC-001 seed). Conventions pinned in [docs/data-model-conventions.md](docs/data-model-conventions.md). Scope per approval: all 4 kernel tables as frozen skeletons; audit+outbox structure only (hash-chain/relay/WORM = 0.6). Kernel tables built here as the frozen contract; modules 0.4/0.7 enrich + own behaviour. ITs validate in CI (no local Linux Docker). PR pending. |
| 0.3 | API skeleton + OpenAPI + error/pagination conventions | specs/technical/37 | TESTS | 2026-07-15: new `esg360-web` support module — decimals-as-strings JSON (`Quantity`/`BigDecimal`), RFC 9457 Problem Details + correlation-id filter, cursor pagination + opaque codec, ETag/If-Match (428/412), idempotency store (V005 `api.idempotency_key`, ON CONFLICT + tenant-context, COL-002). Contract-first OpenAPI (`esg360-api.yaml` → openapi-generator interfaces → `MetaController` at `/api/v1/meta`). Conventions in [docs/api-conventions.md](docs/api-conventions.md). Adversarial review caught 2 critical idempotency bugs pre-merge (race → 25P02 500; RLS write failure under app role) — both fixed + RLS test added. Deviation: offset-ban is a documented convention, not an ArchUnit rule (can't detect reliably). ITs (idempotency + RLS) validate in CI. PR pending. |
| 0.4 | Tenant, Organisation & Boundary | specs/modules/02 | — | **Now built AFTER 0.5** (2026-07-16 reorder — see decision log). Inherits real RBAC + a JWT-backed tenant-context filter from 0.5 instead of provisional stubs. |
| 0.5 | Identity, Access & SoD | specs/modules/03 | WIP | **Built BEFORE 0.4** (2026-07-16 reorder — see decision log). **Critical security gate (V1-SCOPE): cannot reach DONE without a qualified human security review of tenancy/RLS + identity/SoD — "Claude checked it" is explicitly not a substitute.** 2026-07-16 progress: `esg360-identity` module + V006 schema (app_user, role, permission, role_permission, role_assignment, sod_rule, sod_exception, session_policy, access_review_campaign, access_review_item) — **migration verified in CI against PostgreSQL 16**. Structural guards proven by `IdentitySchemaIT` (10/10): no self-approved SoD exception, no self-certified access review, canonical SoD pairs, no duplicate tenant-wide grant (`NULLS NOT DISTINCT`), no hard-deleted users, tenant isolation, FORCE RLS on all 9 business tables, permission catalogue read-only. **Req 3-010 (access reviews) is IN scope and its schema has landed** (deferral reversed on review). Tenant-context chain built: `TenantAwareTransactionManager` binds `app.tenant_id` via `set_config(...,true)` (transaction-scoped per ADR-0003 — a session `SET` would leak the last tenant to the next PgBouncer borrower), parameter-bound (SEC-900), **fail-closed**, and replaces the auto-configured tx manager so coverage doesn't depend on anyone remembering an annotation. `TenantContextFilter` reads the tenant only from the validated JWT, installed after `BearerTokenAuthenticationFilter`. App now **refuses to start without an IdP configured** (deliberate: token-derived tenant isolation is the Critical control, so "up but authenticating nobody" is the worse failure); tests supply a decoder from an in-process keypair, no key material committed. Tenant-isolation mechanism proven by `TenantContextIT` (5/5) — including that context does not outlive its transaction on a reused pooled connection, which is ADR-0003's PgBouncer argument as a tested fact. **Still to do: RBAC evaluation, SoD service, access-review service, endpoints + OpenAPI, and the acceptance tests.** Note the ITs prove the *mechanism*; **spec 03's SEC-001/SEC-002 are API-level** (denied **and** a security event logged) and land with the endpoints + audit service. **Blocked on the DAO layer: jOOQ codegen needs a local Linux Docker daemon — approved fix is Docker Desktop (WSL2), install pending.** |
| 0.6 | Audit event service (append-only) | specs/technical/36, modules/24 | — | Needed by everything |
| 0.7 | Metric & Data-Point Catalogue | specs/modules/04 | — | |
| 0.8 | Workflow, Notifications & Cases | specs/modules/21 | — | |
| 0.9 | Evidence & Lineage (core) | specs/modules/06 | — | Assurance rooms deferred to R2 |
| 0.10 | Integration framework (ingestion, idempotency, quarantine) | specs/modules/22 | — | Adapters come per release |
| 0.11 | Subscription, Configuration & Admin | specs/modules/23 | — | |

## Release 1 — ESG Data & Carbon

| # | Module | Spec | Status | Notes |
|---|--------|------|--------|-------|
| 1.1 | Reporting Cycles & Data Collection | specs/modules/05 | — | |
| 1.2 | Emission Factor Library | specs/modules/07 | — | |
| 1.3 | Calculation Engine (shared) | specs/technical/38 + testing/49 | — | Validate against dataset in spec 49 |
| 1.4 | GHG Inventory Scope 1 & 2 | specs/modules/08 | — | |
| 1.5 | Scope 3 Value-Chain (core categories) | specs/modules/09 | — | |
| 1.6 | Energy, Water, Waste & Environment | specs/modules/10 | — | |
| 1.7 | Dashboards (basic) | specs/modules/19 | — | |
| 1.8 | Disclosure (basic) | specs/modules/18 | — | |

## Release 2 — Reporting & Assurance

| # | Module | Spec | Status | Notes |
|---|--------|------|--------|-------|
| 2.1 | Framework mapping engine (GRI/ISSB/ESRS) | specs/modules/18 | — | |
| 2.2 | Disclosure workspace + immutable snapshots | specs/modules/18 | — | |
| 2.3 | Assurance rooms, sampling, sign-off | specs/modules/06 | — | |
| 2.4 | Carbon Credits & Instruments | specs/modules/17 | — | Release placement indicative |
| 2.5 | Reports & document outputs / exports | specs/modules/25 | — | |

## Release 3 — Social, Governance & Supplier

| # | Module | Spec | Status | Notes |
|---|--------|------|--------|-------|
| 3.1 | Social & Human Capital | specs/modules/11 | — | |
| 3.2 | Governance, Ethics, Risk & Compliance | specs/modules/12 | — | |
| 3.3 | Materiality & Stakeholder Engagement | specs/modules/13 | — | |
| 3.4 | Supplier ESG & Due Diligence (incl. supplier portal) | specs/modules/16 | — | |

## Release 4 — Transition, Climate Risk & AI

| # | Module | Spec | Status | Notes |
|---|--------|------|--------|-------|
| 4.1 | Targets, Transition Plans & Initiatives | specs/modules/14 | — | |
| 4.2 | Climate & Nature Risk, Scenarios | specs/modules/15 | — | |
| 4.3 | AI Copilots & Intelligent Automation | specs/modules/20 | — | AI guardrails per specs/technical/42 |
| 4.4 | Advanced analytics & benchmarking | specs/modules/19 | — | |

## Cross-cutting (verify continuously, gate every release)

| Item | Spec | Status | Notes |
|------|------|--------|-------|
| NFR/performance tests (NFR-001/002) | specs/testing/48 | — | |
| DR restore test (DR-001) | specs/testing/48 | — | |
| Accessibility (ACC-001, WCAG 2.2 AA) | specs/testing/48 | — | |
| Resilience tests | specs/testing/48 | — | |
| Security: cross-tenant + SoD suites (SEC-*) | specs/modules/03 | — | Re-run on every module merge |

## Decision log

Full reasoning in `docs/adr/`. This table is the index of what's settled vs open.

| Date | Decision | Status | ADR |
|------|----------|--------|-----|
| 2026-07-15 | ~~Backend: .NET 8 LTS~~ | ⛔ Superseded by 0018 | [0001](docs/adr/0001-backend-platform.md) |
| 2026-07-15 | **Backend: Java 21 + Spring Boot 3 + Flowable** | ✅ **Accepted — signed off** | [0018](docs/adr/0018-backend-platform-revised.md) |
| 2026-07-15 | Database: **PostgreSQL 16+** | ✅ **Accepted — signed off** | [0002](docs/adr/0002-relational-database.md) |
| 2026-07-15 | Tenancy: shared schema + RLS + cells | Proposed | [0003](docs/adr/0003-tenancy-and-cells.md) |
| 2026-07-15 | Modular monolith, enforced boundaries | Proposed | [0004](docs/adr/0004-modular-monolith.md) |
| 2026-07-15 | Calc engine: manifest hash, AST, inline lineage | Proposed | [0005](docs/adr/0005-calculation-engine.md) |
| 2026-07-15 | Precision: decimal, strings over JSON | Proposed | [0006](docs/adr/0006-precision-and-rounding.md) |
| 2026-07-15 | Audit: append-only + hash chain + WORM | Proposed | [0007](docs/adr/0007-audit-log.md) |
| 2026-07-15 | Transactional outbox | Proposed | [0008](docs/adr/0008-transactional-outbox.md) |
| 2026-07-15 | ~~Workflow: custom state machine~~ | ⛔ Superseded by 0019 | [0009](docs/adr/0009-workflow-engine.md) |
| 2026-07-15 | **Workflow: Flowable embedded** | ✅ **Accepted — signed off** | [0019](docs/adr/0019-workflow-revised.md) |
| 2026-07-15 | Time-series: native partitioning | Proposed | [0010](docs/adr/0010-time-series.md) |
| 2026-07-15 | Analytics: replicas + matviews, defer lakehouse | Proposed | [0011](docs/adr/0011-analytics.md) |
| 2026-07-15 | Evidence: content-addressed + quarantine | Proposed | [0012](docs/adr/0012-evidence-storage.md) |
| 2026-07-15 | AI: gateway sidecar, never load-bearing | Proposed | [0013](docs/adr/0013-ai-containment.md) |
| 2026-07-15 | API: contract-first OpenAPI | Proposed | [0014](docs/adr/0014-api-contract.md) |
| 2026-07-15 | Frontend: Next.js, separate supplier portal | Proposed | [0015](docs/adr/0015-frontend.md) |
| 2026-07-15 | Deployment: cells, residency by placement | Proposed (runtime amended by 0020) | [0016](docs/adr/0016-deployment-and-residency.md) |
| 2026-07-15 | **Toolchain: GH Actions, Terraform, Flyway, jOOQ, Maven, Testcontainers** | ✅ **Accepted — signed off** | [0017](docs/adr/0017-toolchain.md) |
| 2026-07-15 | **Runtime: managed containers, defer Kubernetes** | ✅ **Accepted — signed off** | [0020](docs/adr/0020-runtime-platform-revised.md) |
| 2026-07-15 | **v1 scope: 1 cell, shared only, Scope 1+2, one framework** | ✅ **Signed off** | [V1-SCOPE.md](V1-SCOPE.md) |
| 2026-07-16 | **Build order: 0.5 (Identity) before 0.4 (Tenant/Org).** 0.4's scoped-access test (ORG-103) and the "tenant resolved from trusted token" invariant (SEC-001) both depend on identity. Building 0.5 first gives 0.4 real RBAC and a real JWT-backed tenant-context filter instead of a provisional one that would be rewritten. | ✅ **Approved** | — (build order only; no ADR affected) |
| 2026-07-16 | **Item 0.5 deviations.** ~~(a) Req 3-010 access reviews deferred to 0.11~~ — **reversed on review: 3-010 is IN 0.5** (V1-SCOPE lists "access review" under v1 Identity; cheaper to build while the identity schema is unsettled than to retrofit). (b) Reqs 3-001/3-002 SSO/MFA land as **claim validation only** (`acr`/`amr`), IdP pluggable, since no IdP is chosen. (c) Reqs 3-006/3-007 (external assurer, supplier access) deferred per V1-SCOPE. | ✅ **Reviewed** — (a) rejected, (b)/(c) accepted | — |
| 2026-07-16 | **jOOQ codegen: install Docker Desktop (WSL2 backend) locally** rather than committing CI-generated sources or deferring jOOQ. ADR-0017 mandates jOOQ and Testcontainers; the alternatives meant either debugging codegen through CI round-trips forever, or a superseding ADR to drop jOOQ. One-time install also unblocks running ITs locally (they currently only run in CI) and every future module's codegen. | ✅ **Approved** — install pending | [0017](docs/adr/0017-toolchain.md) |
| — | Cloud provider (Azure / AWS / GCP) | **Deliberately open** — must not block session 1 | [0017](docs/adr/0017-toolchain.md) |

## Architecture verification tasks (add to R0)

| Task | Why | Status |
|------|-----|--------|
| Architecture tests: module boundaries, no cross-module FK | ADR-0004 rots without CI enforcement | — |
| Analyzer: ban float/double in domain assemblies | ADR-0006 — highest-frequency violation | — |
| Golden-manifest hash gate from spec 49 datasets | ADR-0005 — protects the core product claim | — |
| Cross-tenant isolation fuzzer on every merge | ADR-0003 — SEC-001/SUP-001 are Critical | — |
| 1M-record calculation load test | NFR-003 — verify the 10–30× headroom claim early | — |
| Outbox replay/idempotency test | ADR-0008 — every consumer must be idempotent | — |
| `Quantity` value type wrapping BigDecimal | ADR-0018 — juniors must never touch raw BigDecimal | — |
| Flowable `tenantId` wrapper + workflow isolation tests | ADR-0019 — Flowable's tables sit outside RLS | — |
| **Carbon accountant validates spec 49 golden datasets** | **V1-SCOPE gate — highest-value expert hour on the project** | — |
| **Security review of tenancy/RLS + identity/SoD** | **V1-SCOPE gate — Critical severity (spec 50)** | — |
