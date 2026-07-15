# 22. Integration and Data Exchange

> **Source:** PSSTEC ESG360 FDD v1.0, section 22. **Indicative release:** R0 (framework) / per-release adapters.
> Read `CLAUDE.md` (root) before implementing. Cross-functional rules BR-001–BR-020 (spec 24) apply to this module.


Defines inbound/outbound integration capabilities for enterprise and external ESG data.

| **Req. ID** | **Requirement**            | **Functional Description**                                                                           | **Priority** | **Acceptance Criteria**                                                       |
|-------------|----------------------------|------------------------------------------------------------------------------------------------------|--------------|-------------------------------------------------------------------------------|
| 22-001      | REST API                   | Provide versioned APIs for master, metrics, submissions, factors, disclosures and evidence metadata. | Must         | API follows authentication, validation, idempotency and pagination standards. |
| 22-002      | Event API                  | Publish domain events through webhook or message broker.                                             | Should       | Subscribers can replay from retained offset where supported.                  |
| 22-003      | File ingestion             | Support CSV/XLSX/SFTP/object-storage ingestion.                                                      | Must         | Files are virus scanned, schema validated and quarantined on failure.         |
| 22-004      | ERP connectors             | Provide adapters for D365, SAP, Oracle and NetSuite patterns.                                        | Should       | Connector exposes mapping, schedule and reconciliation status.                |
| 22-005      | HR connectors              | Support HR/workforce systems through API/file.                                                       | Should       | Personal data minimisation rules apply.                                       |
| 22-006      | Utility and meter data     | Ingest utility bills, meters, BMS, SCADA and IoT streams.                                            | Should       | Time-series deduplication and quality flags are supported.                    |
| 22-007      | Travel and expense         | Ingest travel, hotel, car hire and expenses.                                                         | Should       | Source categories map to Scope 3 methods.                                     |
| 22-008      | External datasets          | Integrate factor, climate hazard, water stress, biodiversity and screening providers.                | Should       | Licence, version and usage rights are tracked.                                |
| 22-009      | Master-data reconciliation | Reconcile source-system masters with ESG hierarchy.                                                  | Must         | Unmapped records enter stewardship queue.                                     |
| 22-010      | Outbound journal/API       | Return approved ESG status or allocations to enterprise systems.                                     | Could        | Outbound posting is controlled and reconcilable.                              |
| 22-011      | Developer portal           | Publish OpenAPI specs, examples, credentials and sandbox.                                            | Should       | Tenant admins control application registrations.                              |
| 22-012      | Integration monitoring     | Show jobs, records, errors, retries and latency.                                                     | Must         | Operators can reprocess without duplication.                                  |

## Outputs and Analytics

- Integration health

- Data reconciliation

- Unmapped records

- API usage and errors



## Acceptance test cases (from Master Test Catalogue, spec 48)

Implementation of this module is not complete until these pass.

| **Test ID** | **Title**                                            | **Module**     | **Preconditions**                                  | **Test Steps**                                                                                            | **Expected Result**                                                                                       | **Type**      | **Priority** |
|---|---|---|---|---|---|---|---|
| INT-001     | File import quarantine                               | Integration    | Import template available                          | Upload file containing malware test signature.                                                            | File is quarantined; no rows are processed; alert generated.                                              | Functional    | High         |
| INT-002     | Integration retry idempotency                        | Integration    | External API temporarily fails                     | Retry same batch after recovery.                                                                          | No duplicate records; job reconciles source count and target count.                                       | Functional    | High         |
| INT-100     | Integration: create valid record                     | Integration    | Configured tenant, roles and representative data   | Execute create valid record; inspect UI/API response, audit event and downstream status.                  | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| INT-101     | Integration: reject missing mandatory field          | Integration    | Configured tenant, roles and representative data   | Execute reject missing mandatory field; inspect UI/API response, audit event and downstream status.       | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| INT-102     | Integration: validate effective date overlap         | Integration    | Configured tenant, roles and representative data   | Execute validate effective date overlap; inspect UI/API response, audit event and downstream status.      | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| INT-103     | Integration: enforce scoped access                   | Integration    | Configured tenant, roles and representative data   | Execute enforce scoped access; inspect UI/API response, audit event and downstream status.                | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| INT-104     | Integration: bulk import mixed valid/invalid rows    | Integration    | Configured tenant, roles and representative data   | Execute bulk import mixed valid/invalid rows; inspect UI/API response, audit event and downstream status. | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| INT-105     | Integration: export authorised data                  | Integration    | Configured tenant, roles and representative data   | Execute export authorised data; inspect UI/API response, audit event and downstream status.               | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
| INT-106     | Integration: retain revision history                 | Integration    | Configured tenant, roles and representative data   | Execute retain revision history; inspect UI/API response, audit event and downstream status.              | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
| INT-107     | Integration: trigger approval workflow               | Integration    | Configured tenant, roles and representative data   | Execute trigger approval workflow; inspect UI/API response, audit event and downstream status.            | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
| INT-108     | Integration: send overdue escalation                 | Integration    | Configured tenant, roles and representative data   | Execute send overdue escalation; inspect UI/API response, audit event and downstream status.              | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
| INT-109     | Integration: filter dashboard by entity              | Integration    | Configured tenant, roles and representative data   | Execute filter dashboard by entity; inspect UI/API response, audit event and downstream status.           | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
