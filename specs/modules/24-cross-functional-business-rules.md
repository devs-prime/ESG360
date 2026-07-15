# 24. Cross-Functional Business Rules

> **Source:** PSSTEC ESG360 FDD v1.0, section 24. **Indicative release:** ALL — applies to every module.
> Read `CLAUDE.md` (root) before implementing. Cross-functional rules BR-001–BR-020 (spec 24) apply to this module.


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

