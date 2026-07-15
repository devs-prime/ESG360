# 18. Disclosure Management and Framework Packs

> **Source:** PSSTEC ESG360 FDD v1.0, section 18. **Indicative release:** R1 (basic) / R2 (full).
> Read `CLAUDE.md` (root) before implementing. Cross-functional rules BR-001–BR-020 (spec 24) apply to this module.


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



## Acceptance test cases (from Master Test Catalogue, spec 48)

Implementation of this module is not complete until these pass.

| **Test ID** | **Title**                                            | **Module**     | **Preconditions**                                  | **Test Steps**                                                                                            | **Expected Result**                                                                                       | **Type**      | **Priority** |
|---|---|---|---|---|---|---|---|
| DIS-001     | Disclosure snapshot immutability                     | Disclosure     | Disclosure approved                                | Create snapshot; modify source value later.                                                               | Snapshot retains original approved values and hash.                                                       | Functional    | High         |
| DIS-002     | Framework crosswalk                                  | Disclosure     | Metric mapped to two frameworks                    | Approve metric and inspect both packs.                                                                    | Both packs reuse the same approved datapoint with mapping lineage.                                        | Functional    | High         |
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
