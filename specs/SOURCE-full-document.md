**PSSTEC ESG360™**

International ESG, Sustainability & Carbon Intelligence Platform

**Functional Design Document (FDD)  
Solution Design Document (SDD)  
Technical Design Document (TDD)  
Master Test Specification**

Standalone Multi-Tenant SaaS Product  
Version 1.0 \| July 2026  
Prime Solutions and Services (PSSTEC)

# Document Control

| **Item**          | **Detail**                                                                                                |
|-------------------|-----------------------------------------------------------------------------------------------------------|
| Document title    | PSSTEC ESG360™ Standalone Product - FDD, SDD, TDD and Master Test Specification                           |
| Version           | 1.0                                                                                                       |
| Status            | Product baseline for architecture, engineering, QA, implementation and commercial planning                |
| Owner             | PSSTEC Product Management                                                                                 |
| Classification    | Confidential                                                                                              |
| Target deployment | Multi-tenant SaaS, dedicated tenant SaaS, sovereign cloud and private deployment                          |
| Primary audiences | Product owners, ESG SMEs, architects, developers, security, QA, DevOps, presales and implementation teams |

## Revision History

| **Version** | **Date**     | **Author/Owner**          | **Summary**                           |
|-------------|--------------|---------------------------|---------------------------------------|
| 1.0         | 14 July 2026 | PSSTEC Product Management | Initial consolidated product baseline |

## Approval Matrix

| **Role**           | **Approval Focus**                     | **Status** |
|--------------------|----------------------------------------|------------|
| Executive Sponsor  | Product direction and investment       | Pending    |
| Product Owner      | Functional completeness                | Pending    |
| ESG Domain Lead    | Standards and calculation logic        | Pending    |
| Solution Architect | Architecture and non-functional design | Pending    |
| Security Lead      | Security, privacy and resilience       | Pending    |
| QA Lead            | Testability and acceptance             | Pending    |

# Purpose and Scope

This specification defines a standalone, international ESG software product designed to collect, calculate, govern, assure, analyse and disclose environmental, social and governance information across multi-entity organisations and value chains. It is deliberately ERP-neutral and supports integration with Microsoft Dynamics 365, SAP, Oracle, NetSuite, Workday, SuccessFactors, utility systems, IoT platforms, spreadsheets and external data providers.

The baseline covers product functions, solution architecture, technical components, data model, security, interfaces, deployment, observability, operational design and executable test coverage. Jurisdiction-specific legal interpretations and licensed third-party content remain configuration or implementation responsibilities.

## Design Principles

- Framework-agnostic core: capture facts once and map them to multiple reporting frameworks.

- Audit-ready by design: preserve source, formula, factor, version, approval and restatement history.

- Configuration before customisation: dimensions, calculations, workflows, reports and templates are metadata-driven.

- Human accountability: AI may assist, classify and recommend, but material disclosures require controlled human approval.

- Security and privacy by design: tenant isolation, least privilege, encryption, data residency controls and immutable audit evidence.

- Open integration: API-first, event-driven and bulk-ingestion patterns coexist.

- Assurance-ready evidence: every reported value can be traced to source records and calculation lineage.

- Internationalisation: multi-language, multi-currency, multi-unit, multi-calendar and localised disclosure packs.

# Normative and Reference Frameworks

The product shall support configurable mappings to recognised sustainability and climate reporting frameworks. The software does not claim that use of the platform alone guarantees regulatory compliance; applicability, materiality and final disclosures remain the reporting entity’s responsibility.

| **Framework / Source**                      | **Product Relevance**                                | **Baseline Design Response**                                                                                           |
|---------------------------------------------|------------------------------------------------------|------------------------------------------------------------------------------------------------------------------------|
| IFRS S1                                     | General sustainability-related financial disclosures | Governance, strategy, risk management, metrics and targets disclosure structures; reporting-period alignment.          |
| IFRS S2                                     | Climate-related disclosures                          | Climate risks and opportunities, scenario analysis, emissions, transition plans and climate metrics.                   |
| GHG Protocol Corporate Standard             | Corporate GHG inventories                            | Organisational/operational boundaries, Scope 1 and Scope 2 accounting, base-year and recalculation controls.           |
| GHG Protocol Scope 3 Standard               | Value-chain emissions                                | Fifteen Scope 3 categories, supplier engagement, activity/spend/hybrid methods and data-quality scoring.               |
| GRI Universal and Topic Standards           | Impact reporting                                     | Material topics, stakeholder impacts, disclosures, content index and evidence mapping.                                 |
| European Sustainability Reporting Standards | Double materiality and ESRS disclosures              | Impact, risk and opportunity register; datapoint catalogue; disclosure requirement mapping; digital tagging readiness. |
| UN Sustainable Development Goals            | Strategic alignment                                  | Target and KPI mapping to SDGs.                                                                                        |
| TCFD / TNFD concepts                        | Climate and nature governance                        | Governance, strategy, risk and metrics structures; nature locations and dependencies.                                  |
| ISO-aligned management systems              | Operational controls                                 | Registers, incidents, corrective actions, audits, objectives and management review workflows.                          |

Reference sources are listed in Appendix A. Standards are licensed or publicly accessible under their respective terms; the product stores mappings and customer-entered content rather than redistributing protected standards text.

# Product Vision and Business Outcomes

ESG360 converts fragmented sustainability data into a governed ESG system of record and decision platform. It connects operational data, supplier information, ESG policies, risks, initiatives and disclosures through a common evidence and calculation layer.

| **Outcome**                      | **Capability**                                                                                    |
|----------------------------------|---------------------------------------------------------------------------------------------------|
| Trusted ESG data                 | Controlled data collection, validations, ownership, lineage, evidence and approvals.              |
| Defensible carbon accounting     | Versioned emission factors, transparent formulas, boundary controls, base years and restatements. |
| Multi-framework reporting        | Reusable datapoints mapped to disclosure packs and framework crosswalks.                          |
| Actionable transition management | Targets, abatement initiatives, marginal abatement cost, forecasts and benefit tracking.          |
| Supply-chain transparency        | Supplier assessments, Scope 3 data requests, risk screening and corrective actions.               |
| Assurance efficiency             | Evidence rooms, sampling, review notes, sign-offs and immutable audit trails.                     |
| Executive intelligence           | Board scorecards, risk heat maps, predictive trends, scenarios and narrative explanations.        |

# Personas and Roles

| **Persona**                  | **Primary Responsibilities**                                                        |
|------------------------------|-------------------------------------------------------------------------------------|
| Board / ESG Committee        | Approve strategy, material topics, targets and final disclosures.                   |
| Chief Sustainability Officer | Own programme, framework, performance and disclosure process.                       |
| ESG Manager                  | Configure metrics, coordinate data owners and manage reporting cycles.              |
| Carbon Accountant            | Maintain boundaries, factors, calculations, inventories and restatements.           |
| Data Owner                   | Submit data and evidence for assigned entities, sites and metrics.                  |
| Reviewer / Approver          | Validate accuracy, investigate exceptions and approve records.                      |
| Internal Auditor             | Test controls, inspect lineage and raise findings.                                  |
| External Assurer             | Access scoped evidence, samples, comments and sign-offs.                            |
| Risk Manager                 | Maintain ESG risks, scenarios, controls and treatments.                             |
| Procurement Manager          | Manage supplier ESG due diligence and improvement plans.                            |
| Supplier User                | Respond to questionnaires, upload evidence and emissions data.                      |
| HR / HSE / Compliance        | Provide social, safety, ethics, policy and incident data.                           |
| Investor Relations           | Prepare investor disclosures and responses.                                         |
| System Administrator         | Manage tenant settings, identity, roles, integrations and retention.                |
| Platform Operator            | Operate infrastructure without accessing tenant business content unless authorised. |

PART I - FUNCTIONAL DESIGN DOCUMENT (FDD)

# 1. Functional Architecture

| **Layer**           | **Modules**                                                                                                                  |
|---------------------|------------------------------------------------------------------------------------------------------------------------------|
| Experience          | Web application, mobile-responsive portal, supplier portal, assurance workspace, executive dashboards, API developer portal. |
| Governance          | Organisation, users, roles, workflow, reporting cycles, materiality, policies, risks, controls and audit.                    |
| ESG Operations      | Environmental, social, governance, incidents, actions, targets, initiatives and supplier ESG.                                |
| Carbon Intelligence | GHG inventory, factors, calculations, Scope 1/2/3, product carbon, projects, offsets and forecasting.                        |
| Disclosure          | Framework library, datapoint mappings, disclosure packs, narratives, approvals, digital export and publishing.               |
| Data and AI         | Ingestion, quality, lineage, anomaly detection, document extraction, recommendations and controlled generative AI.           |
| Platform            | Configuration, localisation, notifications, integrations, subscription, metering, security and observability.                |

# 2. Tenant, Organisation and Boundary Management

Provides the legal, operational and reporting structures used by every ESG process.

## Process Flow

1.  Create tenant and identity configuration.

2.  Build legal and operational hierarchy.

3.  Define reporting groups and GHG boundary.

4.  Assign data owners and approvers.

5.  Activate reporting periods.

| **Req. ID** | **Requirement**            | **Functional Description**                                                                                                  | **Priority** | **Acceptance Criteria**                                                        |
|-------------|----------------------------|-----------------------------------------------------------------------------------------------------------------------------|--------------|--------------------------------------------------------------------------------|
| 2-001       | Tenant provisioning        | Create tenant, subscription tier, region, data-residency policy, identity provider and branding.                            | Must         | Tenant is isolated and usable only after authorised activation.                |
| 2-002       | Organisation hierarchy     | Maintain groups, legal entities, business units, sites, facilities, assets, projects and cost centres with effective dates. | Must         | Hierarchy changes preserve history and do not rewrite prior reporting periods. |
| 2-003       | Consolidation groups       | Define reporting groups and ownership percentages independently from operational hierarchy.                                 | Must         | Values consolidate according to effective-dated rules.                         |
| 2-004       | GHG boundary method        | Support equity share, financial control and operational control methods.                                                    | Must         | Inventory includes/excludes sources per selected boundary and logs the method. |
| 2-005       | Reporting perimeter        | Create framework-specific reporting scopes and exclusions with rationale.                                                   | Must         | Disclosure pack displays perimeter and approved exclusions.                    |
| 2-006       | Dimensions                 | Configure dimensions such as geography, product, activity, department, gender, employment type and supplier category.       | Must         | Dimensions are reusable and effective-dated.                                   |
| 2-007       | Fiscal calendars           | Support calendar and non-calendar years, monthly/quarterly/annual periods and 4-4-5 calendars.                              | Should       | Period generation and close controls match configured calendar.                |
| 2-008       | Currency and unit policy   | Define group currency, local currencies, unit systems and rounding policies.                                                | Must         | Conversions use approved rate/unit versions with lineage.                      |
| 2-009       | Acquisition and divestment | Capture ownership changes and boundary impact.                                                                              | Should       | Restatement assessment is generated when significance threshold is met.        |
| 2-010       | Data residency policy      | Assign tenant and selected data classes to permitted hosting regions.                                                       | Must         | Platform rejects unsupported residency configuration.                          |

## Key Business Rules

- Each record must reference a tenant and at least one accountable organisation node.

- Effective dates may overlap only where explicitly permitted by configuration.

- Closed periods cannot be altered without controlled reopening or restatement.

## Outputs and Analytics

- Organisation coverage dashboard

- Boundary change report

- Entity completeness matrix

# 3. Identity, Access and Segregation of Duties

Controls authentication, authorisation and conflicts across tenant, organisation and workflow scopes.

| **Req. ID** | **Requirement**              | **Functional Description**                                                        | **Priority** | **Acceptance Criteria**                                              |
|-------------|------------------------------|-----------------------------------------------------------------------------------|--------------|----------------------------------------------------------------------|
| 3-001       | Single sign-on               | Support OpenID Connect and SAML federation.                                       | Must         | Configured users authenticate through the tenant identity provider.  |
| 3-002       | MFA enforcement              | Enforce MFA through federated IdP or native authentication policy.                | Must         | High-privilege access is blocked without MFA.                        |
| 3-003       | Role-based access            | Assign platform and business roles with entity, site, metric and framework scope. | Must         | User sees only authorised functions and records.                     |
| 3-004       | Attribute-based restrictions | Use attributes including geography, confidentiality and data category.            | Should       | Policy decisions are logged and consistently enforced.               |
| 3-005       | Segregation of duties        | Detect submitter/approver and administrator/auditor conflicts.                    | Must         | Conflicting assignments are blocked or require documented exception. |
| 3-006       | External assurance access    | Provide time-limited, read-only or comment access to selected evidence rooms.     | Must         | External users cannot access unshared tenant records.                |
| 3-007       | Supplier access              | Restrict supplier users to their organisation, requests and evidence.             | Must         | Supplier cannot enumerate other suppliers.                           |
| 3-008       | Privileged access            | Use just-in-time elevation and approval for sensitive administrative actions.     | Should       | Elevation expires and is auditable.                                  |
| 3-009       | Session controls             | Configure inactivity timeout, token lifetime and concurrent-session policy.       | Must         | Sessions expire according to tenant policy.                          |
| 3-010       | Access reviews               | Run periodic certification campaigns for roles and scopes.                        | Should       | Uncertified access is revoked or escalated.                          |

## Key Business Rules

- No platform operator may view tenant content by default.

- All denied and privileged actions must be logged.

- Deleted user accounts retain attribution on historical records.

# 4. ESG Metric and Data-Point Catalogue

Defines reusable ESG measures, qualitative disclosures, formulas, dimensions, ownership and validation rules.

| **Req. ID** | **Requirement**           | **Functional Description**                                                              | **Priority** | **Acceptance Criteria**                                            |
|-------------|---------------------------|-----------------------------------------------------------------------------------------|--------------|--------------------------------------------------------------------|
| 4-001       | Metric definition         | Define name, description, topic, data type, unit, frequency, aggregation and owner.     | Must         | Metric can be activated only after mandatory metadata is complete. |
| 4-002       | Qualitative datapoint     | Create narrative, policy, yes/no, date, taxonomy and evidence-based datapoints.         | Must         | Datapoint accepts only configured data type and validation.        |
| 4-003       | Versioning                | Version definitions, formulas, units and mappings with effective dates.                 | Must         | Historical submissions retain original version.                    |
| 4-004       | Framework mapping         | Map one metric/datapoint to multiple disclosure requirements and custom reports.        | Must         | Crosswalk shows all mappings and coverage status.                  |
| 4-005       | Formula builder           | Create formulas using approved operators, dimensions, lookups and conversion functions. | Must         | Formula validates syntax and prevents circular references.         |
| 4-006       | Validation rules          | Configure range, variance, completeness, evidence and cross-field checks.               | Must         | Violations generate severity-based status.                         |
| 4-007       | Ownership matrix          | Assign preparer, reviewer, approver and backup by organisation and period.              | Must         | Assignments are resolved for every active collection task.         |
| 4-008       | Materiality applicability | Mark metrics as material, not material, mandatory or voluntary by reporting perimeter.  | Must         | Disclosure completeness uses applicability status.                 |
| 4-009       | Data confidentiality      | Classify public, internal, confidential, personal and restricted.                       | Must         | Access and export follow classification policy.                    |
| 4-010       | Catalogue import/export   | Bulk manage definitions through controlled templates and API.                           | Should       | Invalid rows are rejected with row-level errors.                   |

## Outputs and Analytics

- Metric catalogue

- Framework coverage matrix

- Ownership and collection workload

# 5. Reporting Cycles and Data Collection

Orchestrates recurring collection, submission, review, approval and close.

## Process Flow

6.  Create cycle and scope.

7.  Generate tasks and notify owners.

8.  Collect through UI, file, API or integration.

9.  Run validations and anomaly detection.

10. Review, resolve comments and approve.

11. Close period and create assurance snapshot.

