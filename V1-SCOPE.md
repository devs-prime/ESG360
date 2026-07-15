# ESG360 v1 — Scope

> **Status:** Signed off 2026-07-15, alongside ADR-0017/0018/0019/0020.
> Nothing here is deleted. Everything cut stays in `specs/` and returns on the roadmap.

---

## Why cut

`specs/delivery/51-*.md` plans five releases across 25 modules, and `52-*.md` assumes a team with
a solution architect, security engineer, SRE, QA architect, carbon accountants and ESG SMEs.
Release 0 alone is scoped at 4–5 months **with that team**.

Your team is BTech grads with C/C++, some JS and Java, working with AI assistance. That is not
"less capable" — but it is differently shaped, and the plan must match the team you have rather
than the team the document assumed.

**The cut is not about ambition. It's about proving the core claim once, end to end, before
building 25 modules on top of it.** If v1 can produce a Scope 1+2 inventory that replays
bit-identically two years later and survives an assurer's questions, the hard part is done and
everything else is addition. If it can't, no amount of breadth saves the product.

---

## In scope for v1

**One cell. Shared tenancy only. Scope 1 + 2. One framework. One reporting cycle.**

| Area | v1 includes | Spec |
|---|---|---|
| Tenancy | Shared multi-tenant only, RLS + composite keys + cell (**one** cell) | ADR-0003, modules/02 |
| Identity | OIDC SSO, RBAC, SoD, access review | modules/03 |
| Audit | Append-only + hash chain + WORM archive | ADR-0007 |
| Organisation | Hierarchy, boundaries, effective-dated (ORG-001) | modules/02 |
| Catalogue | Metric definitions, formulas, validations, versioning | modules/04 |
| Workflow | Flowable — approval flows only | ADR-0019, modules/21 |
| Collection | Cycles, tasks, submissions, review, approval, period close | modules/05 |
| Evidence | Upload, quarantine, checksum, lineage links | ADR-0012, modules/06 |
| Factors | Emission factor library, versioning, validity dates | modules/07 |
| **Calculation** | **Engine + manifest hash + replay + golden datasets** | ADR-0005, modules/08 |
| **Carbon** | **Scope 1 + Scope 2 (location & market)** | modules/08 |
| Disclosure | **One** framework, immutable snapshot, export | modules/18 |
| Dashboard | Basic — inventory by scope/period/org | modules/19 |
| Import | CSV / Excel with idempotency + quarantine | modules/22 |

**Framework choice:** driven by your first customer, not by us. GRI or ISSB are the usual
starting points. Pick **one**. The mapping engine's value only appears at framework two —
building for N frameworks before you have one paying customer is speculative.

---

## Deferred (in `specs/`, not in v1)

| Deferred | Why |
|---|---|
| **Scope 3 (15 categories)** | Larger than Scope 1+2 combined; every category is its own method. Do it after the engine is proven. |
| Social, Governance, Materiality | R3 breadth; no bearing on the core claim |
| Supplier portal & due diligence | Second app, second auth realm, external attack surface (ADR-0015). Big. |
| Targets, Climate risk, Scenarios | R4 |
| AI copilots | ADR-0013 makes AI non-load-bearing by design → deferrable by design |
| Carbon credits & instruments | §53 already excludes trading/custody; the rest is R2 |
| Cross-tenant benchmarking | Needs the separate anonymised pipeline (ADR-0011) + DPIA + consent |
| Assurance rooms | Evidence/lineage core ships in v1; the assurer workspace doesn't |
| Dedicated / sovereign / private cells | Architecture supports them (ADR-0003/0016). Don't *operate* them yet. |
| ERP connectors, IoT streaming | CSV/Excel import covers v1. Connectors are per-customer work. |
| Lakehouse | ADR-0011 already defers it |
| Kubernetes | ADR-0020 |
| Message broker | ADR-0017 — outbox → in-process dispatcher |

---

## Competency gates — the part that matters most

Claude will carry more of this than you might expect: boilerplate, migrations, tests, learning
Java fast, code review, explaining Flowable. That's real leverage and you should use it hard.

**What it will not reliably carry is domain correctness in regulated methodology.**

Whether your Scope 3 spend-based factor mapping conforms to the GHG Protocol, whether your
market-based Scope 2 instrument hierarchy is right, whether a restatement threshold is
defensible — on these, a confident wrong answer looks exactly like a right one. And §50 says a
materially incorrect published calculation is **Critical severity, zero open**.

So these are **gates, not advice**:

| Gate | Who | Blocks |
|---|---|---|
| **Carbon accountant sign-off** | A qualified GHG practitioner | Factor library, calculation methodology, the §49 golden datasets, GWP set handling, restatement rules, base-year recalculation |
| **Security review** | Someone senior and security-minded | Tenancy/RLS, identity/SoD, evidence access, audit chain, any endpoint touching another tenant's ID space |

The §49 golden datasets are the leverage point: **once a carbon accountant validates the expected
outputs, they're locked into CI forever** (ADR-0005) and every future change is checked against a
human expert's judgement automatically. That is the highest-value hour a domain expert can spend
on this project. Get it early.

You don't need these people full-time. You do need them, and "Claude checked it" is not a
substitute for either.

---

## On timeline

**Don't plan against `specs/delivery/51-*.md`'s dates.** They assume §52's team.

Don't let anyone (including me) hand you a number instead. Do this:

1. Build items **0.1 → 0.4** (scaffolding, data model, API skeleton, Tenant/Org)
2. **Measure** what those actually took
3. Extrapolate from your own velocity

Four items in, you'll have a better estimate than any guess made today.

---

## Success criteria for v1

The bar is not "has features". It's:

1. A tenant collects Scope 1+2 activity data through an approval workflow with evidence attached
2. The engine produces an inventory that **replays bit-identically** from its manifest hash
3. A disclosure snapshot is published, immutable, with an evidence manifest of hashes
4. An assurer can pick any number and be shown its full lineage and a live replay
5. All §49 golden datasets pass, validated by a carbon accountant
6. Cross-tenant isolation tests pass on every merge
7. 1M-record calculation completes within NFR-003 (expect 10–30× headroom — verify it)

Hit those seven and you have a defensible product. Everything after is breadth.
