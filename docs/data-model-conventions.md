# ESG360 data-model conventions

Spec 39 (database design rules) is deliberately principle-level — it names the
standard columns and the invariants but pins no SQL types, key strategy, or
precision. Item 0.2 fills that gap. These conventions are now the contract every
migration and feature module follows; changing them is an ADR-level decision.

Traceability: spec 39, spec 33, ADR-0003 (tenancy/RLS), ADR-0004 (shared kernel),
ADR-0006 (decimals), ADR-0007 (audit), ADR-0008 (outbox), BR-001/002/012/014/017.

## Schemas

| Schema | Holds |
|---|---|
| `shared` | The FK-able shared kernel (Tenant, OrganisationNode, ReportingPeriod, Unit, MetricDefinition) and shared functions |
| `audit` | Append-only, tamper-evident audit log |
| `messaging` | Transactional outbox |

Feature modules get their own schema (schema-per-module, ADR-0004) and may FK only
into `shared`.

## Standard columns (every business table)

```
id           uuid        not null default shared.uuidv7()
tenant_id    uuid        not null
created_at   timestamptz not null default now()
created_by   uuid        not null
updated_at   timestamptz not null default now()
updated_by   uuid        not null
row_version  integer     not null default 1
primary key (tenant_id, id)
```

- **`id` is UUIDv7** (time-ordered) for index locality (ADR-0003). `shared.uuidv7()`
  provides it until PostgreSQL 18's native function is available.
- **Composite PK `(tenant_id, id)`** and **every FK includes `tenant_id`**, so
  cross-tenant references are structurally impossible, not merely validated.
- **`created_by` / `updated_by`** are user ids; the sentinel
  `00000000-0000-0000-0000-000000000000` denotes a system/control-plane actor until
  the identity module (0.5) lands.
- **`row_version`** is bumped by the `shared.touch_row_version()` trigger on UPDATE;
  application writes still guard with `WHERE row_version = ?` (spec 37 If-Match).

## Types

- **Quantities and money:** `numeric` (never `float`/`double`) — ADR-0006. Store
  original value/unit AND normalised value/unit (BR-001); round only at presentation.
- **Timestamps:** `timestamptz`, always UTC (BR-017). Date-only stays `date`.
- **Enumerations:** `text` + `CHECK` constraint (readable in psql, cheap to evolve via
  expand/contract) rather than native `enum` types.

## Tenancy / RLS

- Business tables: `ENABLE` + `FORCE ROW LEVEL SECURITY` via `shared.apply_tenant_rls()`,
  policy `tenant_id = nullif(current_setting('app.tenant_id', true), '')::uuid`.
- Tenant context is set **per transaction** with `SET LOCAL` / `set_config(..., true)`,
  never session-scoped (PgBouncer transaction pooling would leak it).
- The runtime role `esg360_app` has **no `BYPASSRLS`**, is not a superuser, and holds
  no DDL rights. The `tenant` table is the one exception to FORCE RLS: it is
  control-plane managed (provisioning has no tenant context) and the app role cannot
  INSERT into it.

## Lifecycle

- **No `DELETE` grant** to the app role anywhere — controlled records are inactivated
  via `is_active` / status, never hard-deleted (BR-012).
- **Append-only** tables (audit) additionally withhold `UPDATE`; corrections supersede.
- **Versioned definitions** (MetricDefinition) use immutable version rows keyed by a
  stable `logical_key` + `version` (BR-014); closed-period results never change
  retroactively (BR-004).

## Partitioning

High-volume activity, audit, and telemetry tables are `RANGE`-partitioned by time
(spec 39). The 0.2 baseline creates a `DEFAULT` partition so inserts work; monthly
rotation and pruning are item 0.6.
