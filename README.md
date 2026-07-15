# ESG360 — Claude Code workspace

Decomposed from `PSSTEC_ESG360_Standalone_Product_FDD_SDD_TDD_Test_Cases_v1_0_1.docx`.

- `CLAUDE.md` — auto-loaded by Claude Code every session. Rules, stack, invariants.
- `V1-SCOPE.md` — what v1 actually is, and the competency gates. **Read before planning.**
- `PROGRESS.md` — build ledger. Updated every session.
- `docs/ARCHITECTURE.md` + `docs/adr/` — how it will be built, and why not otherwise.
- `specs/` — the contract. One file per module (with its acceptance tests attached),
  plus architecture, technical, testing and delivery folders. Read-only by convention.

## Building

Requires JDK 21 (Temurin) — everything else is pinned by the Maven wrapper.

```
./mvnw verify              # full build: format check, unit + architecture tests, ITs, SBOM
./mvnw verify -DskipITs    # without Docker (integration tests need Linux containers)
./mvnw spotless:apply      # fix formatting
docker compose up -d       # local PostgreSQL 16 for running the app (not used by tests)
```

## Getting started

1. ~~Sign off the pending decisions in PROGRESS.md~~ **Done 2026-07-15**: ADR-0018 (Java 21 +
   Spring Boot + Flowable), ADR-0002 (PostgreSQL), ADR-0017 (toolchain) and V1-SCOPE.md are all
   signed off. Cloud provider remains deliberately open — the DB is locally hosted for now.
2. `claude` in this directory, then start with PROGRESS.md item 0.1.
3. Suggested opening prompt:
   "Read CLAUDE.md, V1-SCOPE.md and PROGRESS.md. We're starting item 0.1. Read specs/technical/43-devsecops-and-release-management.md and specs/architecture/35-recommended-technology-baseline.md, then propose a repo scaffolding plan. Don't write code until I approve the plan."

## Regenerating specs after a document revision

`pandoc -t gfm --wrap=none NEW_DOC.docx -o full_doc.md && python3 tools/split_specs.py`
(run from the directory containing full_doc.md; it rebuilds `esg360-project/specs/`).
