# 37. API Design Standards

> **Source:** PSSTEC ESG360 v1.0, section 37 (TDD).


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

