# 3. Identity, Access and Segregation of Duties

> **Source:** PSSTEC ESG360 FDD v1.0, section 3. **Indicative release:** R0.
> Read `CLAUDE.md` (root) before implementing. Cross-functional rules BR-001–BR-020 (spec 24) apply to this module.


Controls authentication, authorisation and conflicts across tenant, organisation and workflow scopes.

| **Req. ID** | **Requirement**              | **Functional Description**                                                        | **Priority** | **Acceptance Criteria**                                              |
|-------------|------------------------------|-----------------------------------------------------------------------------------|--------------|----------------------------------------------------------------------|
| 3-001       | Single sign-on               | Support OpenID Connect and SAML federation.                                       | Must         | Configured users authenticate through the tenant identity provider.  |
| 3-002       | MFA enforcement              | Enforce MFA through federated IdP or native authentication policy.                | Must         | High-privilege access is blocked without MFA.                        |
| 3-003       | Role-based access            | Assign platform and business roles with entity, site, metric and framework scope. | Must         | User sees only authorised functions and records.                     |
| 3-004       | Attribute-based restrictions | Use attributes including geography, confidentiality and data category.            | Should       | Policy decisions are logged and consistently enforced.               |
| 3-005       | Segregation of duties        | Detect submitter/approver and administrator/auditor conflicts.                    | Must         | Conflicting assignments are blocked or require documented exception. |
| 3-006       | External assurance access    | Provide time-limited, read-only or comment access to selected evidence rooms.     | Must         | External users cannot access unshared tenant records.                |
| 3-007       | Supplier access              | Restrict supplier users to their organisation, requests and evidence.             | Must         | Supplier cannot enumerate other suppliers.                           |
| 3-008       | Privileged access            | Use just-in-time elevation and approval for sensitive administrative actions.     | Should       | Elevation expires and is auditable.                                  |
| 3-009       | Session controls             | Configure inactivity timeout, token lifetime and concurrent-session policy.       | Must         | Sessions expire according to tenant policy.                          |
| 3-010       | Access reviews               | Run periodic certification campaigns for roles and scopes.                        | Should       | Uncertified access is revoked or escalated.                          |

## Key Business Rules

- No platform operator may view tenant content by default.

- All denied and privileged actions must be logged.

- Deleted user accounts retain attribution on historical records.



## Acceptance test cases (from Master Test Catalogue, spec 48)

Implementation of this module is not complete until these pass.

| **Test ID** | **Title**                                            | **Module**     | **Preconditions**                                  | **Test Steps**                                                                                            | **Expected Result**                                                                                       | **Type**      | **Priority** |
|---|---|---|---|---|---|---|---|
| SEC-001     | Prevent cross-tenant API access                      | Security       | Tokens for tenant A and record in tenant B         | Call tenant B record endpoint with tenant A token and guessed ID.                                         | Request is denied and security event is logged.                                                           | Functional    | High         |
| SEC-002     | Segregation of duties conflict                       | Security       | User is preparer                                   | Assign same user as final approver for same scope.                                                        | Assignment is blocked or exception workflow required.                                                     | Functional    | High         |
| SEC-100     | Security: create valid record                        | Security       | Configured tenant, roles and representative data   | Execute create valid record; inspect UI/API response, audit event and downstream status.                  | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| SEC-101     | Security: reject missing mandatory field             | Security       | Configured tenant, roles and representative data   | Execute reject missing mandatory field; inspect UI/API response, audit event and downstream status.       | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| SEC-102     | Security: validate effective date overlap            | Security       | Configured tenant, roles and representative data   | Execute validate effective date overlap; inspect UI/API response, audit event and downstream status.      | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| SEC-103     | Security: enforce scoped access                      | Security       | Configured tenant, roles and representative data   | Execute enforce scoped access; inspect UI/API response, audit event and downstream status.                | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| SEC-104     | Security: bulk import mixed valid/invalid rows       | Security       | Configured tenant, roles and representative data   | Execute bulk import mixed valid/invalid rows; inspect UI/API response, audit event and downstream status. | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| SEC-105     | Security: export authorised data                     | Security       | Configured tenant, roles and representative data   | Execute export authorised data; inspect UI/API response, audit event and downstream status.               | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
| SEC-106     | Security: retain revision history                    | Security       | Configured tenant, roles and representative data   | Execute retain revision history; inspect UI/API response, audit event and downstream status.              | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
| SEC-107     | Security: trigger approval workflow                  | Security       | Configured tenant, roles and representative data   | Execute trigger approval workflow; inspect UI/API response, audit event and downstream status.            | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
| SEC-108     | Security: send overdue escalation                    | Security       | Configured tenant, roles and representative data   | Execute send overdue escalation; inspect UI/API response, audit event and downstream status.              | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
| SEC-109     | Security: filter dashboard by entity                 | Security       | Configured tenant, roles and representative data   | Execute filter dashboard by entity; inspect UI/API response, audit event and downstream status.           | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
| SEC-900     | SQL injection resistance                             | Security       | Test environment                                   | Submit injection payloads in search/import/API fields.                                                    | Inputs are safely handled; no data leakage or query manipulation.                                         | Security      | Critical     |
| SEC-901     | Broken object-level authorisation                    | Security       | Multiple scoped users                              | Enumerate IDs across entities and tenants.                                                                | All unauthorised objects return denied/not found without leakage.                                         | Security      | Critical     |
| SEC-902     | Privilege elevation expiry                           | Security       | JIT workflow configured                            | Elevate admin; wait for expiry; retry privileged action.                                                  | Action denied after expiry and elevation history logged.                                                  | Security      | High         |
| SEC-903     | Evidence signed URL expiry                           | Security       | Evidence exists                                    | Use download URL after expiry and from unauthorised session.                                              | Download denied.                                                                                          | Security      | High         |
| SEC-904     | Audit tamper detection                               | Security       | Audit archive exists                               | Attempt update/delete through DB/application path.                                                        | Modification is blocked or detected and alerted.                                                          | Security      | Critical     |
