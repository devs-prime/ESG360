-- ESG360 migration baseline (item 0.2). Plain SQL, expand/contract (ADR-0016/0017).
-- V001: schemas and the reusable functions every later migration relies on.
--
-- Schema-per-concern (ADR-0004): `shared` holds the FK-able shared kernel; `audit`
-- and `messaging` hold the cross-cutting append-only log and transactional outbox.
create schema if not exists shared;
create schema if not exists audit;
create schema if not exists messaging;

-- UUIDv7 (RFC 9562): time-ordered ids for index locality on high-volume tables
-- (ADR-0003). PostgreSQL 16 has no native uuidv7(); PostgreSQL 18 does, at which
-- point this can be dropped (expand/contract). Randomness comes from the core
-- gen_random_uuid(), so no pgcrypto extension is required.
create or replace function shared.uuidv7()
returns uuid
language plpgsql
parallel safe
as $func$
declare
  ts_ms bigint := (extract(epoch from clock_timestamp()) * 1000)::bigint;
  b bytea := decode(replace(gen_random_uuid()::text, '-', ''), 'hex');
begin
  b := set_byte(b, 0, ((ts_ms >> 40) & 255)::int);
  b := set_byte(b, 1, ((ts_ms >> 32) & 255)::int);
  b := set_byte(b, 2, ((ts_ms >> 24) & 255)::int);
  b := set_byte(b, 3, ((ts_ms >> 16) & 255)::int);
  b := set_byte(b, 4, ((ts_ms >> 8) & 255)::int);
  b := set_byte(b, 5, (ts_ms & 255)::int);
  b := set_byte(b, 6, ((get_byte(b, 6) & 15) | 112)::int); -- version 7 in the high nibble
  b := set_byte(b, 8, ((get_byte(b, 8) & 63) | 128)::int); -- RFC variant 0b10
  return encode(b, 'hex')::uuid;
end;
$func$;

-- Optimistic-concurrency + audit-column maintenance: bump row_version and updated_at
-- on every UPDATE. Application code still guards writes with WHERE row_version = ?
-- (spec 37 ETag/If-Match); this trigger keeps the stored counter honest.
create or replace function shared.touch_row_version()
returns trigger
language plpgsql
as $func$
begin
  new.row_version := old.row_version + 1;
  new.updated_at := now();
  return new;
end;
$func$;

-- The single, reviewed way to make a table tenant-isolated (ADR-0003). Applying RLS
-- by hand per table is exactly where a junior team slips; every business table calls
-- this instead. ENABLE + FORCE (owner is subject too) + a policy keyed off the
-- per-transaction GUC app.tenant_id, set via SET LOCAL (never session — PgBouncer
-- transaction pooling would otherwise leak it across tenants).
create or replace function shared.apply_tenant_rls(target regclass)
returns void
language plpgsql
as $func$
begin
  execute format('alter table %s enable row level security', target);
  execute format('alter table %s force row level security', target);
  execute format($pol$
    create policy tenant_isolation on %s
      using (tenant_id = nullif(current_setting('app.tenant_id', true), '')::uuid)
      with check (tenant_id = nullif(current_setting('app.tenant_id', true), '')::uuid)
  $pol$, target);
end;
$func$;