| **Req. ID** | **Requirement**          | **Functional Description**                                                            | **Priority** | **Acceptance Criteria**                                      |
|-------------|--------------------------|---------------------------------------------------------------------------------------|--------------|--------------------------------------------------------------|
| 5-001       | Cycle setup              | Create annual, quarterly, monthly or ad hoc reporting cycles.                         | Must         | Cycle generates expected tasks for active metrics and scope. |
| 5-002       | Task generation          | Generate tasks by metric, entity, site and period.                                    | Must         | No duplicate active task for same assignment key.            |
| 5-003       | Manual entry             | Provide forms with instructions, prior-period values, validation and evidence upload. | Must         | Valid submission creates immutable revision and status.      |
| 5-004       | Bulk templates           | Generate and ingest controlled spreadsheet templates.                                 | Must         | Template identity and version are validated.                 |
| 5-005       | API ingestion            | Accept idempotent ingestion with source identifiers.                                  | Must         | Retries do not create duplicates.                            |
| 5-006       | Review workflow          | Route submissions through configurable review and approval stages.                    | Must         | Approval requires resolved blocking validations.             |
| 5-007       | Delegation               | Allow time-bound task delegation while retaining accountability.                      | Should       | Delegation is approved and logged.                           |
| 5-008       | Reminders and escalation | Send reminders and escalate overdue tasks.                                            | Must         | Notification follows tenant schedule and hierarchy.          |
| 5-009       | Period close             | Lock approved period after completion checks.                                         | Must         | Closed data cannot be changed through normal entry.          |
| 5-010       | Reopen and restate       | Reopen selected records or launch formal restatement.                                 | Must         | Reason, approver and impact are captured.                    |
| 5-011       | Comment threads          | Support contextual discussion, mentions and resolution.                               | Should       | Comments remain linked to record revision.                   |
| 5-012       | Offline/mobile capture   | Support constrained field capture with sync conflict handling.                        | Could        | Offline records synchronise without silent overwrite.        |

## Outputs and Analytics

- Collection progress dashboard

- Overdue tasks

- Validation exceptions

- Period close checklist

# 6. Evidence, Lineage and Assurance

Maintains traceable evidence from source to disclosed value and supports internal/external assurance.

| **Req. ID** | **Requirement**     | **Functional Description**                                                            | **Priority** | **Acceptance Criteria**                                            |
|-------------|---------------------|---------------------------------------------------------------------------------------|--------------|--------------------------------------------------------------------|
| 6-001       | Evidence upload     | Attach files, links, source extracts, certificates and signed declarations.           | Must         | Evidence receives checksum, metadata and retention classification. |
| 6-002       | Evidence reuse      | Link one evidence item to multiple records without duplicating the file.              | Should       | Link history is visible.                                           |
| 6-003       | Source lineage      | Capture source system, transaction/file identifier, transformation and ingestion job. | Must         | User can trace value to source and transformations.                |
| 6-004       | Calculation lineage | Store input versions, factor, formula, unit conversions and result.                   | Must         | Recalculation is reproducible.                                     |
| 6-005       | Evidence room       | Create scoped assurance workspaces by period, framework and disclosure.               | Must         | Only explicitly shared records are accessible.                     |
| 6-006       | Sampling            | Select statistical or judgemental samples and record rationale.                       | Should       | Sample population and selection are preserved.                     |
| 6-007       | Assurance requests  | Raise questions, request evidence and track response SLA.                             | Must         | Request status and response are auditable.                         |
| 6-008       | Control testing     | Document control design, frequency, test steps, sample and conclusion.                | Should       | Failed tests create findings/actions.                              |
| 6-009       | Sign-off            | Capture electronic sign-off from responsible executives.                              | Must         | Sign-off binds to a fixed disclosure snapshot.                     |
| 6-010       | Audit export        | Export evidence index, lineage and activity history.                                  | Must         | Export includes checksum manifest.                                 |

## Key Business Rules

- Evidence deletion must follow retention policy and legal-hold status.

- Approved/disclosed snapshots are immutable; corrections create new versions.

# 7. Emission Factor Library

Governed repository for emission factors, GWPs, calorific values and conversion coefficients.

| **Req. ID** | **Requirement**      | **Functional Description**                                                                                       | **Priority** | **Acceptance Criteria**                                           |
|-------------|----------------------|------------------------------------------------------------------------------------------------------------------|--------------|-------------------------------------------------------------------|
| 7-001       | Factor sources       | Maintain publisher, dataset, jurisdiction, year, methodology and licence metadata.                               | Must         | Each factor has source and effective period.                      |
| 7-002       | Factor dimensions    | Support fuel, vehicle, grid, spend category, process, refrigerant, transport mode and supplier-specific factors. | Must         | Lookup resolves unambiguously or returns controlled exception.    |
| 7-003       | Unit compatibility   | Define numerator/denominator units and conversion rules.                                                         | Must         | Incompatible activity units are rejected.                         |
| 7-004       | GWP sets             | Maintain assessment-report-specific global warming potential sets.                                               | Must         | Inventory records the chosen GWP version.                         |
| 7-005       | Grid factors         | Support location-based and market-based electricity factors.                                                     | Must         | Scope 2 method and contractual instrument hierarchy are recorded. |
| 7-006       | Supplier factors     | Accept verified supplier-specific factors with validity and assurance status.                                    | Should       | Expired factor is flagged.                                        |
| 7-007       | Approval workflow    | Review and approve factors before production use.                                                                | Must         | Draft factor cannot calculate approved inventory.                 |
| 7-008       | Bulk import          | Load factor datasets with validation and duplicate detection.                                                    | Must         | Import report identifies rejected records.                        |
| 7-009       | Factor hierarchy     | Configure preferred factor selection by geography, period, technology and quality.                               | Must         | System explains selected factor.                                  |
| 7-010       | Factor update impact | Identify records affected by factor revisions and support controlled recalculation.                              | Must         | Impact assessment lists affected periods and disclosures.         |

## Outputs and Analytics

- Factor catalogue

- Expiring factors

- Factor usage and impact analysis

# 8. Corporate GHG Inventory - Scope 1 and Scope 2

Calculates and governs direct and purchased-energy emissions.

## Process Flow

12. Register sources and boundaries.

13. Collect activity and contractual data.

14. Resolve factors and calculate gas-level emissions.

15. Validate completeness and outliers.

16. Consolidate by entity, scope and category.

17. Approve inventory and lock baseline.

| **Req. ID** | **Requirement**          | **Functional Description**                                                                         | **Priority** | **Acceptance Criteria**                                        |
|-------------|--------------------------|----------------------------------------------------------------------------------------------------|--------------|----------------------------------------------------------------|
| 8-001       | Emission source register | Register stationary combustion, mobile combustion, fugitive, process and purchased energy sources. | Must         | Each source is classified, owned and linked to facility.       |
| 8-002       | Activity capture         | Capture quantity, unit, period, source and evidence.                                               | Must         | Activity passes unit and period validations.                   |
| 8-003       | Scope classification     | Classify source as Scope 1, Scope 2 location-based or Scope 2 market-based.                        | Must         | Classification is controlled by source type and contract data. |
| 8-004       | Calculation engine       | Calculate gas-level emissions and CO2e using factor and GWP versions.                              | Must         | Result is reproducible and rounded only at presentation.       |
| 8-005       | Renewable instruments    | Track RECs, GOs, PPAs and residual mix information.                                                | Should       | Instrument allocation cannot exceed eligible consumption.      |
| 8-006       | Refrigerants             | Track additions, recovery, disposal and leakage by gas.                                            | Must         | Mass balance and leakage calculation are supported.            |
| 8-007       | Fleet                    | Capture fuel, distance or telematics-based activity.                                               | Should       | Method and data quality are shown.                             |
| 8-008       | Base year                | Set base year and inventory baseline by boundary and method.                                       | Must         | Base-year approval creates fixed baseline snapshot.            |
| 8-009       | Recalculation policy     | Configure significance thresholds and triggers.                                                    | Must         | Structural/method/factor changes generate assessment.          |
| 8-010       | Intensity metrics        | Calculate emissions per revenue, production, FTE, area or custom denominator.                      | Must         | Denominator version and unit are traceable.                    |
| 8-011       | Uncertainty and quality  | Score source, activity and factor quality; capture uncertainty.                                    | Should       | Quality score rolls up using configured method.                |
| 8-012       | Inventory approval       | Review and sign off inventory by entity and group.                                                 | Must         | Group inventory cannot close with blocking exceptions.         |

## Outputs and Analytics

- Scope 1 and 2 inventory

- Location vs market-based comparison

- Emission source trend

- Data quality and uncertainty dashboard

# 9. Scope 3 Value-Chain Accounting

Supports all upstream and downstream Scope 3 categories using multiple calculation methods.

| **Req. ID** | **Requirement**              | **Functional Description**                                                                               | **Priority** | **Acceptance Criteria**                                           |
|-------------|------------------------------|----------------------------------------------------------------------------------------------------------|--------------|-------------------------------------------------------------------|
| 9-001       | Category catalogue           | Support the fifteen GHG Protocol Scope 3 categories.                                                     | Must         | Every record maps to one category and lifecycle direction.        |
| 9-002       | Calculation methods          | Support supplier-specific, activity-based, distance-based, spend-based, average-data and hybrid methods. | Must         | Method and hierarchy are stored.                                  |
| 9-003       | Purchased goods and services | Calculate from supplier/product/activity/spend data.                                                     | Must         | Category 1 results can be segmented by supplier and commodity.    |
| 9-004       | Capital goods                | Track capital purchases and applicable factors.                                                          | Must         | Capital-goods emissions are separated from expense purchases.     |
| 9-005       | Fuel and energy activities   | Calculate upstream fuel and T&D losses.                                                                  | Should       | No double count with Scope 1/2.                                   |
| 9-006       | Transport and distribution   | Calculate based on mass, distance, mode, load and temperature control.                                   | Must         | Legs and allocation method are traceable.                         |
| 9-007       | Waste                        | Calculate treatment-specific emissions by waste stream.                                                  | Must         | Diversion and treatment method are captured.                      |
| 9-008       | Business travel              | Integrate travel provider or expense data.                                                               | Should       | Cabin class, distance and radiative forcing policy are supported. |
| 9-009       | Employee commuting           | Support surveys, HR populations and remote-work scenarios.                                               | Should       | Survey extrapolation is documented.                               |
| 9-010       | Leased assets and franchises | Apply boundary and ownership rules.                                                                      | Should       | Classification prevents overlap with Scope 1/2.                   |
| 9-011       | Use and end-of-life          | Model lifetime use, energy, maintenance and disposal.                                                    | Should       | Assumptions are versioned.                                        |
| 9-012       | Investments                  | Support financed-emissions datasets and methodology metadata.                                            | Could        | Asset class, attribution and data-quality fields are retained.    |
| 9-013       | Hotspot analysis             | Rank suppliers/categories by emissions and quality.                                                      | Must         | Users can drill to contributing records.                          |
| 9-014       | Supplier primary data        | Request, validate and approve supplier-specific emissions.                                               | Must         | Primary data retains assurance status and validity.               |

## Outputs and Analytics

- Scope 3 category inventory

- Supplier hotspot and engagement

- Method mix and data-quality score

- Spend-to-primary-data conversion progress

# 10. Energy, Water, Waste and Environmental Operations

Manages non-carbon environmental performance and operational controls.

| **Req. ID** | **Requirement**        | **Functional Description**                                                    | **Priority** | **Acceptance Criteria**                            |
|-------------|------------------------|-------------------------------------------------------------------------------|--------------|----------------------------------------------------|
| 10-001      | Energy register        | Capture electricity, steam, heat, cooling and fuels by meter/source.          | Must         | Energy balances and meter coverage are available.  |
| 10-002      | Renewable energy       | Track generated, purchased, exported and certificate-backed renewable energy. | Must         | Claims reconcile with consumption and instruments. |
| 10-003      | Water withdrawal       | Capture source, stress area, quality and volume.                              | Must         | Water is reported by source and geography.         |
| 10-004      | Water discharge        | Capture destination, treatment, quality parameters and incidents.             | Should       | Discharge violations create alerts.                |
| 10-005      | Water consumption      | Calculate withdrawal less discharge with configured rules.                    | Must         | Result is traceable to inputs.                     |
| 10-006      | Waste generation       | Capture hazardous/non-hazardous waste by material and site.                   | Must         | Quantity, treatment and destination are recorded.  |
| 10-007      | Waste hierarchy        | Track prevention, reuse, recycling, recovery, treatment and disposal.         | Must         | Diversion rate is calculated.                      |
| 10-008      | Pollutants             | Capture air emissions, effluent and other releases.                           | Should       | Permit thresholds and exceedances are tracked.     |
| 10-009      | Environmental permits  | Maintain permits, conditions, limits, renewals and evidence.                  | Must         | Expiry alerts and compliance status are generated. |
| 10-010      | Incidents              | Record spills, releases, breaches, impact and response.                       | Must         | Material incidents invoke escalation workflow.     |
| 10-011      | Biodiversity locations | Record proximity to protected/key biodiversity areas and dependencies.        | Should       | Map view displays locations and risk attributes.   |
| 10-012      | Objectives and plans   | Manage environmental objectives, actions, owners and benefits.                | Must         | Progress rolls into target dashboard.              |

## Outputs and Analytics

- Energy performance

- Water balance and stress dashboard

- Waste and circularity dashboard

- Permit compliance

- Environmental incident trends

# 11. Social and Human Capital

Captures workforce, safety, diversity, human rights, community and social-impact information.

| **Req. ID** | **Requirement**            | **Functional Description**                                                                             | **Priority** | **Acceptance Criteria**                                  |
|-------------|----------------------------|--------------------------------------------------------------------------------------------------------|--------------|----------------------------------------------------------|
| 11-001      | Workforce profile          | Ingest employees and non-employee workers by entity, location, contract and demographic dimensions.    | Must         | Privacy rules restrict person-level access.              |
| 11-002      | Diversity metrics          | Calculate representation, hiring, promotion, turnover and pay-gap indicators.                          | Must         | Small-population suppression is configurable.            |
| 11-003      | Health and safety          | Capture hours, fatalities, recordable injuries, lost-time cases, occupational illness and near misses. | Must         | Rates use approved denominator and period.               |
| 11-004      | Training                   | Track ESG, ethics, safety and role-based training completion.                                          | Must         | Expiry and non-compliance alerts are available.          |
| 11-005      | Human rights due diligence | Maintain salient issues, assessments, actions and remedy.                                              | Should       | Affected stakeholders and evidence are linked.           |
| 11-006      | Grievances                 | Record channel, category, anonymity, investigation and remedy.                                         | Must         | Confidentiality and retaliation controls apply.          |
| 11-007      | Labour practices           | Track working hours, wages, collective bargaining and forced/child-labour controls.                    | Should       | Country-specific thresholds are configurable.            |
| 11-008      | Employee engagement        | Import survey outcomes and action plans.                                                               | Could        | Anonymous survey confidentiality is preserved.           |
| 11-009      | Community investment       | Track cash, in-kind, volunteering, beneficiaries and outcomes.                                         | Should       | Impact indicators link to programmes.                    |
| 11-010      | Product responsibility     | Capture product safety, recalls, accessibility and customer impacts.                                   | Should       | Material incidents are escalated.                        |
| 11-011      | Data privacy incidents     | Record privacy breaches and remediation.                                                               | Should       | Restricted access and notification dates are controlled. |
| 11-012      | Social targets             | Set targets by workforce group, site or geography.                                                     | Must         | Progress is calculated from governed metrics.            |

## Outputs and Analytics

- Workforce and diversity scorecard

- H&S dashboard

- Human-rights due diligence status

- Community impact

# 12. Governance, Ethics, Risk and Compliance

Provides governance structures, ESG risk management, policies, controls, incidents and board oversight.

| **Req. ID** | **Requirement**              | **Functional Description**                                                               | **Priority** | **Acceptance Criteria**                                |
|-------------|------------------------------|------------------------------------------------------------------------------------------|--------------|--------------------------------------------------------|
| 12-001      | Governance bodies            | Maintain board/committee mandates, membership, independence, skills and meeting records. | Must         | Effective-dated composition supports period reporting. |
| 12-002      | Policy library               | Manage policies, versions, approvals, attestations and review dates.                     | Must         | Only approved version is published.                    |
| 12-003      | Ethics declarations          | Capture conflicts, gifts, anti-bribery and code-of-conduct attestations.                 | Should       | Over-threshold declarations are routed for review.     |
| 12-004      | ESG risk register            | Record impacts, risks and opportunities with inherent/residual ratings.                  | Must         | Risk scoring uses configurable matrices.               |
| 12-005      | Controls                     | Define preventive/detective controls, owners, frequency and evidence.                    | Must         | Control performance influences residual risk.          |
| 12-006      | Compliance obligations       | Maintain regulatory/voluntary obligations by jurisdiction and topic.                     | Should       | Applicability and evidence are assigned.               |
| 12-007      | Incidents and investigations | Record allegations, investigations, outcomes and remediation.                            | Must         | Sensitive cases use restricted case teams.             |
| 12-008      | Audit management             | Plan audits, execute procedures and track findings.                                      | Should       | Findings create corrective actions.                    |
| 12-009      | Business continuity linkage  | Link climate/ESG risks to continuity plans and exercises.                                | Could        | Critical dependencies are visible.                     |
| 12-010      | Board packs                  | Generate board-ready ESG performance, risks, actions and decisions.                      | Must         | Pack is generated from approved snapshot.              |
| 12-011      | Whistleblowing channels      | Integrate or record protected reports and case status.                                   | Should       | Reporter confidentiality is maintained.                |
| 12-012      | Governance disclosures       | Map governance facts to framework datapoints.                                            | Must         | Disclosure lineage traces to governance records.       |

