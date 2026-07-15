# ADR-0004: Modular monolith with enforced boundaries

**Status:** Proposed
**Drivers:** §35 ("modular monolith initially"), §36 (module boundaries), NFR-014, NFR-019

## Context

§36 defines 12 modules with owned data and published events. §35 explicitly prefers a modular
monolith first, extracting services only where scale demands. The failure mode is well known:
"modular monolith" becomes a big ball of mud within months because boundaries are enforced by
convention, and convention loses to deadlines.

## Decision

1. **One assembly per module**; a module owns a **PostgreSQL schema** of the same name.
2. **No module reads another module's tables.** Interface call or domain event only.
3. **No cross-module foreign keys** — *except* into a small **shared kernel**.
4. **Shared kernel** = `Tenant`, `OrganisationNode`, `ReportingPeriod`, `MetricDefinition`
   (+ unit/conversion reference). FKs into it are allowed and encouraged.
5. **Boundaries are enforced in CI by architecture tests** (NetArchTest / ArchUnit), not review.
6. Adding anything to the shared kernel **requires a new ADR**.

## Rationale

The shared kernel is the honest compromise, and it deserves to be named rather than smuggled in.
Every ESG record is scoped by tenant, org node, period and metric. Making those eventually
consistent with submissions and calculations would mean giving up DB-enforced referential
integrity on the system's most important invariants — directly against NFR-019 ("ACID for
controlled transactions") and pointlessly hard.

So: a small, slow-changing, FK-able core; strict isolation everywhere else.

Architecture tests are what makes this real. Without a failing build, module boundaries decay
in weeks. Sample rules:
- No type in `Modules.*` references `Modules.*` internals of another module.
- No `DbContext` maps a table outside its own schema.
- No `float`/`double`/`Single` in domain assemblies (ADR-0006).
- Shared kernel assembly has no dependency on any feature module.

## Consequences

**Positive:** single deployable, single transaction where it matters, ACID for controlled
writes, no distributed-systems tax before there is scale to justify it. Extraction stays
available: schema-per-module + events-not-joins is exactly the seam you'd cut along.

**Negative — stated plainly:**
- **The shared kernel is the thing that will resist later extraction.** Accepted, deliberate
  debt. Keeping it tiny is the mitigation, and the ADR gate is how it stays tiny.
- Cross-module referential integrity is application-enforced (or eventually consistent).
- The monolith must be kept genuinely modular by discipline that CI enforces on your behalf.

## Extraction triggers

Extract a module to a service when it needs independent scaling or an independent release
cadence — not before. Likely first candidates: **Carbon** (compute-heavy, bursty) and
**Integration** (I/O-heavy, third-party-failure-prone).
