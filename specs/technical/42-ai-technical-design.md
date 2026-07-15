# 42. AI Technical Design

> **Source:** PSSTEC ESG360 v1.0, section 42 (TDD).


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

