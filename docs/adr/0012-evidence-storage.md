# ADR-0012: Evidence — content-addressed object storage, quarantine pipeline, object-lock holds

**Status:** Proposed
**Drivers:** §41, §29, EVD-001, EVD-002, INT-001, BR-020, NFR-012

## Context

§41 requires: signed upload sessions, quarantine until malware scan and type validation,
SHA-256 checksums, isolated preview/text extraction, audited downloads, snapshot manifests of
hashes, and legal hold blocking deletion. EVD-001 requires one stored object linked to two
submissions. Evidence is uploaded by **suppliers** — i.e. by parties outside your trust boundary.

## Decision

1. **Content-addressed storage**: object key derives from the **SHA-256** of the content.
2. **Upload pipeline**: client requests session → short-lived signed URL → object lands in a
   **quarantine bucket** → malware scan + MIME/type validation + checksum → promote to the
   evidence bucket. Nothing skips quarantine.
3. **Evidence metadata** (DB) is separate from bytes (object storage): checksum, size, MIME,
   uploader, classification, period, retention rule, links.
4. **Links are many-to-one**: N `evidence_link` rows → 1 stored object (EVD-001).
5. **Legal hold** = DB flag **and** storage **object-lock**. Two independent enforcement layers.
6. **Preview/text extraction runs in an isolated sandbox**; the original file is never modified.
7. **Snapshots store an evidence manifest of hashes**, not mutable links (§41, DIS-001).
8. **Tenant prefix** in shared cells; **separate bucket** for dedicated/sovereign/private.

## Rationale

**Content addressing gives EVD-001 for free.** Two submissions attaching the same file produce
the same hash → the same object → one stored copy, two links. Deduplication isn't a feature you
build; it's a property of the key. It also makes the §41 checksum requirement and the snapshot
manifest requirement the *same mechanism*.

**Quarantine is mandatory because suppliers upload files.** A supplier-uploaded PDF is
attacker-controlled input reaching your parsers, your preview renderer, and — via §42 — an LLM
(see ADR-0013, AI-002). INT-001 tests exactly this.

**Two independent legal-hold layers** because BR-020 says legal hold overrides retention, and a
retention job with a bug should not be able to destroy evidence under hold. The object lock
holds even if the application is wrong.

## Consequences

**Positive:** dedupe by construction; checksums and manifests unified; EVD-002 and BR-020
enforced at the storage layer; strong tamper evidence for assurance.

**Negative:**
- **Deletion is subtle.** With dedupe, "delete evidence" means delete the *link*; the object may
  only be collected when no links and no holds remain, across the whole tenant. Get this wrong
  and you either leak storage or destroy someone else's evidence. Write this logic once, test it
  hard.
- Content-addressed keys are opaque — operators need the metadata DB to make sense of the bucket.
- Quarantine adds upload latency (scan time). Acceptable and non-negotiable.
- Hash-before-store requires streaming the hash during upload for large files.
