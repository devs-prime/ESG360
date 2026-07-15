# ADR-0019: Workflow (revised) — Flowable, embedded in-process

**Status:** Accepted (signed off 2026-07-15) — **supersedes [ADR-0009](0009-workflow-engine.md)**
**Date:** 2026-07-15
**Drivers:** §40, §21, NFR-019, ADR-0018

## Context

ADR-0009 chose a custom state machine, explicitly because ".NET has no embeddable BPMN engine of
Flowable/Camunda-7 quality", and named its own reversal trigger: *"If ADR-0001 flips to Java, this
decision should flip with it."* ADR-0018 flipped. So does this.

## Decision

**Flowable, embedded in the Spring Boot application**, sharing the same `DataSource` and the same
Spring transaction. Flowable's tables live in a `workflow` PostgreSQL schema (consistent with
ADR-0004's schema-per-module). Kept behind the Workflow module interface, per ADR-0009's mitigation.

## Rationale — §40 maps almost point-for-point

| §40 requirement | Flowable |
|---|---|
| Versioned definitions, immutable published version | Deployments are versioned and immutable — native |
| Instance references definition version | Native |
| Human task: candidate role/scope, assignee, due date, form schema | Native (candidate groups/users) |
| Business-calendar-aware durable timers | Timer events + custom `BusinessCalendar` extension point |
| Decision (expression or rules service) | Native expressions; DMN engine included |
| Escalation | Boundary timer events + listeners |
| Compensation for non-atomic integrations | BPMN compensation events — native |
| Controlled instance migration | `ProcessInstanceMigrationBuilder` — native |

That is the entirety of ADR-0009's build list, already built and battle-tested.

**Crucially, ADR-0009's two real arguments survive:**
- **Atomicity** — Flowable embedded uses your `DataSource` and your transaction. A workflow
  transition and the business change commit together. NFR-019 and §7's consistency model hold.
  This is what Zeebe/Temporal could not give us.
- **No extra cluster per cell** — it's a library, not infrastructure. ADR-0016/0020 portability
  and the private-cloud profile are unaffected.

We were choosing "custom state machine" *only* because .NET lacked this. It doesn't lack it in Java.

## Consequences

**Positive:** deletes the highest-regret item in the plan; visual BPMN authoring becomes possible
later at near-zero cost (removing ADR-0009's RFP risk entirely); Flowable is Apache 2.0.

**Negative — one is subtle and important:**

- **⚠ Flowable's own tables are not under our RLS model.** ADR-0003 gives three independent
  isolation layers; Flowable's internal tables (`ACT_RU_*`, `ACT_HI_*`) get **two**. Flowable
  queries won't set `app.tenant_id`, and its schema doesn't use our composite `(tenant_id, id)` keys.
  → **Mitigation:** use Flowable's native `tenantId` on every deployment, instance and query; make
  the Workflow module interface the *only* path to Flowable and have it inject `tenantId`
  unconditionally; add isolation tests over workflow queries specifically. Note the residual risk
  honestly: **workflow state** could leak under a wrapper bug, though **ESG business records could
  not** (they stay under RLS). Moot in dedicated/sovereign cells.
- Flowable is a large dependency with its own schema migrations to coordinate with Flyway.
- BPMN is a real thing to learn; the team will need a week on it.
- Camunda 7 is the obvious alternative (also embeddable, Apache 2.0) — **verify its current
  long-term support roadmap before choosing it**; Flowable is the safer default today.

## Consequence for ADR-0009

ADR-0009's "review at end of Release 0" is discharged. The RFP-driven reversal risk it flagged is
gone — Flowable is BPMN-native, so a future demand for visual authoring is a feature, not a rewrite.
