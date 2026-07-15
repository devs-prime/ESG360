# 30. Integration Architecture

> **Source:** PSSTEC ESG360 v1.0, section 30 (SDD).


Integration uses four patterns: synchronous REST APIs for transactional operations, asynchronous messaging for domain events, scheduled/bulk pipelines for enterprise datasets and streaming ingestion for meters/IoT. A canonical ESG data contract decouples source-specific fields from product domains.

| **Pattern**      | **Controls**                                                                                                         |
|------------------|----------------------------------------------------------------------------------------------------------------------|
| REST             | OAuth2 client credentials, scopes, rate limits, idempotency keys, schema validation, pagination and correlation IDs. |
| Events           | At-least-once delivery, unique event IDs, retry/dead-letter, ordering key where required and consumer replay policy. |
| Files            | Signed/encrypted transfer, template version, malware scan, quarantine, row validation and reconciliation.            |
| Streaming        | Device/source identity, timestamp tolerance, deduplication, unit validation, late-arrival handling and backpressure. |
| Outbound exports | Approval snapshot, classification, checksum manifest, expiry and download audit.                                     |

