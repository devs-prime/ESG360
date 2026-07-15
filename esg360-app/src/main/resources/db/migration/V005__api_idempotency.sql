-- V005: API idempotency store (spec 37, ADR-0014, BR-011, COL-002).
-- Keyed by (tenant_id, idem_key, endpoint); stores the original response so a replay
-- returns the same outcome. Tenant-scoped + RLS like every business table. High-churn:
-- pruned by expires_at via a maintenance job in item 0.6 (no DELETE grant to the app).
create schema if not exists api;
grant usage on schema api to esg360_app;

create table api.idempotency_key (
  tenant_id uuid not null,
  idem_key text not null,
  endpoint text not null,
  request_hash text not null,
  response_status integer,
  response_body text,
  created_at timestamptz not null default now(),
  expires_at timestamptz not null,
  primary key (tenant_id, idem_key, endpoint),
  constraint idempotency_key_tenant_fk foreign key (tenant_id) references shared.tenant (id)
);

select shared.apply_tenant_rls('api.idempotency_key');

grant select, insert, update on api.idempotency_key to esg360_app;
