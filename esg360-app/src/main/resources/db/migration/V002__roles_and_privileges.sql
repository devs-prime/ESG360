-- V002: the runtime application role and its privilege envelope.
--
-- Flyway runs migrations as the owner/DDL role. The application connects only as
-- esg360_app (or a LOGIN role that inherits it), which:
--   * has NO BYPASSRLS and is not a superuser — RLS is inescapable (ADR-0003);
--   * has NO DDL rights — schema changes go through Flyway;
--   * gets DELETE on nothing — controlled records are inactivated, never hard-deleted
--     (BR-012); append-only tables additionally withhold UPDATE (set per schema below).
--
-- In a real deployment: `CREATE ROLE esg360_login LOGIN PASSWORD ...; GRANT esg360_app
-- TO esg360_login;`. Tests SET ROLE esg360_app on a throwaway superuser connection.
do $$
begin
  if not exists (select 1 from pg_roles where rolname = 'esg360_app') then
    create role esg360_app nologin noinherit nobypassrls;
  end if;
end;
$$;

grant usage on schema shared, audit, messaging to esg360_app;

-- Default privileges apply to tables the migration role creates AFTER this statement
-- (V003). No DELETE anywhere (inactivate-never-delete). Only the `shared` schema uses
-- schema-wide defaults — its tables are non-partitioned.
--
-- The `audit` and `messaging` schemas deliberately do NOT use schema-wide default
-- privileges: their tables are RANGE-partitioned, and a default-privilege grant would
-- also land on every leaf partition — which does not inherit the parent's RLS policy,
-- opening a direct-partition cross-tenant read. V004 grants on the PARENT tables only,
-- so access flows through the RLS-guarded parent and direct-partition access is denied.
alter default privileges in schema shared grant select, insert, update on tables to esg360_app;

-- Functions the app invokes at runtime (uuidv7 as a column default; touch_row_version
-- fires on UPDATE). apply_tenant_rls is DDL-time only and stays owner-only.
grant execute on function shared.uuidv7() to esg360_app;
grant execute on function shared.touch_row_version() to esg360_app;
