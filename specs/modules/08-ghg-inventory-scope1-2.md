# 8. Corporate GHG Inventory - Scope 1 and Scope 2

> **Source:** PSSTEC ESG360 FDD v1.0, section 8. **Indicative release:** R1.
> Read `CLAUDE.md` (root) before implementing. Cross-functional rules BR-001–BR-020 (spec 24) apply to this module.


Calculates and governs direct and purchased-energy emissions.

## Process Flow

12. Register sources and boundaries.

13. Collect activity and contractual data.

14. Resolve factors and calculate gas-level emissions.

15. Validate completeness and outliers.

16. Consolidate by entity, scope and category.

17. Approve inventory and lock baseline.

| **Req. ID** | **Requirement**          | **Functional Description**                                                                         | **Priority** | **Acceptance Criteria**                                        |
|-------------|--------------------------|----------------------------------------------------------------------------------------------------|--------------|----------------------------------------------------------------|
| 8-001       | Emission source register | Register stationary combustion, mobile combustion, fugitive, process and purchased energy sources. | Must         | Each source is classified, owned and linked to facility.       |
| 8-002       | Activity capture         | Capture quantity, unit, period, source and evidence.                                               | Must         | Activity passes unit and period validations.                   |
| 8-003       | Scope classification     | Classify source as Scope 1, Scope 2 location-based or Scope 2 market-based.                        | Must         | Classification is controlled by source type and contract data. |
| 8-004       | Calculation engine       | Calculate gas-level emissions and CO2e using factor and GWP versions.                              | Must         | Result is reproducible and rounded only at presentation.       |
| 8-005       | Renewable instruments    | Track RECs, GOs, PPAs and residual mix information.                                                | Should       | Instrument allocation cannot exceed eligible consumption.      |
| 8-006       | Refrigerants             | Track additions, recovery, disposal and leakage by gas.                                            | Must         | Mass balance and leakage calculation are supported.            |
| 8-007       | Fleet                    | Capture fuel, distance or telematics-based activity.                                               | Should       | Method and data quality are shown.                             |
| 8-008       | Base year                | Set base year and inventory baseline by boundary and method.                                       | Must         | Base-year approval creates fixed baseline snapshot.            |
| 8-009       | Recalculation policy     | Configure significance thresholds and triggers.                                                    | Must         | Structural/method/factor changes generate assessment.          |
| 8-010       | Intensity metrics        | Calculate emissions per revenue, production, FTE, area or custom denominator.                      | Must         | Denominator version and unit are traceable.                    |
| 8-011       | Uncertainty and quality  | Score source, activity and factor quality; capture uncertainty.                                    | Should       | Quality score rolls up using configured method.                |
| 8-012       | Inventory approval       | Review and sign off inventory by entity and group.                                                 | Must         | Group inventory cannot close with blocking exceptions.         |

## Outputs and Analytics

- Scope 1 and 2 inventory

- Location vs market-based comparison

- Emission source trend

- Data quality and uncertainty dashboard



## Acceptance test cases (from Master Test Catalogue, spec 48)

Implementation of this module is not complete until these pass.

| **Test ID** | **Title**                                            | **Module**     | **Preconditions**                                  | **Test Steps**                                                                                            | **Expected Result**                                                                                       | **Type**      | **Priority** |
|---|---|---|---|---|---|---|---|
| CAR-001     | Scope 1 stationary combustion                        | Carbon         | Fuel activity and factor exist                     | Calculate 1,000 litres for configured fuel.                                                               | Expected gas-level and CO2e result matches independent calculation.                                       | Functional    | High         |
| CAR-002     | Scope 2 dual reporting                               | Carbon         | Electricity and instruments configured             | Calculate location and market methods.                                                                    | Both results are reported separately with correct factor hierarchy.                                       | Functional    | High         |
| CAR-003     | Refrigerant mass balance                             | Carbon         | Refrigerant equipment exists                       | Enter opening, additions, recovery and closing stock.                                                     | Leakage and CO2e equal approved mass-balance formula.                                                     | Functional    | High         |
| CAR-004     | Base-year restatement                                | Carbon         | Approved base year exists                          | Change boundary above threshold and approve restatement.                                                  | Original baseline retained; restated baseline and rationale published.                                    | Functional    | High         |
| CAR-100     | Carbon: create valid record                          | Carbon         | Configured tenant, roles and representative data   | Execute create valid record; inspect UI/API response, audit event and downstream status.                  | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| CAR-101     | Carbon: reject missing mandatory field               | Carbon         | Configured tenant, roles and representative data   | Execute reject missing mandatory field; inspect UI/API response, audit event and downstream status.       | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| CAR-102     | Carbon: validate effective date overlap              | Carbon         | Configured tenant, roles and representative data   | Execute validate effective date overlap; inspect UI/API response, audit event and downstream status.      | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| CAR-103     | Carbon: enforce scoped access                        | Carbon         | Configured tenant, roles and representative data   | Execute enforce scoped access; inspect UI/API response, audit event and downstream status.                | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| CAR-104     | Carbon: bulk import mixed valid/invalid rows         | Carbon         | Configured tenant, roles and representative data   | Execute bulk import mixed valid/invalid rows; inspect UI/API response, audit event and downstream status. | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| CAR-105     | Carbon: export authorised data                       | Carbon         | Configured tenant, roles and representative data   | Execute export authorised data; inspect UI/API response, audit event and downstream status.               | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
| CAR-106     | Carbon: retain revision history                      | Carbon         | Configured tenant, roles and representative data   | Execute retain revision history; inspect UI/API response, audit event and downstream status.              | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
| CAR-107     | Carbon: trigger approval workflow                    | Carbon         | Configured tenant, roles and representative data   | Execute trigger approval workflow; inspect UI/API response, audit event and downstream status.            | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
| CAR-108     | Carbon: send overdue escalation                      | Carbon         | Configured tenant, roles and representative data   | Execute send overdue escalation; inspect UI/API response, audit event and downstream status.              | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
| CAR-109     | Carbon: filter dashboard by entity                   | Carbon         | Configured tenant, roles and representative data   | Execute filter dashboard by entity; inspect UI/API response, audit event and downstream status.           | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
