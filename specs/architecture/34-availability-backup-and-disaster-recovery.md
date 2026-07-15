# 34. Availability, Backup and Disaster Recovery

> **Source:** PSSTEC ESG360 v1.0, section 34 (SDD).


- Multi-zone deployment for production services where the selected region supports it.

- Database point-in-time recovery, daily full backup and periodic immutable backup according to tier.

- Object storage versioning and soft delete; legal-hold objects cannot be purged.

- Cross-region replication only when tenant residency policy permits.

- Quarterly restore tests and at least annual disaster-recovery exercise.

- Degraded-mode operation for non-critical AI, analytics or external-data services.

- Recovery priority: identity/tenant, transactional ESG records, evidence, workflows, integrations, analytics, AI.

PART III - TECHNICAL DESIGN DOCUMENT (TDD)

