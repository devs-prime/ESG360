# 2. Tenant, Organisation and Boundary Management

> **Source:** PSSTEC ESG360 FDD v1.0, section 2. **Indicative release:** R0.
> Read `CLAUDE.md` (root) before implementing. Cross-functional rules BR-001–BR-020 (spec 24) apply to this module.


Provides the legal, operational and reporting structures used by every ESG process.

## Process Flow

1.  Create tenant and identity configuration.

2.  Build legal and operational hierarchy.

3.  Define reporting groups and GHG boundary.

4.  Assign data owners and approvers.

5.  Activate reporting periods.

| **Req. ID** | **Requirement**            | **Functional Description**                                                                                                  | **Priority** | **Acceptance Criteria**                                                        |
|-------------|----------------------------|-----------------------------------------------------------------------------------------------------------------------------|--------------|--------------------------------------------------------------------------------|
| 2-001       | Tenant provisioning        | Create tenant, subscription tier, region, data-residency policy, identity provider and branding.                            | Must         | Tenant is isolated and usable only after authorised activation.                |
| 2-002       | Organisation hierarchy     | Maintain groups, legal entities, business units, sites, facilities, assets, projects and cost centres with effective dates. | Must         | Hierarchy changes preserve history and do not rewrite prior reporting periods. |
| 2-003       | Consolidation groups       | Define reporting groups and ownership percentages independently from operational hierarchy.                                 | Must         | Values consolidate according to effective-dated rules.                         |
| 2-004       | GHG boundary method        | Support equity share, financial control and operational control methods.                                                    | Must         | Inventory includes/excludes sources per selected boundary and logs the method. |
| 2-005       | Reporting perimeter        | Create framework-specific reporting scopes and exclusions with rationale.                                                   | Must         | Disclosure pack displays perimeter and approved exclusions.                    |
| 2-006       | Dimensions                 | Configure dimensions such as geography, product, activity, department, gender, employment type and supplier category.       | Must         | Dimensions are reusable and effective-dated.                                   |
| 2-007       | Fiscal calendars           | Support calendar and non-calendar years, monthly/quarterly/annual periods and 4-4-5 calendars.                              | Should       | Period generation and close controls match configured calendar.                |
| 2-008       | Currency and unit policy   | Define group currency, local currencies, unit systems and rounding policies.                                                | Must         | Conversions use approved rate/unit versions with lineage.                      |
| 2-009       | Acquisition and divestment | Capture ownership changes and boundary impact.                                                                              | Should       | Restatement assessment is generated when significance threshold is met.        |
| 2-010       | Data residency policy      | Assign tenant and selected data classes to permitted hosting regions.                                                       | Must         | Platform rejects unsupported residency configuration.                          |

## Key Business Rules

- Each record must reference a tenant and at least one accountable organisation node.

- Effective dates may overlap only where explicitly permitted by configuration.

- Closed periods cannot be altered without controlled reopening or restatement.

## Outputs and Analytics

- Organisation coverage dashboard

- Boundary change report

- Entity completeness matrix



## Acceptance test cases (from Master Test Catalogue, spec 48)

Implementation of this module is not complete until these pass.

| **Test ID** | **Title**                                            | **Module**     | **Preconditions**                                  | **Test Steps**                                                                                            | **Expected Result**                                                                                       | **Type**      | **Priority** |
|---|---|---|---|---|---|---|---|
| TEN-001     | Provision isolated tenant                            | Tenant         | Operator authorised                                | Create two tenants; add same user email separately; create records in each.                               | Each tenant has independent configuration and records; no cross-tenant visibility.                        | Functional    | High         |
| ORG-001     | Effective-dated hierarchy change                     | Organisation   | Entity and site exist                              | Move site to new business unit effective next quarter; open prior and future reports.                     | Prior period retains old hierarchy; future period uses new hierarchy.                                     | Functional    | High         |
| ORG-100     | Organisation: create valid record                    | Organisation   | Configured tenant, roles and representative data   | Execute create valid record; inspect UI/API response, audit event and downstream status.                  | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| ORG-101     | Organisation: reject missing mandatory field         | Organisation   | Configured tenant, roles and representative data   | Execute reject missing mandatory field; inspect UI/API response, audit event and downstream status.       | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| ORG-102     | Organisation: validate effective date overlap        | Organisation   | Configured tenant, roles and representative data   | Execute validate effective date overlap; inspect UI/API response, audit event and downstream status.      | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| ORG-103     | Organisation: enforce scoped access                  | Organisation   | Configured tenant, roles and representative data   | Execute enforce scoped access; inspect UI/API response, audit event and downstream status.                | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| ORG-104     | Organisation: bulk import mixed valid/invalid rows   | Organisation   | Configured tenant, roles and representative data   | Execute bulk import mixed valid/invalid rows; inspect UI/API response, audit event and downstream status. | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| ORG-105     | Organisation: export authorised data                 | Organisation   | Configured tenant, roles and representative data   | Execute export authorised data; inspect UI/API response, audit event and downstream status.               | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
| ORG-106     | Organisation: retain revision history                | Organisation   | Configured tenant, roles and representative data   | Execute retain revision history; inspect UI/API response, audit event and downstream status.              | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
| ORG-107     | Organisation: trigger approval workflow              | Organisation   | Configured tenant, roles and representative data   | Execute trigger approval workflow; inspect UI/API response, audit event and downstream status.            | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
| ORG-108     | Organisation: send overdue escalation                | Organisation   | Configured tenant, roles and representative data   | Execute send overdue escalation; inspect UI/API response, audit event and downstream status.              | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
| ORG-109     | Organisation: filter dashboard by entity             | Organisation   | Configured tenant, roles and representative data   | Execute filter dashboard by entity; inspect UI/API response, audit event and downstream status.           | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
