# 36. Service and Module Boundaries

> **Source:** PSSTEC ESG360 v1.0, section 36 (TDD).


| **Module**      | **Owned Data**                               | **Published Events**                                    |
|-----------------|----------------------------------------------|---------------------------------------------------------|
| Tenant/Identity | Tenant, entitlements, users, roles, policies | TenantProvisioned, RoleChanged, AccessReviewDue         |
| Organisation    | Hierarchy, boundary, calendar, dimensions    | OrganisationChanged, BoundaryChanged                    |
| Catalogue       | Metric, formula, validation, mapping         | MetricVersionPublished, MappingChanged                  |
| Collection      | Cycle, task, submission, approval            | TaskCreated, SubmissionApproved, PeriodClosed           |
| Evidence        | Evidence, lineage, assurance request         | EvidenceAdded, AssuranceRequestRaised                   |
| Carbon          | Source, factor, activity, run, inventory     | FactorApproved, CalculationCompleted, InventoryApproved |
| Risk/Target     | Risk, target, initiative, scenario           | RiskChanged, TargetApproved, InitiativeBenefitVerified  |
| Supplier        | Supplier, assessment, score, action          | AssessmentIssued, SupplierScoreChanged                  |
| Disclosure      | Disclosure, narrative, snapshot, export      | DisclosureApproved, PublicationCreated                  |
| Integration     | Connection, mapping, job, reconciliation     | IngestionCompleted, IntegrationFailed                   |
| AI              | Prompt, model policy, evaluation, activity   | AIRequestCompleted, ModelEvaluationFailed               |
| Audit           | Append-only event records                    | AuditArchiveCreated                                     |

