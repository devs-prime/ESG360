# 14. Targets, Transition Plans and Initiatives

> **Source:** PSSTEC ESG360 FDD v1.0, section 14. **Indicative release:** R4.
> Read `CLAUDE.md` (root) before implementing. Cross-functional rules BR-001–BR-020 (spec 24) apply to this module.


Connects commitments to baselines, forecasts, initiatives, budgets and realised outcomes.

| **Req. ID** | **Requirement**            | **Functional Description**                                                                        | **Priority** | **Acceptance Criteria**                                               |
|-------------|----------------------------|---------------------------------------------------------------------------------------------------|--------------|-----------------------------------------------------------------------|
| 14-001      | Target definition          | Create absolute/intensity targets with baseline, target year, scope and method.                   | Must         | Target is valid only when baseline and metric are approved.           |
| 14-002      | Interim milestones         | Define annual/quarterly pathways.                                                                 | Must         | Expected vs actual trajectory is calculated.                          |
| 14-003      | Science alignment metadata | Capture validation body, methodology and status.                                                  | Should       | Claims display status and evidence.                                   |
| 14-004      | Initiative portfolio       | Record decarbonisation/social/governance initiatives, owner, cost, schedule and expected benefit. | Must         | Initiatives link to targets and affected metrics.                     |
| 14-005      | Abatement calculation      | Estimate annual and lifetime emissions reduction.                                                 | Must         | Avoided emissions are kept separate from inventory reductions.        |
| 14-006      | Marginal abatement cost    | Calculate cost per tonne and prioritise initiatives.                                              | Should       | Assumptions and discount rate are versioned.                          |
| 14-007      | Scenario forecast          | Forecast business-as-usual and initiative-adjusted performance.                                   | Must         | Scenario inputs and model version are traceable.                      |
| 14-008      | Budget and actuals         | Track capex/opex budget, commitment and actual.                                                   | Should       | Financial integration reconciles initiative cost.                     |
| 14-009      | Benefits realisation       | Validate and approve realised environmental/social benefits.                                      | Must         | Realised benefit cannot exceed supported evidence without exception.  |
| 14-010      | Transition plan narrative  | Generate structured governance, actions, finance and dependency narrative.                        | Should       | Narrative is linked to approved data and reviewed before publication. |
| 14-011      | Dependencies and risks     | Track technology, policy, finance and supplier dependencies.                                      | Must         | Critical dependency generates risk/action.                            |
| 14-012      | Portfolio dashboard        | Rank initiatives by impact, cost, risk and feasibility.                                           | Must         | Filters support entity, target and time horizon.                      |

## Outputs and Analytics

- Target trajectory

- Transition plan dashboard

- Abatement waterfall

- Initiative portfolio and MACC



## Acceptance test cases (from Master Test Catalogue, spec 48)

Implementation of this module is not complete until these pass.

| **Test ID** | **Title**                                            | **Module**     | **Preconditions**                                  | **Test Steps**                                                                                            | **Expected Result**                                                                                       | **Type**      | **Priority** |
|---|---|---|---|---|---|---|---|
| TGT-001     | Target trajectory                                    | Targets        | Approved baseline and milestones exist             | Post actuals and run forecast.                                                                            | Dashboard shows actual, pathway, forecast and variance.                                                   | Functional    | High         |
| TGT-100     | Targets: create valid record                         | Targets        | Configured tenant, roles and representative data   | Execute create valid record; inspect UI/API response, audit event and downstream status.                  | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| TGT-101     | Targets: reject missing mandatory field              | Targets        | Configured tenant, roles and representative data   | Execute reject missing mandatory field; inspect UI/API response, audit event and downstream status.       | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| TGT-102     | Targets: validate effective date overlap             | Targets        | Configured tenant, roles and representative data   | Execute validate effective date overlap; inspect UI/API response, audit event and downstream status.      | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| TGT-103     | Targets: enforce scoped access                       | Targets        | Configured tenant, roles and representative data   | Execute enforce scoped access; inspect UI/API response, audit event and downstream status.                | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| TGT-104     | Targets: bulk import mixed valid/invalid rows        | Targets        | Configured tenant, roles and representative data   | Execute bulk import mixed valid/invalid rows; inspect UI/API response, audit event and downstream status. | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| TGT-105     | Targets: export authorised data                      | Targets        | Configured tenant, roles and representative data   | Execute export authorised data; inspect UI/API response, audit event and downstream status.               | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
| TGT-106     | Targets: retain revision history                     | Targets        | Configured tenant, roles and representative data   | Execute retain revision history; inspect UI/API response, audit event and downstream status.              | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
| TGT-107     | Targets: trigger approval workflow                   | Targets        | Configured tenant, roles and representative data   | Execute trigger approval workflow; inspect UI/API response, audit event and downstream status.            | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
| TGT-108     | Targets: send overdue escalation                     | Targets        | Configured tenant, roles and representative data   | Execute send overdue escalation; inspect UI/API response, audit event and downstream status.              | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
| TGT-109     | Targets: filter dashboard by entity                  | Targets        | Configured tenant, roles and representative data   | Execute filter dashboard by entity; inspect UI/API response, audit event and downstream status.           | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
