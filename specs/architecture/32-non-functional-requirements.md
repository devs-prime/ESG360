# 32. Non-Functional Requirements

> **Source:** PSSTEC ESG360 v1.0, section 32 (SDD).


| **ID**  | **Category**     | **Requirement**                                                                                                          |
|---------|------------------|--------------------------------------------------------------------------------------------------------------------------|
| NFR-001 | Availability     | Production service target 99.9% monthly excluding approved maintenance; higher tier configurable.                        |
| NFR-002 | Performance      | 95th percentile interactive read \<2.5 seconds and save \<3 seconds under reference load.                                |
| NFR-003 | Calculation      | Standard monthly inventory for 1 million activity records completes within 60 minutes under reference tier.              |
| NFR-004 | Scalability      | Horizontally scale to at least 10,000 tenants, 100,000 users and billions of activity/time-series records.               |
| NFR-005 | RPO              | Standard SaaS target \<=15 minutes; dedicated tier configurable.                                                         |
| NFR-006 | RTO              | Standard SaaS target \<=4 hours for regional service restoration.                                                        |
| NFR-007 | Security         | No critical/high unresolved vulnerabilities in production release; severity SLA defined.                                 |
| NFR-008 | Privacy          | Support data classification, residency, retention, export and deletion workflows.                                        |
| NFR-009 | Accessibility    | Target WCAG 2.2 AA for web user journeys.                                                                                |
| NFR-010 | Localisation     | Unicode, RTL readiness, locale formats, translation catalogue and localised templates.                                   |
| NFR-011 | Browser          | Current and previous major versions of Edge, Chrome, Firefox and Safari.                                                 |
| NFR-012 | Audit            | Security and controlled-business events retained for configurable 7-10 years or jurisdiction requirement.                |
| NFR-013 | Observability    | Every request/job carries correlation ID and emits structured logs, metrics and traces.                                  |
| NFR-014 | Maintainability  | Modular domain ownership, automated tests and backward-compatible APIs.                                                  |
| NFR-015 | Portability      | Containerised services and infrastructure-as-code for supported cloud/private profiles.                                  |
| NFR-016 | Interoperability | Versioned REST/OpenAPI, webhooks, standard file formats and configurable structured-report adapters.                     |
| NFR-017 | Resilience       | Retries, circuit breakers, queue buffering, idempotency and graceful degradation.                                        |
| NFR-018 | Supportability   | Tenant-safe diagnostics, health dashboards, runbooks and controlled support access.                                      |
| NFR-019 | Data integrity   | ACID for controlled transactions and checksum/lineage for files and calculations.                                        |
| NFR-020 | Sustainability   | Measure platform energy/carbon indicators where cloud-provider data is available and optimise compute/storage lifecycle. |

