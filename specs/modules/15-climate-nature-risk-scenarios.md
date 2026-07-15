# 15. Climate and Nature Risk, Scenario Analysis

> **Source:** PSSTEC ESG360 FDD v1.0, section 15. **Indicative release:** R4.
> Read `CLAUDE.md` (root) before implementing. Cross-functional rules BR-001–BR-020 (spec 24) apply to this module.


Assesses physical, transition and nature-related dependencies, impacts, risks and opportunities.

| **Req. ID** | **Requirement**       | **Functional Description**                                      | **Priority** | **Acceptance Criteria**                                 |
|-------------|-----------------------|-----------------------------------------------------------------|--------------|---------------------------------------------------------|
| 15-001      | Hazard library        | Maintain acute/chronic physical hazards and transition drivers. | Must         | Hazard definitions are versioned.                       |
| 15-002      | Asset geolocation     | Store coordinates, address and asset attributes.                | Must         | Sensitive locations follow access policy.               |
| 15-003      | Exposure assessment   | Map assets/suppliers to hazard datasets and time horizons.      | Should       | Dataset source and model are recorded.                  |
| 15-004      | Vulnerability scoring | Assess sensitivity and adaptive capacity.                       | Must         | Scoring method is configurable.                         |
| 15-005      | Financial impact      | Estimate revenue, cost, asset, liability and financing effects. | Should       | Assumptions and uncertainty are captured.               |
| 15-006      | Climate scenarios     | Configure scenarios, pathways, variables and horizons.          | Must         | Scenario version and source are retained.               |
| 15-007      | Scenario runs         | Execute baseline and alternative scenarios.                     | Should       | Run is reproducible and immutable after approval.       |
| 15-008      | Adaptation actions    | Create treatments and resilience investments.                   | Must         | Actions link to risks and assets.                       |
| 15-009      | Nature dependencies   | Record ecosystem services, impacts, dependencies and locations. | Should       | Nature records link to material topics and risks.       |
| 15-010      | Risk integration      | Synchronise material risks with enterprise risk system.         | Must         | Source-of-truth and conflict rules are defined.         |
| 15-011      | Disclosure outputs    | Generate scenario and resilience summaries.                     | Must         | Only approved scenario results can populate disclosure. |

## Outputs and Analytics

- Physical risk map

- Transition risk heat map

- Scenario comparison

- Adaptation plan



## Acceptance test cases (from Master Test Catalogue, spec 48)

Implementation of this module is not complete until these pass.

| **Test ID** | **Title**                                            | **Module**     | **Preconditions**                                  | **Test Steps**                                                                                            | **Expected Result**                                                                                       | **Type**      | **Priority** |
|---|---|---|---|---|---|---|---|
| CLI-001     | Physical risk map access                             | Climate Risk   | Assets and hazard dataset exist                    | Run exposure analysis and view map.                                                                       | Only authorised assets appear; dataset/model version is shown.                                            | Functional    | High         |
| CLI-100     | Climate Risk: create valid record                    | Climate Risk   | Configured tenant, roles and representative data   | Execute create valid record; inspect UI/API response, audit event and downstream status.                  | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| CLI-101     | Climate Risk: reject missing mandatory field         | Climate Risk   | Configured tenant, roles and representative data   | Execute reject missing mandatory field; inspect UI/API response, audit event and downstream status.       | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| CLI-102     | Climate Risk: validate effective date overlap        | Climate Risk   | Configured tenant, roles and representative data   | Execute validate effective date overlap; inspect UI/API response, audit event and downstream status.      | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| CLI-103     | Climate Risk: enforce scoped access                  | Climate Risk   | Configured tenant, roles and representative data   | Execute enforce scoped access; inspect UI/API response, audit event and downstream status.                | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| CLI-104     | Climate Risk: bulk import mixed valid/invalid rows   | Climate Risk   | Configured tenant, roles and representative data   | Execute bulk import mixed valid/invalid rows; inspect UI/API response, audit event and downstream status. | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| CLI-105     | Climate Risk: export authorised data                 | Climate Risk   | Configured tenant, roles and representative data   | Execute export authorised data; inspect UI/API response, audit event and downstream status.               | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
| CLI-106     | Climate Risk: retain revision history                | Climate Risk   | Configured tenant, roles and representative data   | Execute retain revision history; inspect UI/API response, audit event and downstream status.              | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
| CLI-107     | Climate Risk: trigger approval workflow              | Climate Risk   | Configured tenant, roles and representative data   | Execute trigger approval workflow; inspect UI/API response, audit event and downstream status.            | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
| CLI-108     | Climate Risk: send overdue escalation                | Climate Risk   | Configured tenant, roles and representative data   | Execute send overdue escalation; inspect UI/API response, audit event and downstream status.              | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
| CLI-109     | Climate Risk: filter dashboard by entity             | Climate Risk   | Configured tenant, roles and representative data   | Execute filter dashboard by entity; inspect UI/API response, audit event and downstream status.           | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
