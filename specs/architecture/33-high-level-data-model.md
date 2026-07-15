# 33. High-Level Data Model

> **Source:** PSSTEC ESG360 v1.0, section 33 (SDD).


| **Entity**            | **Purpose**                                                          |
|-----------------------|----------------------------------------------------------------------|
| Tenant                | Subscription, residency, branding and policy root.                   |
| OrganisationNode      | Legal entity, site, facility, asset, project and hierarchy.          |
| ReportingPeriod       | Fiscal period, status and close metadata.                            |
| MetricDefinition      | Type, unit, frequency, formula, validation and classification.       |
| FrameworkRequirement  | Framework/version/disclosure/datapoint metadata.                     |
| MetricMapping         | Links metric/datapoint to framework requirement.                     |
| CollectionTask        | Expected submission assignment and workflow.                         |
| Submission            | Value/narrative, source, status and revision.                        |
| Evidence              | File/link metadata, checksum, retention and access.                  |
| LineageEdge           | Directed link between source, transformation, result and disclosure. |
| EmissionSource        | Facility/activity source classification.                             |
| EmissionFactor        | Factor value, units, gases, source, geography and validity.          |
| CalculationRun        | Formula, input versions, result and execution metadata.              |
| GHGInventory          | Boundary, method, period, base year and approval.                    |
| RiskOpportunity       | Impact/risk/opportunity, score, horizon and owner.                   |
| Target                | Baseline, target, pathway and scope.                                 |
| Initiative            | Cost, timing, benefit, risk and target linkage.                      |
| Supplier              | Supplier identity, segmentation, risk and portal relationship.       |
| QuestionnaireResponse | Campaign answers, evidence and scores.                               |
| Disclosure            | Narrative and quantitative disclosure workspace.                     |
| DisclosureSnapshot    | Immutable approved publication package.                              |
| WorkflowInstance      | State, participants, timers and decisions.                           |
| AuditEvent            | Actor, action, object, before/after hash and timestamp.              |

