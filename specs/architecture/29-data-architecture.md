# 29. Data Architecture

> **Source:** PSSTEC ESG360 v1.0, section 29 (SDD).


| **Store**           | **Data Types**                                                                | **Design**                                                                               |
|---------------------|-------------------------------------------------------------------------------|------------------------------------------------------------------------------------------|
| Relational database | Tenant configuration, master data, workflows, submissions, risks, disclosures | Tenant key on all business tables; row security; temporal/version tables; read replicas. |
| Time-series store   | Meter readings, telemetry and high-volume activity                            | Partition by tenant/source/time; retention tiers; quality flags.                         |
| Object storage      | Evidence, imports, exports and generated reports                              | Tenant containers/prefixes, malware scan, encryption, checksums, legal hold.             |
| Search index        | Full-text discovery over permitted metadata/documents                         | Security-filtered indexing; no cross-tenant index leakage.                               |
| Vector index        | Optional grounded AI retrieval                                                | Tenant and classification filters; encrypted embeddings; opt-out mode.                   |
| Cache               | Session-independent reference data, factors and configuration                 | Tenant-aware keys; no uncontrolled caching of restricted content.                        |
| Audit log           | Security and domain events                                                    | Append-only/WORM-capable retention; tamper evidence.                                     |
| Analytics lakehouse | Approved curated facts, dimensions and semantic models                        | Bronze/silver/gold separation; snapshot and lineage metadata.                            |

