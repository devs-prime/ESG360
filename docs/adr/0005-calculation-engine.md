# ADR-0005: Calculation engine — manifest-hash determinism, AST evaluator, inline lineage

**Status:** Proposed
**Drivers:** §38, §49, BR-002, BR-019, NFR-003, FAC-002, CAR-001..004, CAT-001

## Context

This is the subsystem the product's credibility rests on. §38 requires deterministic,
explainable, replayable calculation with lineage to every input, factor and formula version.
NFR-003 requires 1M activity records inventoried within 60 minutes. Formulas are
**user-authored metadata**, not code.

## Decision

### 1. Determinism via a hashed manifest

`manifest_hash + engine_version → identical output, forever`

Resolve every result-affecting input into a canonically serialized **CalculationManifest**:
tenant, boundary and consolidation method, period, org hierarchy version, metric versions,
formula versions, factor versions, GWP set version, conversion table version, allocation rules,
rounding policy version, engine semantic version. SHA-256 it. Store on `CalculationRun`.

### 2. Lineage inline on leaf results — **not** as edges

```
calc_result(tenant_id, id, run_id,
            activity_id, factor_version_id, formula_version_id,
            gwp_set_id, conversion_id, allocation_ratio,
            gas, gas_mass, co2e, ...)
```
`lineage_edge` is used **only** for aggregate→child and disclosure→result.

### 3. Restricted grammar → AST → interpreter

Never `eval`, never dynamic compilation of tenant strings, never tenant-generated SQL.
Validate and compile at **publish** time; published formula versions are immutable.

### 4. Append-only runs

A correction is a **new run**. Results are never mutated.

## Rationale

**Why the manifest hash.** It buys three otherwise-hard properties at once:
- *Replay* (BR-019): re-run the manifest; differing output hash ⇒ engine regression.
- *Assurance*: "prove this number" — show the manifest and replay it live for the assurer.
- *CI gate*: the §49 validation datasets become **golden manifests**. Any engine change that
  alters a golden output hash fails the build. This one test protects the product's core claim.

**Why inline lineage — the key performance insight.** A literal reading of §38 gives ~5 edge
rows per activity record → **5M rows per run** at NFR-003's 1M scale. That write amplification,
not the arithmetic, is the real threat to NFR-003. Inline columns collapse it:
- "Where did this number come from?" → read the row.
- "Which results used factor X?" (§7 recalculation triggers) → index scan on `factor_version_id`.
- Aggregation edges are orders of magnitude fewer.

Net: ~1M result rows + a small edge set, instead of ~6M.

**Why an AST is mandatory anyway.** CAT-001 requires circular-reference detection at publish
time — that is a DAG cycle check over the formula graph. Once you have the AST for validation,
interpreting it is the safe and obvious execution strategy.

## Throughput check (NFR-003)

```
1,000,000 records / 3,600 s = 278 records/sec required
```
Per record: cached factor resolution + a few decimal ops + a row emit. Factor and conversion
tables are small and fully cacheable. Partition by org-node/source across ~8 workers; write via
`COPY` in batches. **Realistic: 2–6 minutes → 10–30× headroom.**

The headroom exists *because* of the inline-lineage decision. Verify with a 1M-record load test
in **Release 0**, not Release 3.

## Consequences

**Positive:** replayable, auditable, assurance-ready by construction; restatement (CAR-004) and
factor-revision impact analysis (§7) fall out naturally; performance headroom.

**Negative:**
- Canonical serialization must be exactly stable (key order, decimal formatting, null handling).
  A sloppy serializer silently breaks every hash. Write it once, test it hard, freeze it.
- `engine_version` must bump on *any* semantic change; golden hashes then need deliberate,
  reviewed re-baselining with a documented reason.
- Interpreted ASTs are slower than compiled — irrelevant given 10–30× headroom, and worth it
  for the safety.