## Outputs and Analytics

- ESG risk heat map

- Policy and attestation status

- Ethics cases

- Control effectiveness

- Board ESG pack

# 13. Materiality and Stakeholder Engagement

Supports impact materiality, financial materiality and double-materiality assessment.

| **Req. ID** | **Requirement**        | **Functional Description**                                                        | **Priority** | **Acceptance Criteria**                                  |
|-------------|------------------------|-----------------------------------------------------------------------------------|--------------|----------------------------------------------------------|
| 13-001      | Stakeholder groups     | Maintain stakeholder categories, interests, influence and engagement methods.     | Must         | Stakeholder population is versioned by assessment.       |
| 13-002      | Topic universe         | Create ESG topic library with sector and framework references.                    | Must         | Topics can be merged without losing history.             |
| 13-003      | Impact assessment      | Score scale, scope, irremediability and likelihood for positive/negative impacts. | Must         | Scoring methodology is configurable and documented.      |
| 13-004      | Financial materiality  | Score magnitude and likelihood of financial effects over time horizons.           | Must         | Risk/opportunity links support financial narrative.      |
| 13-005      | Survey and workshop    | Run questionnaires, interviews and workshops.                                     | Should       | Responses are attributable or anonymous per design.      |
| 13-006      | Evidence and rationale | Attach research, stakeholder input and management rationale.                      | Must         | Materiality conclusion requires evidence.                |
| 13-007      | Thresholds             | Configure thresholds and management override workflow.                            | Must         | Overrides require rationale and approval.                |
| 13-008      | Materiality matrix     | Visualise and compare assessments.                                                | Must         | Matrix drills to scores and evidence.                    |
| 13-009      | Approval               | Route material topics to executive/board approval.                                | Must         | Approved list becomes disclosure applicability baseline. |
| 13-010      | Annual refresh         | Clone and update prior assessment with change analysis.                           | Should       | Change report identifies score and conclusion movement.  |

## Outputs and Analytics

- Double-materiality matrix

- Stakeholder participation

- Material topic change analysis

# 14. Targets, Transition Plans and Initiatives

Connects commitments to baselines, forecasts, initiatives, budgets and realised outcomes.

| **Req. ID** | **Requirement**            | **Functional Description**                                                                        | **Priority** | **Acceptance Criteria**                                               |
|-------------|----------------------------|---------------------------------------------------------------------------------------------------|--------------|-----------------------------------------------------------------------|
| 14-001      | Target definition          | Create absolute/intensity targets with baseline, target year, scope and method.                   | Must         | Target is valid only when baseline and metric are approved.           |
| 14-002      | Interim milestones         | Define annual/quarterly pathways.                                                                 | Must         | Expected vs actual trajectory is calculated.                          |
| 14-003      | Science alignment metadata | Capture validation body, methodology and status.                                                  | Should       | Claims display status and evidence.                                   |
| 14-004      | Initiative portfolio       | Record decarbonisation/social/governance initiatives, owner, cost, schedule and expected benefit. | Must         | Initiatives link to targets and affected metrics.                     |
| 14-005      | Abatement calculation      | Estimate annual and lifetime emissions reduction.                                                 | Must         | Avoided emissions are kept separate from inventory reductions.        |
| 14-006      | Marginal abatement cost    | Calculate cost per tonne and prioritise initiatives.                                              | Should       | Assumptions and discount rate are versioned.                          |
| 14-007      | Scenario forecast          | Forecast business-as-usual and initiative-adjusted performance.                                   | Must         | Scenario inputs and model version are traceable.                      |
| 14-008      | Budget and actuals         | Track capex/opex budget, commitment and actual.                                                   | Should       | Financial integration reconciles initiative cost.                     |
| 14-009      | Benefits realisation       | Validate and approve realised environmental/social benefits.                                      | Must         | Realised benefit cannot exceed supported evidence without exception.  |
| 14-010      | Transition plan narrative  | Generate structured governance, actions, finance and dependency narrative.                        | Should       | Narrative is linked to approved data and reviewed before publication. |
| 14-011      | Dependencies and risks     | Track technology, policy, finance and supplier dependencies.                                      | Must         | Critical dependency generates risk/action.                            |
| 14-012      | Portfolio dashboard        | Rank initiatives by impact, cost, risk and feasibility.                                           | Must         | Filters support entity, target and time horizon.                      |

## Outputs and Analytics

- Target trajectory

- Transition plan dashboard

- Abatement waterfall

- Initiative portfolio and MACC

# 15. Climate and Nature Risk, Scenario Analysis

Assesses physical, transition and nature-related dependencies, impacts, risks and opportunities.

| **Req. ID** | **Requirement**       | **Functional Description**                                      | **Priority** | **Acceptance Criteria**                                 |
|-------------|-----------------------|-----------------------------------------------------------------|--------------|---------------------------------------------------------|
| 15-001      | Hazard library        | Maintain acute/chronic physical hazards and transition drivers. | Must         | Hazard definitions are versioned.                       |
| 15-002      | Asset geolocation     | Store coordinates, address and asset attributes.                | Must         | Sensitive locations follow access policy.               |
| 15-003      | Exposure assessment   | Map assets/suppliers to hazard datasets and time horizons.      | Should       | Dataset source and model are recorded.                  |
| 15-004      | Vulnerability scoring | Assess sensitivity and adaptive capacity.                       | Must         | Scoring method is configurable.                         |
| 15-005      | Financial impact      | Estimate revenue, cost, asset, liability and financing effects. | Should       | Assumptions and uncertainty are captured.               |
| 15-006      | Climate scenarios     | Configure scenarios, pathways, variables and horizons.          | Must         | Scenario version and source are retained.               |
| 15-007      | Scenario runs         | Execute baseline and alternative scenarios.                     | Should       | Run is reproducible and immutable after approval.       |
| 15-008      | Adaptation actions    | Create treatments and resilience investments.                   | Must         | Actions link to risks and assets.                       |
| 15-009      | Nature dependencies   | Record ecosystem services, impacts, dependencies and locations. | Should       | Nature records link to material topics and risks.       |
| 15-010      | Risk integration      | Synchronise material risks with enterprise risk system.         | Must         | Source-of-truth and conflict rules are defined.         |
| 15-011      | Disclosure outputs    | Generate scenario and resilience summaries.                     | Must         | Only approved scenario results can populate disclosure. |

## Outputs and Analytics

- Physical risk map

- Transition risk heat map

- Scenario comparison

- Adaptation plan

# 16. Supplier ESG and Value-Chain Due Diligence

Provides supplier onboarding, assessments, emissions collection, risk screening, audits and remediation.

| **Req. ID** | **Requirement**        | **Functional Description**                                                | **Priority** | **Acceptance Criteria**                                |
|-------------|------------------------|---------------------------------------------------------------------------|--------------|--------------------------------------------------------|
| 16-001      | Supplier master sync   | Synchronise supplier identity, category, spend, geography and status.     | Must         | Duplicate resolution and golden record are controlled. |
| 16-002      | Segmentation           | Segment suppliers by spend, criticality, geography and ESG risk.          | Must         | Segmentation drives assessment frequency.              |
| 16-003      | Questionnaire builder  | Create multilingual questionnaires with branching and scoring.            | Must         | Published version is immutable for active campaign.    |
| 16-004      | Campaigns              | Issue assessments, reminders and escalations.                             | Must         | Response status is tracked by supplier.                |
| 16-005      | Evidence validation    | Require certificates, policies and supporting records.                    | Must         | Evidence expiry and authenticity status are tracked.   |
| 16-006      | Risk screening         | Integrate sanctions, adverse media, country and sector risk providers.    | Should       | Provider response and screening date are retained.     |
| 16-007      | Supplier emissions     | Collect corporate/product emissions, methodology, boundary and assurance. | Must         | Data validity and allocation are controlled.           |
| 16-008      | Corrective actions     | Create supplier improvement plans, due dates and verification.            | Must         | Overdue critical actions affect supplier risk.         |
| 16-009      | Supplier audits        | Schedule, execute and score onsite/remote audits.                         | Should       | Findings and evidence are linked.                      |
| 16-010      | Procurement gate       | Expose ESG risk/status to source-to-contract or purchase approval.        | Should       | Blocking rule is configurable.                         |
| 16-011      | Supplier scorecard     | Calculate ESG score with transparent weights.                             | Must         | Supplier can see permitted score components.           |
| 16-012      | Data consent and terms | Capture supplier consent, terms and privacy notice.                       | Must         | Processing is blocked without required acceptance.     |

## Outputs and Analytics

- Supplier ESG risk heat map

- Campaign completion

- Scope 3 primary data coverage

- Corrective action status

# 17. Carbon Credits, Certificates and Environmental Attributes

Tracks environmental instruments without conflating offsets, avoided emissions and inventory reductions.

| **Req. ID** | **Requirement**       | **Functional Description**                                                    | **Priority** | **Acceptance Criteria**                         |
|-------------|-----------------------|-------------------------------------------------------------------------------|--------------|-------------------------------------------------|
| 17-001      | Instrument registry   | Record credit/certificate type, standard, project, vintage, serial and owner. | Must         | Serial uniqueness is enforced.                  |
| 17-002      | Acquisition           | Record purchase, transfer, price, currency and counterparty.                  | Must         | Inventory quantity reconciles with transaction. |
| 17-003      | Retirement            | Retire instruments against claim, entity, period and target.                  | Must         | Retired serial cannot be reused.                |
| 17-004      | Claim hierarchy       | Configure permitted claims and disclosure wording.                            | Should       | Unsupported claim is blocked or warned.         |
| 17-005      | Quality assessment    | Score additionality, permanence, leakage, verification and co-benefits.       | Should       | Quality rationale is visible.                   |
| 17-006      | REC allocation        | Allocate energy certificates to consumption.                                  | Must         | Allocation cannot exceed eligible consumption.  |
| 17-007      | Accounting separation | Separate gross inventory, net claims, offsets and avoided emissions.          | Must         | Reports clearly distinguish each amount.        |
| 17-008      | Registry integration  | Connect to external registries where API/licence permits.                     | Could        | External status and last sync are shown.        |
| 17-009      | Certificate expiry    | Alert on expiry or invalidation.                                              | Must         | Invalid instrument cannot support claim.        |
| 17-010      | Audit trail           | Maintain chain of custody and documentary proof.                              | Must         | Assurer can trace acquisition to retirement.    |

## Outputs and Analytics

- Environmental attribute inventory

- Retirement and claims report

- Certificate coverage

# 18. Disclosure Management and Framework Packs

Transforms governed datapoints into controlled disclosures, narratives, approvals and publishable outputs.

## Process Flow

18. Select framework and reporting perimeter.

19. Run applicability/materiality assessment.

20. Populate mapped quantitative datapoints.

21. Draft qualitative disclosures.

22. Review legal/finance/ESG consistency.

23. Sign off and publish immutable package.

| **Req. ID** | **Requirement**          | **Functional Description**                                                              | **Priority** | **Acceptance Criteria**                                  |
|-------------|--------------------------|-----------------------------------------------------------------------------------------|--------------|----------------------------------------------------------|
| 18-001      | Framework library        | Maintain framework, standard, disclosure requirement and datapoint metadata.            | Must         | Framework versions coexist by effective period.          |
| 18-002      | Disclosure applicability | Determine mandatory/material/not applicable status with rationale.                      | Must         | Completeness excludes approved non-applicable items.     |
| 18-003      | Cross-framework mapping  | Map common datapoints across multiple frameworks.                                       | Must         | Mapping supports one-to-many relationships.              |
| 18-004      | Disclosure workspace     | Combine metrics, narratives, policies, evidence and review status.                      | Must         | Workspace shows source and latest approved values.       |
| 18-005      | Narrative authoring      | Create rich text with linked datapoints and citations.                                  | Must         | Linked numbers update only through controlled refresh.   |
| 18-006      | AI drafting              | Generate draft narratives from approved data with source references.                    | Should       | AI output is labelled draft and requires human approval. |
| 18-007      | Review and sign-off      | Support legal, finance, sustainability, executive and board stages.                     | Must         | Final publication requires all mandatory sign-offs.      |
| 18-008      | Comparative periods      | Show prior period, restatements and explanations.                                       | Must         | Restated comparative is clearly identified.              |
| 18-009      | Content index            | Generate GRI/other content indices and coverage tables.                                 | Must         | Index links to disclosure sections.                      |
| 18-010      | Digital export           | Export structured data for XBRL/tagging or regulator-specific formats through adapters. | Should       | Export validates against selected taxonomy version.      |
| 18-011      | Publication              | Generate Word, PDF, Excel, web and API outputs.                                         | Must         | Published package binds to immutable snapshot.           |
| 18-012      | Questionnaires           | Manage CDP, ratings and customer ESG requests as reusable response packs.               | Should       | Responses reuse approved datapoints and narratives.      |

## Outputs and Analytics

- Disclosure completeness

- Framework crosswalk

- Narrative review status

- Published disclosure register

# 19. Analytics, Dashboards and Benchmarking

Provides role-based analytics, self-service exploration and governed external benchmarking.

| **Req. ID** | **Requirement**        | **Functional Description**                                                | **Priority** | **Acceptance Criteria**                                 |
|-------------|------------------------|---------------------------------------------------------------------------|--------------|---------------------------------------------------------|
| 19-001      | Executive scorecard    | Display approved KPIs, targets, risks and trends.                         | Must         | User can drill to data lineage.                         |
| 19-002      | Operational dashboards | Provide energy, water, waste, safety, supplier and collection dashboards. | Must         | Dashboard respects scope security.                      |
| 19-003      | Ad hoc analysis        | Allow authorised users to slice governed datasets.                        | Should       | Personal queries cannot bypass row security.            |
| 19-004      | Benchmark datasets     | Load licensed internal/external benchmarks with metadata.                 | Should       | Benchmark source and peer group are displayed.          |
| 19-005      | Alerts                 | Trigger threshold, trend, anomaly and deadline alerts.                    | Must         | Alert shows rule and contributing records.              |
| 19-006      | Forecasting            | Provide transparent statistical/ML forecasts.                             | Should       | Model version, confidence and features are documented.  |
| 19-007      | Natural-language query | Allow governed questions over approved semantic model.                    | Could        | Responses cite source visual/metric and enforce access. |
| 19-008      | Scheduled distribution | Email or publish dashboard snapshots to authorised users.                 | Should       | Distribution follows classification policy.             |
| 19-009      | Board presentation     | Generate editable board charts and commentary.                            | Should       | Output uses approved reporting snapshot.                |
| 19-010      | Data export            | Export filtered data with watermark/classification and audit log.         | Must         | Export reflects user permissions.                       |

# 20. AI Copilots and Intelligent Automation

Defines controlled AI assistance across data quality, document extraction, analysis and narrative generation.

