# ADR-0001: Backend platform — .NET 8 LTS

**Status:** ⛔ **Superseded by [ADR-0018](0018-backend-platform-revised.md)** (2026-07-15)
> Retained for its reasoning, per the supersede-don't-edit rule. ADR-0018 explains why it
> reversed on a *different* ground than the reversal condition below anticipated.
**Drivers:** §35 (baseline permits ".NET 8/10 or Java 21"), §38, BR-002, §30 integrations

## Context

The spec deliberately leaves this open. Both platforms can build this system. The choice is
therefore decided by second-order fit, and by one thing the spec can't know: your team.

## Decision

**.NET 8 LTS**, C#, with the reversal condition below.

## Rationale

1. **Integration surface leans Microsoft.** §26/§30/§35 name D365 first among ERP adapters,
   Entra ID as the identity reference, Azure Blob and Service Bus as storage/messaging
   baselines. First-party SDK quality and enterprise auth ergonomics favour .NET here.
2. **`decimal` is native and safer-by-default than `BigDecimal`.** .NET's 128-bit decimal
   (28–29 significant digits, range ±7.9e28) is far beyond ESG needs, has natural operator
   syntax, and — importantly — division silently yields a fixed precision rather than
   throwing. Java's `BigDecimal` is arbitrary-precision (theoretically superior) but requires
   an explicit `MathContext` on every division or it throws `ArithmeticException` on
   non-terminating results, and its verbosity in a formula evaluator invites mistakes.
   For a determinism-critical engine (BR-019), .NET's fixed semantics are easier to make
   provably repeatable.
3. **EF Core + Npgsql** has mature PostgreSQL support including RLS session context, composite
   keys and `numeric` mapping — the exact features ADR-0002/0003 depend on.
4. **Talent pool.** Enterprise compliance/ESG engineering skews Microsoft; hiring for a
   multi-year, multi-team product (§52) is a real architectural input.

## Consequences

**Positive:** strong Entra/D365/Azure path; ergonomic decimal; single language across API and
calc engine; excellent analyzers for enforcing the float ban (ADR-0006).

**Negative — stated honestly:**
- **The workflow ecosystem is materially weaker than Java's.** Java has Flowable and Camunda 7
  as mature *embeddable* BPMN engines that share your transaction. .NET has no true equal
  (Elsa 3 is younger; Temporal is excellent but code-first and out-of-process). This is a real
  loss and is what drives ADR-0009. If §40's BPMN requirements harden, Java's advantage grows.
- Kubernetes/OSS-native tooling culture is marginally more Java-centric.

## Alternatives considered

| Option | Why not (today) |
|---|---|
| **Java 21 + Flowable/Camunda 7** | Genuinely competitive; **wins outright if BPMN is a hard requirement or the team is Java-native.** Loses on D365/Entra ergonomics and decimal verbosity. |
| Node/TypeScript | No native decimal; unsuitable for a determinism-critical financial-grade engine. Disqualified for the calculation core. |
| Go | Excellent ops story; `shopspring/decimal` is fine; but the domain modelling and workflow/human-task ecosystem is thin for a system this rule-heavy. |

## Reversal condition

**Flip to Java 21 + Flowable if either is true:**
- Your engineering team is predominantly Java-native (team skill beats every argument above —
  this decision is not worth retraining a team over), or
- Visual BPMN authoring by customers becomes a confirmed RFP requirement (then ADR-0009 also
  reverses, and Java's embeddable engines make both decisions cheap at once).

Reversal is cheapest **before Release 0 scaffolding** and rapidly becomes prohibitive after.
Decide now.
