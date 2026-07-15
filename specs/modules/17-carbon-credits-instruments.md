# 17. Carbon Credits, Certificates and Environmental Attributes

> **Source:** PSSTEC ESG360 FDD v1.0, section 17. **Indicative release:** R2 (indicative).
> Read `CLAUDE.md` (root) before implementing. Cross-functional rules BR-001–BR-020 (spec 24) apply to this module.


Tracks environmental instruments without conflating offsets, avoided emissions and inventory reductions.

| **Req. ID** | **Requirement**       | **Functional Description**                                                    | **Priority** | **Acceptance Criteria**                         |
|-------------|-----------------------|-------------------------------------------------------------------------------|--------------|-------------------------------------------------|
| 17-001      | Instrument registry   | Record credit/certificate type, standard, project, vintage, serial and owner. | Must         | Serial uniqueness is enforced.                  |
| 17-002      | Acquisition           | Record purchase, transfer, price, currency and counterparty.                  | Must         | Inventory quantity reconciles with transaction. |
| 17-003      | Retirement            | Retire instruments against claim, entity, period and target.                  | Must         | Retired serial cannot be reused.                |
| 17-004      | Claim hierarchy       | Configure permitted claims and disclosure wording.                            | Should       | Unsupported claim is blocked or warned.         |
| 17-005      | Quality assessment    | Score additionality, permanence, leakage, verification and co-benefits.       | Should       | Quality rationale is visible.                   |
| 17-006      | REC allocation        | Allocate energy certificates to consumption.                                  | Must         | Allocation cannot exceed eligible consumption.  |
| 17-007      | Accounting separation | Separate gross inventory, net claims, offsets and avoided emissions.          | Must         | Reports clearly distinguish each amount.        |
| 17-008      | Registry integration  | Connect to external registries where API/licence permits.                     | Could        | External status and last sync are shown.        |
| 17-009      | Certificate expiry    | Alert on expiry or invalidation.                                              | Must         | Invalid instrument cannot support claim.        |
| 17-010      | Audit trail           | Maintain chain of custody and documentary proof.                              | Must         | Assurer can trace acquisition to retirement.    |

## Outputs and Analytics

- Environmental attribute inventory

- Retirement and claims report

- Certificate coverage



## Acceptance test cases (from Master Test Catalogue, spec 48)

Implementation of this module is not complete until these pass.

| **Test ID** | **Title**                                            | **Module**     | **Preconditions**                                  | **Test Steps**                                                                                            | **Expected Result**                                                                                       | **Type**      | **Priority** |
|---|---|---|---|---|---|---|---|
| INS-001     | Retire carbon credit                                 | Instruments    | Owned valid credits exist                          | Retire serials then attempt reuse.                                                                        | Retirement succeeds once; reuse is blocked.                                                               | Functional    | High         |
| INS-100     | Instruments: create valid record                     | Instruments    | Configured tenant, roles and representative data   | Execute create valid record; inspect UI/API response, audit event and downstream status.                  | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| INS-101     | Instruments: reject missing mandatory field          | Instruments    | Configured tenant, roles and representative data   | Execute reject missing mandatory field; inspect UI/API response, audit event and downstream status.       | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| INS-102     | Instruments: validate effective date overlap         | Instruments    | Configured tenant, roles and representative data   | Execute validate effective date overlap; inspect UI/API response, audit event and downstream status.      | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| INS-103     | Instruments: enforce scoped access                   | Instruments    | Configured tenant, roles and representative data   | Execute enforce scoped access; inspect UI/API response, audit event and downstream status.                | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| INS-104     | Instruments: bulk import mixed valid/invalid rows    | Instruments    | Configured tenant, roles and representative data   | Execute bulk import mixed valid/invalid rows; inspect UI/API response, audit event and downstream status. | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| INS-105     | Instruments: export authorised data                  | Instruments    | Configured tenant, roles and representative data   | Execute export authorised data; inspect UI/API response, audit event and downstream status.               | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
| INS-106     | Instruments: retain revision history                 | Instruments    | Configured tenant, roles and representative data   | Execute retain revision history; inspect UI/API response, audit event and downstream status.              | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
| INS-107     | Instruments: trigger approval workflow               | Instruments    | Configured tenant, roles and representative data   | Execute trigger approval workflow; inspect UI/API response, audit event and downstream status.            | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
| INS-108     | Instruments: send overdue escalation                 | Instruments    | Configured tenant, roles and representative data   | Execute send overdue escalation; inspect UI/API response, audit event and downstream status.              | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
| INS-109     | Instruments: filter dashboard by entity              | Instruments    | Configured tenant, roles and representative data   | Execute filter dashboard by entity; inspect UI/API response, audit event and downstream status.           | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
