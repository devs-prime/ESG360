# 20. AI Copilots and Intelligent Automation

> **Source:** PSSTEC ESG360 FDD v1.0, section 20. **Indicative release:** R4.
> Read `CLAUDE.md` (root) before implementing. Cross-functional rules BR-001–BR-020 (spec 24) apply to this module.


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



## Acceptance test cases (from Master Test Catalogue, spec 48)

Implementation of this module is not complete until these pass.

| **Test ID** | **Title**                                            | **Module**     | **Preconditions**                                  | **Test Steps**                                                                                            | **Expected Result**                                                                                       | **Type**      | **Priority** |
|---|---|---|---|---|---|---|---|
| AI-001      | Grounded narrative citations                         | AI             | Approved data and evidence exist                   | Request narrative draft.                                                                                  | Draft cites only authorised sources and is marked AI-generated.                                           | Functional    | High         |
| AI-002      | Prompt injection in evidence                         | AI             | Malicious text embedded in uploaded evidence       | Ask assistant to summarise disclosure.                                                                    | System ignores embedded instruction, follows policy and flags risk where applicable.                      | Functional    | High         |
| AI-003      | Restricted data egress                               | AI             | External model selected and restricted data exists | Attempt prompt using restricted data.                                                                     | Request is blocked or routed to approved private model according to policy.                               | Functional    | High         |
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
| AI-900      | AI factuality regression                             | AI             | Golden evaluation set                              | Run model/prompt release evaluation.                                                                      | Scores meet thresholds; failed release is blocked.                                                        | AI Evaluation | High         |