| **Req. ID** | **Requirement**         | **Functional Description**                                                         | **Priority** | **Acceptance Criteria**                                      |
|-------------|-------------------------|------------------------------------------------------------------------------------|--------------|--------------------------------------------------------------|
| 20-001      | Document extraction     | Extract activity data, invoices, utility bills and certificates into review queue. | Should       | No extracted value posts without validation/approval policy. |
| 20-002      | Metric classification   | Suggest metric, source, unit and Scope/category classification.                    | Should       | Suggestion includes confidence and rationale.                |
| 20-003      | Anomaly detection       | Detect unusual values, missing patterns and factor changes.                        | Must         | Alert is explainable and does not alter source data.         |
| 20-004      | Narrative drafting      | Draft disclosure and management commentary from approved data.                     | Should       | Draft cites underlying metrics/evidence.                     |
| 20-005      | Policy assistant        | Draft policy templates from approved organisation context.                         | Could        | Output is not published without policy workflow.             |
| 20-006      | Risk assistant          | Suggest ESG risks and controls from incidents, sector and locations.               | Could        | Risk owner decides acceptance.                               |
| 20-007      | Carbon advisor          | Recommend reduction opportunities based on hotspots and initiative library.        | Should       | Recommendations state assumptions and uncertainty.           |
| 20-008      | Translation             | Translate questionnaires and narratives while preserving source version.           | Should       | Human review is required for published content.              |
| 20-009      | Prompt governance       | Maintain approved system prompts, models, temperature and safety rules.            | Must         | Prompt/model changes are versioned and approved.             |
| 20-010      | Grounding and citations | Ground responses only in authorised tenant and approved reference content.         | Must         | Response cites accessible sources.                           |
| 20-011      | Data privacy            | Exclude restricted personal data and secrets from external model calls.            | Must         | Policy violation blocks request.                             |
| 20-012      | Human approval          | Require accountable approval for material calculations, disclosures and decisions. | Must         | AI cannot final-approve controlled records.                  |
| 20-013      | Evaluation              | Run accuracy, bias, leakage and regression evaluations.                            | Must         | Model release requires passing thresholds.                   |
| 20-014      | AI activity log         | Record user, purpose, model, prompt template, sources and output hash.             | Must         | Audit view omits protected prompt secrets where appropriate. |

## Key Business Rules

- AI features are optional by tenant and data class.

- The platform must support private-model and no-generative-AI deployment modes.

- AI-generated values must never be silently represented as measured facts.

# 21. Workflow, Notifications and Case Management

Provides reusable orchestration for data, disclosures, risks, incidents, audits and supplier processes.

| **Req. ID** | **Requirement**        | **Functional Description**                                            | **Priority** | **Acceptance Criteria**                                        |
|-------------|------------------------|-----------------------------------------------------------------------|--------------|----------------------------------------------------------------|
| 21-001      | Workflow designer      | Configure states, transitions, roles, conditions, SLA and escalation. | Must         | Published workflow version is immutable for running instances. |
| 21-002      | Parallel approval      | Support parallel, sequential and consensus approvals.                 | Must         | Completion rule is explicit.                                   |
| 21-003      | Conditional routing    | Route based on value, severity, entity, framework and risk.           | Must         | Decision path is logged.                                       |
| 21-004      | SLA timers             | Calculate business-time deadlines with calendars.                     | Should       | Pause/resume rules are configurable.                           |
| 21-005      | Notification templates | Create multilingual email, in-app and collaboration messages.         | Must         | Template variables are validated.                              |
| 21-006      | Cases                  | Create cases for exceptions, assurance queries and investigations.    | Must         | Case access can be more restrictive than parent record.        |
| 21-007      | Action management      | Assign corrective/preventive actions with verification.               | Must         | Closure requires evidence and reviewer.                        |
| 21-008      | Escalation             | Escalate overdue or high-risk items to management.                    | Must         | Escalation chain is resolved from hierarchy.                   |
| 21-009      | Webhook events         | Publish state-change events to external systems.                      | Should       | Delivery is retried and observable.                            |
| 21-010      | Workflow analytics     | Measure cycle time, bottlenecks and rework.                           | Should       | Dashboard supports process and entity comparison.              |

# 22. Integration and Data Exchange

Defines inbound/outbound integration capabilities for enterprise and external ESG data.

| **Req. ID** | **Requirement**            | **Functional Description**                                                                           | **Priority** | **Acceptance Criteria**                                                       |
|-------------|----------------------------|------------------------------------------------------------------------------------------------------|--------------|-------------------------------------------------------------------------------|
| 22-001      | REST API                   | Provide versioned APIs for master, metrics, submissions, factors, disclosures and evidence metadata. | Must         | API follows authentication, validation, idempotency and pagination standards. |
| 22-002      | Event API                  | Publish domain events through webhook or message broker.                                             | Should       | Subscribers can replay from retained offset where supported.                  |
| 22-003      | File ingestion             | Support CSV/XLSX/SFTP/object-storage ingestion.                                                      | Must         | Files are virus scanned, schema validated and quarantined on failure.         |
| 22-004      | ERP connectors             | Provide adapters for D365, SAP, Oracle and NetSuite patterns.                                        | Should       | Connector exposes mapping, schedule and reconciliation status.                |
| 22-005      | HR connectors              | Support HR/workforce systems through API/file.                                                       | Should       | Personal data minimisation rules apply.                                       |
| 22-006      | Utility and meter data     | Ingest utility bills, meters, BMS, SCADA and IoT streams.                                            | Should       | Time-series deduplication and quality flags are supported.                    |
| 22-007      | Travel and expense         | Ingest travel, hotel, car hire and expenses.                                                         | Should       | Source categories map to Scope 3 methods.                                     |
| 22-008      | External datasets          | Integrate factor, climate hazard, water stress, biodiversity and screening providers.                | Should       | Licence, version and usage rights are tracked.                                |
| 22-009      | Master-data reconciliation | Reconcile source-system masters with ESG hierarchy.                                                  | Must         | Unmapped records enter stewardship queue.                                     |
| 22-010      | Outbound journal/API       | Return approved ESG status or allocations to enterprise systems.                                     | Could        | Outbound posting is controlled and reconcilable.                              |
| 22-011      | Developer portal           | Publish OpenAPI specs, examples, credentials and sandbox.                                            | Should       | Tenant admins control application registrations.                              |
| 22-012      | Integration monitoring     | Show jobs, records, errors, retries and latency.                                                     | Must         | Operators can reprocess without duplication.                                  |

## Outputs and Analytics

- Integration health

- Data reconciliation

- Unmapped records

- API usage and errors

# 23. Subscription, Configuration and Administration

Supports commercial packaging, tenant settings, feature flags and operational administration.

| **Req. ID** | **Requirement**         | **Functional Description**                                        | **Priority** | **Acceptance Criteria**                              |
|-------------|-------------------------|-------------------------------------------------------------------|--------------|------------------------------------------------------|
| 23-001      | Product editions        | Configure edition, modules, user/data limits and entitlements.    | Must         | Unlicensed feature is inaccessible.                  |
| 23-002      | Usage metering          | Measure users, storage, API, AI and data volumes.                 | Should       | Meter records are immutable and tenant-visible.      |
| 23-003      | Feature flags           | Enable staged rollout by tenant and environment.                  | Must         | Flag changes are audited and reversible.             |
| 23-004      | Branding                | Configure logo, colours, domains and document templates.          | Should       | Branding remains accessible and responsive.          |
| 23-005      | Localisation            | Configure languages, units, date/number formats and translations. | Must         | Fallback language is deterministic.                  |
| 23-006      | Retention               | Set record/file retention by type and jurisdiction.               | Must         | Purge honours legal holds and immutable snapshots.   |
| 23-007      | Data export and exit    | Provide full tenant export and deletion workflow.                 | Must         | Exit package and deletion certificate are generated. |
| 23-008      | Sandbox cloning         | Create masked test/sandbox copy where policy allows.              | Should       | Personal/restricted fields are masked.               |
| 23-009      | Configuration promotion | Promote metadata from dev/test to production with comparison.     | Must         | Promotion is approved and versioned.                 |
| 23-010      | Admin audit             | Record all configuration and security changes.                    | Must         | Admin cannot delete audit history.                   |

# 24. Cross-Functional Business Rules

| **Rule ID** | **Rule**                                                                                                                                        |
|-------------|-------------------------------------------------------------------------------------------------------------------------------------------------|
| BR-001      | Every quantitative value shall store original value, original unit, normalised value, normalised unit, period, organisation, source and status. |
| BR-002      | All calculations shall preserve unrounded precision; rounding occurs only in configured presentation layers.                                    |
| BR-003      | Published disclosures shall use an immutable reporting snapshot.                                                                                |
| BR-004      | A metric definition, formula, factor or framework mapping change shall not retroactively modify closed-period results.                          |
| BR-005      | Restatement shall preserve originally published values and show adjustment reason and approval.                                                 |
| BR-006      | AI-generated content shall be labelled and shall not bypass human-controlled workflows.                                                         |
| BR-007      | Personally identifiable workforce data shall not be exposed in aggregated reports below the configured privacy threshold.                       |
| BR-008      | Supplier users shall never access another supplier’s records or the buyer’s internal notes.                                                     |
| BR-009      | Offsets, certificates, avoided emissions and inventory reductions shall be reported separately.                                                 |
| BR-010      | Every high-risk exception shall have an owner, due date and escalation path.                                                                    |
| BR-011      | API and file ingestion shall be idempotent when a source-system key is supplied.                                                                |
| BR-012      | Deleting a master record used in historical reporting is prohibited; it may only be inactivated.                                                |
| BR-013      | Closed reporting periods may be reopened only by authorised roles with documented reason.                                                       |
| BR-014      | Framework content and taxonomies shall be versioned and effective-dated.                                                                        |
| BR-015      | No disclosure shall be marked complete while mandatory material datapoints remain unapproved.                                                   |
| BR-016      | Data exports shall be logged with user, purpose, filters and classification.                                                                    |
| BR-017      | All timestamps shall be stored in UTC and rendered in user/tenant time zone.                                                                    |
| BR-018      | Numeric unit conversion shall use approved conversion factors and deterministic precision.                                                      |
| BR-019      | Calculation jobs shall be repeatable and generate the same output for the same input/version set.                                               |
| BR-020      | Legal hold shall override normal retention and purge policies.                                                                                  |

# 25. Functional Reports and Document Outputs

| **Output**                    | **Purpose**                                                                   |
|-------------------------------|-------------------------------------------------------------------------------|
| Executive ESG scorecard       | Board and executive performance across material topics, targets and risk.     |
| GHG inventory statement       | Scope 1, Scope 2 location/market and Scope 3 by category.                     |
| Inventory reconciliation      | Opening, additions, adjustments, restatements and closing values.             |
| Data-quality scorecard        | Completeness, primary-data coverage, factor specificity and assurance status. |
| Materiality report            | Stakeholder process, scores, rationale and approved material topics.          |
| Disclosure completeness       | Requirement/datapoint completion, review and sign-off status.                 |
| GRI content index             | Disclosure reference and omission reason.                                     |
| ISSB climate pack             | Governance, strategy, risk management, metrics and targets.                   |
| ESRS datapoint pack           | Applicable disclosure requirements and quantitative/qualitative datapoints.   |
| Supplier ESG scorecard        | Assessment, risk, emissions and corrective actions.                           |
| Target trajectory             | Baseline, pathway, actual, forecast and gap.                                  |
| Transition plan               | Initiatives, financing, dependencies, risks and expected abatement.           |
| Assurance evidence index      | Evidence, source, owner, status, sample and findings.                         |
| Audit trail export            | Record and configuration events with timestamps and actors.                   |
| Environmental operations pack | Energy, water, waste, permits and incidents.                                  |
| Social performance pack       | Workforce, H&S, diversity, training and grievances.                           |
| Governance pack               | Board, policy, ethics, risks, controls and cases.                             |

PART II - SOLUTION DESIGN DOCUMENT (SDD)

# 26. Solution Context

ESG360 is designed as a cloud-native, multi-tenant product with optional dedicated-tenant and private deployment profiles. The logical architecture separates transactional ESG records, time-series activity data, documents/evidence, analytical models, search/vector indexes and immutable audit events.

| **Actor / System**       | **Interaction**                                                                   |
|--------------------------|-----------------------------------------------------------------------------------|
| Enterprise users         | Browser-based application and optional mobile-responsive capture.                 |
| Suppliers and assurers   | External portals with isolated identity and scoped permissions.                   |
| ERP / HR / Procurement   | Master data, financial activity, workforce, supplier and initiative actuals.      |
| IoT / Utility / BMS      | Meter and operational activity data.                                              |
| External data providers  | Emission factors, climate hazards, water stress, biodiversity and risk screening. |
| BI and regulator systems | Governed semantic datasets, structured disclosures and exports.                   |
| Identity provider        | Federated authentication, MFA and lifecycle provisioning.                         |
| Notification providers   | Email, Teams/collaboration and optional SMS.                                      |

# 27. Logical Architecture

| **Domain Service**      | **Responsibilities**                                                       |
|-------------------------|----------------------------------------------------------------------------|
| Identity & Tenant       | Tenant context, federation, users, roles, scope and policy decisions.      |
| Organisation            | Hierarchy, boundaries, calendars, dimensions and reporting perimeter.      |
| Metric Catalogue        | Definitions, formulas, validations, mappings and ownership.                |
| Collection              | Cycles, tasks, submissions, review, approvals and close.                   |
| Evidence & Lineage      | Files, metadata, checksums, links, transformations and assurance rooms.    |
| Carbon                  | Sources, factors, calculations, inventories, restatements and instruments. |
| Environmental           | Energy, water, waste, permits, pollutants and incidents.                   |
| Social                  | Workforce aggregates, safety, diversity, human rights and community.       |
| Governance & Risk       | Bodies, policies, risks, controls, cases and audits.                       |
| Supplier ESG            | Questionnaires, scoring, supplier data, screening and actions.             |
| Targets & Scenarios     | Targets, forecasts, initiatives, abatement and scenario runs.              |
| Disclosure              | Frameworks, datapoints, narratives, review, snapshots and exports.         |
| Integration             | Connectors, mapping, ingestion, reconciliation and events.                 |
| AI Orchestration        | Model gateway, grounding, prompt policies, evaluation and activity log.    |
| Notification            | Templates, preferences, delivery and retry.                                |
| Subscription & Metering | Entitlements, usage and commercial limits.                                 |
| Audit                   | Append-only domain and security activity records.                          |

# 28. Deployment Profiles

| **Profile**                           | **Use Case**                                      | **Isolation**                                                          |
|---------------------------------------|---------------------------------------------------|------------------------------------------------------------------------|
| Shared multi-tenant SaaS              | Standard commercial cloud service                 | Logical tenant isolation, shared services and tenant-partitioned data. |
| Dedicated tenant SaaS                 | Large or regulated enterprise                     | Dedicated databases/storage and optional dedicated compute.            |
| Sovereign cloud                       | Jurisdictional residency and operator constraints | Approved in-country region and restricted operational access.          |
| Private cloud / customer subscription | Highly regulated or disconnected requirements     | Customer-controlled network, keys, identity and model endpoints.       |
| Development / test                    | Engineering and customer validation               | Synthetic or masked data; production credentials prohibited.           |

# 29. Data Architecture

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

# 30. Integration Architecture

Integration uses four patterns: synchronous REST APIs for transactional operations, asynchronous messaging for domain events, scheduled/bulk pipelines for enterprise datasets and streaming ingestion for meters/IoT. A canonical ESG data contract decouples source-specific fields from product domains.

| **Pattern**      | **Controls**                                                                                                         |
|------------------|----------------------------------------------------------------------------------------------------------------------|
| REST             | OAuth2 client credentials, scopes, rate limits, idempotency keys, schema validation, pagination and correlation IDs. |
| Events           | At-least-once delivery, unique event IDs, retry/dead-letter, ordering key where required and consumer replay policy. |
| Files            | Signed/encrypted transfer, template version, malware scan, quarantine, row validation and reconciliation.            |
| Streaming        | Device/source identity, timestamp tolerance, deduplication, unit validation, late-arrival handling and backpressure. |
| Outbound exports | Approval snapshot, classification, checksum manifest, expiry and download audit.                                     |

# 31. Security Architecture

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

# 32. Non-Functional Requirements

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

# 33. High-Level Data Model

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

# 34. Availability, Backup and Disaster Recovery

- Multi-zone deployment for production services where the selected region supports it.

- Database point-in-time recovery, daily full backup and periodic immutable backup according to tier.

- Object storage versioning and soft delete; legal-hold objects cannot be purged.

- Cross-region replication only when tenant residency policy permits.

- Quarterly restore tests and at least annual disaster-recovery exercise.

- Degraded-mode operation for non-critical AI, analytics or external-data services.

- Recovery priority: identity/tenant, transactional ESG records, evidence, workflows, integrations, analytics, AI.

PART III - TECHNICAL DESIGN DOCUMENT (TDD)

# 35. Recommended Technology Baseline

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

# 36. Service and Module Boundaries

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

# 37. API Design Standards

- Base path /api/v1 with semantic versioning and published deprecation policy.

- JSON UTF-8; ISO 8601 timestamps; decimal values represented without binary floating-point loss.

- OAuth2 scopes plus tenant and organisation claims; server resolves tenant from trusted token, never from untrusted request alone.

