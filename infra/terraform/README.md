# ESG360 infrastructure

**Status: skeleton.** The cloud provider is deliberately open (ADR-0017) and v1
development runs against a locally hosted PostgreSQL (`docker-compose.yml` at the
repo root). Nothing here is needed to build or run the product today.

## Decisions that shape this directory

- **Terraform** (ADR-0017) — portability across §28's five deployment profiles;
  Bicep is disqualified by the sovereign/private-cloud profiles.
- **Managed containers, not Kubernetes** (ADR-0020).
- **Residency is cell placement, never application logic** (ADR-0016): each cell
  becomes its own root module/state; the application never branches on region.
- **Policy-as-code + approval on IaC changes** (spec 43 pt 38): wire OPA/Conftest
  or cloud-native policy checks into CI when the first real module lands here.

## Intended layout (when the cloud is chosen)

```
infra/terraform/
├── modules/          # reusable building blocks (network, postgres, app runtime, storage)
└── cells/
    └── cell-001/     # one root module + state per cell (ADR-0003/0016)
```

## What decides the cloud

Who the first customers are (ADR-0017): Microsoft-shop enterprises → Azure;
otherwise AWS/GCP on their constraints. Do not decide ahead of that signal.
