# 11. Social and Human Capital

> **Source:** PSSTEC ESG360 FDD v1.0, section 11. **Indicative release:** R3.
> Read `CLAUDE.md` (root) before implementing. Cross-functional rules BR-001–BR-020 (spec 24) apply to this module.


Captures workforce, safety, diversity, human rights, community and social-impact information.

| **Req. ID** | **Requirement**            | **Functional Description**                                                                             | **Priority** | **Acceptance Criteria**                                  |
|-------------|----------------------------|--------------------------------------------------------------------------------------------------------|--------------|----------------------------------------------------------|
| 11-001      | Workforce profile          | Ingest employees and non-employee workers by entity, location, contract and demographic dimensions.    | Must         | Privacy rules restrict person-level access.              |
| 11-002      | Diversity metrics          | Calculate representation, hiring, promotion, turnover and pay-gap indicators.                          | Must         | Small-population suppression is configurable.            |
| 11-003      | Health and safety          | Capture hours, fatalities, recordable injuries, lost-time cases, occupational illness and near misses. | Must         | Rates use approved denominator and period.               |
| 11-004      | Training                   | Track ESG, ethics, safety and role-based training completion.                                          | Must         | Expiry and non-compliance alerts are available.          |
| 11-005      | Human rights due diligence | Maintain salient issues, assessments, actions and remedy.                                              | Should       | Affected stakeholders and evidence are linked.           |
| 11-006      | Grievances                 | Record channel, category, anonymity, investigation and remedy.                                         | Must         | Confidentiality and retaliation controls apply.          |
| 11-007      | Labour practices           | Track working hours, wages, collective bargaining and forced/child-labour controls.                    | Should       | Country-specific thresholds are configurable.            |
| 11-008      | Employee engagement        | Import survey outcomes and action plans.                                                               | Could        | Anonymous survey confidentiality is preserved.           |
| 11-009      | Community investment       | Track cash, in-kind, volunteering, beneficiaries and outcomes.                                         | Should       | Impact indicators link to programmes.                    |
| 11-010      | Product responsibility     | Capture product safety, recalls, accessibility and customer impacts.                                   | Should       | Material incidents are escalated.                        |
| 11-011      | Data privacy incidents     | Record privacy breaches and remediation.                                                               | Should       | Restricted access and notification dates are controlled. |
| 11-012      | Social targets             | Set targets by workforce group, site or geography.                                                     | Must         | Progress is calculated from governed metrics.            |

## Outputs and Analytics

- Workforce and diversity scorecard

- H&S dashboard

- Human-rights due diligence status

- Community impact



## Acceptance test cases (from Master Test Catalogue, spec 48)

Implementation of this module is not complete until these pass.

| **Test ID** | **Title**                                            | **Module**     | **Preconditions**                                  | **Test Steps**                                                                                            | **Expected Result**                                                                                       | **Type**      | **Priority** |
|---|---|---|---|---|---|---|---|
| SOC-001     | Safety rate denominator                              | Social         | Hours and incidents exist                          | Calculate TRIR/LTIFR under configured formula.                                                            | Rate matches expected denominator and rounding.                                                           | Functional    | High         |
| SOC-002     | Privacy suppression                                  | Social         | Small demographic group exists                     | Open diversity dashboard as standard manager.                                                             | Small group values are suppressed per threshold.                                                          | Functional    | High         |
| SOC-100     | Social: create valid record                          | Social         | Configured tenant, roles and representative data   | Execute create valid record; inspect UI/API response, audit event and downstream status.                  | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| SOC-101     | Social: reject missing mandatory field               | Social         | Configured tenant, roles and representative data   | Execute reject missing mandatory field; inspect UI/API response, audit event and downstream status.       | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| SOC-102     | Social: validate effective date overlap              | Social         | Configured tenant, roles and representative data   | Execute validate effective date overlap; inspect UI/API response, audit event and downstream status.      | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| SOC-103     | Social: enforce scoped access                        | Social         | Configured tenant, roles and representative data   | Execute enforce scoped access; inspect UI/API response, audit event and downstream status.                | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| SOC-104     | Social: bulk import mixed valid/invalid rows         | Social         | Configured tenant, roles and representative data   | Execute bulk import mixed valid/invalid rows; inspect UI/API response, audit event and downstream status. | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| SOC-105     | Social: export authorised data                       | Social         | Configured tenant, roles and representative data   | Execute export authorised data; inspect UI/API response, audit event and downstream status.               | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
| SOC-106     | Social: retain revision history                      | Social         | Configured tenant, roles and representative data   | Execute retain revision history; inspect UI/API response, audit event and downstream status.              | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
| SOC-107     | Social: trigger approval workflow                    | Social         | Configured tenant, roles and representative data   | Execute trigger approval workflow; inspect UI/API response, audit event and downstream status.            | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
| SOC-108     | Social: send overdue escalation                      | Social         | Configured tenant, roles and representative data   | Execute send overdue escalation; inspect UI/API response, audit event and downstream status.              | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
| SOC-109     | Social: filter dashboard by entity                   | Social         | Configured tenant, roles and representative data   | Execute filter dashboard by entity; inspect UI/API response, audit event and downstream status.           | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