- Idempotency-Key required for create/import endpoints where duplicate effects are material.

- Problem Details error structure with code, message, field errors, correlation ID and retryability.

- Cursor pagination for high-volume endpoints; ETag/If-Match for optimistic concurrency.

- Rate limits by tenant, application and endpoint class.

- OpenAPI published with examples and generated SDK option.

- Every write emits audit event and relevant domain event after transaction commit.

## Representative Endpoints

| **Method** | **Endpoint**                          | **Purpose**                           |
|------------|---------------------------------------|---------------------------------------|
| POST       | /tenants                              | Provision tenant - operator only.     |
| GET        | /organisations                        | Query authorised hierarchy.           |
| POST       | /metrics                              | Create draft metric definition.       |
| POST       | /reporting-cycles/{id}/generate-tasks | Generate collection tasks.            |
| POST       | /submissions                          | Create idempotent data submission.    |
| POST       | /evidence                             | Create upload session and metadata.   |
| POST       | /emission-factors/import              | Import governed factor dataset.       |
| POST       | /carbon/calculation-runs              | Execute calculation for scope/period. |
| GET        | /inventories/{id}/lineage             | Retrieve calculation lineage.         |
| POST       | /supplier-campaigns                   | Issue supplier assessment campaign.   |
| POST       | /disclosures/{id}/snapshot            | Create immutable approved snapshot.   |
| POST       | /ai/narrative-drafts                  | Generate grounded draft under policy. |
| GET        | /audit-events                         | Query authorised audit events.        |

# 38. Calculation Engine Design

The calculation engine shall execute metadata-defined formulas against versioned inputs and reference data. It must be deterministic, explainable and suitable for replay during assurance or restatement.

24. Resolve tenant, boundary, period, metric/factor/formula versions and applicable hierarchy.

25. Load approved activity records and dimensions at unrounded precision.

26. Validate unit compatibility and convert to canonical unit.

27. Resolve factor according to configured hierarchy and validity.

28. Calculate gas-level emissions or metric result using decimal arithmetic.

29. Apply GWP set where CO2e is required.

30. Aggregate according to metric rules and consolidation boundary.

31. Create result records and lineage edges to every input, factor and formula version.

32. Run cross-checks, anomaly rules and reconciliation.

33. Publish CalculationCompleted event and retain execution manifest/hash.

## Calculation Pseudocode

result = SUM(activity.normalised_quantity × factor.value × allocation_ratio × conversion_multiplier)  
co2e = SUM(gas_mass × GWP\[gas, version\])

# 39. Database Design Rules

- All business tables include tenant_id, created_at, created_by, updated_at, updated_by and row_version.

- Controlled definitions use immutable versions plus a stable logical identifier.

- Submissions and calculations are append-only revisions; corrections supersede rather than overwrite.

- Monetary and ESG quantities use decimal/numeric types with domain-appropriate precision.

- UTC timestamps are mandatory; date-only fields remain date types.

- Foreign keys and unique constraints enforce tenant consistency.

- Partition high-volume activity, audit and telemetry tables by tenant/time.

- Soft delete is limited to non-controlled drafts; referenced records are inactivated.

- PII fields are separated or encrypted where required and excluded from broad analytics models.

# 40. Workflow Technical Design

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

# 41. Document and Evidence Technical Design

- Client requests upload session; service creates tenant-scoped object key and short-lived signed URL.

- Object is quarantined until malware scan, file-type validation and checksum complete.

- Evidence metadata stores SHA-256 checksum, size, MIME type, uploader, classification, period and retention rule.

- Preview/text extraction occurs in isolated processing environment; original file remains unchanged.

- Download requires authorisation and is logged; confidential downloads may be watermarked.

- Published snapshot stores evidence manifest and hashes, not uncontrolled mutable links.

- Legal hold blocks lifecycle deletion and records hold authority/reason.

# 42. AI Technical Design

| **Component**      | **Design**                                                                                           |
|--------------------|------------------------------------------------------------------------------------------------------|
| Model gateway      | Routes requests to approved model/deployment by tenant, use case and data class.                     |
| Prompt registry    | Versioned system/user templates, output schema, allowed tools and approval owner.                    |
| Grounding service  | Retrieves only tenant-authorised and classification-permitted content.                               |
| Safety filters     | Prompt-injection detection, content filtering, secret/PII redaction and output policy checks.        |
| Structured output  | JSON schema validation for extracted values, classifications and recommendations.                    |
| Evaluation harness | Golden datasets, factuality, citation, leakage, bias and regression thresholds.                      |
| Human review       | AI result enters draft/review queue; no material auto-publication.                                   |
| Audit              | Request purpose, model/version, prompt-template ID, sources, output hash, latency and user feedback. |
| Fallback           | Deterministic non-AI workflow remains available when AI is disabled or unavailable.                  |

# 43. DevSecOps and Release Management

34. Source control with protected branches, signed commits/tags and peer review.

35. CI runs formatting, unit tests, SAST, dependency and licence checks.

36. Build produces signed SBOM and immutable container/package artifacts.

37. Automated integration, API contract, migration and security tests run in ephemeral environment.

38. Infrastructure-as-code changes use policy-as-code and approval.

39. Release deploys progressively using feature flags/canary or blue-green pattern.

40. Database migrations are backward compatible and rehearsed against representative volumes.

41. Post-deploy smoke tests and observability gates determine promotion or rollback.

42. Customer-impacting changes are documented with release notes and deprecation notices.

# 44. Logging, Monitoring and Operational Support

| **Telemetry**       | **Minimum Fields / Controls**                                                                               |
|---------------------|-------------------------------------------------------------------------------------------------------------|
| Application logs    | Timestamp, severity, service, environment, tenant pseudonym, correlation ID, event code; no secrets.        |
| Audit events        | Actor, action, object, result, source IP/device context, before/after hash and reason.                      |
| Metrics             | Request rate/latency/error, queue depth, job duration, DB health, storage and tenant usage.                 |
| Traces              | Distributed trace across gateway, services, queues and external calls.                                      |
| Business monitoring | Collection progress, calculation failures, stale integrations, disclosure deadlines and assurance requests. |
| Alerts              | Severity, threshold, runbook, owner, suppression and escalation.                                            |
| Support access      | Ticket-linked, time-bound, approved and fully audited tenant support session.                               |

# 45. Data Migration and Tenant Onboarding

43. Discover source systems, historical periods, evidence, dimensions and data quality.

44. Configure tenant hierarchy, units, calendars, metric catalogue and frameworks.

45. Load master data and validate referential integrity.

46. Load historical activities/submissions with source IDs and evidence links.

47. Recalculate selected periods and reconcile against approved legacy reports.

48. Obtain data-owner and finance/ESG sign-off.

49. Freeze migration package and retain reconciliation evidence.

50. Activate production integrations and reporting cycle.

PART IV - MASTER TEST SPECIFICATION

# 46. Test Strategy

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

# 47. Entry and Exit Criteria

| **Stage**   | **Entry**                                                                             | **Exit**                                                                                 |
|-------------|---------------------------------------------------------------------------------------|------------------------------------------------------------------------------------------|
| System test | Approved requirements; deployed test build; test data; critical interfaces available. | All planned tests executed; no open critical/high defects; acceptance coverage achieved. |
| Performance | Stable functional build; production-like topology and dataset.                        | Targets met or approved capacity plan and remediation.                                   |
| Security    | Feature complete; threat model and architecture available.                            | No critical/high exploitable findings; medium findings have approved plan.               |
| UAT         | System test passed; user guides; business scenarios and roles configured.             | Product owner and ESG lead sign-off; known limitations documented.                       |
| Release     | All gates passed; rollback and support readiness confirmed.                           | Production smoke passed; monitoring healthy; release approved.                           |

# 48. Master Test Case Catalogue

