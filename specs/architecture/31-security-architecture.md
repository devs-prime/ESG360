# 31. Security Architecture

> **Source:** PSSTEC ESG360 v1.0, section 31 (SDD).


| **Control Area**     | **Design**                                                                                                                                             |
|----------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------|
| Tenant isolation     | Tenant context resolved from trusted identity/token; database row policies and service-level guards; automated isolation tests.                        |
| Encryption           | TLS 1.2+ in transit; platform-managed or customer-managed encryption at rest; key rotation and secrets vault.                                          |
| Identity             | Federated SSO, MFA, SCIM/lifecycle integration, break-glass accounts and privileged identity management.                                               |
| Authorisation        | RBAC plus scope/attribute policies; server-side enforcement; export and field-level restrictions.                                                      |
| Application security | Secure SDLC, threat modelling, SAST, dependency scanning, DAST, penetration testing and signed artifacts.                                              |
| Data protection      | Classification, minimisation, masking, retention, legal hold, privacy thresholds and data-subject workflows where applicable.                          |
| Network              | Private endpoints, WAF, DDoS protection, egress controls, service-to-service identity and zero-trust segmentation.                                     |
| Audit and detection  | Central security logs, SIEM integration, anomaly detection, alerting and incident-response runbooks.                                                   |
| Backup and recovery  | Encrypted point-in-time backups, geo-redundant copies where permitted, restore tests and tenant-aware recovery.                                        |
| AI security          | Model gateway, prompt-injection controls, grounding filters, content safety, no training on tenant data by default and configurable private endpoints. |

