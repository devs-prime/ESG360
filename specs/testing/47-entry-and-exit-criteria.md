# 47. Entry and Exit Criteria

> **Source:** PSSTEC ESG360 v1.0, section 47 (Master Test Specification).


| **Stage**   | **Entry**                                                                             | **Exit**                                                                                 |
|-------------|---------------------------------------------------------------------------------------|------------------------------------------------------------------------------------------|
| System test | Approved requirements; deployed test build; test data; critical interfaces available. | All planned tests executed; no open critical/high defects; acceptance coverage achieved. |
| Performance | Stable functional build; production-like topology and dataset.                        | Targets met or approved capacity plan and remediation.                                   |
| Security    | Feature complete; threat model and architecture available.                            | No critical/high exploitable findings; medium findings have approved plan.               |
| UAT         | System test passed; user guides; business scenarios and roles configured.             | Product owner and ESG lead sign-off; known limitations documented.                       |
| Release     | All gates passed; rollback and support readiness confirmed.                           | Production smoke passed; monitoring healthy; release approved.                           |

