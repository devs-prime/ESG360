# 12. Governance, Ethics, Risk and Compliance

> **Source:** PSSTEC ESG360 FDD v1.0, section 12. **Indicative release:** R3.
> Read `CLAUDE.md` (root) before implementing. Cross-functional rules BR-001–BR-020 (spec 24) apply to this module.


Provides governance structures, ESG risk management, policies, controls, incidents and board oversight.

| **Req. ID** | **Requirement**              | **Functional Description**                                                               | **Priority** | **Acceptance Criteria**                                |
|-------------|------------------------------|------------------------------------------------------------------------------------------|--------------|--------------------------------------------------------|
| 12-001      | Governance bodies            | Maintain board/committee mandates, membership, independence, skills and meeting records. | Must         | Effective-dated composition supports period reporting. |
| 12-002      | Policy library               | Manage policies, versions, approvals, attestations and review dates.                     | Must         | Only approved version is published.                    |
| 12-003      | Ethics declarations          | Capture conflicts, gifts, anti-bribery and code-of-conduct attestations.                 | Should       | Over-threshold declarations are routed for review.     |
| 12-004      | ESG risk register            | Record impacts, risks and opportunities with inherent/residual ratings.                  | Must         | Risk scoring uses configurable matrices.               |
| 12-005      | Controls                     | Define preventive/detective controls, owners, frequency and evidence.                    | Must         | Control performance influences residual risk.          |
| 12-006      | Compliance obligations       | Maintain regulatory/voluntary obligations by jurisdiction and topic.                     | Should       | Applicability and evidence are assigned.               |
| 12-007      | Incidents and investigations | Record allegations, investigations, outcomes and remediation.                            | Must         | Sensitive cases use restricted case teams.             |
| 12-008      | Audit management             | Plan audits, execute procedures and track findings.                                      | Should       | Findings create corrective actions.                    |
| 12-009      | Business continuity linkage  | Link climate/ESG risks to continuity plans and exercises.                                | Could        | Critical dependencies are visible.                     |
| 12-010      | Board packs                  | Generate board-ready ESG performance, risks, actions and decisions.                      | Must         | Pack is generated from approved snapshot.              |
| 12-011      | Whistleblowing channels      | Integrate or record protected reports and case status.                                   | Should       | Reporter confidentiality is maintained.                |
| 12-012      | Governance disclosures       | Map governance facts to framework datapoints.                                            | Must         | Disclosure lineage traces to governance records.       |

## Outputs and Analytics

- ESG risk heat map

- Policy and attestation status

- Ethics cases

- Control effectiveness

- Board ESG pack



## Acceptance test cases (from Master Test Catalogue, spec 48)

Implementation of this module is not complete until these pass.

| **Test ID** | **Title**                                            | **Module**     | **Preconditions**                                  | **Test Steps**                                                                                            | **Expected Result**                                                                                       | **Type**      | **Priority** |
|---|---|---|---|---|---|---|---|
| GOV-001     | Policy version publication                           | Governance     | Draft policy and approvals configured              | Approve and publish new version.                                                                          | New version becomes current; prior version remains historically available.                                | Functional    | High         |
| GOV-100     | Governance: create valid record                      | Governance     | Configured tenant, roles and representative data   | Execute create valid record; inspect UI/API response, audit event and downstream status.                  | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| GOV-101     | Governance: reject missing mandatory field           | Governance     | Configured tenant, roles and representative data   | Execute reject missing mandatory field; inspect UI/API response, audit event and downstream status.       | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| GOV-102     | Governance: validate effective date overlap          | Governance     | Configured tenant, roles and representative data   | Execute validate effective date overlap; inspect UI/API response, audit event and downstream status.      | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| GOV-103     | Governance: enforce scoped access                    | Governance     | Configured tenant, roles and representative data   | Execute enforce scoped access; inspect UI/API response, audit event and downstream status.                | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| GOV-104     | Governance: bulk import mixed valid/invalid rows     | Governance     | Configured tenant, roles and representative data   | Execute bulk import mixed valid/invalid rows; inspect UI/API response, audit event and downstream status. | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| GOV-105     | Governance: export authorised data                   | Governance     | Configured tenant, roles and representative data   | Execute export authorised data; inspect UI/API response, audit event and downstream status.               | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
| GOV-106     | Governance: retain revision history                  | Governance     | Configured tenant, roles and representative data   | Execute retain revision history; inspect UI/API response, audit event and downstream status.              | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
| GOV-107     | Governance: trigger approval workflow                | Governance     | Configured tenant, roles and representative data   | Execute trigger approval workflow; inspect UI/API response, audit event and downstream status.            | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
| GOV-108     | Governance: send overdue escalation                  | Governance     | Configured tenant, roles and representative data   | Execute send overdue escalation; inspect UI/API response, audit event and downstream status.              | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
| GOV-109     | Governance: filter dashboard by entity               | Governance     | Configured tenant, roles and representative data   | Execute filter dashboard by entity; inspect UI/API response, audit event and downstream status.           | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
