## What

<!-- One paragraph: what this PR does and which PROGRESS.md item / spec module it belongs to. -->

**PROGRESS.md item:** <!-- e.g. 0.4 Tenant, Organisation & Boundary -->
**Spec:** <!-- e.g. specs/modules/02-tenant-organisation-boundary.md -->

## Checklist (CLAUDE.md — definition of done)

- [ ] Acceptance test cases from the module spec implemented, IDs preserved in test names
- [ ] No BR-001–BR-020 violation (multi-tenancy, decimals, append-only, determinism, …)
- [ ] No floats/doubles; all quantity arithmetic goes through `Quantity`
- [ ] Tenant never resolved from request payload; RLS covers every new table
- [ ] New tables carry the standard columns (spec 39) and tenant-consistent FKs
- [ ] OpenAPI updated in this PR if any endpoint changed
- [ ] Migrations are expand/contract only
- [ ] `PROGRESS.md` updated (status + one-line note; deviations get an explicit entry)
- [ ] No `specs/` file modified

## Deviations from spec

<!-- "None" or list each with rationale; deviations also go into PROGRESS.md. -->
