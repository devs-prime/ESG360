# 7. Emission Factor Library

> **Source:** PSSTEC ESG360 FDD v1.0, section 7. **Indicative release:** R1.
> Read `CLAUDE.md` (root) before implementing. Cross-functional rules BR-001–BR-020 (spec 24) apply to this module.


Governed repository for emission factors, GWPs, calorific values and conversion coefficients.

| **Req. ID** | **Requirement**      | **Functional Description**                                                                                       | **Priority** | **Acceptance Criteria**                                           |
|-------------|----------------------|------------------------------------------------------------------------------------------------------------------|--------------|-------------------------------------------------------------------|
| 7-001       | Factor sources       | Maintain publisher, dataset, jurisdiction, year, methodology and licence metadata.                               | Must         | Each factor has source and effective period.                      |
| 7-002       | Factor dimensions    | Support fuel, vehicle, grid, spend category, process, refrigerant, transport mode and supplier-specific factors. | Must         | Lookup resolves unambiguously or returns controlled exception.    |
| 7-003       | Unit compatibility   | Define numerator/denominator units and conversion rules.                                                         | Must         | Incompatible activity units are rejected.                         |
| 7-004       | GWP sets             | Maintain assessment-report-specific global warming potential sets.                                               | Must         | Inventory records the chosen GWP version.                         |
| 7-005       | Grid factors         | Support location-based and market-based electricity factors.                                                     | Must         | Scope 2 method and contractual instrument hierarchy are recorded. |
| 7-006       | Supplier factors     | Accept verified supplier-specific factors with validity and assurance status.                                    | Should       | Expired factor is flagged.                                        |
| 7-007       | Approval workflow    | Review and approve factors before production use.                                                                | Must         | Draft factor cannot calculate approved inventory.                 |
| 7-008       | Bulk import          | Load factor datasets with validation and duplicate detection.                                                    | Must         | Import report identifies rejected records.                        |
| 7-009       | Factor hierarchy     | Configure preferred factor selection by geography, period, technology and quality.                               | Must         | System explains selected factor.                                  |
| 7-010       | Factor update impact | Identify records affected by factor revisions and support controlled recalculation.                              | Must         | Impact assessment lists affected periods and disclosures.         |

## Outputs and Analytics

- Factor catalogue

- Expiring factors

- Factor usage and impact analysis



## Acceptance test cases (from Master Test Catalogue, spec 48)

Implementation of this module is not complete until these pass.

| **Test ID** | **Title**                                            | **Module**     | **Preconditions**                                  | **Test Steps**                                                                                            | **Expected Result**                                                                                       | **Type**      | **Priority** |
|---|---|---|---|---|---|---|---|
| FAC-001     | Factor unit validation                               | Factor         | Factor kgCO2e/litre exists                         | Calculate activity in kWh using factor.                                                                   | Calculation fails or requires approved conversion; no silent mismatch.                                    | Functional    | High         |
| FAC-002     | Factor effective date selection                      | Factor         | Two yearly factors exist                           | Calculate records across both years.                                                                      | Correct factor version is selected by date and shown in lineage.                                          | Functional    | High         |
| FAC-100     | Factor: create valid record                          | Factor         | Configured tenant, roles and representative data   | Execute create valid record; inspect UI/API response, audit event and downstream status.                  | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| FAC-101     | Factor: reject missing mandatory field               | Factor         | Configured tenant, roles and representative data   | Execute reject missing mandatory field; inspect UI/API response, audit event and downstream status.       | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| FAC-102     | Factor: validate effective date overlap              | Factor         | Configured tenant, roles and representative data   | Execute validate effective date overlap; inspect UI/API response, audit event and downstream status.      | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| FAC-103     | Factor: enforce scoped access                        | Factor         | Configured tenant, roles and representative data   | Execute enforce scoped access; inspect UI/API response, audit event and downstream status.                | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| FAC-104     | Factor: bulk import mixed valid/invalid rows         | Factor         | Configured tenant, roles and representative data   | Execute bulk import mixed valid/invalid rows; inspect UI/API response, audit event and downstream status. | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| FAC-105     | Factor: export authorised data                       | Factor         | Configured tenant, roles and representative data   | Execute export authorised data; inspect UI/API response, audit event and downstream status.               | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
| FAC-106     | Factor: retain revision history                      | Factor         | Configured tenant, roles and representative data   | Execute retain revision history; inspect UI/API response, audit event and downstream status.              | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
| FAC-107     | Factor: trigger approval workflow                    | Factor         | Configured tenant, roles and representative data   | Execute trigger approval workflow; inspect UI/API response, audit event and downstream status.            | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
| FAC-108     | Factor: send overdue escalation                      | Factor         | Configured tenant, roles and representative data   | Execute send overdue escalation; inspect UI/API response, audit event and downstream status.              | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
| FAC-109     | Factor: filter dashboard by entity                   | Factor         | Configured tenant, roles and representative data   | Execute filter dashboard by entity; inspect UI/API response, audit event and downstream status.           | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
