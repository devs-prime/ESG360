# 10. Energy, Water, Waste and Environmental Operations

> **Source:** PSSTEC ESG360 FDD v1.0, section 10. **Indicative release:** R1.
> Read `CLAUDE.md` (root) before implementing. Cross-functional rules BR-001–BR-020 (spec 24) apply to this module.


Manages non-carbon environmental performance and operational controls.

| **Req. ID** | **Requirement**        | **Functional Description**                                                    | **Priority** | **Acceptance Criteria**                            |
|-------------|------------------------|-------------------------------------------------------------------------------|--------------|----------------------------------------------------|
| 10-001      | Energy register        | Capture electricity, steam, heat, cooling and fuels by meter/source.          | Must         | Energy balances and meter coverage are available.  |
| 10-002      | Renewable energy       | Track generated, purchased, exported and certificate-backed renewable energy. | Must         | Claims reconcile with consumption and instruments. |
| 10-003      | Water withdrawal       | Capture source, stress area, quality and volume.                              | Must         | Water is reported by source and geography.         |
| 10-004      | Water discharge        | Capture destination, treatment, quality parameters and incidents.             | Should       | Discharge violations create alerts.                |
| 10-005      | Water consumption      | Calculate withdrawal less discharge with configured rules.                    | Must         | Result is traceable to inputs.                     |
| 10-006      | Waste generation       | Capture hazardous/non-hazardous waste by material and site.                   | Must         | Quantity, treatment and destination are recorded.  |
| 10-007      | Waste hierarchy        | Track prevention, reuse, recycling, recovery, treatment and disposal.         | Must         | Diversion rate is calculated.                      |
| 10-008      | Pollutants             | Capture air emissions, effluent and other releases.                           | Should       | Permit thresholds and exceedances are tracked.     |
| 10-009      | Environmental permits  | Maintain permits, conditions, limits, renewals and evidence.                  | Must         | Expiry alerts and compliance status are generated. |
| 10-010      | Incidents              | Record spills, releases, breaches, impact and response.                       | Must         | Material incidents invoke escalation workflow.     |
| 10-011      | Biodiversity locations | Record proximity to protected/key biodiversity areas and dependencies.        | Should       | Map view displays locations and risk attributes.   |
| 10-012      | Objectives and plans   | Manage environmental objectives, actions, owners and benefits.                | Must         | Progress rolls into target dashboard.              |

## Outputs and Analytics

- Energy performance

- Water balance and stress dashboard

- Waste and circularity dashboard

- Permit compliance

- Environmental incident trends



## Acceptance test cases (from Master Test Catalogue, spec 48)

Implementation of this module is not complete until these pass.

| **Test ID** | **Title**                                            | **Module**     | **Preconditions**                                  | **Test Steps**                                                                                            | **Expected Result**                                                                                       | **Type**      | **Priority** |
|---|---|---|---|---|---|---|---|
| ENV-001     | Water balance                                        | Environment    | Withdrawal and discharge values exist              | Run water consumption calculation.                                                                        | Consumption equals configured balance and lineage shows inputs.                                           | Functional    | High         |
| ENV-002     | Waste diversion                                      | Environment    | Waste treatment records exist                      | Calculate diversion rate.                                                                                 | Only configured reuse/recycling/recovery treatments count as diverted.                                    | Functional    | High         |
| ENV-100     | Environment: create valid record                     | Environment    | Configured tenant, roles and representative data   | Execute create valid record; inspect UI/API response, audit event and downstream status.                  | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| ENV-101     | Environment: reject missing mandatory field          | Environment    | Configured tenant, roles and representative data   | Execute reject missing mandatory field; inspect UI/API response, audit event and downstream status.       | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| ENV-102     | Environment: validate effective date overlap         | Environment    | Configured tenant, roles and representative data   | Execute validate effective date overlap; inspect UI/API response, audit event and downstream status.      | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| ENV-103     | Environment: enforce scoped access                   | Environment    | Configured tenant, roles and representative data   | Execute enforce scoped access; inspect UI/API response, audit event and downstream status.                | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| ENV-104     | Environment: bulk import mixed valid/invalid rows    | Environment    | Configured tenant, roles and representative data   | Execute bulk import mixed valid/invalid rows; inspect UI/API response, audit event and downstream status. | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| ENV-105     | Environment: export authorised data                  | Environment    | Configured tenant, roles and representative data   | Execute export authorised data; inspect UI/API response, audit event and downstream status.               | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
| ENV-106     | Environment: retain revision history                 | Environment    | Configured tenant, roles and representative data   | Execute retain revision history; inspect UI/API response, audit event and downstream status.              | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
| ENV-107     | Environment: trigger approval workflow               | Environment    | Configured tenant, roles and representative data   | Execute trigger approval workflow; inspect UI/API response, audit event and downstream status.            | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
| ENV-108     | Environment: send overdue escalation                 | Environment    | Configured tenant, roles and representative data   | Execute send overdue escalation; inspect UI/API response, audit event and downstream status.              | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
| ENV-109     | Environment: filter dashboard by entity              | Environment    | Configured tenant, roles and representative data   | Execute filter dashboard by entity; inspect UI/API response, audit event and downstream status.           | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