| **Test ID** | **Title**                                            | **Module**     | **Preconditions**                                  | **Test Steps**                                                                                            | **Expected Result**                                                                                       | **Type**      | **Priority** |
|-------------|------------------------------------------------------|----------------|----------------------------------------------------|-----------------------------------------------------------------------------------------------------------|-----------------------------------------------------------------------------------------------------------|---------------|--------------|
| TEN-001     | Provision isolated tenant                            | Tenant         | Operator authorised                                | Create two tenants; add same user email separately; create records in each.                               | Each tenant has independent configuration and records; no cross-tenant visibility.                        | Functional    | High         |
| ORG-001     | Effective-dated hierarchy change                     | Organisation   | Entity and site exist                              | Move site to new business unit effective next quarter; open prior and future reports.                     | Prior period retains old hierarchy; future period uses new hierarchy.                                     | Functional    | High         |
| SEC-001     | Prevent cross-tenant API access                      | Security       | Tokens for tenant A and record in tenant B         | Call tenant B record endpoint with tenant A token and guessed ID.                                         | Request is denied and security event is logged.                                                           | Functional    | High         |
| SEC-002     | Segregation of duties conflict                       | Security       | User is preparer                                   | Assign same user as final approver for same scope.                                                        | Assignment is blocked or exception workflow required.                                                     | Functional    | High         |
| CAT-001     | Metric formula circular reference                    | Catalogue      | Metrics A and B exist                              | Define A=B+1 and B=A+1; publish.                                                                          | Publication fails with circular-reference error.                                                          | Functional    | High         |
| COL-001     | Generate reporting tasks                             | Collection     | Cycle, scope and ownership configured              | Generate tasks.                                                                                           | One task is created per applicable metric/entity/period with correct owner.                               | Functional    | High         |
| COL-002     | Idempotent API submission                            | Collection     | Task exists                                        | POST same payload twice with same idempotency key.                                                        | One submission revision is created; second returns original outcome.                                      | Functional    | High         |
| COL-003     | Period close lock                                    | Collection     | All tasks approved                                 | Close period; attempt edit and normal API update.                                                         | Period closes and edits are blocked.                                                                      | Functional    | High         |
| EVD-001     | Evidence checksum and reuse                          | Evidence       | User can upload                                    | Upload file and link to two submissions.                                                                  | One stored object with checksum; two links; downloads audited.                                            | Functional    | High         |
| EVD-002     | Legal hold blocks deletion                           | Evidence       | Evidence on legal hold                             | Run retention purge.                                                                                      | Held evidence remains and purge report records exclusion.                                                 | Functional    | High         |
| FAC-001     | Factor unit validation                               | Factor         | Factor kgCO2e/litre exists                         | Calculate activity in kWh using factor.                                                                   | Calculation fails or requires approved conversion; no silent mismatch.                                    | Functional    | High         |
| FAC-002     | Factor effective date selection                      | Factor         | Two yearly factors exist                           | Calculate records across both years.                                                                      | Correct factor version is selected by date and shown in lineage.                                          | Functional    | High         |
| CAR-001     | Scope 1 stationary combustion                        | Carbon         | Fuel activity and factor exist                     | Calculate 1,000 litres for configured fuel.                                                               | Expected gas-level and CO2e result matches independent calculation.                                       | Functional    | High         |
| CAR-002     | Scope 2 dual reporting                               | Carbon         | Electricity and instruments configured             | Calculate location and market methods.                                                                    | Both results are reported separately with correct factor hierarchy.                                       | Functional    | High         |
| CAR-003     | Refrigerant mass balance                             | Carbon         | Refrigerant equipment exists                       | Enter opening, additions, recovery and closing stock.                                                     | Leakage and CO2e equal approved mass-balance formula.                                                     | Functional    | High         |
| CAR-004     | Base-year restatement                                | Carbon         | Approved base year exists                          | Change boundary above threshold and approve restatement.                                                  | Original baseline retained; restated baseline and rationale published.                                    | Functional    | High         |
| S3-001      | Purchased goods spend method                         | Scope 3        | Spend and factor mapping exist                     | Import supplier spend and calculate category 1.                                                           | Emissions aggregate by supplier/category with spend method label.                                         | Functional    | High         |
| S3-002      | Supplier-specific data priority                      | Scope 3        | Spend estimate and approved supplier data exist    | Recalculate same supplier/period.                                                                         | Configured hierarchy uses supplier-specific data and shows replacement impact.                            | Functional    | High         |
| ENV-001     | Water balance                                        | Environment    | Withdrawal and discharge values exist              | Run water consumption calculation.                                                                        | Consumption equals configured balance and lineage shows inputs.                                           | Functional    | High         |
| ENV-002     | Waste diversion                                      | Environment    | Waste treatment records exist                      | Calculate diversion rate.                                                                                 | Only configured reuse/recycling/recovery treatments count as diverted.                                    | Functional    | High         |
| SOC-001     | Safety rate denominator                              | Social         | Hours and incidents exist                          | Calculate TRIR/LTIFR under configured formula.                                                            | Rate matches expected denominator and rounding.                                                           | Functional    | High         |
| SOC-002     | Privacy suppression                                  | Social         | Small demographic group exists                     | Open diversity dashboard as standard manager.                                                             | Small group values are suppressed per threshold.                                                          | Functional    | High         |
| GOV-001     | Policy version publication                           | Governance     | Draft policy and approvals configured              | Approve and publish new version.                                                                          | New version becomes current; prior version remains historically available.                                | Functional    | High         |
| MAT-001     | Double materiality threshold                         | Materiality    | Assessment scores complete                         | Apply thresholds and management override.                                                                 | Material result follows threshold; override requires rationale and approval.                              | Functional    | High         |
| TGT-001     | Target trajectory                                    | Targets        | Approved baseline and milestones exist             | Post actuals and run forecast.                                                                            | Dashboard shows actual, pathway, forecast and variance.                                                   | Functional    | High         |
| CLI-001     | Physical risk map access                             | Climate Risk   | Assets and hazard dataset exist                    | Run exposure analysis and view map.                                                                       | Only authorised assets appear; dataset/model version is shown.                                            | Functional    | High         |
| SUP-001     | Supplier isolation                                   | Supplier       | Two supplier accounts exist                        | Supplier A attempts URL/API access to Supplier B response.                                                | Access denied and event logged.                                                                           | Functional    | High         |
| SUP-002     | Corrective action effect                             | Supplier       | Critical action overdue                            | Recalculate supplier score.                                                                               | Risk/score changes according to transparent configured rule.                                              | Functional    | High         |
| INS-001     | Retire carbon credit                                 | Instruments    | Owned valid credits exist                          | Retire serials then attempt reuse.                                                                        | Retirement succeeds once; reuse is blocked.                                                               | Functional    | High         |
| DIS-001     | Disclosure snapshot immutability                     | Disclosure     | Disclosure approved                                | Create snapshot; modify source value later.                                                               | Snapshot retains original approved values and hash.                                                       | Functional    | High         |
| DIS-002     | Framework crosswalk                                  | Disclosure     | Metric mapped to two frameworks                    | Approve metric and inspect both packs.                                                                    | Both packs reuse the same approved datapoint with mapping lineage.                                        | Functional    | High         |
| AI-001      | Grounded narrative citations                         | AI             | Approved data and evidence exist                   | Request narrative draft.                                                                                  | Draft cites only authorised sources and is marked AI-generated.                                           | Functional    | High         |
| AI-002      | Prompt injection in evidence                         | AI             | Malicious text embedded in uploaded evidence       | Ask assistant to summarise disclosure.                                                                    | System ignores embedded instruction, follows policy and flags risk where applicable.                      | Functional    | High         |
| AI-003      | Restricted data egress                               | AI             | External model selected and restricted data exists | Attempt prompt using restricted data.                                                                     | Request is blocked or routed to approved private model according to policy.                               | Functional    | High         |
| INT-001     | File import quarantine                               | Integration    | Import template available                          | Upload file containing malware test signature.                                                            | File is quarantined; no rows are processed; alert generated.                                              | Functional    | High         |
| INT-002     | Integration retry idempotency                        | Integration    | External API temporarily fails                     | Retry same batch after recovery.                                                                          | No duplicate records; job reconciles source count and target count.                                       | Functional    | High         |
| NFR-001     | Interactive performance                              | Performance    | Reference load active                              | Execute standard dashboard and save operations.                                                           | 95th percentile meets defined targets.                                                                    | Functional    | High         |
| NFR-002     | Million-record calculation                           | Performance    | 1M approved activity records                       | Run monthly inventory calculation.                                                                        | Completes within target and result is correct.                                                            | Functional    | High         |
| DR-001      | Database point-in-time restore                       | DR             | Backup and recovery environment available          | Restore to selected point; run integrity checks.                                                          | RPO achieved; records/evidence references are consistent.                                                 | Functional    | High         |
| ACC-001     | Keyboard-only task completion                        | Accessibility  | User has collection task                           | Complete data entry, evidence and submit using keyboard only.                                             | All controls are reachable, visible focus exists and task completes.                                      | Functional    | High         |
| ORG-100     | Organisation: create valid record                    | Organisation   | Configured tenant, roles and representative data   | Execute create valid record; inspect UI/API response, audit event and downstream status.                  | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| ORG-101     | Organisation: reject missing mandatory field         | Organisation   | Configured tenant, roles and representative data   | Execute reject missing mandatory field; inspect UI/API response, audit event and downstream status.       | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| ORG-102     | Organisation: validate effective date overlap        | Organisation   | Configured tenant, roles and representative data   | Execute validate effective date overlap; inspect UI/API response, audit event and downstream status.      | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| ORG-103     | Organisation: enforce scoped access                  | Organisation   | Configured tenant, roles and representative data   | Execute enforce scoped access; inspect UI/API response, audit event and downstream status.                | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| ORG-104     | Organisation: bulk import mixed valid/invalid rows   | Organisation   | Configured tenant, roles and representative data   | Execute bulk import mixed valid/invalid rows; inspect UI/API response, audit event and downstream status. | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| ORG-105     | Organisation: export authorised data                 | Organisation   | Configured tenant, roles and representative data   | Execute export authorised data; inspect UI/API response, audit event and downstream status.               | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
| ORG-106     | Organisation: retain revision history                | Organisation   | Configured tenant, roles and representative data   | Execute retain revision history; inspect UI/API response, audit event and downstream status.              | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
| ORG-107     | Organisation: trigger approval workflow              | Organisation   | Configured tenant, roles and representative data   | Execute trigger approval workflow; inspect UI/API response, audit event and downstream status.            | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
| ORG-108     | Organisation: send overdue escalation                | Organisation   | Configured tenant, roles and representative data   | Execute send overdue escalation; inspect UI/API response, audit event and downstream status.              | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
| ORG-109     | Organisation: filter dashboard by entity             | Organisation   | Configured tenant, roles and representative data   | Execute filter dashboard by entity; inspect UI/API response, audit event and downstream status.           | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
| SEC-100     | Security: create valid record                        | Security       | Configured tenant, roles and representative data   | Execute create valid record; inspect UI/API response, audit event and downstream status.                  | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| SEC-101     | Security: reject missing mandatory field             | Security       | Configured tenant, roles and representative data   | Execute reject missing mandatory field; inspect UI/API response, audit event and downstream status.       | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| SEC-102     | Security: validate effective date overlap            | Security       | Configured tenant, roles and representative data   | Execute validate effective date overlap; inspect UI/API response, audit event and downstream status.      | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| SEC-103     | Security: enforce scoped access                      | Security       | Configured tenant, roles and representative data   | Execute enforce scoped access; inspect UI/API response, audit event and downstream status.                | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| SEC-104     | Security: bulk import mixed valid/invalid rows       | Security       | Configured tenant, roles and representative data   | Execute bulk import mixed valid/invalid rows; inspect UI/API response, audit event and downstream status. | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| SEC-105     | Security: export authorised data                     | Security       | Configured tenant, roles and representative data   | Execute export authorised data; inspect UI/API response, audit event and downstream status.               | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
| SEC-106     | Security: retain revision history                    | Security       | Configured tenant, roles and representative data   | Execute retain revision history; inspect UI/API response, audit event and downstream status.              | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
| SEC-107     | Security: trigger approval workflow                  | Security       | Configured tenant, roles and representative data   | Execute trigger approval workflow; inspect UI/API response, audit event and downstream status.            | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
| SEC-108     | Security: send overdue escalation                    | Security       | Configured tenant, roles and representative data   | Execute send overdue escalation; inspect UI/API response, audit event and downstream status.              | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
| SEC-109     | Security: filter dashboard by entity                 | Security       | Configured tenant, roles and representative data   | Execute filter dashboard by entity; inspect UI/API response, audit event and downstream status.           | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
| CAT-100     | Catalogue: create valid record                       | Catalogue      | Configured tenant, roles and representative data   | Execute create valid record; inspect UI/API response, audit event and downstream status.                  | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| CAT-101     | Catalogue: reject missing mandatory field            | Catalogue      | Configured tenant, roles and representative data   | Execute reject missing mandatory field; inspect UI/API response, audit event and downstream status.       | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| CAT-102     | Catalogue: validate effective date overlap           | Catalogue      | Configured tenant, roles and representative data   | Execute validate effective date overlap; inspect UI/API response, audit event and downstream status.      | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| CAT-103     | Catalogue: enforce scoped access                     | Catalogue      | Configured tenant, roles and representative data   | Execute enforce scoped access; inspect UI/API response, audit event and downstream status.                | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| CAT-104     | Catalogue: bulk import mixed valid/invalid rows      | Catalogue      | Configured tenant, roles and representative data   | Execute bulk import mixed valid/invalid rows; inspect UI/API response, audit event and downstream status. | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| CAT-105     | Catalogue: export authorised data                    | Catalogue      | Configured tenant, roles and representative data   | Execute export authorised data; inspect UI/API response, audit event and downstream status.               | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
| CAT-106     | Catalogue: retain revision history                   | Catalogue      | Configured tenant, roles and representative data   | Execute retain revision history; inspect UI/API response, audit event and downstream status.              | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
| CAT-107     | Catalogue: trigger approval workflow                 | Catalogue      | Configured tenant, roles and representative data   | Execute trigger approval workflow; inspect UI/API response, audit event and downstream status.            | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
| CAT-108     | Catalogue: send overdue escalation                   | Catalogue      | Configured tenant, roles and representative data   | Execute send overdue escalation; inspect UI/API response, audit event and downstream status.              | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
| CAT-109     | Catalogue: filter dashboard by entity                | Catalogue      | Configured tenant, roles and representative data   | Execute filter dashboard by entity; inspect UI/API response, audit event and downstream status.           | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
| COL-100     | Collection: create valid record                      | Collection     | Configured tenant, roles and representative data   | Execute create valid record; inspect UI/API response, audit event and downstream status.                  | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| COL-101     | Collection: reject missing mandatory field           | Collection     | Configured tenant, roles and representative data   | Execute reject missing mandatory field; inspect UI/API response, audit event and downstream status.       | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| COL-102     | Collection: validate effective date overlap          | Collection     | Configured tenant, roles and representative data   | Execute validate effective date overlap; inspect UI/API response, audit event and downstream status.      | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| COL-103     | Collection: enforce scoped access                    | Collection     | Configured tenant, roles and representative data   | Execute enforce scoped access; inspect UI/API response, audit event and downstream status.                | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| COL-104     | Collection: bulk import mixed valid/invalid rows     | Collection     | Configured tenant, roles and representative data   | Execute bulk import mixed valid/invalid rows; inspect UI/API response, audit event and downstream status. | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| COL-105     | Collection: export authorised data                   | Collection     | Configured tenant, roles and representative data   | Execute export authorised data; inspect UI/API response, audit event and downstream status.               | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
| COL-106     | Collection: retain revision history                  | Collection     | Configured tenant, roles and representative data   | Execute retain revision history; inspect UI/API response, audit event and downstream status.              | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
| COL-107     | Collection: trigger approval workflow                | Collection     | Configured tenant, roles and representative data   | Execute trigger approval workflow; inspect UI/API response, audit event and downstream status.            | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
| COL-108     | Collection: send overdue escalation                  | Collection     | Configured tenant, roles and representative data   | Execute send overdue escalation; inspect UI/API response, audit event and downstream status.              | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
| COL-109     | Collection: filter dashboard by entity               | Collection     | Configured tenant, roles and representative data   | Execute filter dashboard by entity; inspect UI/API response, audit event and downstream status.           | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
| EVD-100     | Evidence: create valid record                        | Evidence       | Configured tenant, roles and representative data   | Execute create valid record; inspect UI/API response, audit event and downstream status.                  | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| EVD-101     | Evidence: reject missing mandatory field             | Evidence       | Configured tenant, roles and representative data   | Execute reject missing mandatory field; inspect UI/API response, audit event and downstream status.       | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| EVD-102     | Evidence: validate effective date overlap            | Evidence       | Configured tenant, roles and representative data   | Execute validate effective date overlap; inspect UI/API response, audit event and downstream status.      | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| EVD-103     | Evidence: enforce scoped access                      | Evidence       | Configured tenant, roles and representative data   | Execute enforce scoped access; inspect UI/API response, audit event and downstream status.                | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| EVD-104     | Evidence: bulk import mixed valid/invalid rows       | Evidence       | Configured tenant, roles and representative data   | Execute bulk import mixed valid/invalid rows; inspect UI/API response, audit event and downstream status. | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| EVD-105     | Evidence: export authorised data                     | Evidence       | Configured tenant, roles and representative data   | Execute export authorised data; inspect UI/API response, audit event and downstream status.               | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
| EVD-106     | Evidence: retain revision history                    | Evidence       | Configured tenant, roles and representative data   | Execute retain revision history; inspect UI/API response, audit event and downstream status.              | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
| EVD-107     | Evidence: trigger approval workflow                  | Evidence       | Configured tenant, roles and representative data   | Execute trigger approval workflow; inspect UI/API response, audit event and downstream status.            | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
| EVD-108     | Evidence: send overdue escalation                    | Evidence       | Configured tenant, roles and representative data   | Execute send overdue escalation; inspect UI/API response, audit event and downstream status.              | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
| EVD-109     | Evidence: filter dashboard by entity                 | Evidence       | Configured tenant, roles and representative data   | Execute filter dashboard by entity; inspect UI/API response, audit event and downstream status.           | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
| FAC-100     | Factor: create valid record                          | Factor         | Configured tenant, roles and representative data   | Execute create valid record; inspect UI/API response, audit event and downstream status.                  | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| FAC-101     | Factor: reject missing mandatory field               | Factor         | Configured tenant, roles and representative data   | Execute reject missing mandatory field; inspect UI/API response, audit event and downstream status.       | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| FAC-102     | Factor: validate effective date overlap              | Factor         | Configured tenant, roles and representative data   | Execute validate effective date overlap; inspect UI/API response, audit event and downstream status.      | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| FAC-103     | Factor: enforce scoped access                        | Factor         | Configured tenant, roles and representative data   | Execute enforce scoped access; inspect UI/API response, audit event and downstream status.                | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| FAC-104     | Factor: bulk import mixed valid/invalid rows         | Factor         | Configured tenant, roles and representative data   | Execute bulk import mixed valid/invalid rows; inspect UI/API response, audit event and downstream status. | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| FAC-105     | Factor: export authorised data                       | Factor         | Configured tenant, roles and representative data   | Execute export authorised data; inspect UI/API response, audit event and downstream status.               | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
| FAC-106     | Factor: retain revision history                      | Factor         | Configured tenant, roles and representative data   | Execute retain revision history; inspect UI/API response, audit event and downstream status.              | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
| FAC-107     | Factor: trigger approval workflow                    | Factor         | Configured tenant, roles and representative data   | Execute trigger approval workflow; inspect UI/API response, audit event and downstream status.            | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
| FAC-108     | Factor: send overdue escalation                      | Factor         | Configured tenant, roles and representative data   | Execute send overdue escalation; inspect UI/API response, audit event and downstream status.              | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
| FAC-109     | Factor: filter dashboard by entity                   | Factor         | Configured tenant, roles and representative data   | Execute filter dashboard by entity; inspect UI/API response, audit event and downstream status.           | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
| CAR-100     | Carbon: create valid record                          | Carbon         | Configured tenant, roles and representative data   | Execute create valid record; inspect UI/API response, audit event and downstream status.                  | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| CAR-101     | Carbon: reject missing mandatory field               | Carbon         | Configured tenant, roles and representative data   | Execute reject missing mandatory field; inspect UI/API response, audit event and downstream status.       | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| CAR-102     | Carbon: validate effective date overlap              | Carbon         | Configured tenant, roles and representative data   | Execute validate effective date overlap; inspect UI/API response, audit event and downstream status.      | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| CAR-103     | Carbon: enforce scoped access                        | Carbon         | Configured tenant, roles and representative data   | Execute enforce scoped access; inspect UI/API response, audit event and downstream status.                | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| CAR-104     | Carbon: bulk import mixed valid/invalid rows         | Carbon         | Configured tenant, roles and representative data   | Execute bulk import mixed valid/invalid rows; inspect UI/API response, audit event and downstream status. | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| CAR-105     | Carbon: export authorised data                       | Carbon         | Configured tenant, roles and representative data   | Execute export authorised data; inspect UI/API response, audit event and downstream status.               | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
| CAR-106     | Carbon: retain revision history                      | Carbon         | Configured tenant, roles and representative data   | Execute retain revision history; inspect UI/API response, audit event and downstream status.              | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
| CAR-107     | Carbon: trigger approval workflow                    | Carbon         | Configured tenant, roles and representative data   | Execute trigger approval workflow; inspect UI/API response, audit event and downstream status.            | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
| CAR-108     | Carbon: send overdue escalation                      | Carbon         | Configured tenant, roles and representative data   | Execute send overdue escalation; inspect UI/API response, audit event and downstream status.              | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
| CAR-109     | Carbon: filter dashboard by entity                   | Carbon         | Configured tenant, roles and representative data   | Execute filter dashboard by entity; inspect UI/API response, audit event and downstream status.           | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
| S3-100      | Scope 3: create valid record                         | Scope 3        | Configured tenant, roles and representative data   | Execute create valid record; inspect UI/API response, audit event and downstream status.                  | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| S3-101      | Scope 3: reject missing mandatory field              | Scope 3        | Configured tenant, roles and representative data   | Execute reject missing mandatory field; inspect UI/API response, audit event and downstream status.       | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| S3-102      | Scope 3: validate effective date overlap             | Scope 3        | Configured tenant, roles and representative data   | Execute validate effective date overlap; inspect UI/API response, audit event and downstream status.      | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| S3-103      | Scope 3: enforce scoped access                       | Scope 3        | Configured tenant, roles and representative data   | Execute enforce scoped access; inspect UI/API response, audit event and downstream status.                | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| S3-104      | Scope 3: bulk import mixed valid/invalid rows        | Scope 3        | Configured tenant, roles and representative data   | Execute bulk import mixed valid/invalid rows; inspect UI/API response, audit event and downstream status. | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| S3-105      | Scope 3: export authorised data                      | Scope 3        | Configured tenant, roles and representative data   | Execute export authorised data; inspect UI/API response, audit event and downstream status.               | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
| S3-106      | Scope 3: retain revision history                     | Scope 3        | Configured tenant, roles and representative data   | Execute retain revision history; inspect UI/API response, audit event and downstream status.              | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
| S3-107      | Scope 3: trigger approval workflow                   | Scope 3        | Configured tenant, roles and representative data   | Execute trigger approval workflow; inspect UI/API response, audit event and downstream status.            | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
| S3-108      | Scope 3: send overdue escalation                     | Scope 3        | Configured tenant, roles and representative data   | Execute send overdue escalation; inspect UI/API response, audit event and downstream status.              | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
| S3-109      | Scope 3: filter dashboard by entity                  | Scope 3        | Configured tenant, roles and representative data   | Execute filter dashboard by entity; inspect UI/API response, audit event and downstream status.           | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
| ENV-100     | Environment: create valid record                     | Environment    | Configured tenant, roles and representative data   | Execute create valid record; inspect UI/API response, audit event and downstream status.                  | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| ENV-101     | Environment: reject missing mandatory field          | Environment    | Configured tenant, roles and representative data   | Execute reject missing mandatory field; inspect UI/API response, audit event and downstream status.       | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| ENV-102     | Environment: validate effective date overlap         | Environment    | Configured tenant, roles and representative data   | Execute validate effective date overlap; inspect UI/API response, audit event and downstream status.      | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| ENV-103     | Environment: enforce scoped access                   | Environment    | Configured tenant, roles and representative data   | Execute enforce scoped access; inspect UI/API response, audit event and downstream status.                | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| ENV-104     | Environment: bulk import mixed valid/invalid rows    | Environment    | Configured tenant, roles and representative data   | Execute bulk import mixed valid/invalid rows; inspect UI/API response, audit event and downstream status. | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| ENV-105     | Environment: export authorised data                  | Environment    | Configured tenant, roles and representative data   | Execute export authorised data; inspect UI/API response, audit event and downstream status.               | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
| ENV-106     | Environment: retain revision history                 | Environment    | Configured tenant, roles and representative data   | Execute retain revision history; inspect UI/API response, audit event and downstream status.              | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
| ENV-107     | Environment: trigger approval workflow               | Environment    | Configured tenant, roles and representative data   | Execute trigger approval workflow; inspect UI/API response, audit event and downstream status.            | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
| ENV-108     | Environment: send overdue escalation                 | Environment    | Configured tenant, roles and representative data   | Execute send overdue escalation; inspect UI/API response, audit event and downstream status.              | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
| ENV-109     | Environment: filter dashboard by entity              | Environment    | Configured tenant, roles and representative data   | Execute filter dashboard by entity; inspect UI/API response, audit event and downstream status.           | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
| SOC-100     | Social: create valid record                          | Social         | Configured tenant, roles and representative data   | Execute create valid record; inspect UI/API response, audit event and downstream status.                  | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| SOC-101     | Social: reject missing mandatory field               | Social         | Configured tenant, roles and representative data   | Execute reject missing mandatory field; inspect UI/API response, audit event and downstream status.       | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| SOC-102     | Social: validate effective date overlap              | Social         | Configured tenant, roles and representative data   | Execute validate effective date overlap; inspect UI/API response, audit event and downstream status.      | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| SOC-103     | Social: enforce scoped access                        | Social         | Configured tenant, roles and representative data   | Execute enforce scoped access; inspect UI/API response, audit event and downstream status.                | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| SOC-104     | Social: bulk import mixed valid/invalid rows         | Social         | Configured tenant, roles and representative data   | Execute bulk import mixed valid/invalid rows; inspect UI/API response, audit event and downstream status. | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| SOC-105     | Social: export authorised data                       | Social         | Configured tenant, roles and representative data   | Execute export authorised data; inspect UI/API response, audit event and downstream status.               | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
| SOC-106     | Social: retain revision history                      | Social         | Configured tenant, roles and representative data   | Execute retain revision history; inspect UI/API response, audit event and downstream status.              | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
| SOC-107     | Social: trigger approval workflow                    | Social         | Configured tenant, roles and representative data   | Execute trigger approval workflow; inspect UI/API response, audit event and downstream status.            | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
| SOC-108     | Social: send overdue escalation                      | Social         | Configured tenant, roles and representative data   | Execute send overdue escalation; inspect UI/API response, audit event and downstream status.              | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
| SOC-109     | Social: filter dashboard by entity                   | Social         | Configured tenant, roles and representative data   | Execute filter dashboard by entity; inspect UI/API response, audit event and downstream status.           | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
| GOV-100     | Governance: create valid record                      | Governance     | Configured tenant, roles and representative data   | Execute create valid record; inspect UI/API response, audit event and downstream status.                  | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| GOV-101     | Governance: reject missing mandatory field           | Governance     | Configured tenant, roles and representative data   | Execute reject missing mandatory field; inspect UI/API response, audit event and downstream status.       | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| GOV-102     | Governance: validate effective date overlap          | Governance     | Configured tenant, roles and representative data   | Execute validate effective date overlap; inspect UI/API response, audit event and downstream status.      | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| GOV-103     | Governance: enforce scoped access                    | Governance     | Configured tenant, roles and representative data   | Execute enforce scoped access; inspect UI/API response, audit event and downstream status.                | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| GOV-104     | Governance: bulk import mixed valid/invalid rows     | Governance     | Configured tenant, roles and representative data   | Execute bulk import mixed valid/invalid rows; inspect UI/API response, audit event and downstream status. | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| GOV-105     | Governance: export authorised data                   | Governance     | Configured tenant, roles and representative data   | Execute export authorised data; inspect UI/API response, audit event and downstream status.               | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
| GOV-106     | Governance: retain revision history                  | Governance     | Configured tenant, roles and representative data   | Execute retain revision history; inspect UI/API response, audit event and downstream status.              | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
| GOV-107     | Governance: trigger approval workflow                | Governance     | Configured tenant, roles and representative data   | Execute trigger approval workflow; inspect UI/API response, audit event and downstream status.            | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
| GOV-108     | Governance: send overdue escalation                  | Governance     | Configured tenant, roles and representative data   | Execute send overdue escalation; inspect UI/API response, audit event and downstream status.              | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
| GOV-109     | Governance: filter dashboard by entity               | Governance     | Configured tenant, roles and representative data   | Execute filter dashboard by entity; inspect UI/API response, audit event and downstream status.           | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
| MAT-100     | Materiality: create valid record                     | Materiality    | Configured tenant, roles and representative data   | Execute create valid record; inspect UI/API response, audit event and downstream status.                  | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| MAT-101     | Materiality: reject missing mandatory field          | Materiality    | Configured tenant, roles and representative data   | Execute reject missing mandatory field; inspect UI/API response, audit event and downstream status.       | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| MAT-102     | Materiality: validate effective date overlap         | Materiality    | Configured tenant, roles and representative data   | Execute validate effective date overlap; inspect UI/API response, audit event and downstream status.      | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| MAT-103     | Materiality: enforce scoped access                   | Materiality    | Configured tenant, roles and representative data   | Execute enforce scoped access; inspect UI/API response, audit event and downstream status.                | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| MAT-104     | Materiality: bulk import mixed valid/invalid rows    | Materiality    | Configured tenant, roles and representative data   | Execute bulk import mixed valid/invalid rows; inspect UI/API response, audit event and downstream status. | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| MAT-105     | Materiality: export authorised data                  | Materiality    | Configured tenant, roles and representative data   | Execute export authorised data; inspect UI/API response, audit event and downstream status.               | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
| MAT-106     | Materiality: retain revision history                 | Materiality    | Configured tenant, roles and representative data   | Execute retain revision history; inspect UI/API response, audit event and downstream status.              | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
| MAT-107     | Materiality: trigger approval workflow               | Materiality    | Configured tenant, roles and representative data   | Execute trigger approval workflow; inspect UI/API response, audit event and downstream status.            | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
| MAT-108     | Materiality: send overdue escalation                 | Materiality    | Configured tenant, roles and representative data   | Execute send overdue escalation; inspect UI/API response, audit event and downstream status.              | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
| MAT-109     | Materiality: filter dashboard by entity              | Materiality    | Configured tenant, roles and representative data   | Execute filter dashboard by entity; inspect UI/API response, audit event and downstream status.           | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
| TGT-100     | Targets: create valid record                         | Targets        | Configured tenant, roles and representative data   | Execute create valid record; inspect UI/API response, audit event and downstream status.                  | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| TGT-101     | Targets: reject missing mandatory field              | Targets        | Configured tenant, roles and representative data   | Execute reject missing mandatory field; inspect UI/API response, audit event and downstream status.       | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| TGT-102     | Targets: validate effective date overlap             | Targets        | Configured tenant, roles and representative data   | Execute validate effective date overlap; inspect UI/API response, audit event and downstream status.      | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| TGT-103     | Targets: enforce scoped access                       | Targets        | Configured tenant, roles and representative data   | Execute enforce scoped access; inspect UI/API response, audit event and downstream status.                | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| TGT-104     | Targets: bulk import mixed valid/invalid rows        | Targets        | Configured tenant, roles and representative data   | Execute bulk import mixed valid/invalid rows; inspect UI/API response, audit event and downstream status. | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| TGT-105     | Targets: export authorised data                      | Targets        | Configured tenant, roles and representative data   | Execute export authorised data; inspect UI/API response, audit event and downstream status.               | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
| TGT-106     | Targets: retain revision history                     | Targets        | Configured tenant, roles and representative data   | Execute retain revision history; inspect UI/API response, audit event and downstream status.              | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
| TGT-107     | Targets: trigger approval workflow                   | Targets        | Configured tenant, roles and representative data   | Execute trigger approval workflow; inspect UI/API response, audit event and downstream status.            | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
| TGT-108     | Targets: send overdue escalation                     | Targets        | Configured tenant, roles and representative data   | Execute send overdue escalation; inspect UI/API response, audit event and downstream status.              | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
| TGT-109     | Targets: filter dashboard by entity                  | Targets        | Configured tenant, roles and representative data   | Execute filter dashboard by entity; inspect UI/API response, audit event and downstream status.           | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
| CLI-100     | Climate Risk: create valid record                    | Climate Risk   | Configured tenant, roles and representative data   | Execute create valid record; inspect UI/API response, audit event and downstream status.                  | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| CLI-101     | Climate Risk: reject missing mandatory field         | Climate Risk   | Configured tenant, roles and representative data   | Execute reject missing mandatory field; inspect UI/API response, audit event and downstream status.       | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| CLI-102     | Climate Risk: validate effective date overlap        | Climate Risk   | Configured tenant, roles and representative data   | Execute validate effective date overlap; inspect UI/API response, audit event and downstream status.      | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| CLI-103     | Climate Risk: enforce scoped access                  | Climate Risk   | Configured tenant, roles and representative data   | Execute enforce scoped access; inspect UI/API response, audit event and downstream status.                | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| CLI-104     | Climate Risk: bulk import mixed valid/invalid rows   | Climate Risk   | Configured tenant, roles and representative data   | Execute bulk import mixed valid/invalid rows; inspect UI/API response, audit event and downstream status. | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| CLI-105     | Climate Risk: export authorised data                 | Climate Risk   | Configured tenant, roles and representative data   | Execute export authorised data; inspect UI/API response, audit event and downstream status.               | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
| CLI-106     | Climate Risk: retain revision history                | Climate Risk   | Configured tenant, roles and representative data   | Execute retain revision history; inspect UI/API response, audit event and downstream status.              | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
| CLI-107     | Climate Risk: trigger approval workflow              | Climate Risk   | Configured tenant, roles and representative data   | Execute trigger approval workflow; inspect UI/API response, audit event and downstream status.            | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
| CLI-108     | Climate Risk: send overdue escalation                | Climate Risk   | Configured tenant, roles and representative data   | Execute send overdue escalation; inspect UI/API response, audit event and downstream status.              | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
| CLI-109     | Climate Risk: filter dashboard by entity             | Climate Risk   | Configured tenant, roles and representative data   | Execute filter dashboard by entity; inspect UI/API response, audit event and downstream status.           | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
| SUP-100     | Supplier: create valid record                        | Supplier       | Configured tenant, roles and representative data   | Execute create valid record; inspect UI/API response, audit event and downstream status.                  | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| SUP-101     | Supplier: reject missing mandatory field             | Supplier       | Configured tenant, roles and representative data   | Execute reject missing mandatory field; inspect UI/API response, audit event and downstream status.       | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| SUP-102     | Supplier: validate effective date overlap            | Supplier       | Configured tenant, roles and representative data   | Execute validate effective date overlap; inspect UI/API response, audit event and downstream status.      | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| SUP-103     | Supplier: enforce scoped access                      | Supplier       | Configured tenant, roles and representative data   | Execute enforce scoped access; inspect UI/API response, audit event and downstream status.                | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| SUP-104     | Supplier: bulk import mixed valid/invalid rows       | Supplier       | Configured tenant, roles and representative data   | Execute bulk import mixed valid/invalid rows; inspect UI/API response, audit event and downstream status. | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| SUP-105     | Supplier: export authorised data                     | Supplier       | Configured tenant, roles and representative data   | Execute export authorised data; inspect UI/API response, audit event and downstream status.               | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
| SUP-106     | Supplier: retain revision history                    | Supplier       | Configured tenant, roles and representative data   | Execute retain revision history; inspect UI/API response, audit event and downstream status.              | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
| SUP-107     | Supplier: trigger approval workflow                  | Supplier       | Configured tenant, roles and representative data   | Execute trigger approval workflow; inspect UI/API response, audit event and downstream status.            | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
| SUP-108     | Supplier: send overdue escalation                    | Supplier       | Configured tenant, roles and representative data   | Execute send overdue escalation; inspect UI/API response, audit event and downstream status.              | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
| SUP-109     | Supplier: filter dashboard by entity                 | Supplier       | Configured tenant, roles and representative data   | Execute filter dashboard by entity; inspect UI/API response, audit event and downstream status.           | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
| INS-100     | Instruments: create valid record                     | Instruments    | Configured tenant, roles and representative data   | Execute create valid record; inspect UI/API response, audit event and downstream status.                  | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| INS-101     | Instruments: reject missing mandatory field          | Instruments    | Configured tenant, roles and representative data   | Execute reject missing mandatory field; inspect UI/API response, audit event and downstream status.       | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| INS-102     | Instruments: validate effective date overlap         | Instruments    | Configured tenant, roles and representative data   | Execute validate effective date overlap; inspect UI/API response, audit event and downstream status.      | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| INS-103     | Instruments: enforce scoped access                   | Instruments    | Configured tenant, roles and representative data   | Execute enforce scoped access; inspect UI/API response, audit event and downstream status.                | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| INS-104     | Instruments: bulk import mixed valid/invalid rows    | Instruments    | Configured tenant, roles and representative data   | Execute bulk import mixed valid/invalid rows; inspect UI/API response, audit event and downstream status. | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| INS-105     | Instruments: export authorised data                  | Instruments    | Configured tenant, roles and representative data   | Execute export authorised data; inspect UI/API response, audit event and downstream status.               | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
| INS-106     | Instruments: retain revision history                 | Instruments    | Configured tenant, roles and representative data   | Execute retain revision history; inspect UI/API response, audit event and downstream status.              | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
| INS-107     | Instruments: trigger approval workflow               | Instruments    | Configured tenant, roles and representative data   | Execute trigger approval workflow; inspect UI/API response, audit event and downstream status.            | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
| INS-108     | Instruments: send overdue escalation                 | Instruments    | Configured tenant, roles and representative data   | Execute send overdue escalation; inspect UI/API response, audit event and downstream status.              | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
| INS-109     | Instruments: filter dashboard by entity              | Instruments    | Configured tenant, roles and representative data   | Execute filter dashboard by entity; inspect UI/API response, audit event and downstream status.           | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
| DIS-100     | Disclosure: create valid record                      | Disclosure     | Configured tenant, roles and representative data   | Execute create valid record; inspect UI/API response, audit event and downstream status.                  | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| DIS-101     | Disclosure: reject missing mandatory field           | Disclosure     | Configured tenant, roles and representative data   | Execute reject missing mandatory field; inspect UI/API response, audit event and downstream status.       | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| DIS-102     | Disclosure: validate effective date overlap          | Disclosure     | Configured tenant, roles and representative data   | Execute validate effective date overlap; inspect UI/API response, audit event and downstream status.      | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| DIS-103     | Disclosure: enforce scoped access                    | Disclosure     | Configured tenant, roles and representative data   | Execute enforce scoped access; inspect UI/API response, audit event and downstream status.                | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| DIS-104     | Disclosure: bulk import mixed valid/invalid rows     | Disclosure     | Configured tenant, roles and representative data   | Execute bulk import mixed valid/invalid rows; inspect UI/API response, audit event and downstream status. | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| DIS-105     | Disclosure: export authorised data                   | Disclosure     | Configured tenant, roles and representative data   | Execute export authorised data; inspect UI/API response, audit event and downstream status.               | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
| DIS-106     | Disclosure: retain revision history                  | Disclosure     | Configured tenant, roles and representative data   | Execute retain revision history; inspect UI/API response, audit event and downstream status.              | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
| DIS-107     | Disclosure: trigger approval workflow                | Disclosure     | Configured tenant, roles and representative data   | Execute trigger approval workflow; inspect UI/API response, audit event and downstream status.            | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
| DIS-108     | Disclosure: send overdue escalation                  | Disclosure     | Configured tenant, roles and representative data   | Execute send overdue escalation; inspect UI/API response, audit event and downstream status.              | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
| DIS-109     | Disclosure: filter dashboard by entity               | Disclosure     | Configured tenant, roles and representative data   | Execute filter dashboard by entity; inspect UI/API response, audit event and downstream status.           | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
| AI-100      | AI: create valid record                              | AI             | Configured tenant, roles and representative data   | Execute create valid record; inspect UI/API response, audit event and downstream status.                  | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| AI-101      | AI: reject missing mandatory field                   | AI             | Configured tenant, roles and representative data   | Execute reject missing mandatory field; inspect UI/API response, audit event and downstream status.       | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| AI-102      | AI: validate effective date overlap                  | AI             | Configured tenant, roles and representative data   | Execute validate effective date overlap; inspect UI/API response, audit event and downstream status.      | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| AI-103      | AI: enforce scoped access                            | AI             | Configured tenant, roles and representative data   | Execute enforce scoped access; inspect UI/API response, audit event and downstream status.                | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| AI-104      | AI: bulk import mixed valid/invalid rows             | AI             | Configured tenant, roles and representative data   | Execute bulk import mixed valid/invalid rows; inspect UI/API response, audit event and downstream status. | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| AI-105      | AI: export authorised data                           | AI             | Configured tenant, roles and representative data   | Execute export authorised data; inspect UI/API response, audit event and downstream status.               | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
| AI-106      | AI: retain revision history                          | AI             | Configured tenant, roles and representative data   | Execute retain revision history; inspect UI/API response, audit event and downstream status.              | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
| AI-107      | AI: trigger approval workflow                        | AI             | Configured tenant, roles and representative data   | Execute trigger approval workflow; inspect UI/API response, audit event and downstream status.            | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
| AI-108      | AI: send overdue escalation                          | AI             | Configured tenant, roles and representative data   | Execute send overdue escalation; inspect UI/API response, audit event and downstream status.              | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
| AI-109      | AI: filter dashboard by entity                       | AI             | Configured tenant, roles and representative data   | Execute filter dashboard by entity; inspect UI/API response, audit event and downstream status.           | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
| INT-100     | Integration: create valid record                     | Integration    | Configured tenant, roles and representative data   | Execute create valid record; inspect UI/API response, audit event and downstream status.                  | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| INT-101     | Integration: reject missing mandatory field          | Integration    | Configured tenant, roles and representative data   | Execute reject missing mandatory field; inspect UI/API response, audit event and downstream status.       | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| INT-102     | Integration: validate effective date overlap         | Integration    | Configured tenant, roles and representative data   | Execute validate effective date overlap; inspect UI/API response, audit event and downstream status.      | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| INT-103     | Integration: enforce scoped access                   | Integration    | Configured tenant, roles and representative data   | Execute enforce scoped access; inspect UI/API response, audit event and downstream status.                | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| INT-104     | Integration: bulk import mixed valid/invalid rows    | Integration    | Configured tenant, roles and representative data   | Execute bulk import mixed valid/invalid rows; inspect UI/API response, audit event and downstream status. | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| INT-105     | Integration: export authorised data                  | Integration    | Configured tenant, roles and representative data   | Execute export authorised data; inspect UI/API response, audit event and downstream status.               | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
| INT-106     | Integration: retain revision history                 | Integration    | Configured tenant, roles and representative data   | Execute retain revision history; inspect UI/API response, audit event and downstream status.              | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
| INT-107     | Integration: trigger approval workflow               | Integration    | Configured tenant, roles and representative data   | Execute trigger approval workflow; inspect UI/API response, audit event and downstream status.            | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
| INT-108     | Integration: send overdue escalation                 | Integration    | Configured tenant, roles and representative data   | Execute send overdue escalation; inspect UI/API response, audit event and downstream status.              | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
| INT-109     | Integration: filter dashboard by entity              | Integration    | Configured tenant, roles and representative data   | Execute filter dashboard by entity; inspect UI/API response, audit event and downstream status.           | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
| ADM-100     | Administration: create valid record                  | Administration | Configured tenant, roles and representative data   | Execute create valid record; inspect UI/API response, audit event and downstream status.                  | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| ADM-101     | Administration: reject missing mandatory field       | Administration | Configured tenant, roles and representative data   | Execute reject missing mandatory field; inspect UI/API response, audit event and downstream status.       | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| ADM-102     | Administration: validate effective date overlap      | Administration | Configured tenant, roles and representative data   | Execute validate effective date overlap; inspect UI/API response, audit event and downstream status.      | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| ADM-103     | Administration: enforce scoped access                | Administration | Configured tenant, roles and representative data   | Execute enforce scoped access; inspect UI/API response, audit event and downstream status.                | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| ADM-104     | Administration: bulk import mixed valid/invalid rows | Administration | Configured tenant, roles and representative data   | Execute bulk import mixed valid/invalid rows; inspect UI/API response, audit event and downstream status. | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | High         |
| ADM-105     | Administration: export authorised data               | Administration | Configured tenant, roles and representative data   | Execute export authorised data; inspect UI/API response, audit event and downstream status.               | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
| ADM-106     | Administration: retain revision history              | Administration | Configured tenant, roles and representative data   | Execute retain revision history; inspect UI/API response, audit event and downstream status.              | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
| ADM-107     | Administration: trigger approval workflow            | Administration | Configured tenant, roles and representative data   | Execute trigger approval workflow; inspect UI/API response, audit event and downstream status.            | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
| ADM-108     | Administration: send overdue escalation              | Administration | Configured tenant, roles and representative data   | Execute send overdue escalation; inspect UI/API response, audit event and downstream status.              | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
| ADM-109     | Administration: filter dashboard by entity           | Administration | Configured tenant, roles and representative data   | Execute filter dashboard by entity; inspect UI/API response, audit event and downstream status.           | Expected business rule is enforced; authorised outcome is accurate; audit and error details are complete. | Functional    | Medium       |
| SEC-900     | SQL injection resistance                             | Security       | Test environment                                   | Submit injection payloads in search/import/API fields.                                                    | Inputs are safely handled; no data leakage or query manipulation.                                         | Security      | Critical     |
| SEC-901     | Broken object-level authorisation                    | Security       | Multiple scoped users                              | Enumerate IDs across entities and tenants.                                                                | All unauthorised objects return denied/not found without leakage.                                         | Security      | Critical     |
| SEC-902     | Privilege elevation expiry                           | Security       | JIT workflow configured                            | Elevate admin; wait for expiry; retry privileged action.                                                  | Action denied after expiry and elevation history logged.                                                  | Security      | High         |
| SEC-903     | Evidence signed URL expiry                           | Security       | Evidence exists                                    | Use download URL after expiry and from unauthorised session.                                              | Download denied.                                                                                          | Security      | High         |
| SEC-904     | Audit tamper detection                               | Security       | Audit archive exists                               | Attempt update/delete through DB/application path.                                                        | Modification is blocked or detected and alerted.                                                          | Security      | Critical     |
| PER-900     | Concurrent collection load                           | Performance    | 5,000 concurrent virtual users                     | Submit/read tasks during peak load.                                                                       | Error and latency remain within agreed capacity envelope.                                                 | Performance   | High         |
| PER-901     | High-volume file ingestion                           | Performance    | 10M-row synthetic file                             | Ingest, validate and reconcile.                                                                           | Completes within capacity target without memory failure or duplicates.                                    | Performance   | High         |
| RES-900     | Message broker interruption                          | Resilience     | Active event processing                            | Interrupt broker then restore.                                                                            | Transactions remain consistent; events retry without duplicates.                                          | Resilience    | High         |
| RES-901     | External factor provider outage                      | Resilience     | Provider integration enabled                       | Simulate timeout/outage.                                                                                  | Cached approved factors remain usable; sync alerts and retries.                                           | Resilience    | Medium       |
| AI-900      | AI factuality regression                             | AI             | Golden evaluation set                              | Run model/prompt release evaluation.                                                                      | Scores meet thresholds; failed release is blocked.                                                        | AI Evaluation | High         |

