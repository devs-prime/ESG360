# ADR-0009: Workflow — custom state machine + durable timers; defer BPMN

**Status:** ⛔ **Superseded by [ADR-0019](0019-workflow-revised.md)** (2026-07-15)
> Its own reversal trigger fired: ADR-0001 flipped to Java (ADR-0018), so Flowable became
> available embedded. This ADR's analysis of *why* an embedded engine beats a cluster still stands.
**Drivers:** §40, §35 ("BPMN-capable workflow engine **or** durable orchestration"), §21, NFR-019

## Context

§40 requires: versioned definitions with immutable published versions, human tasks with
candidate role/scope, business-calendar-aware durable timers, decision expressions, escalation,
compensation for non-atomic integrations, and controlled instance migration.

Workflow touches almost everything: Collection approval, Disclosure approval, Materiality
override, Supplier campaigns, Cases, Restatement approval.

## Decision

Build a **state-machine engine backed by the same PostgreSQL**, plus a **durable job/timer
scheduler**. **Defer a full BPMN engine.** Keep it behind a module interface.

## Rationale

1. **§35 explicitly permits "durable orchestration"** as an alternative to BPMN. This decision
   is within spec, not a deviation.
2. **The actual flows are state machines, not BPMN.** Collection (draft → submitted → in review
   → approved → locked), Disclosure (draft → review → approved → snapshot), Materiality
   (assessed → threshold → override → approved). These are role-gated transitions with timers
   and escalation. They do not need parallel gateways, event subgateways, or compensation
   choreography.
3. **Atomicity.** An in-process, same-database engine lets a workflow transition and the
   business change commit in **one transaction** (NFR-019). Camunda 8/Zeebe and Temporal are
   out-of-process clusters — every transition becomes a distributed transaction requiring
   outbox + compensation for what should be a single `UPDATE`.
4. **Ops burden and portability.** A Zeebe or Temporal cluster in *every cell* — including
   sovereign and private-cloud cells the customer operates (§28) — multiplies the operational
   surface across 20–40+ cells. That cost is real and recurring.
5. **.NET has no embeddable BPMN engine of Flowable/Camunda-7 quality** (ADR-0001's stated
   weakness). So the .NET options are: separate cluster, immature library, or own it. Owning a
   state machine is ~weeks; a BPMN engine is not what you'd be buying anyway.

## Consequences

**Positive:** transactional consistency; no extra cluster per cell; portable across all five
profiles; small and comprehensible.

**Negative — and these are serious:**
- **No visual process authoring for customers.** Enterprise ESG RFPs *do* ask for this.
- **You own workflow correctness**: durable timers, business calendars, escalation, instance
  migration (§40's hardest requirement) are genuinely fiddly.
- If §40's compensation and migration requirements harden, you will have rebuilt a worse BPMN
  engine — the classic trap.

## Alternatives

| Option | Why not now |
|---|---|
| **Camunda 7 / Flowable (embedded)** | Excellent — shares your transaction, mature, BPMN-native. **Java only.** If ADR-0001 flips to Java, **this decision should flip with it.** |
| Camunda 8 / Zeebe | Separate cluster per cell; breaks atomicity; licence cost. |
| Temporal | Superb durability, but code-first (not BPMN), out-of-process, and human-task/approval modelling is not its sweet spot. |
| Elsa 3 (.NET) | Closest .NET fit; younger and thinner ecosystem. **Re-evaluate at Release 2.** |

## Reversal trigger — watch this actively

Reverse if **any** of:
- Visual BPMN authoring by customers appears as a confirmed requirement in two or more RFPs
- Instance migration (§40) proves materially harder than estimated in Release 0/1
- ADR-0001 flips to Java (then take Flowable immediately — the pairing is the point)

**Mitigation that makes reversal affordable:** keep every workflow interaction behind the
Workflow module's interface. No feature module ever touches workflow state directly. Then
swapping the engine is a module replacement, not a rewrite.

Review this ADR explicitly at the **end of Release 0**.
