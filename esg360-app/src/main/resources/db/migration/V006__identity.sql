-- V006: the identity module (module 03) — users, roles, scoped assignments, SoD and
-- session policy. Owns schema `identity` (ADR-0004: a module owns its schema; §36 puts
-- users/roles/policies in the Tenant/Identity boundary).
--
-- Conventions are V003's (see docs/data-model-conventions.md): composite (tenant_id, id)
-- PKs, every FK carries tenant_id, apply_tenant_rls on every business table, standard
-- columns, no DELETE grant. Two rules bite hardest in this module:
--   * "Deleted user accounts retain attribution on historical records" (spec 03) — app_user
--     is never deleted, only moved to status 'deactivated'. The absent DELETE grant makes
--     that structural rather than a convention.
--   * "All denied and privileged actions must be logged" (spec 03) — enforced by the
--     identity service writing audit.audit_event in the same transaction, not here.
create schema if not exists identity;
grant usage on schema identity to esg360_app;

-- ---------------------------------------------------------------------------
-- permission — the platform permission catalogue.
--
-- Deliberately NOT tenant-scoped and NOT RLS-guarded: permission codes are static
-- platform reference data defined by the code that checks them, not tenant business
-- data. Tenants cannot invent permissions, so there is nothing to isolate. This is the
-- one table in the module without tenant_id; the BR "every business table carries
-- tenant_id" is about business data, and a compile-time code list is not that.
-- Read-only to the app role — rows arrive via migration only.
-- ---------------------------------------------------------------------------
create table identity.permission (
  code text primary key,
  description text not null
);

grant select on identity.permission to esg360_app;

insert into identity.permission (code, description) values
  ('identity.user.read',              'View users'),
  ('identity.user.write',             'Invite, update and deactivate users'),
  ('identity.role.read',              'View roles and their permissions'),
  ('identity.role.write',             'Create and update roles'),
  ('identity.role_assignment.read',   'View role assignments'),
  ('identity.role_assignment.write',  'Grant and revoke role assignments'),
  ('identity.sod.read',               'View segregation-of-duties rules and exceptions'),
  ('identity.sod.write',              'Create and update segregation-of-duties rules'),
  ('identity.sod.approve_exception',  'Approve a documented segregation-of-duties exception'),
  ('identity.session_policy.read',    'View the tenant session policy'),
  ('identity.session_policy.write',   'Change the tenant session policy');

-- ---------------------------------------------------------------------------
-- app_user — a human principal, federated from the tenant's IdP (req 3-001).
--
-- Identity lives in the IdP; this table is the local projection that role assignments
-- hang off. (issuer, external_subject) is the trusted join key from the validated token
-- — never email, which is mutable and re-assignable at the IdP.
--
-- No is_active column: `status` already carries it, and two overlapping liveness flags
-- is how a stale one ends up authorising someone.
-- ---------------------------------------------------------------------------
create table identity.app_user (
  id uuid not null default shared.uuidv7(),
  tenant_id uuid not null,
  issuer text not null,
  external_subject text not null,
  email text not null,
  display_name text not null,
  status text not null default 'invited'
    check (status in ('invited', 'active', 'suspended', 'deactivated')),
  created_at timestamptz not null default now(),
  created_by uuid not null,
  updated_at timestamptz not null default now(),
  updated_by uuid not null,
  row_version integer not null default 1,
  primary key (tenant_id, id),
  constraint app_user_tenant_fk foreign key (tenant_id) references shared.tenant (id),
  constraint app_user_subject_unique unique (tenant_id, issuer, external_subject)
);

select shared.apply_tenant_rls('identity.app_user');

-- Email is case-insensitively unique per tenant. A plain unique constraint would let
-- Ada@x.com and ada@x.com coexist and collide at login-time matching.
create unique index app_user_email_unique on identity.app_user (tenant_id, lower(email));

create trigger trg_app_user_touch
  before update on identity.app_user
  for each row execute function shared.touch_row_version();

-- ---------------------------------------------------------------------------
-- role — a named bundle of permissions (req 3-003). Tenant-scoped: tenants tailor
-- their own roles. System roles (is_system) are seeded at tenant provisioning and
-- may not be edited or unassigned from their permissions by tenant admins.
-- ---------------------------------------------------------------------------
create table identity.role (
  id uuid not null default shared.uuidv7(),
  tenant_id uuid not null,
  code text not null,
  name text not null,
  description text,
  is_system boolean not null default false,
  is_active boolean not null default true,
  created_at timestamptz not null default now(),
  created_by uuid not null,
  updated_at timestamptz not null default now(),
  updated_by uuid not null,
  row_version integer not null default 1,
  primary key (tenant_id, id),
  constraint role_tenant_fk foreign key (tenant_id) references shared.tenant (id),
  constraint role_code_unique unique (tenant_id, code)
);

select shared.apply_tenant_rls('identity.role');

create trigger trg_role_touch
  before update on identity.role
  for each row execute function shared.touch_row_version();