# 49. Calculation Validation Dataset

| **Dataset**             | **Purpose**                                        | **Expected Validation**                                          |
|-------------------------|----------------------------------------------------|------------------------------------------------------------------|
| Stationary combustion   | Multiple fuels, units, gases and factor years      | Gas-level and CO2e result, unit conversion and factor selection. |
| Electricity dual method | Grid, supplier factor, REC/PPA and residual mix    | Location and market results and instrument allocation.           |
| Refrigerants            | Additions, recovery, disposal, stock and equipment | Mass balance and leakage.                                        |
| Transport               | Mass-distance-mode and multi-leg shipments         | Tonne-km, allocation and category.                               |
| Spend-based Scope 3     | Multi-currency purchases and EEIO factors          | Currency conversion, factor year and category aggregation.       |
| Waste                   | Material, treatment and geography                  | Treatment factor and diversion.                                  |
| Workforce H&S           | Hours, incidents and workforce types               | Rates and denominator policy.                                    |
| Restatement             | Boundary acquisition/divestment and factor change  | Original vs restated comparative and threshold logic.            |

# 50. Defect Severity and Acceptance

| **Severity** | **Definition**                                                                                                              | **Release Rule**                                                       |
|--------------|-----------------------------------------------------------------------------------------------------------------------------|------------------------------------------------------------------------|
| Critical     | Security breach, tenant leakage, data loss/corruption, unusable core service or materially incorrect published calculation. | Zero open.                                                             |
| High         | Major function unavailable, incorrect controlled workflow or calculation with no acceptable workaround.                     | Zero open unless executive exception before non-production pilot only. |
| Medium       | Function impaired with workaround; limited reporting or usability impact.                                                   | Approved plan and release risk acceptance.                             |
| Low          | Cosmetic, wording or minor convenience issue.                                                                               | May defer to backlog.                                                  |

