# ADR-0006: Precision — decimal everywhere, strings over JSON, pinned rounding

**Status:** Proposed
**Drivers:** BR-001, BR-002, BR-018, BR-019, §37, §39, NFR-019

## Context

BR-002: "All calculations shall preserve unrounded precision; rounding occurs only in configured
presentation layers." §37: "decimal values represented without binary floating-point loss."

This is the single most-violated rule in systems of this kind, because violating it produces
numbers that *look* right. A float error surfaces as a materially incorrect published
calculation — **Critical severity, zero open** (§50) — often years later, in front of an assurer.

## Decision

1. **`decimal` end to end.** In C#: `decimal`. In PostgreSQL: `numeric`. Never `double`,
   `float`, `real`, `float8`, `Single`.
2. **Ban the type mechanically.** Roslyn analyzer + architecture test: no floating-point type
   may appear in any domain or calculation assembly. Build fails.
3. **Division is the only precision-loss point** — pin it: **scale 12, `MidpointRounding.ToEven`**,
   versioned as part of the manifest's rounding policy (ADR-0005).
4. **Rounding only at presentation/disclosure**, governed by a **versioned rounding policy**
   object (per tenant/report), never ad hoc in a query or a view model.
5. **JSON serializes decimals as strings.** A JSON number is IEEE-754 in most parsers —
   `0.1 + 0.2` silently ceases to be `0.3` at the API boundary.
6. **Store original value + original unit + normalised value + normalised unit** (BR-001).
   Never discard what the user actually entered.
7. **Unit conversion uses approved, versioned conversion factors** (BR-018), part of the manifest.

## Rationale

Determinism (BR-019) requires that arithmetic be exactly reproducible. Binary floating point is
not associative and not reproducible across platforms/JIT versions — it is fundamentally
incompatible with "same input + same versions ⇒ same output".

.NET `decimal`: 128-bit, 28–29 significant digits, range ±7.9e28. Vastly beyond ESG needs
(largest plausible: ~1e9 kWh × factor ≈ 1e5 kgCO2e).

Pinning midpoint rounding matters: without an explicit mode, "round half up" vs "round half to
even" diverges on exact `.5` cases, which **do** occur in factor arithmetic. `ToEven` is the
IEEE/statistical default and avoids upward bias; the important property is that it's pinned and
versioned, not which one you pick.

## Consequences

**Positive:** exact, reproducible, defensible arithmetic; determinism achievable; no
floating-point class of defect can exist.

**Negative:**
- Decimal is slower than double. **Irrelevant here** — ADR-0005 shows 10–30× headroom.
- Decimals-as-strings surprises API consumers. Document it prominently in the OpenAPI spec
  (ADR-0014) and generated SDKs; it is a deliberate correctness choice.
- Every developer will at some point reach for `double`. The analyzer, not code review, is what
  stops them.

## Test hooks

- §49 golden datasets assert exact decimal equality, not tolerance-based comparison.
- Architecture test: zero floating-point types in domain assemblies.
- Round-trip test: decimal → JSON → decimal must be bit-identical.
