# 40. Workflow Technical Design

> **Source:** PSSTEC ESG360 v1.0, section 40 (TDD).


| **Concept**  | **Implementation**                                                                    |
|--------------|---------------------------------------------------------------------------------------|
| Definition   | Versioned BPMN/state-machine metadata with immutable published version.               |
| Instance     | References definition version, tenant, business object and current state.             |
| Human task   | Candidate role/scope, assignee, due date, form schema and decision options.           |
| Timer        | Business calendar aware; durable and recoverable.                                     |
| Decision     | Expression or rules service with input/output audit.                                  |
| Escalation   | Creates notification/action and optionally reassigns.                                 |
| Compensation | Defined for external integrations where rollback is not atomic.                       |
| Migration    | Running instances remain on original version unless controlled migration is executed. |

