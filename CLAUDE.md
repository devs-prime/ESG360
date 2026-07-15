# ESG360 — Claude Code Project Instructions

Multi-tenant SaaS platform for ESG, sustainability and carbon intelligence.
Source of truth: PSSTEC ESG360 FDD/SDD/TDD v1.0 (July 2026), decomposed into `specs/`.

## How to work in this repo

1. **Never implement from memory of "how ESG platforms usually work."** Every module has a
   spec file in `specs/modules/`. Read it fully before planning. Read the linked
   acceptance test cases at the bottom of each module spec — they define "done."
2. **One module per session.** Do not start a second module in the same session.
   Before coding, produce a plan and wait for approval.
3. **Update `PROGRESS.md`** at the end of every session: status change + one-line note
   of decisions made or deviations from spec (deviations require an explicit entry).
4. **Contract-first.** The database schema (`specs/architecture/33-*.md`,
   `specs/technical/39-*.md`) and API standards (`specs/technical/37-*.md`) are frozen
   contracts. If a module needs a schema/API change, stop and propose it — do not
   improvise tables or endpoints.
5. **Build order** follows `specs/delivery/51-recommended-product-releases.md`:
   R0 Foundation → R1 Data & Carbon → R2 Reporting & Assurance → R3 Social/Gov/Supplier
   → R4 Transition/Climate/AI. Do not build a module whose dependencies are not done.

## Spec map

| Path | Contents |
|---|---|
| `specs/00-product-overview.md` | Purpose, design principles, frameworks, personas |
| `specs/modules/NN-*.md` | One functional module each (FDD §2–25) + its acceptance tests |
| `specs/architecture/` | SDD §26–35: logical architecture, data model, security, NFRs, tech baseline |
| `specs/technical/` | TDD §36–45: service boundaries, API, calc engine, DB rules, workflow, AI, DevSecOps |
| `specs/testing/` | Test strategy, full catalogue, calculation validation dataset, defect severity |
| `specs/delivery/` | Release plan, team model, out-of-scope, risks, glossary, traceability |

## Non-negotiable invariants (from spec 24, BR-001–BR-020 — full text in `specs/modules/24-*.md`)

These override convenience in every module. Violating any of these is a defect, not a style choice.

- **Multi-tenancy:** every business table carries `tenant_id`; tenant is resolved from the
  trusted token server-side, never from request payload. Cross-tenant reads/writes must be
  impossible at the query layer (row-level security), not just the controller layer. (BR: SEC-001)
- **Precision:** quantitative values store original value/unit AND normalised value/unit.
  All arithmetic is decimal — never binary float. Rounding happens only at presentation. (BR-001, BR-002)
- **Immutability & versioning:** published disclosures use immutable snapshots; metric/formula/
  factor/mapping changes never retroactively alter closed periods; restatements preserve
  originals with reason + approval. Controlled definitions are versioned and effective-dated.
  Master records used in history are inactivated, never deleted. (BR-003, BR-004, BR-005, BR-012, BR-014)
- **Append-only:** submissions and calculation results are append-only revisions; corrections
  supersede, never overwrite. Audit events are append-only.
- **Determinism:** calculation runs are repeatable — same inputs + same versions = same output,
  with an execution manifest/hash. Lineage edges link every result to every input, factor and
  formula version. (BR-019, spec 38)
- **Idempotency:** all create/import APIs honour an `Idempotency-Key` / source-system key. (BR-011)
- **Human accountability for AI:** AI output is labelled, grounded only in authorised tenant
  content, never auto-approves anything, and never bypasses workflow. Restricted data never
  goes to external models. (BR-006, spec 42)
- **Privacy:** workforce PII is suppressed in aggregates below the configured threshold;
  PII columns are separated/encrypted and excluded from broad analytics. (BR-007)
- **Supplier isolation:** supplier users can never see another supplier's records or buyer
  internal notes. (BR-008)
- **Timestamps:** stored UTC, rendered in user/tenant timezone. (BR-017)
- **Auditability:** every write emits an audit event and domain event after commit; exports
  are logged with user, purpose, filters, classification. Legal hold overrides retention. (BR-016, BR-020)
- **Offsets vs inventory:** offsets, certificates, avoided emissions and inventory reductions
  are always reported separately — never netted silently. (BR-009)

## Architecture

**Read `docs/ARCHITECTURE.md` before your first plan in any module.** Decisions live in
`docs/adr/` — one file per decision, with rationale and reversal triggers. If you are about to
make a choice that contradicts an ADR, stop and propose a superseding ADR instead.

The one-line shape: **modular monolith in .NET 8 over PostgreSQL, deployed as independent cells,
where all ESG quantities are decimal, all controlled records are append-only, and every published
number carries a replayable manifest hash.**

### Stack (⚠ = awaiting human sign-off, do not scaffold until confirmed)

**Revised 2026-07-15** after the team shape was known: ADR-0018 supersedes 0001 (Java, not .NET),
ADR-0019 supersedes 0009 (Flowable, not a custom engine), ADR-0020 amends 0016 (no Kubernetes yet).

