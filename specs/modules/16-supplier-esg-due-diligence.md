# 16. Supplier ESG and Value-Chain Due Diligence

> **Source:** PSSTEC ESG360 FDD v1.0, section 16. **Indicative release:** R3.
> Read `CLAUDE.md` (root) before implementing. Cross-functional rules BR-001–BR-020 (spec 24) apply to this module.


Provides supplier onboarding, assessments, emissions collection, risk screening, audits and remediation.

| **Req. ID** | **Requirement**        | **Functional Description**                                                | **Priority** | **Acceptance Criteria**                                |
|-------------|------------------------|---------------------------------------------------------------------------|--------------|--------------------------------------------------------|
| 16-001      | Supplier master sync   | Synchronise supplier identity, category, spend, geography and status.     | Must         | Duplicate resolution and golden record are controlled. |
| 16-002      | Segmentation           | Segment suppliers by spend, criticality, geography and ESG risk.          | Must         | Segmentation drives assessment frequency.              |
| 16-003      | Questionnaire builder  | Create multilingual questionnaires with branching and scoring.            | Must         | Published version is immutable for active campaign.    |
| 16-004      | Campaigns              | Issue assessments, reminders and escalations.                             | Must         | Response status is tracked by supplier.                |
| 16-005      | Evidence validation    | Require certificates, policies and supporting records.                    | Must         | Evidence expiry and authenticity status are tracked.   |
| 16-006      | Risk screening         | Integrate sanctions, adverse media, country and sector risk providers.    | Should       | Provider response and screening date are retained.     |
| 16-007      | Supplier emissions     | Collect corporate/product emissions, methodology, boundary and assurance. | Must         | Data validity and allocation are controlled.           |
| 16-008      | Corrective actions     | Create supplier improvement plans, due dates and verification.            | Must         | Overdue critical actions affect supplier risk.         |
| 16-009      | Supplier audits        | Schedule, execute and score onsite/remote audits.                         | Should       | Findings and evidence are linked.                      |
| 16-010      | Procurement gate       | Expose ESG risk/status to source-to-contract or purchase approval.        | Should       | Blocking rule is configurable.                         |
| 16-011      | Supplier scorecard     | Calculate ESG score with transparent weights.                             | Must         | Supplier can see permitted score components.           |
| 16-012      | Data consent and terms | Capture supplier consent, terms and privacy notice.                       | Must         | Processing is blocked without required acceptance.     |

## Outputs and Analytics

- Supplier ESG risk heat map

- Campaign completion

- Scope 3 primary data coverage

- Corrective action status



## Acceptance test cases (from Master Test Catalogue, spec 48)

Implementation of this module is not complete until these pass.

| **Test ID** | **Title**                                            | **Module**     | **Preconditions**                                  | **Test Steps**                                                                                            | **Expected Result**                                                                                       | **Type**      | **Priority** |
|---|---|---|---|---|---|---|---|
| SUP-001     | Supplier isolation                                   | Supplier       | Two supplier accounts exist                        | Supplier A attempts URL/API access to Supplier B response.                                                | Access denied and event logged.                                                                           | Functional    | High         |
| SUP-002     | Corrective action effect                             | Supplier       | Critical action overdue                            | Recalculate supplier score.                                                                               | Risk/score changes according to transparent configured rule.                                              | Functional    | High         |
| SUP-100     | Supplier: create valid record                        | Supplier       | Configured tenant, roles and representative data   | Execute create valid record; inspect UI/API response, audit event and downstream status.                  | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| SUP-101     | Supplier: reject missing mandatory field             | Supplier       | Configured tenant, roles and representative data   | Execute reject missing mandatory field; inspect UI/API response, audit event and downstream status.       | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| SUP-102     | Supplier: validate effective date overlap            | Supplier       | Configured tenant, roles and representative data   | Execute validate effective date overlap; inspect UI/API response, audit event and downstream status.      | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| SUP-103     | Supplier: enforce scoped access                      | Supplier       | Configured tenant, roles and representative data   | Execute enforce scoped access; inspect UI/API response, audit event and downstream status.                | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| SUP-104     | Supplier: bulk import mixed valid/invalid rows       | Supplier       | Configured tenant, roles and representative data   | Execute bulk import mixed valid/invalid rows; inspect UI/API response, audit event and downstream status. | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| SUP-105     | Supplier: export authorised data                     | Supplier       | Configured tenant, roles and representative data   | Execute export authorised data; inspect UI/API response, audit event and downstream status.               | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
| SUP-106     | Supplier: retain revision history                    | Supplier       | Configured tenant, roles and representative data   | Execute retain revision history; inspect UI/API response, audit event and downstream status.              | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
| SUP-107     | Supplier: trigger approval workflow                  | Supplier       | Configured tenant, roles and representative data   | Execute trigger approval workflow; inspect UI/API response, audit event and downstream status.            | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
| SUP-108     | Supplier: send overdue escalation                    | Supplier       | Configured tenant, roles and representative data   | Execute send overdue escalation; inspect UI/API response, audit event and downstream status.              | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
| SUP-109     | Supplier: filter dashboard by entity                 | Supplier       | Configured tenant, roles and representative data   | Execute filter dashboard by entity; inspect UI/API response, audit event and downstream status.           | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
