# 21. Workflow, Notifications and Case Management

> **Source:** PSSTEC ESG360 FDD v1.0, section 21. **Indicative release:** R0.
> Read `CLAUDE.md` (root) before implementing. Cross-functional rules BR-001–BR-020 (spec 24) apply to this module.


Provides reusable orchestration for data, disclosures, risks, incidents, audits and supplier processes.

| **Req. ID** | **Requirement**        | **Functional Description**                                            | **Priority** | **Acceptance Criteria**                                        |
|-------------|------------------------|-----------------------------------------------------------------------|--------------|----------------------------------------------------------------|
| 21-001      | Workflow designer      | Configure states, transitions, roles, conditions, SLA and escalation. | Must         | Published workflow version is immutable for running instances. |
| 21-002      | Parallel approval      | Support parallel, sequential and consensus approvals.                 | Must         | Completion rule is explicit.                                   |
| 21-003      | Conditional routing    | Route based on value, severity, entity, framework and risk.           | Must         | Decision path is logged.                                       |
| 21-004      | SLA timers             | Calculate business-time deadlines with calendars.                     | Should       | Pause/resume rules are configurable.                           |
| 21-005      | Notification templates | Create multilingual email, in-app and collaboration messages.         | Must         | Template variables are validated.                              |
| 21-006      | Cases                  | Create cases for exceptions, assurance queries and investigations.    | Must         | Case access can be more restrictive than parent record.        |
| 21-007      | Action management      | Assign corrective/preventive actions with verification.               | Must         | Closure requires evidence and reviewer.                        |
| 21-008      | Escalation             | Escalate overdue or high-risk items to management.                    | Must         | Escalation chain is resolved from hierarchy.                   |
| 21-009      | Webhook events         | Publish state-change events to external systems.                      | Should       | Delivery is retried and observable.                            |
| 21-010      | Workflow analytics     | Measure cycle time, bottlenecks and rework.                           | Should       | Dashboard supports process and entity comparison.              |

