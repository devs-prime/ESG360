# ADR-0008: Transactional outbox for audit and domain events

**Status:** Proposed
**Drivers:** §37 ("every write emits audit event and relevant domain event after transaction commit"), §30, §36, NFR-019, NFR-017

## Context

§37 requires every write to emit an audit event and a domain event *after commit*. §36 defines
the published events per module. §30 requires at-least-once delivery with unique event IDs,
retry and dead-letter.

The naive implementation — commit the transaction, then publish to the broker — is a
dual-write. If the process dies between the two, the event is lost forever and no error is
raised. In a system whose value proposition is auditability, silently missing events are a
data-integrity defect that surfaces years later as unexplainable gaps.

## Decision

1. **Business change + `AuditEvent` + `outbox` row are written in one ACID transaction.**
2. A **relay process** reads the outbox and publishes to the broker, marking rows dispatched.
3. Events carry a **unique event ID**; consumers are **idempotent** (at-least-once, not
   exactly-once).
4. Ordering key where required (§30) — typically `(tenant_id, aggregate_id)`.
5. Dead-letter + replay policy per §30.

## Rationale

The outbox is what makes "after commit" honest. Either the business change and its events all
land, or none do. There is no window in which the database says one thing and the event stream
says another.

This is not optional in a system with BR-003/BR-004/BR-005 immutability guarantees and a 7–10
year audit horizon (NFR-012). Every alternative trades a silent, unbounded correctness hole for
a small amount of implementation convenience.

Consumers being idempotent is not a nicety: at-least-once delivery means duplicates *will*
occur, and BR-011 already requires idempotent ingestion, so the discipline is present anyway.

## Consequences

**Positive:** no lost events; no dual-write; audit completeness provable; replay possible;
degrades safely (broker outage → outbox grows → drains on recovery, per NFR-017's queue
buffering and graceful degradation).

**Negative:**
- Publication latency is relay-poll-bound (typically <1s). Fine — §7's consistency model already
  places cross-module reactions in the eventual bucket.
- The outbox table is high-churn: partition it, and prune dispatched rows aggressively.
- The relay needs at-least-once semantics and its own monitoring (queue depth is a §44 metric).
- Every consumer must be idempotent. Test it explicitly — replay each event twice in CI.

## Anti-pattern to reject explicitly

> "Just publish the event in the same method after `SaveChanges()`."

This is the dual-write. It will lose events under crash, deploy, and OOM. Do not ship it.
