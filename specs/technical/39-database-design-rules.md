# 39. Database Design Rules

> **Source:** PSSTEC ESG360 v1.0, section 39 (TDD).


- All business tables include tenant_id, created_at, created_by, updated_at, updated_by and row_version.

- Controlled definitions use immutable versions plus a stable logical identifier.

- Submissions and calculations are append-only revisions; corrections supersede rather than overwrite.

- Monetary and ESG quantities use decimal/numeric types with domain-appropriate precision.

- UTC timestamps are mandatory; date-only fields remain date types.

- Foreign keys and unique constraints enforce tenant consistency.

- Partition high-volume activity, audit and telemetry tables by tenant/time.

- Soft delete is limited to non-controlled drafts; referenced records are inactivated.

- PII fields are separated or encrypted where required and excluded from broad analytics models.

