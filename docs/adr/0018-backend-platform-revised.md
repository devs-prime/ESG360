# ADR-0018: Backend platform (revised) — Java 21 + Spring Boot 3 + Flowable

**Status:** Accepted (signed off 2026-07-15) — **supersedes [ADR-0001](0001-backend-platform.md)**
**Date:** 2026-07-15
**Drivers:** §35, §40, §52 (team model), ADR-0009's stated reversal trigger

## Context — and an honest note on why this reverses

ADR-0001 chose .NET 8 and named two reversal conditions: *team is Java-native*, or *BPMN is a
hard requirement*. **Neither strictly fired.** The team is a generalist junior team
(C/C++, some JS and Java, AI-assisted), not Java-native, and no customer has demanded BPMN.

So this ADR reverses ADR-0001 on a **different ground than ADR-0001 anticipated** — which is
worth stating plainly rather than quietly re-using the old trigger.

**The decision driver changed.** ADR-0001 optimised for *integration ergonomics* (D365, Entra)
and *decimal syntax*. Those mattered when the implicit assumption was a senior team per §52. With
the actual team, the dominant question becomes:

> **How much hard, subtle, from-scratch systems code must a junior team write?**

Under that lens the answer inverts, because of one thing: **Flowable**.

## Decision

**Java 21 LTS + Spring Boot 3 + Flowable (embedded)**.

## Rationale

1. **It deletes the riskiest thing we were going to build.** ADR-0009 committed to a custom
   state-machine engine with durable timers, business calendars, escalation, compensation and
   instance migration — the largest block of hard systems engineering in the whole plan, on my
   lowest-confidence ADR. Flowable is that engine, embedded, mature, and free. A junior team
   should not be writing durable timer recovery from scratch. See ADR-0019.
2. **ADR-0001's Microsoft argument was overweighted.** D365 adapters are REST. Entra is
   standards-based OIDC/SAML, which Spring Security handles natively. Azure Blob and Service Bus
   have first-class Java SDKs. The real advantage was narrower than ADR-0001 implied.
3. **The team already has some Java** — more than they have C#. Small factor, but it points the
   same way rather than against.
4. **The decimal argument survives the flip.** ADR-0001 was right that `BigDecimal` is uglier and
   more error-prone than `decimal`. But decimal errors are **caught by tests** (ADR-0005's golden
   datasets assert exact equality; a wrong `MathContext` fails the build). Workflow errors —
   a timer that silently doesn't fire after a pod restart — are **not** caught by tests. Buy
   protection against the class of bug you can't test for.

## Consequences

**Positive:** ADR-0009's custom engine is deleted; §40 is met by a mature engine (see ADR-0019);
Testcontainers/ArchUnit/jOOQ ecosystem is excellent for the guardrails this architecture depends on.

**Negative — real, and juniors will feel them:**
- **`BigDecimal` verbosity and `MathContext` discipline.** `a.multiply(b)` not `a * b`, and
  `divide()` throws on non-terminating results without an explicit scale.
  → **Mitigation:** a `Quantity` value type wrapping `BigDecimal` + unit. Juniors never touch raw
  `BigDecimal` arithmetic. Division is pinned inside it: `divide(b, 12, RoundingMode.HALF_EVEN)`,
  exactly per ADR-0006. Plus an ArchUnit rule banning `float`/`double`/`Float`/`Double` in domain
  packages.
- **Spring's magic is a genuine junior trap** — autoconfiguration, AOP proxies, "why is my bean
  null", `@Transactional` silently not applying to self-invocation.
  → **Mitigation:** constructor injection only (no field injection), no `@Transactional` on
  private/self-invoked methods, explicit configuration over autoconfiguration in the calc and
  audit paths.
- Loses .NET's first-party Azure/D365 SDK polish. Acceptable per point 2.

## Java version

**Java 21 LTS** — named in §35, maximum ecosystem maturity, fully supported by Spring Boot 3 and
Flowable. Java 25 LTS exists; consider it only once Flowable and Spring Boot support is confirmed.
Do not lead on the version.

## Reversal condition

Reverse to .NET 8 only if you hire a .NET-native senior team **and** decide to buy or drop the
workflow requirement. Both would have to be true.
