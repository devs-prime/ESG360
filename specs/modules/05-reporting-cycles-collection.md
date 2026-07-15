# 5. Reporting Cycles and Data Collection

> **Source:** PSSTEC ESG360 FDD v1.0, section 5. **Indicative release:** R1.
> Read `CLAUDE.md` (root) before implementing. Cross-functional rules BR-001–BR-020 (spec 24) apply to this module.


Orchestrates recurring collection, submission, review, approval and close.

## Process Flow

6.  Create cycle and scope.

7.  Generate tasks and notify owners.

8.  Collect through UI, file, API or integration.

9.  Run validations and anomaly detection.

10. Review, resolve comments and approve.

11. Close period and create assurance snapshot.

| **Req. ID** | **Requirement**          | **Functional Description**                                                            | **Priority** | **Acceptance Criteria**                                      |
|-------------|--------------------------|---------------------------------------------------------------------------------------|--------------|--------------------------------------------------------------|
| 5-001       | Cycle setup              | Create annual, quarterly, monthly or ad hoc reporting cycles.                         | Must         | Cycle generates expected tasks for active metrics and scope. |
| 5-002       | Task generation          | Generate tasks by metric, entity, site and period.                                    | Must         | No duplicate active task for same assignment key.            |
| 5-003       | Manual entry             | Provide forms with instructions, prior-period values, validation and evidence upload. | Must         | Valid submission creates immutable revision and status.      |
| 5-004       | Bulk templates           | Generate and ingest controlled spreadsheet templates.                                 | Must         | Template identity and version are validated.                 |
| 5-005       | API ingestion            | Accept idempotent ingestion with source identifiers.                                  | Must         | Retries do not create duplicates.                            |
| 5-006       | Review workflow          | Route submissions through configurable review and approval stages.                    | Must         | Approval requires resolved blocking validations.             |
| 5-007       | Delegation               | Allow time-bound task delegation while retaining accountability.                      | Should       | Delegation is approved and logged.                           |
| 5-008       | Reminders and escalation | Send reminders and escalate overdue tasks.                                            | Must         | Notification follows tenant schedule and hierarchy.          |
| 5-009       | Period close             | Lock approved period after completion checks.                                         | Must         | Closed data cannot be changed through normal entry.          |
| 5-010       | Reopen and restate       | Reopen selected records or launch formal restatement.                                 | Must         | Reason, approver and impact are captured.                    |
| 5-011       | Comment threads          | Support contextual discussion, mentions and resolution.                               | Should       | Comments remain linked to record revision.                   |
| 5-012       | Offline/mobile capture   | Support constrained field capture with sync conflict handling.                        | Could        | Offline records synchronise without silent overwrite.        |

## Outputs and Analytics

- Collection progress dashboard

- Overdue tasks

- Validation exceptions

- Period close checklist



## Acceptance test cases (from Master Test Catalogue, spec 48)

Implementation of this module is not complete until these pass.

| **Test ID** | **Title**                                            | **Module**     | **Preconditions**                                  | **Test Steps**                                                                                            | **Expected Result**                                                                                       | **Type**      | **Priority** |
|---|---|---|---|---|---|---|---|
| COL-001     | Generate reporting tasks                             | Collection     | Cycle, scope and ownership configured              | Generate tasks.                                                                                           | One task is created per applicable metric/entity/period with correct owner.                               | Functional    | High         |
| COL-002     | Idempotent API submission                            | Collection     | Task exists                                        | POST same payload twice with same idempotency key.                                                        | One submission revision is created; second returns original outcome.                                      | Functional    | High         |
| COL-003     | Period close lock                                    | Collection     | All tasks approved                                 | Close period; attempt edit and normal API update.                                                         | Period closes and edits are blocked.                                                                      | Functional    | High         |
| COL-100     | Collection: create valid record                      | Collection     | Configured tenant, roles and representative data   | Execute create valid record; inspect UI/API response, audit event and downstream status.                  | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| COL-101     | Collection: reject missing mandatory field           | Collection     | Configured tenant, roles and representative data   | Execute reject missing mandatory field; inspect UI/API response, audit event and downstream status.       | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| COL-102     | Collection: validate effective date overlap          | Collection     | Configured tenant, roles and representative data   | Execute validate effective date overlap; inspect UI/API response, audit event and downstream status.      | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| COL-103     | Collection: enforce scoped access                    | Collection     | Configured tenant, roles and representative data   | Execute enforce scoped access; inspect UI/API response, audit event and downstream status.                | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| COL-104     | Collection: bulk import mixed valid/invalid rows     | Collection     | Configured tenant, roles and representative data   | Execute bulk import mixed valid/invalid rows; inspect UI/API response, audit event and downstream status. | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| COL-105     | Collection: export authorised data                   | Collection     | Configured tenant, roles and representative data   | Execute export authorised data; inspect UI/API response, audit event and downstream status.               | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
| COL-106     | Collection: retain revision history                  | Collection     | Configured tenant, roles and representative data   | Execute retain revision history; inspect UI/API response, audit event and downstream status.              | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
| COL-107     | Collection: trigger approval workflow                | Collection     | Configured tenant, roles and representative data   | Execute trigger approval workflow; inspect UI/API response, audit event and downstream status.            | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
| COL-108     | Collection: send overdue escalation                  | Collection     | Configured tenant, roles and representative data   | Execute send overdue escalation; inspect UI/API response, audit event and downstream status.              | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
| COL-109     | Collection: filter dashboard by entity               | Collection     | Configured tenant, roles and representative data   | Execute filter dashboard by entity; inspect UI/API response, audit event and downstream status.           | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
