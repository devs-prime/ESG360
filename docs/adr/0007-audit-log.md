# ADR-0007: Audit log — append-only partitions, hash chain, WORM archive

**Status:** Proposed
**Drivers:** NFR-012 (7–10y retention), §29 (append-only/WORM, tamper evidence), §31, §44, BR-016, BR-020

## Context

Security and controlled-business events retained 7–10 years, append-only, tamper-evident, with
actor/action/object/before-after hash. Auditors must be able to trust that history wasn't edited.

## Decision

1. **Append-only PostgreSQL table, monthly partitions**, in the `audit` schema.
2. **`UPDATE` and `DELETE` grants revoked** from the application role. Insert only.
3. **Hash chain**: each event stores `prev_hash`; `hash = SHA256(prev_hash || canonical(event))`.
   Chain is per tenant per partition.
4. **Chain head published periodically to object storage with object-lock (WORM)**.
5. **Closed partitions archived** to object-lock storage; hot table stays small.
6. Audit events are written **in the same transaction as the business change** (ADR-0008).

## Rationale

**Why a hash chain and not a blockchain.** The auditor-facing property required is *tamper
evidence*: prove the log wasn't retroactively edited. A hash chain whose head is periodically
sealed into WORM storage delivers exactly that — altering any historical event breaks the chain
from that point forward, and the sealed head proves what the chain was at time T. Cost: one
SHA-256 per event. A distributed ledger would add enormous operational complexity for the same
property, and would fail the §28 private-cloud profile.

**Why partitions.** NFR-012's 7–10 year horizon at 10k tenants makes a single table unusable.
Monthly partitions let you archive and detach cold data while keeping recent audit queries fast,
and satisfy §39's partitioning rule.

**Why revoked grants.** Append-only enforced by application code is not append-only. Revoking
the grant means even a compromised application cannot rewrite history.

**Legal hold (BR-020) overrides retention** — hold flag blocks partition purge, and the object
lock enforces it at the storage layer independently.

## Consequences

**Positive:** cheap, portable, provable; no exotic infrastructure; survives the private-cloud
profile; auditors understand it.

**Negative:**
- Hash chaining serialises writes per chain. Mitigate by chaining **per tenant per partition**,
  not globally — concurrency stays high, and per-tenant is the only scope an auditor cares about.
- Canonical event serialization must be frozen and versioned (same discipline as ADR-0005's
  manifest). Change it carelessly and you invalidate historical verification.
- Archive retrieval is slower — acceptable for 7-year-old audit queries.

## Related

- BR-016: exports logged with user, purpose, filters, classification — these are audit events.
- §44: audit fields = actor, action, object, result, source IP/device, before/after hash, reason.
