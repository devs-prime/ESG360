-- V004: the two cross-cutting infrastructure tables every module writes to.
-- Structure + grant model only; the hash-chain, WORM sealing and outbox relay are
-- item 0.6. Both are range-partitioned by time; a default partition is created here
-- so the baseline works, with monthly-partition rotation deferred to 0.6.

-- ---------------------------------------------------------------------------
-- audit.audit_event — append-only, tamper-evident log (ADR-0007, BR-016/020).
-- Append-only is enforced at the grant layer, not in application code: esg360_app
-- was granted select/insert only on this schema (V002) and never update/delete, so
-- even a compromised app cannot rewrite history. Hash-chain columns (prev_hash/hash)
-- are populated by the audit service in 0.6.
-- ---------------------------------------------------------------------------
create table audit.audit_event (
  id uuid not null default shared.uuidv7(),
  tenant_id uuid not null,
  occurred_at timestamptz not null default now(),
  actor uuid,
  action text not null,
  object_type text not null,
  object_id uuid,
  result text,
  source_ip inet,
  source_device text,
  reason text,
  before_hash bytea,
  after_hash bytea,
  prev_hash bytea,
  hash bytea,
  primary key (tenant_id, id, occurred_at),
  constraint audit_event_tenant_fk foreign key (tenant_id) references shared.tenant (id)
) partition by range (occurred_at);

create table audit.audit_event_default partition of audit.audit_event default;

-- RLS on the parent AND every partition. Policies do NOT propagate to partitions, so a
-- partition without its own policy is readable cross-tenant if directly named. The 0.6
-- monthly-rotation routine must call apply_tenant_rls on every partition it creates.
select shared.apply_tenant_rls('audit.audit_event');
select shared.apply_tenant_rls('audit.audit_event_default');

-- Append-only, and PARENT-ONLY grant: privileges are never granted on partitions, so
-- access flows through the RLS-guarded parent and `select ... from audit_event_default`
-- is denied outright. select/insert only — never update/delete/truncate (ADR-0007).
grant select, insert on audit.audit_event to esg360_app;

-- ---------------------------------------------------------------------------
-- messaging.outbox — transactional outbox (ADR-0008). Written in the SAME
-- transaction as the business change + audit event; a separate relay (0.6) reads
-- undispatched rows and publishes, then sets dispatched_at. Never publish events
-- outside the writing transaction.
-- ---------------------------------------------------------------------------
create table messaging.outbox (
  id uuid not null default shared.uuidv7(),
  tenant_id uuid not null,
  aggregate_type text not null,
  aggregate_id uuid not null,
  event_type text not null,
  payload jsonb not null,
  occurred_at timestamptz not null default now(),
  dispatched_at timestamptz,
  primary key (tenant_id, id, occurred_at),
  constraint outbox_tenant_fk foreign key (tenant_id) references shared.tenant (id)
) partition by range (occurred_at);

create table messaging.outbox_default partition of messaging.outbox default;

-- RLS on parent + partition (see the audit note above), and parent-only grant. The
-- relay marks rows dispatched, so update is allowed; delete/truncate are not (pruning
-- runs as a separate maintenance role in 0.6).
select shared.apply_tenant_rls('messaging.outbox');
select shared.apply_tenant_rls('messaging.outbox_default');
grant select, insert, update on messaging.outbox to esg360_app;

-- Relay drains by (tenant_id, occurred_at); index the undispatched tail.
create index outbox_undispatched_idx on messaging.outbox (tenant_id, occurred_at)
  where dispatched_at is null;