- Backend: **Java 21 LTS + Spring Boot 3** (ADR-0018) — ✅ signed off 2026-07-15
- Workflow: **Flowable, embedded, same DataSource/transaction** (ADR-0019).
  **Never hand-roll workflow orchestration, timers or state machines.**
- Database: ⚠ **PostgreSQL 16+** (ADR-0002) — forced by the sovereign/private-cloud profiles
- DB access: **jOOQ** (ADR-0017). Bulk paths use raw JDBC `CopyManager`. Not Hibernate/JPA.
- Migrations: **Flyway**, plain SQL, expand/contract only
- Build: **Maven** multi-module — one module per §36 boundary
- Frontend: **Next.js + TypeScript**, self-hosted. Supplier/assurer portal is a **separate app**.
  WCAG 2.2 AA + i18n + RTL from day one (ADR-0015).
- Architecture style: **modular monolith**, schema-per-module, small shared kernel (ADR-0004),
  boundaries enforced by **ArchUnit tests in CI**
- Tenancy: shared schema + **RLS**, composite `(tenant_id, id)` keys, **cell** topology (ADR-0003)
- Numbers: **`BigDecimal` via a `Quantity` value type** — never raw BigDecimal arithmetic in
  domain code, never `float`/`double`/`Float`/`Double`. Division is pinned inside `Quantity`:
  `divide(b, 12, RoundingMode.HALF_EVEN)` (ADR-0006, ADR-0018).
- Messaging: **no broker in v1** — outbox dispatches **in-process** (ADR-0017).
  **The transactional outbox is still mandatory** (ADR-0008).
- Object storage: **content-addressed**, quarantine + ClamAV, object-lock legal hold (ADR-0012)
- Time-series: native PostgreSQL partitioning + BRIN; no separate store yet (ADR-0010)
- Analytics: read replicas + matviews; lakehouse deferred (ADR-0011)
- AI: provider-neutral gateway **sidecar**, never load-bearing (ADR-0013)
- Identity: OIDC/SAML via standards-based IdP, Spring Security
- Tests: **JUnit 5 + Testcontainers** (never H2 — RLS can't be tested in-memory), ArchUnit,
  REST Assured, k6/Gatling for NFR-002/003
- CI: **GitHub Actions** + CodeQL + Dependabot + CycloneDX SBOM + cosign
- Infra: **Terraform**; **managed containers, not Kubernetes** (ADR-0020); residency is **cell
  placement**, never application logic (ADR-0016)
- Cloud: **deliberately undecided** — do not let it block work (ADR-0017)

## Scope

**Read `V1-SCOPE.md`.** v1 is one cell, shared tenancy only, Scope 1+2, one framework, one
reporting cycle. Do not build deferred modules, even if their spec file is present and inviting.

## API rules (spec 37 — applies to every endpoint)

- Base path `/api/v1`; JSON UTF-8; ISO 8601 timestamps; decimals serialised losslessly (strings if needed)
- OAuth2 scopes + tenant/organisation claims; Problem Details errors with correlation ID
- Cursor pagination on list endpoints; ETag/If-Match optimistic concurrency
- OpenAPI spec updated in the same PR as any endpoint change

## Database rules (spec 39 — applies to every table)

- Standard columns: `tenant_id, created_at, created_by, updated_at, updated_by, row_version`
- `decimal/numeric` for all quantities and money; UTC `timestamptz`; date-only stays `date`
- FKs + unique constraints enforce tenant consistency; partition high-volume tables by tenant/time
- Soft delete only for non-controlled drafts

## Definition of done (per module)

1. All acceptance test cases at the bottom of the module spec are implemented as automated
   tests and pass (IDs preserved in test names, e.g. `carbon_CAR_001_scope1_stationary_combustion`).
2. Calculation-touching modules also pass the validation dataset in `specs/testing/49-*.md`.
3. No BR-001–BR-020 violation. Lint + typecheck clean. OpenAPI updated. `PROGRESS.md` updated.

## Never do

- Never store floats for quantities or money
- Never resolve tenant from request body/query params
- Never hard-delete controlled or historically-referenced records
- Never let a schema "just for this module" bypass the shared data model
- Never mark a module done with failing or skipped acceptance tests
- Never modify files under `specs/` (they are the contract; propose changes instead)
- Never contradict an ADR silently — propose a superseding ADR in `docs/adr/`
- Never add a cross-tenant query, or use a DB role with `BYPASSRLS` (ADR-0003, ADR-0011)
- Never publish a domain event outside the transaction that wrote the data (ADR-0008)
- Never add to the shared kernel without an ADR (ADR-0004)
- Never let a controlled workflow transition depend on an AI call (ADR-0013)
- Never hand-roll workflow state machines, durable timers or escalation — use Flowable (ADR-0019)
- Never use raw `BigDecimal` arithmetic in domain code — use the `Quantity` type (ADR-0018)
- Never test RLS against H2 or an in-memory DB — Testcontainers only (ADR-0017)
- Never build a module that `V1-SCOPE.md` defers, however tempting its spec file looks
