# 6. Evidence, Lineage and Assurance

> **Source:** PSSTEC ESG360 FDD v1.0, section 6. **Indicative release:** R0 (core) / R2 (assurance rooms).
> Read `CLAUDE.md` (root) before implementing. Cross-functional rules BR-001–BR-020 (spec 24) apply to this module.


Maintains traceable evidence from source to disclosed value and supports internal/external assurance.

| **Req. ID** | **Requirement**     | **Functional Description**                                                            | **Priority** | **Acceptance Criteria**                                            |
|-------------|---------------------|---------------------------------------------------------------------------------------|--------------|--------------------------------------------------------------------|
| 6-001       | Evidence upload     | Attach files, links, source extracts, certificates and signed declarations.           | Must         | Evidence receives checksum, metadata and retention classification. |
| 6-002       | Evidence reuse      | Link one evidence item to multiple records without duplicating the file.              | Should       | Link history is visible.                                           |
| 6-003       | Source lineage      | Capture source system, transaction/file identifier, transformation and ingestion job. | Must         | User can trace value to source and transformations.                |
| 6-004       | Calculation lineage | Store input versions, factor, formula, unit conversions and result.                   | Must         | Recalculation is reproducible.                                     |
| 6-005       | Evidence room       | Create scoped assurance workspaces by period, framework and disclosure.               | Must         | Only explicitly shared records are accessible.                     |
| 6-006       | Sampling            | Select statistical or judgemental samples and record rationale.                       | Should       | Sample population and selection are preserved.                     |
| 6-007       | Assurance requests  | Raise questions, request evidence and track response SLA.                             | Must         | Request status and response are auditable.                         |
| 6-008       | Control testing     | Document control design, frequency, test steps, sample and conclusion.                | Should       | Failed tests create findings/actions.                              |
| 6-009       | Sign-off            | Capture electronic sign-off from responsible executives.                              | Must         | Sign-off binds to a fixed disclosure snapshot.                     |
| 6-010       | Audit export        | Export evidence index, lineage and activity history.                                  | Must         | Export includes checksum manifest.                                 |

## Key Business Rules

- Evidence deletion must follow retention policy and legal-hold status.

- Approved/disclosed snapshots are immutable; corrections create new versions.



## Acceptance test cases (from Master Test Catalogue, spec 48)

Implementation of this module is not complete until these pass.

| **Test ID** | **Title**                                            | **Module**     | **Preconditions**                                  | **Test Steps**                                                                                            | **Expected Result**                                                                                       | **Type**      | **Priority** |
|---|---|---|---|---|---|---|---|
| EVD-001     | Evidence checksum and reuse                          | Evidence       | User can upload                                    | Upload file and link to two submissions.                                                                  | One stored object with checksum; two links; downloads audited.                                            | Functional    | High         |
| EVD-002     | Legal hold blocks deletion                           | Evidence       | Evidence on legal hold                             | Run retention purge.                                                                                      | Held evidence remains and purge report records exclusion.                                                 | Functional    | High         |
| EVD-100     | Evidence: create valid record                        | Evidence       | Configured tenant, roles and representative data   | Execute create valid record; inspect UI/API response, audit event and downstream status.                  | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| EVD-101     | Evidence: reject missing mandatory field             | Evidence       | Configured tenant, roles and representative data   | Execute reject missing mandatory field; inspect UI/API response, audit event and downstream status.       | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| EVD-102     | Evidence: validate effective date overlap            | Evidence       | Configured tenant, roles and representative data   | Execute validate effective date overlap; inspect UI/API response, audit event and downstream status.      | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| EVD-103     | Evidence: enforce scoped access                      | Evidence       | Configured tenant, roles and representative data   | Execute enforce scoped access; inspect UI/API response, audit event and downstream status.                | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| EVD-104     | Evidence: bulk import mixed valid/invalid rows       | Evidence       | Configured tenant, roles and representative data   | Execute bulk import mixed valid/invalid rows; inspect UI/API response, audit event and downstream status. | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| EVD-105     | Evidence: export authorised data                     | Evidence       | Configured tenant, roles and representative data   | Execute export authorised data; inspect UI/API response, audit event and downstream status.               | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
| EVD-106     | Evidence: retain revision history                    | Evidence       | Configured tenant, roles and representative data   | Execute retain revision history; inspect UI/API response, audit event and downstream status.              | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
| EVD-107     | Evidence: trigger approval workflow                  | Evidence       | Configured tenant, roles and representative data   | Execute trigger approval workflow; inspect UI/API response, audit event and downstream status.            | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
| EVD-108     | Evidence: send overdue escalation                    | Evidence       | Configured tenant, roles and representative data   | Execute send overdue escalation; inspect UI/API response, audit event and downstream status.              | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
| EVD-109     | Evidence: filter dashboard by entity                 | Evidence       | Configured tenant, roles and representative data   | Execute filter dashboard by entity; inspect UI/API response, audit event and downstream status.           | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
