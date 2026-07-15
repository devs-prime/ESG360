# IaC skeleton (ADR-0017, spec 43 pt 38). The cloud provider is deliberately
# undecided — no provider blocks are declared yet. When the first customer's
# constraints pick the cloud, providers land here and cells are modelled as
# separate root modules per ADR-0016 (residency = cell placement, never
# application logic).
#
# For v1 development the database is locally hosted (docker-compose.yml at the
# repo root) — nothing in this directory is required to build or run the product.

terraform {
  required_version = ">= 1.9"
}
