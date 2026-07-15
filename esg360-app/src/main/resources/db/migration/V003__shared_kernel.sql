-- V003: the shared kernel (ADR-0004) — Tenant, OrganisationNode, ReportingPeriod,
-- Unit and MetricDefinition. Frozen skeletons: enough structure to be the FK-able
-- contract every feature module builds on. Modules 0.4 (Tenant/Org) and 0.7
-- (Catalogue) enrich these via expand migrations and own their behaviour + endpoints.
--
-- Conventions pinned here (spec 39 is principle-level; see docs/data-model-conventions.md):
--   * Composite primary key (tenant_id, id); id uuid default shared.uuidv7().
--   * Every FK carries tenant_id → cross-tenant references are structurally impossible.
--   * Standard columns on every table: tenant_id, created_at/by, updated_at/by, row_version.
--   * timestamptz (UTC, BR-017); date stays date; numeric for quantities (BR-001/002).

-- ---------------------------------------------------------------------------
-- Tenant — the isolation root (control-plane managed).
-- Unlike business tables, Tenant is NOT force-RLS: provisioning a new tenant
-- happens with no tenant context, done by the owner/control-plane role. The app
-- role (esg360_app) is still policy-bound to its own tenant row, and cannot INSERT.
-- ---------------------------------------------------------------------------
create table shared.tenant (
  id uuid primary key default shared.uuidv7(),
  name text not null,
  status text not null default 'provisioned'
    check (status in ('provisioned', 'active', 'suspended', 'closed')),
  subscription_tier text,
  residency_region text,
  group_currency text,
  created_at timestamptz not null default now(),
  created_by uuid not null,
  updated_at timestamptz not null default now(),
  updated_by uuid not null,
  row_version integer not null default 1
);

alter table shared.tenant enable row level security;
create policy tenant_self_isolation on shared.tenant
  using (id = nullif(current_setting('app.tenant_id', true), '')::uuid)
  with check (id = nullif(current_setting('app.tenant_id', true), '')::uuid);
-- Provisioning and control-plane fields (status, tier, residency) are owner-managed:
-- the app role may read its own tenant row but neither create nor mutate it.
revoke insert, update on shared.tenant from esg360_app;

create trigger trg_tenant_touch
  before update on shared.tenant
  for each row execute function shared.touch_row_version();

-- ---------------------------------------------------------------------------
-- OrganisationNode — effective-dated hierarchy + boundary (module 02, ORG-001).
-- ---------------------------------------------------------------------------
create table shared.organisation_node (
  id uuid not null default shared.uuidv7(),
  tenant_id uuid not null,
  node_type text not null check (node_type in (
    'group', 'legal_entity', 'business_unit', 'site', 'facility', 'asset', 'project', 'cost_centre')),
  name text not null,
  parent_id uuid,
  boundary_method text check (boundary_method in (
    'equity_share', 'financial_control', 'operational_control')),
  effective_from date not null,
  effective_to date,
  is_active boolean not null default true,
  created_at timestamptz not null default now(),
  created_by uuid not null,
  updated_at timestamptz not null default now(),
  updated_by uuid not null,
  row_version integer not null default 1,
  primary key (tenant_id, id),
  constraint organisation_node_tenant_fk foreign key (tenant_id) references shared.tenant (id),
  constraint organisation_node_parent_fk foreign key (tenant_id, parent_id)
    references shared.organisation_node (tenant_id, id),
  constraint organisation_node_effective_dates check (effective_to is null or effective_to >= effective_from)
);

select shared.apply_tenant_rls('shared.organisation_node');

create trigger trg_organisation_node_touch
  before update on shared.organisation_node
  for each row execute function shared.touch_row_version();

-- ---------------------------------------------------------------------------
-- ReportingPeriod — the time axis other records bind to (module 02, BR-013).
-- ---------------------------------------------------------------------------
create table shared.reporting_period (
  id uuid not null default shared.uuidv7(),
  tenant_id uuid not null,
  period_type text not null check (period_type in ('monthly', 'quarterly', 'annual')),
  calendar_model text not null check (calendar_model in ('calendar', 'non_calendar', 'four_four_five')),
  start_date date not null,
  end_date date not null,
  status text not null default 'open' check (status in ('open', 'closed', 'reopened')),
  created_at timestamptz not null default now(),
  created_by uuid not null,
  updated_at timestamptz not null default now(),
  updated_by uuid not null,
  row_version integer not null default 1,
  primary key (tenant_id, id),
  constraint reporting_period_tenant_fk foreign key (tenant_id) references shared.tenant (id),
  constraint reporting_period_dates check (end_date >= start_date)
);

select shared.apply_tenant_rls('shared.reporting_period');

create trigger trg_reporting_period_touch
  before update on shared.reporting_period
  for each row execute function shared.touch_row_version();

-- ---------------------------------------------------------------------------
-- Unit — tenant-scoped unit reference with exact decimal conversion (BR-001/018).
-- ---------------------------------------------------------------------------
create table shared.unit (
  id uuid not null default shared.uuidv7(),
  tenant_id uuid not null,
  code text not null,
  dimension text not null,
  factor_to_base numeric not null,
  base_unit_code text not null,
  is_active boolean not null default true,
  created_at timestamptz not null default now(),
  created_by uuid not null,
  updated_at timestamptz not null default now(),
  updated_by uuid not null,
  row_version integer not null default 1,
  primary key (tenant_id, id),
  constraint unit_code_unique unique (tenant_id, code),
  constraint unit_tenant_fk foreign key (tenant_id) references shared.tenant (id)
);

select shared.apply_tenant_rls('shared.unit');

create trigger trg_unit_touch
  before update on shared.unit
  for each row execute function shared.touch_row_version();

-- ---------------------------------------------------------------------------
-- MetricDefinition — versioned, effective-dated catalogue entry (module 04, CAT-001).
-- Immutable version rows keyed by a stable logical_key (BR-014, spec 04 req 4-003).
-- ---------------------------------------------------------------------------
create table shared.metric_definition (
  id uuid not null default shared.uuidv7(),
  tenant_id uuid not null,
  logical_key uuid not null,
  version integer not null default 1,
  is_current boolean not null default true,
  name text not null,
  description text,
  topic text,
  data_type text not null,
  unit_code text,
  frequency text,
  aggregation text,
  confidentiality text not null default 'internal'
    check (confidentiality in ('public', 'internal', 'confidential', 'personal', 'restricted')),
  owner_user_id uuid,
  effective_from date not null,
  effective_to date,
  is_active boolean not null default true,
  created_at timestamptz not null default now(),
  created_by uuid not null,
  updated_at timestamptz not null default now(),
  updated_by uuid not null,
  row_version integer not null default 1,
  primary key (tenant_id, id),
  constraint metric_definition_version_unique unique (tenant_id, logical_key, version),
  constraint metric_definition_tenant_fk foreign key (tenant_id) references shared.tenant (id),
  constraint metric_definition_unit_fk foreign key (tenant_id, unit_code)
    references shared.unit (tenant_id, code),
  constraint metric_definition_effective_dates check (effective_to is null or effective_to >= effective_from)
);

select shared.apply_tenant_rls('shared.metric_definition');

create trigger trg_metric_definition_touch
  before update on shared.metric_definition
  for each row execute function shared.touch_row_version();
