# ESG360 API conventions

How every endpoint behaves (spec 37, ADR-0014). The OpenAPI document
`esg360-app/src/main/resources/openapi/esg360-api.yaml` is the **source of truth**:
Spring API interfaces are generated from it, and controllers implement those
interfaces, so an endpoint cannot drift from the published contract without a
compile error. Shared runtime plumbing lives in the `esg360-web` module.

## Base

- Base path **`/api/v1`** (applied centrally by `ApiPathPrefixConfig`; controllers don't repeat it).
- JSON UTF-8; ISO-8601 UTC timestamps (BR-017).
- Tenant is resolved from the OAuth2 token, never from the request body (ADR-0003). The
  security scheme is documented in OpenAPI; enforcement arrives with identity (item 0.5).

## Numbers — decimals as strings

Every `BigDecimal` and every `Quantity` serialises as a JSON **string**, so no consumer can
parse an ESG value into a binary float (ADR-0006):

```json
{ "amount": "1234.567", "throughput": { "value": "12.50", "unit": "kWh" } }
```

Registered by `QuantityJsonModule`.

## Errors — Problem Details (RFC 9457)

`application/problem+json` with a stable `code`, the request `correlationId`, a `retryable`
flag, and — for validation — a `fieldErrors` array:

```json
{
  "type": "about:blank",
  "title": "Not Found",
  "status": 404,
  "detail": "missing",
  "code": "RESOURCE_NOT_FOUND",
  "correlationId": "6f1c…",
  "retryable": false
}
```

Throw an `ApiException` (or `NotFoundException` / `ConflictException` /
`PreconditionFailedException` / `PreconditionRequiredException`); `ApiExceptionHandler` renders it.
Unexpected exceptions become a `500 INTERNAL_ERROR` with no internals leaked.

## Correlation IDs

`CorrelationIdFilter` reads `X-Correlation-Id` (or generates one), puts it on the logging MDC and
the response header, and threads it into every Problem Details body (spec 44).

## Pagination — cursor, never offset

List endpoints return a `CursorPage<T>` (`items`, `nextCursor`, `limit`); `nextCursor` is an
opaque Base64 token (`Cursor`). Clients treat it as opaque. Offset pagination is prohibited —
it degrades and skips/duplicates rows under concurrent writes (ADR-0014). `PageLimit` clamps the
page size (default 50, max 200).

> Convention, not yet machine-enforced: there is no ArchUnit rule banning offset pagination (it
> can't be detected reliably in SQL strings). Reviewers enforce it; revisit if it regresses.

## Concurrency — ETag / If-Match

Mutations use optimistic concurrency backed by `row_version` (spec 39). The ETag is the quoted
`row_version` (`ETags.forRowVersion`); a mutating request must send `If-Match` — **428** if
absent, **412** if stale (`ETags.requireMatch`).

## Idempotency

Create/import endpoints honour `Idempotency-Key` (BR-011, COL-002). `IdempotencyService.begin`
returns `Proceed` (first time — execute, then `complete`) or `Replay` (return the stored response
verbatim). The same key with a different payload is a **409**. Backed by the tenant-scoped,
RLS-guarded `api.idempotency_key` table (V005); expired rows are pruned by a maintenance job
(item 0.6).
