# 41. Document and Evidence Technical Design

> **Source:** PSSTEC ESG360 v1.0, section 41 (TDD).


- Client requests upload session; service creates tenant-scoped object key and short-lived signed URL.

- Object is quarantined until malware scan, file-type validation and checksum complete.

- Evidence metadata stores SHA-256 checksum, size, MIME type, uploader, classification, period and retention rule.

- Preview/text extraction occurs in isolated processing environment; original file remains unchanged.

- Download requires authorisation and is logged; confidential downloads may be watermarked.

- Published snapshot stores evidence manifest and hashes, not uncontrolled mutable links.

- Legal hold blocks lifecycle deletion and records hold authority/reason.

