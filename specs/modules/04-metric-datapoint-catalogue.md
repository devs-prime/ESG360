# 4. ESG Metric and Data-Point Catalogue

> **Source:** PSSTEC ESG360 FDD v1.0, section 4. **Indicative release:** R0.
> Read `CLAUDE.md` (root) before implementing. Cross-functional rules BR-001–BR-020 (spec 24) apply to this module.


Defines reusable ESG measures, qualitative disclosures, formulas, dimensions, ownership and validation rules.

| **Req. ID** | **Requirement**           | **Functional Description**                                                              | **Priority** | **Acceptance Criteria**                                            |
|-------------|---------------------------|-----------------------------------------------------------------------------------------|--------------|--------------------------------------------------------------------|
| 4-001       | Metric definition         | Define name, description, topic, data type, unit, frequency, aggregation and owner.     | Must         | Metric can be activated only after mandatory metadata is complete. |
| 4-002       | Qualitative datapoint     | Create narrative, policy, yes/no, date, taxonomy and evidence-based datapoints.         | Must         | Datapoint accepts only configured data type and validation.        |
| 4-003       | Versioning                | Version definitions, formulas, units and mappings with effective dates.                 | Must         | Historical submissions retain original version.                    |
| 4-004       | Framework mapping         | Map one metric/datapoint to multiple disclosure requirements and custom reports.        | Must         | Crosswalk shows all mappings and coverage status.                  |
| 4-005       | Formula builder           | Create formulas using approved operators, dimensions, lookups and conversion functions. | Must         | Formula validates syntax and prevents circular references.         |
| 4-006       | Validation rules          | Configure range, variance, completeness, evidence and cross-field checks.               | Must         | Violations generate severity-based status.                         |
| 4-007       | Ownership matrix          | Assign preparer, reviewer, approver and backup by organisation and period.              | Must         | Assignments are resolved for every active collection task.         |
| 4-008       | Materiality applicability | Mark metrics as material, not material, mandatory or voluntary by reporting perimeter.  | Must         | Disclosure completeness uses applicability status.                 |
| 4-009       | Data confidentiality      | Classify public, internal, confidential, personal and restricted.                       | Must         | Access and export follow classification policy.                    |
| 4-010       | Catalogue import/export   | Bulk manage definitions through controlled templates and API.                           | Should       | Invalid rows are rejected with row-level errors.                   |

## Outputs and Analytics

- Metric catalogue

- Framework coverage matrix

- Ownership and collection workload



## Acceptance test cases (from Master Test Catalogue, spec 48)

Implementation of this module is not complete until these pass.

| **Test ID** | **Title**                                            | **Module**     | **Preconditions**                                  | **Test Steps**                                                                                            | **Expected Result**                                                                                       | **Type**      | **Priority** |
|---|---|---|---|---|---|---|---|
| CAT-001     | Metric formula circular reference                    | Catalogue      | Metrics A and B exist                              | Define A=B+1 and B=A+1; publish.                                                                          | Publication fails with circular-reference error.                                                          | Functional    | High         |
| CAT-100     | Catalogue: create valid record                       | Catalogue      | Configured tenant, roles and representative data   | Execute create valid record; inspect UI/API response, audit event and downstream status.                  | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| CAT-101     | Catalogue: reject missing mandatory field            | Catalogue      | Configured tenant, roles and representative data   | Execute reject missing mandatory field; inspect UI/API response, audit event and downstream status.       | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| CAT-102     | Catalogue: validate effective date overlap           | Catalogue      | Configured tenant, roles and representative data   | Execute validate effective date overlap; inspect UI/API response, audit event and downstream status.      | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| CAT-103     | Catalogue: enforce scoped access                     | Catalogue      | Configured tenant, roles and representative data   | Execute enforce scoped access; inspect UI/API response, audit event and downstream status.                | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| CAT-104     | Catalogue: bulk import mixed valid/invalid rows      | Catalogue      | Configured tenant, roles and representative data   | Execute bulk import mixed valid/invalid rows; inspect UI/API response, audit event and downstream status. | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| CAT-105     | Catalogue: export authorised data                    | Catalogue      | Configured tenant, roles and representative data   | Execute export authorised data; inspect UI/API response, audit event and downstream status.               | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
| CAT-106     | Catalogue: retain revision history                   | Catalogue      | Configured tenant, roles and representative data   | Execute retain revision history; inspect UI/API response, audit event and downstream status.              | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
| CAT-107     | Catalogue: trigger approval workflow                 | Catalogue      | Configured tenant, roles and representative data   | Execute trigger approval workflow; inspect UI/API response, audit event and downstream status.            | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
| CAT-108     | Catalogue: send overdue escalation                   | Catalogue      | Configured tenant, roles and representative data   | Execute send overdue escalation; inspect UI/API response, audit event and downstream status.              | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
| CAT-109     | Catalogue: filter dashboard by entity                | Catalogue      | Configured tenant, roles and representative data   | Execute filter dashboard by entity; inspect UI/API response, audit event and downstream status.           | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