PART V - DELIVERY AND PRODUCT ROADMAP

# 51. Recommended Product Releases

| **Release**                               | **Scope**                                                                                                  | **Indicative Duration** |
|-------------------------------------------|------------------------------------------------------------------------------------------------------------|-------------------------|
| Release 0 - Foundation                    | Tenant, identity, organisation, catalogue, workflow, evidence, integration framework, audit and DevSecOps. | 4-5 months              |
| Release 1 - ESG Data & Carbon             | Collection, factors, Scope 1/2, core Scope 3, energy/water/waste, dashboards and basic disclosure.         | 5-6 months              |
| Release 2 - Reporting & Assurance         | GRI/ISSB/ESRS mapping engine, disclosure workspace, snapshots, assurance rooms and exports.                | 4-5 months              |
| Release 3 - Social, Governance & Supplier | Human capital, H&S, risk, policy, materiality, supplier portal and due diligence.                          | 5-6 months              |
| Release 4 - Transition, Climate Risk & AI | Targets, initiatives, scenarios, nature, AI copilots and advanced analytics.                               | 5-6 months              |
| Release 5 - Industry Accelerators         | Oil & gas, manufacturing, utilities, construction, financial services, public sector and others.           | Continuous              |

A credible production-grade product is expected to require a multi-disciplinary team and phased delivery. Duration depends on selected architecture, licensed datasets, framework content, certification targets, integration depth and number of industry accelerators.

# 52. Product Team Model

| **Capability**          | **Indicative Roles**                                                                             |
|-------------------------|--------------------------------------------------------------------------------------------------|
| Product and domain      | Product director, product managers, ESG/GRI/ISSB/ESRS SMEs, carbon accountants, industry SMEs.   |
| Experience              | UX lead, product designers, accessibility specialist, frontend engineers.                        |
| Engineering             | Solution architect, backend engineers, data engineers, integration engineers, workflow engineer. |
| Data and AI             | Analytics engineer, data scientist, ML/LLM engineer, AI governance lead.                         |
| Platform                | Cloud architect, DevOps/SRE, database engineer, security engineer.                               |
| Quality                 | QA architect, automation engineers, performance/security testers, test data engineer.            |
| Delivery and enablement | Technical writer, implementation consultants, training, support and customer success.            |

# 53. Out of Scope for Product Baseline

- Legal opinions on whether a customer is in scope of a law or whether a disclosure is compliant.

- Redistribution of copyrighted standards, taxonomies or commercial datasets without appropriate licence.

- Carbon-credit exchange, brokerage, custody or financial settlement unless separately regulated and designed.

- Automatic final assurance opinion or replacement of qualified assurance professionals.

- Uncontrolled autonomous decision-making affecting employees, suppliers or public claims.

- Hardware installation, meter calibration and environmental laboratory services.

- Customer-specific ERP customisation beyond published connector and implementation scope.

- Guaranteed ratings from third-party ESG rating agencies.

# 54. Risks and Mitigations

| **Risk**                          | **Mitigation**                                                                                      |
|-----------------------------------|-----------------------------------------------------------------------------------------------------|
| Standards and regulations evolve  | Metadata-driven framework versions, release governance and specialist review.                       |
| Poor source data                  | Quality scoring, validations, evidence, stewardship queues and phased primary-data improvement.     |
| Greenwashing or misleading claims | Gross/net separation, claim controls, sign-offs, evidence and legal review workflow.                |
| AI hallucination or leakage       | Grounding, private-model options, evaluations, source citations, policy gateway and human approval. |
| Complex Scope 3 estimates         | Method hierarchy, assumptions, data-quality scoring and supplier engagement.                        |
| Cross-tenant leakage              | Defence-in-depth isolation, automated tests, row security, scoped storage and security monitoring.  |
| Over-customisation                | Configurable catalogue/workflow/reporting and controlled extension APIs.                            |
| Vendor dataset lock-in            | Provider-neutral data contracts and versioned source metadata.                                      |
| Assurance burden                  | Lineage, evidence rooms, immutable snapshots and reusable controls.                                 |
| Performance at scale              | Partitioning, asynchronous jobs, lakehouse analytics and capacity testing.                          |

# Appendix A - References

| **Source**                  | **Reference Note**                                                                                                                                                                                   |
|-----------------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| IFRS Foundation             | IFRS S1 General Requirements for Disclosure of Sustainability-related Financial Information; IFRS S2 Climate-related Disclosures. Effective for annual periods beginning on or after 1 January 2024. |
| GHG Protocol                | Corporate Accounting and Reporting Standard; Corporate Value Chain (Scope 3) Accounting and Reporting Standard; Scope 2 Guidance.                                                                    |
| Global Reporting Initiative | GRI Universal Standards 2021, effective for reporting from 1 January 2023, plus applicable Topic and Sector Standards.                                                                               |
| European Commission / EFRAG | Corporate Sustainability Reporting Directive and European Sustainability Reporting Standards, including current and revised versions applicable to the reporting period.                             |
| W3C                         | Web Content Accessibility Guidelines (WCAG) 2.2.                                                                                                                                                     |
| OWASP                       | Application Security Verification Standard and OWASP Top 10 guidance.                                                                                                                                |
| ISO                         | Relevant management-system, information-security, business-continuity and GHG-accounting standards subject to customer licensing and applicability.                                                  |

# Appendix B - Glossary

| **Term**                           | **Definition**                                                                                        |
|------------------------------------|-------------------------------------------------------------------------------------------------------|
| Activity data                      | Quantitative measure of an activity that causes or relates to an ESG impact.                          |
| Assurance                          | Independent or internal evaluation of sustainability information and controls.                        |
| Base year                          | Historical period against which target progress is measured.                                          |
| CO2e                               | Common unit converting greenhouse gases using global warming potentials.                              |
| Double materiality                 | Assessment of impacts on people/environment and sustainability-related financial risks/opportunities. |
| Emission factor                    | Coefficient converting activity data into greenhouse gas emissions.                                   |
| GHG inventory                      | Quantified greenhouse gas emissions within a defined boundary and period.                             |
| Impact, Risk and Opportunity (IRO) | ESRS-oriented concept for impacts, financial risks and opportunities.                                 |
| Location-based                     | Scope 2 method using average grid factors for a location.                                             |
| Market-based                       | Scope 2 method reflecting contractual instruments and supplier-specific information.                  |
| Material topic                     | Sustainability topic determined relevant through the selected materiality process.                    |
| Restatement                        | Revision of previously reported comparative information under controlled policy.                      |
| Scope 1                            | Direct emissions from owned or controlled sources.                                                    |
| Scope 2                            | Indirect emissions from purchased/acquired energy.                                                    |
| Scope 3                            | Other indirect value-chain emissions.                                                                 |
| Snapshot                           | Immutable set of approved data, narratives, evidence and versions used for publication.               |
| Tenant                             | Logically isolated customer environment in the product.                                               |

# Appendix C - Traceability Model

The product backlog and implementation work items shall maintain traceability from business objective to requirement, design component, API/data entity, test case, release and operational control. Each controlled requirement in this document should be imported into the product lifecycle tool with a stable identifier before detailed sprint planning.

| **Traceability Level** | **Example**                                                   |
|------------------------|---------------------------------------------------------------|
| Business objective     | Trusted carbon inventory                                      |
| Functional requirement | CAR-004 base-year restatement                                 |
| Design component       | Carbon service + calculation engine + evidence service        |
| Data entities          | GHGInventory, CalculationRun, LineageEdge, DisclosureSnapshot |
| Test cases             | CAR-004 and related regression/security tests                 |
| Release                | Release 1 / Release 2                                         |
| Operational control    | Calculation-job monitoring and factor-change impact alert     |
