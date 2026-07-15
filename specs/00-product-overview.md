# ESG360 Product Overview

> Consolidated from FDD front matter and section 1.

## Purpose and Scope


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


## Normative and Reference Frameworks


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


## Product Vision and Business Outcomes


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


## Personas and Roles


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


## 1. Functional Architecture


| **Layer**           | **Modules**                                                                                                                  |
|---------------------|------------------------------------------------------------------------------------------------------------------------------|
| Experience          | Web application, mobile-responsive portal, supplier portal, assurance workspace, executive dashboards, API developer portal. |
| Governance          | Organisation, users, roles, workflow, reporting cycles, materiality, policies, risks, controls and audit.                    |
| ESG Operations      | Environmental, social, governance, incidents, actions, targets, initiatives and supplier ESG.                                |
| Carbon Intelligence | GHG inventory, factors, calculations, Scope 1/2/3, product carbon, projects, offsets and forecasting.                        |
| Disclosure          | Framework library, datapoint mappings, disclosure packs, narratives, approvals, digital export and publishing.               |
| Data and AI         | Ingestion, quality, lineage, anomaly detection, document extraction, recommendations and controlled generative AI.           |
| Platform            | Configuration, localisation, notifications, integrations, subscription, metering, security and observability.                |