-- ---------------------------------------------------------------------------
-- role_permission — role → permission catalogue edge.
-- The role side carries tenant_id (tenant-inclusive FK); the permission side does not,
-- because identity.permission is global reference data (see above).
-- ---------------------------------------------------------------------------
create table identity.role_permission (
  tenant_id uuid not null,
  role_id uuid not null,
  permission_code text not null,
  created_at timestamptz not null default now(),
  created_by uuid not null,
  primary key (tenant_id, role_id, permission_code),
  constraint role_permission_role_fk foreign key (tenant_id, role_id)
    references identity.role (tenant_id, id),
  constraint role_permission_permission_fk foreign key (permission_code)
    references identity.permission (code)
);

select shared.apply_tenant_rls('identity.role_permission');

-- ---------------------------------------------------------------------------
-- role_assignment — user + role + scope, effective-dated (req 3-003: "entity, site,
-- metric and framework scope").
--
-- scope_node_id NULL means tenant-wide; a non-NULL node scopes the grant to that
-- organisation subtree (SEC-103, SEC-109). Subtree expansion is a query concern, not
-- a stored closure — organisation_node is effective-dated and a materialised closure
-- would silently go stale against it.
--
-- scope_metric_logical_key intentionally has NO foreign key. Metric scope must survive
-- metric versioning, so it references the logical metric, not a version row — but
-- shared.metric_definition is keyed (tenant_id, logical_key, version) and has no
-- unique (tenant_id, logical_key) to point at. Item 0.7 (Catalogue) is expected to
-- introduce the logical parent table; this column gets its FK then. Until it does,
-- tenant consistency here rests on tenant_id + the application check, which is weaker
-- than every other FK in the schema. Flagged in PROGRESS.md.
-- ---------------------------------------------------------------------------
create table identity.role_assignment (
  id uuid not null default shared.uuidv7(),
  tenant_id uuid not null,
  user_id uuid not null,
  role_id uuid not null,
  scope_node_id uuid,
  scope_metric_logical_key uuid,
  scope_framework text,
  effective_from date not null,
  effective_to date,
  is_active boolean not null default true,
  created_at timestamptz not null default now(),
  created_by uuid not null,
  updated_at timestamptz not null default now(),
  updated_by uuid not null,
  row_version integer not null default 1,
  primary key (tenant_id, id),
  constraint role_assignment_tenant_fk foreign key (tenant_id) references shared.tenant (id),
  constraint role_assignment_user_fk foreign key (tenant_id, user_id)
    references identity.app_user (tenant_id, id),
  constraint role_assignment_role_fk foreign key (tenant_id, role_id)
    references identity.role (tenant_id, id),
  constraint role_assignment_scope_node_fk foreign key (tenant_id, scope_node_id)
    references shared.organisation_node (tenant_id, id),
  constraint role_assignment_effective_dates
    check (effective_to is null or effective_to >= effective_from),
  -- NULLS NOT DISTINCT (PostgreSQL 15+): without it, two tenant-wide grants of the same
  -- role to the same user both have scope_node_id NULL, PostgreSQL treats those NULLs as
  -- distinct, and the duplicate slips through.
  constraint role_assignment_unique unique nulls not distinct
    (tenant_id, user_id, role_id, scope_node_id, scope_metric_logical_key, scope_framework, effective_from)
);

select shared.apply_tenant_rls('identity.role_assignment');

create index role_assignment_by_user on identity.role_assignment (tenant_id, user_id)
  where is_active;
create index role_assignment_by_scope_node on identity.role_assignment (tenant_id, scope_node_id)
  where is_active and scope_node_id is not null;

create trigger trg_role_assignment_touch
  before update on identity.role_assignment
  for each row execute function shared.touch_row_version();

-- ---------------------------------------------------------------------------
-- sod_rule — a conflicting role pair (req 3-005, SEC-002). The canonical conflicts are
-- submitter/approver and administrator/auditor; `custom` lets a tenant add its own.
--
-- The pair is unordered: {A,B} and {B,A} are the same conflict. `role_a_id < role_b_id`
-- forces a canonical order so the unique constraint actually prevents duplicates —
-- without it, both orderings insert cleanly and one of them stops being enforced.
-- ---------------------------------------------------------------------------
create table identity.sod_rule (
  id uuid not null default shared.uuidv7(),
  tenant_id uuid not null,
  role_a_id uuid not null,
  role_b_id uuid not null,
  conflict_type text not null
    check (conflict_type in ('submitter_approver', 'admin_auditor', 'custom')),
  description text not null,
  -- 'block' refuses the assignment outright; 'require_exception' refuses it unless an
  -- approved, unexpired sod_exception covers it (req 3-005: "blocked OR require
  -- documented exception").
  enforcement text not null default 'block'
    check (enforcement in ('block', 'require_exception')),
  is_active boolean not null default true,
  created_at timestamptz not null default now(),
  created_by uuid not null,
  updated_at timestamptz not null default now(),
  updated_by uuid not null,
  row_version integer not null default 1,
  primary key (tenant_id, id),
  constraint sod_rule_tenant_fk foreign key (tenant_id) references shared.tenant (id),
  constraint sod_rule_role_a_fk foreign key (tenant_id, role_a_id)
    references identity.role (tenant_id, id),
  constraint sod_rule_role_b_fk foreign key (tenant_id, role_b_id)
    references identity.role (tenant_id, id),
  constraint sod_rule_canonical_pair check (role_a_id < role_b_id),
  constraint sod_rule_unique unique (tenant_id, role_a_id, role_b_id)
);

select shared.apply_tenant_rls('identity.sod_rule');

create trigger trg_sod_rule_touch
  before update on identity.sod_rule
  for each row execute function shared.touch_row_version();

-- ---------------------------------------------------------------------------
-- sod_exception — the documented, approved, expiring exception that req 3-005 allows
-- in place of an outright block.
--
-- approved_by <> requested_by is enforced here, not just in the service: an SoD
-- exception a user can approve for themselves is not an exception, it is a bypass, and
-- that check is too important to leave to a code path someone can forget to call.
-- ---------------------------------------------------------------------------
create table identity.sod_exception (
  id uuid not null default shared.uuidv7(),
  tenant_id uuid not null,
  sod_rule_id uuid not null,
  user_id uuid not null,
  scope_node_id uuid,
  reason text not null,
  status text not null default 'pending'
    check (status in ('pending', 'approved', 'rejected', 'revoked')),
  requested_by uuid not null,
  requested_at timestamptz not null default now(),
  approved_by uuid,
  approved_at timestamptz,
  expires_at timestamptz,
  created_at timestamptz not null default now(),
  created_by uuid not null,
  updated_at timestamptz not null default now(),
  updated_by uuid not null,
  row_version integer not null default 1,
  primary key (tenant_id, id),
  constraint sod_exception_tenant_fk foreign key (tenant_id) references shared.tenant (id),
  constraint sod_exception_rule_fk foreign key (tenant_id, sod_rule_id)
    references identity.sod_rule (tenant_id, id),
  constraint sod_exception_user_fk foreign key (tenant_id, user_id)
    references identity.app_user (tenant_id, id),
  constraint sod_exception_approver_fk foreign key (tenant_id, approved_by)
    references identity.app_user (tenant_id, id),
  constraint sod_exception_requester_fk foreign key (tenant_id, requested_by)
    references identity.app_user (tenant_id, id),
  constraint sod_exception_scope_node_fk foreign key (tenant_id, scope_node_id)
    references shared.organisation_node (tenant_id, id),
  constraint sod_exception_no_self_approval check (approved_by is null or approved_by <> requested_by),
  -- An approved exception must say who approved it and when; a pending one must not.
  constraint sod_exception_approval_complete check (
    (status = 'approved' and approved_by is not null and approved_at is not null)
    or (status <> 'approved')
  ),
  constraint sod_exception_pending_unapproved check (
    status <> 'pending' or (approved_by is null and approved_at is null)
  )
);

select shared.apply_tenant_rls('identity.sod_exception');

create index sod_exception_by_rule_user on identity.sod_exception (tenant_id, sod_rule_id, user_id)
  where status = 'approved';

create trigger trg_sod_exception_touch
  before update on identity.sod_exception
  for each row execute function shared.touch_row_version();

-- ---------------------------------------------------------------------------
-- session_policy — per-tenant session controls (req 3-009). One row per tenant.
--
-- Token lifetime and MFA are ultimately the IdP's to enforce; this table is the
-- tenant's declared policy, which the resource server validates incoming tokens
-- against (an over-long or non-MFA token is rejected even if the IdP minted it).
-- ---------------------------------------------------------------------------
create table identity.session_policy (
  id uuid not null default shared.uuidv7(),
  tenant_id uuid not null,
  inactivity_timeout_minutes integer not null default 30
    check (inactivity_timeout_minutes between 1 and 1440),
  max_token_lifetime_minutes integer not null default 60
    check (max_token_lifetime_minutes between 1 and 1440),
  max_concurrent_sessions integer not null default 5
    check (max_concurrent_sessions between 1 and 100),
  -- req 3-002: high-privilege access is blocked without MFA.
  require_mfa boolean not null default true,
  created_at timestamptz not null default now(),
  created_by uuid not null,
  updated_at timestamptz not null default now(),
  updated_by uuid not null,
  row_version integer not null default 1,
  primary key (tenant_id, id),
  constraint session_policy_tenant_fk foreign key (tenant_id) references shared.tenant (id),
  constraint session_policy_one_per_tenant unique (tenant_id)
);

select shared.apply_tenant_rls('identity.session_policy');

create trigger trg_session_policy_touch
  before update on identity.session_policy
  for each row execute function shared.touch_row_version();

-- Business tables: read/write, never delete (V002's envelope — controlled records are
-- inactivated, not removed). identity.permission stays select-only, granted above.
grant select, insert, update on
  identity.app_user,
  identity.role,
  identity.role_permission,
  identity.role_assignment,
  identity.sod_rule,
  identity.sod_exception,
  identity.session_policy
to esg360_app;
