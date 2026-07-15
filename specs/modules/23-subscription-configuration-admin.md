# 23. Subscription, Configuration and Administration

> **Source:** PSSTEC ESG360 FDD v1.0, section 23. **Indicative release:** R0.
> Read `CLAUDE.md` (root) before implementing. Cross-functional rules BR-001–BR-020 (spec 24) apply to this module.


Supports commercial packaging, tenant settings, feature flags and operational administration.

| **Req. ID** | **Requirement**         | **Functional Description**                                        | **Priority** | **Acceptance Criteria**                              |
|-------------|-------------------------|-------------------------------------------------------------------|--------------|------------------------------------------------------|
| 23-001      | Product editions        | Configure edition, modules, user/data limits and entitlements.    | Must         | Unlicensed feature is inaccessible.                  |
| 23-002      | Usage metering          | Measure users, storage, API, AI and data volumes.                 | Should       | Meter records are immutable and tenant-visible.      |
| 23-003      | Feature flags           | Enable staged rollout by tenant and environment.                  | Must         | Flag changes are audited and reversible.             |
| 23-004      | Branding                | Configure logo, colours, domains and document templates.          | Should       | Branding remains accessible and responsive.          |
| 23-005      | Localisation            | Configure languages, units, date/number formats and translations. | Must         | Fallback language is deterministic.                  |
| 23-006      | Retention               | Set record/file retention by type and jurisdiction.               | Must         | Purge honours legal holds and immutable snapshots.   |
| 23-007      | Data export and exit    | Provide full tenant export and deletion workflow.                 | Must         | Exit package and deletion certificate are generated. |
| 23-008      | Sandbox cloning         | Create masked test/sandbox copy where policy allows.              | Should       | Personal/restricted fields are masked.               |
| 23-009      | Configuration promotion | Promote metadata from dev/test to production with comparison.     | Must         | Promotion is approved and versioned.                 |
| 23-010      | Admin audit             | Record all configuration and security changes.                    | Must         | Admin cannot delete audit history.                   |



## Acceptance test cases (from Master Test Catalogue, spec 48)

Implementation of this module is not complete until these pass.

| **Test ID** | **Title**                                            | **Module**     | **Preconditions**                                  | **Test Steps**                                                                                            | **Expected Result**                                                                                       | **Type**      | **Priority** |
|---|---|---|---|---|---|---|---|
| ADM-100     | Administration: create valid record                  | Administration | Configured tenant, roles and representative data   | Execute create valid record; inspect UI/API response, audit event and downstream status.                  | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| ADM-101     | Administration: reject missing mandatory field       | Administration | Configured tenant, roles and representative data   | Execute reject missing mandatory field; inspect UI/API response, audit event and downstream status.       | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| ADM-102     | Administration: validate effective date overlap      | Administration | Configured tenant, roles and representative data   | Execute validate effective date overlap; inspect UI/API response, audit event and downstream status.      | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| ADM-103     | Administration: enforce scoped access                | Administration | Configured tenant, roles and representative data   | Execute enforce scoped access; inspect UI/API response, audit event and downstream status.                | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| ADM-104     | Administration: bulk import mixed valid/invalid rows | Administration | Configured tenant, roles and representative data   | Execute bulk import mixed valid/invalid rows; inspect UI/API response, audit event and downstream status. | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| ADM-105     | Administration: export authorised data               | Administration | Configured tenant, roles and representative data   | Execute export authorised data; inspect UI/API response, audit event and downstream status.               | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
| ADM-106     | Administration: retain revision history              | Administration | Configured tenant, roles and representative data   | Execute retain revision history; inspect UI/API response, audit event and downstream status.              | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
| ADM-107     | Administration: trigger approval workflow            | Administration | Configured tenant, roles and representative data   | Execute trigger approval workflow; inspect UI/API response, audit event and downstream status.            | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
| ADM-108     | Administration: send overdue escalation              | Administration | Configured tenant, roles and representative data   | Execute send overdue escalation; inspect UI/API response, audit event and downstream status.              | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
| ADM-109     | Administration: filter dashboard by entity           | Administration | Configured tenant, roles and representative data   | Execute filter dashboard by entity; inspect UI/API response, audit event and downstream status.           | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
