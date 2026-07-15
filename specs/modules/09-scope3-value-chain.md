# 9. Scope 3 Value-Chain Accounting

> **Source:** PSSTEC ESG360 FDD v1.0, section 9. **Indicative release:** R1 (core).
> Read `CLAUDE.md` (root) before implementing. Cross-functional rules BR-001–BR-020 (spec 24) apply to this module.


Supports all upstream and downstream Scope 3 categories using multiple calculation methods.

| **Req. ID** | **Requirement**              | **Functional Description**                                                                               | **Priority** | **Acceptance Criteria**                                           |
|-------------|------------------------------|----------------------------------------------------------------------------------------------------------|--------------|-------------------------------------------------------------------|
| 9-001       | Category catalogue           | Support the fifteen GHG Protocol Scope 3 categories.                                                     | Must         | Every record maps to one category and lifecycle direction.        |
| 9-002       | Calculation methods          | Support supplier-specific, activity-based, distance-based, spend-based, average-data and hybrid methods. | Must         | Method and hierarchy are stored.                                  |
| 9-003       | Purchased goods and services | Calculate from supplier/product/activity/spend data.                                                     | Must         | Category 1 results can be segmented by supplier and commodity.    |
| 9-004       | Capital goods                | Track capital purchases and applicable factors.                                                          | Must         | Capital-goods emissions are separated from expense purchases.     |
| 9-005       | Fuel and energy activities   | Calculate upstream fuel and T&D losses.                                                                  | Should       | No double count with Scope 1/2.                                   |
| 9-006       | Transport and distribution   | Calculate based on mass, distance, mode, load and temperature control.                                   | Must         | Legs and allocation method are traceable.                         |
| 9-007       | Waste                        | Calculate treatment-specific emissions by waste stream.                                                  | Must         | Diversion and treatment method are captured.                      |
| 9-008       | Business travel              | Integrate travel provider or expense data.                                                               | Should       | Cabin class, distance and radiative forcing policy are supported. |
| 9-009       | Employee commuting           | Support surveys, HR populations and remote-work scenarios.                                               | Should       | Survey extrapolation is documented.                               |
| 9-010       | Leased assets and franchises | Apply boundary and ownership rules.                                                                      | Should       | Classification prevents overlap with Scope 1/2.                   |
| 9-011       | Use and end-of-life          | Model lifetime use, energy, maintenance and disposal.                                                    | Should       | Assumptions are versioned.                                        |
| 9-012       | Investments                  | Support financed-emissions datasets and methodology metadata.                                            | Could        | Asset class, attribution and data-quality fields are retained.    |
| 9-013       | Hotspot analysis             | Rank suppliers/categories by emissions and quality.                                                      | Must         | Users can drill to contributing records.                          |
| 9-014       | Supplier primary data        | Request, validate and approve supplier-specific emissions.                                               | Must         | Primary data retains assurance status and validity.               |

## Outputs and Analytics

- Scope 3 category inventory

- Supplier hotspot and engagement

- Method mix and data-quality score

- Spend-to-primary-data conversion progress



## Acceptance test cases (from Master Test Catalogue, spec 48)

Implementation of this module is not complete until these pass.

| **Test ID** | **Title**                                            | **Module**     | **Preconditions**                                  | **Test Steps**                                                                                            | **Expected Result**                                                                                       | **Type**      | **Priority** |
|---|---|---|---|---|---|---|---|
| S3-001      | Purchased goods spend method                         | Scope 3        | Spend and factor mapping exist                     | Import supplier spend and calculate category 1.                                                           | Emissions aggregate by supplier/category with spend method label.                                         | Functional    | High         |
| S3-002      | Supplier-specific data priority                      | Scope 3        | Spend estimate and approved supplier data exist    | Recalculate same supplier/period.                                                                         | Configured hierarchy uses supplier-specific data and shows replacement impact.                            | Functional    | High         |
| S3-100      | Scope 3: create valid record                         | Scope 3        | Configured tenant, roles and representative data   | Execute create valid record; inspect UI/API response, audit event and downstream status.                  | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| S3-101      | Scope 3: reject missing mandatory field              | Scope 3        | Configured tenant, roles and representative data   | Execute reject missing mandatory field; inspect UI/API response, audit event and downstream status.       | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| S3-102      | Scope 3: validate effective date overlap             | Scope 3        | Configured tenant, roles and representative data   | Execute validate effective date overlap; inspect UI/API response, audit event and downstream status.      | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| S3-103      | Scope 3: enforce scoped access                       | Scope 3        | Configured tenant, roles and representative data   | Execute enforce scoped access; inspect UI/API response, audit event and downstream status.                | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| S3-104      | Scope 3: bulk import mixed valid/invalid rows        | Scope 3        | Configured tenant, roles and representative data   | Execute bulk import mixed valid/invalid rows; inspect UI/API response, audit event and downstream status. | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| S3-105      | Scope 3: export authorised data                      | Scope 3        | Configured tenant, roles and representative data   | Execute export authorised data; inspect UI/API response, audit event and downstream status.               | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
| S3-106      | Scope 3: retain revision history                     | Scope 3        | Configured tenant, roles and representative data   | Execute retain revision history; inspect UI/API response, audit event and downstream status.              | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
| S3-107      | Scope 3: trigger approval workflow                   | Scope 3        | Configured tenant, roles and representative data   | Execute trigger approval workflow; inspect UI/API response, audit event and downstream status.            | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
| S3-108      | Scope 3: send overdue escalation                     | Scope 3        | Configured tenant, roles and representative data   | Execute send overdue escalation; inspect UI/API response, audit event and downstream status.              | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
| S3-109      | Scope 3: filter dashboard by entity                  | Scope 3        | Configured tenant, roles and representative data   | Execute filter dashboard by entity; inspect UI/API response, audit event and downstream status.           | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
