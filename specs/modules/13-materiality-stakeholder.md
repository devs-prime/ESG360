# 13. Materiality and Stakeholder Engagement

> **Source:** PSSTEC ESG360 FDD v1.0, section 13. **Indicative release:** R3.
> Read `CLAUDE.md` (root) before implementing. Cross-functional rules BR-001–BR-020 (spec 24) apply to this module.


Supports impact materiality, financial materiality and double-materiality assessment.

| **Req. ID** | **Requirement**        | **Functional Description**                                                        | **Priority** | **Acceptance Criteria**                                  |
|-------------|------------------------|-----------------------------------------------------------------------------------|--------------|----------------------------------------------------------|
| 13-001      | Stakeholder groups     | Maintain stakeholder categories, interests, influence and engagement methods.     | Must         | Stakeholder population is versioned by assessment.       |
| 13-002      | Topic universe         | Create ESG topic library with sector and framework references.                    | Must         | Topics can be merged without losing history.             |
| 13-003      | Impact assessment      | Score scale, scope, irremediability and likelihood for positive/negative impacts. | Must         | Scoring methodology is configurable and documented.      |
| 13-004      | Financial materiality  | Score magnitude and likelihood of financial effects over time horizons.           | Must         | Risk/opportunity links support financial narrative.      |
| 13-005      | Survey and workshop    | Run questionnaires, interviews and workshops.                                     | Should       | Responses are attributable or anonymous per design.      |
| 13-006      | Evidence and rationale | Attach research, stakeholder input and management rationale.                      | Must         | Materiality conclusion requires evidence.                |
| 13-007      | Thresholds             | Configure thresholds and management override workflow.                            | Must         | Overrides require rationale and approval.                |
| 13-008      | Materiality matrix     | Visualise and compare assessments.                                                | Must         | Matrix drills to scores and evidence.                    |
| 13-009      | Approval               | Route material topics to executive/board approval.                                | Must         | Approved list becomes disclosure applicability baseline. |
| 13-010      | Annual refresh         | Clone and update prior assessment with change analysis.                           | Should       | Change report identifies score and conclusion movement.  |

## Outputs and Analytics

- Double-materiality matrix

- Stakeholder participation

- Material topic change analysis



## Acceptance test cases (from Master Test Catalogue, spec 48)

Implementation of this module is not complete until these pass.

| **Test ID** | **Title**                                            | **Module**     | **Preconditions**                                  | **Test Steps**                                                                                            | **Expected Result**                                                                                       | **Type**      | **Priority** |
|---|---|---|---|---|---|---|---|
| MAT-001     | Double materiality threshold                         | Materiality    | Assessment scores complete                         | Apply thresholds and management override.                                                                 | Material result follows threshold; override requires rationale and approval.                              | Functional    | High         |
| MAT-100     | Materiality: create valid record                     | Materiality    | Configured tenant, roles and representative data   | Execute create valid record; inspect UI/API response, audit event and downstream status.                  | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| MAT-101     | Materiality: reject missing mandatory field          | Materiality    | Configured tenant, roles and representative data   | Execute reject missing mandatory field; inspect UI/API response, audit event and downstream status.       | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| MAT-102     | Materiality: validate effective date overlap         | Materiality    | Configured tenant, roles and representative data   | Execute validate effective date overlap; inspect UI/API response, audit event and downstream status.      | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| MAT-103     | Materiality: enforce scoped access                   | Materiality    | Configured tenant, roles and representative data   | Execute enforce scoped access; inspect UI/API response, audit event and downstream status.                | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| MAT-104     | Materiality: bulk import mixed valid/invalid rows    | Materiality    | Configured tenant, roles and representative data   | Execute bulk import mixed valid/invalid rows; inspect UI/API response, audit event and downstream status. | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| MAT-105     | Materiality: export authorised data                  | Materiality    | Configured tenant, roles and representative data   | Execute export authorised data; inspect UI/API response, audit event and downstream status.               | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
| MAT-106     | Materiality: retain revision history                 | Materiality    | Configured tenant, roles and representative data   | Execute retain revision history; inspect UI/API response, audit event and downstream status.              | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
| MAT-107     | Materiality: trigger approval workflow               | Materiality    | Configured tenant, roles and representative data   | Execute trigger approval workflow; inspect UI/API response, audit event and downstream status.            | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
| MAT-108     | Materiality: send overdue escalation                 | Materiality    | Configured tenant, roles and representative data   | Execute send overdue escalation; inspect UI/API response, audit event and downstream status.              | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
| MAT-109     | Materiality: filter dashboard by entity              | Materiality    | Configured tenant, roles and representative data   | Execute filter dashboard by entity; inspect UI/API response, audit event and downstream status.           | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
