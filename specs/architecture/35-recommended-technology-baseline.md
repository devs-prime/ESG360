# 35. Recommended Technology Baseline

> **Source:** PSSTEC ESG360 v1.0, section 35 (SDD).


| **Layer**       | **Recommended Baseline**                              | **Notes**                                                                               |
|-----------------|-------------------------------------------------------|-----------------------------------------------------------------------------------------|
| Web UI          | React/Next.js with TypeScript                         | Component library, accessibility, internationalisation and server-side security checks. |
| API gateway     | Managed API gateway / ingress                         | OAuth validation, WAF, throttling, version routing and telemetry.                       |
| Domain services | .NET 8/10 or Java 21 services                         | Domain-driven modules; modular monolith initially or selected services based on scale.  |
| Workflow        | BPMN-capable workflow engine or durable orchestration | Human tasks, timers, SLA and versioned process definitions.                             |
| Relational data | PostgreSQL or Azure SQL                               | Row-level tenant security, temporal/version patterns and HA.                            |
| Time-series     | Cloud time-series service or PostgreSQL extension     | Meter and IoT data with partitioning.                                                   |
| Messaging       | Service Bus/Kafka-compatible broker                   | Events, retries, dead letters and integration decoupling.                               |
| Object storage  | S3-compatible / Azure Blob                            | Evidence, imports, exports and snapshots.                                               |
| Analytics       | Lakehouse + semantic BI layer                         | Curated ESG facts and row-level security.                                               |
| Search          | OpenSearch/managed search                             | Metadata and document search with security filters.                                     |
| AI gateway      | Provider-neutral model orchestration                  | Private/public model routing, grounding, evaluation and audit.                          |
| Identity        | Microsoft Entra ID / standards-based IdP              | OIDC, SAML, SCIM and B2B/external identities.                                           |
| Infrastructure  | Kubernetes or managed app/container platform          | Infrastructure-as-code, autoscaling and policy enforcement.                             |
| Observability   | OpenTelemetry + managed monitoring/SIEM               | Logs, metrics, traces, alerting and security correlation.                               |

