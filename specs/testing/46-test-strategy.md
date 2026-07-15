# 46. Test Strategy

> **Source:** PSSTEC ESG360 v1.0, section 46 (Master Test Specification).


| **Test Level**         | **Objective**                                                                |
|------------------------|------------------------------------------------------------------------------|
| Unit                   | Validate formulas, rules, validators and service logic in isolation.         |
| Component              | Validate service/database/message behaviour.                                 |
| API contract           | Ensure backward compatibility and schema compliance.                         |
| Integration            | Validate ERP, files, identity, messaging, storage and external providers.    |
| System functional      | Validate end-to-end product requirements and workflows.                      |
| Calculation validation | Validate emission and ESG calculations against independent expected results. |
| Security               | Validate isolation, access, OWASP controls, encryption and audit.            |
| Performance            | Validate response time, throughput, calculation and ingestion scale.         |
| Resilience / DR        | Validate failure recovery, retries, restore and regional recovery.           |
| Accessibility          | Validate WCAG keyboard, screen-reader, contrast and responsive flows.        |
| AI evaluation          | Validate groundedness, privacy, citations, safety and regression.            |
| UAT                    | Validate business usability and acceptance by target personas.               |

