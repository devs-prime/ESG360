# ADR-0017: Release 0 toolchain

**Status:** Accepted (signed off 2026-07-15)
**Date:** 2026-07-15
**Drivers:** §43 (DevSecOps), §28 (portability), ADR-0018, team = junior + AI-assisted

## Context

ADR-0001..0016 deliberately left the reversible toolchain open. But item 0.1 *is* scaffolding, so
these now block session 1. Two facts settle most of them: **source control is GitHub**, and
**the team is junior**. The second is the tiebreaker throughout — where two options are close,
prefer the boring, explicit, hard-to-misuse one.

## Decisions

| Concern | Choice | Why this one |
|---|---|---|
| **CI** | **GitHub Actions** | You're on GitHub. Maps near 1:1 to §43 points 34–36. |
| **SAST** | **CodeQL** | Free on GitHub, good Java coverage — §43 point 35 |
| **Dependency + licence scan** | **Dependabot** + **CycloneDX** SBOM | §43 points 35–36 |
| **Artifact signing** | **cosign / Sigstore** | §43 point 36 "signed artifacts" |
| **Build** | **Maven** (multi-module) | Boring and declarative. Gradle is faster but its DSL is one more thing for juniors to learn wrong. Revisit only if build times hurt. |
| **IaC** | **Terraform** | Cloud is undecided and §28 demands portability → Bicep is disqualified. (Note: Terraform is BUSL-licensed; fine for internal use. OpenTofu is the MPL fork if legal prefers.) |
| **Migrations** | **Flyway**, plain SQL | Expand/contract (ADR-0016) needs reviewable SQL, not ORM-generated DDL. Also gives jOOQ a schema to generate from. |
| **DB access** | **jOOQ** | See below — the least obvious call here. |
| **Tests** | **JUnit 5 + Testcontainers** | **Non-negotiable.** RLS cannot be tested against H2/in-memory. RLS is your Critical control (SEC-001). |
| **Architecture tests** | **ArchUnit** | Enforces ADR-0004 boundaries + ADR-0006's float ban. Without this, both rot in weeks. |
| **API tests** | **REST Assured** | §37 conformance |
| **Load tests** | **k6** or **Gatling** | NFR-002/003, from Release 0 — not Release 3 |
| **Format/lint** | **Spotless** + **Checkstyle** | §43 point 35 "CI runs formatting" |
| **Feature flags** | **Unleash**, self-hosted | §43 point 39 needs flags; LaunchDarkly is SaaS-only → fails §28's sovereign/private profiles |
| **Malware scan** | **ClamAV** in the quarantine pipeline | §41; self-hostable → works in every profile |
| **Observability** | **OpenTelemetry SDK → OTLP collector** | NFR-013; backend stays vendor-neutral and follows the cloud choice |
| **Cloud** | **Deferred — deliberately** | Portability was the whole point of ADR-0002/0020. Decide it by who your first customers are (Microsoft-shop enterprises → Azure). **Do not let it block session 1.** |

## The jOOQ call (the one worth arguing about)

Spring Data JPA / Hibernate is the mainstream default. It is the wrong default *here*:

- **Composite `(tenant_id, id)` PKs everywhere** (ADR-0003) → `@EmbeddedId` verbosity on every
  entity, forever.
- **Records are append-only** (BR-003/004) → Hibernate's core value (dirty checking, lazy loading,
  first-level cache) is unused, while its failure modes (N+1, `LazyInitializationException`,
  surprise flushes) remain.
- **Determinism and auditability favour explicit SQL** (ADR-0005).
- **Hibernate's magic is a classic junior trap.** jOOQ is just typed SQL — for a team learning
  the domain *and* the language, transparent beats clever.
- Pairs naturally with Flyway-first: schema → codegen → typesafe queries.
- **jOOQ's Open Source Edition is free for PostgreSQL.** (Commercial licensing only applies to
  proprietary databases — not our case.)

Bulk paths (ADR-0005's 1M-row `COPY`) use raw JDBC / PostgreSQL `CopyManager` regardless of ORM.

## Deferred, explicitly

- **Message broker.** V1 is one cell and a modular monolith → **outbox → in-process dispatcher**.
  ADR-0008 still fully applies (the outbox is what gives durability); only the transport changes.
  Introduce a broker when you extract a service or need external webhook fan-out.
- **Search (OpenSearch)** — behind an interface, not in V1-SCOPE.
- **Component library** — R0 detail; constraint is WCAG 2.2 AA + RTL (ADR-0015).
- **Secrets manager** — follows the cloud decision.

## Consequences

**Positive:** every choice is portable across §28's profiles; free/OSS throughout; §43's pipeline is
largely satisfied by GitHub-native tooling; the guardrails (ArchUnit, Testcontainers, golden
datasets) are exactly what lets an AI-assisted junior team ship safely.

**Negative:**
- jOOQ has a smaller talent pool than JPA, and codegen adds a build step. Accepted — the reasoning
  above outweighs it.
- Maven is more verbose and slower than Gradle.
- Self-hosting Unleash and ClamAV is operational surface you own, in exchange for §28 compliance.
- Deferring the broker means a later migration when the first service is extracted — small, because
  the outbox interface doesn't change.
