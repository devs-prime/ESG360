# ADR-0020: Runtime platform (revised) — managed containers first, defer Kubernetes

**Status:** Accepted (signed off 2026-07-15) — **amends [ADR-0016](0016-deployment-and-residency.md)** (runtime only; residency-by-placement, canary and DR discipline all stand)
**Date:** 2026-07-15
**Drivers:** §28, §52 (no SRE on the team), NFR-001, NFR-015, V1-SCOPE

## Context

ADR-0016 chose Kubernetes + IaC, one cell definition for every profile. That is right for
20–40 cells. **It is wrong for one cell operated by a junior team with no SRE.**

§52 assumes a Platform/SRE capability. You don't have one yet, and V1-SCOPE cuts to a single
shared cell. Kubernetes at that scale is a tarpit: you'd spend the first months on cluster
upgrades, ingress controllers, RBAC, autoscaler tuning and node pools rather than on Scope 1+2.

## Decision

**Run the containers on a managed container platform** (Azure Container Apps / Google Cloud Run /
AWS ECS Fargate — follows the cloud decision). **Defer Kubernetes.**

Everything else in ADR-0016 is unchanged: same containers, same Terraform, same cells, same
residency-by-placement, same canary, same PITR/replica/restore-test discipline.

## Rationale

1. **The architecture does not depend on Kubernetes.** ADR-0016's real content is *cells*,
   *residency as placement*, and *progressive delivery*. All three work on managed containers. K8s
   was an implementation detail that ADR-0016 promoted above its station.
2. **Containers are containers.** Moving to K8s later changes Terraform, not the application. The
   image, the config, the health checks, the migrations all carry over.
3. **It buys back months of platform work you cannot currently staff** — a real trade for a team
   with no SRE, and the difference between shipping v1 and not.
4. **Background workers still work.** The outbox relay and the calculation workers run as
   Container Apps Jobs / Cloud Run Jobs / ECS scheduled tasks. NFR-003's parallel workers are fine.

## Consequences

**Positive:** dramatically smaller operational surface; managed TLS, autoscaling, rollouts and
revisions out of the box; canary/blue-green is a built-in feature on all three platforms; a
junior team can actually operate it.

**Negative:**
- **Some Terraform rework when you adopt K8s.** Bounded, and only the infra layer.
- **Per-cloud divergence** — Container Apps, Cloud Run and Fargate are not interchangeable, so this
  is the one place the "one definition, five profiles" promise weakens until K8s arrives. Keep the
  divergence confined to a single Terraform module.
- **§28's private-cloud profile will likely need Kubernetes** (customers run their own clusters
  and will expect a Helm chart). That profile is out of V1-SCOPE, so the need arrives with the
  trigger below — not before.

## Adoption trigger — move to Kubernetes when any of:

- Cell count exceeds ~5, or fleet management becomes a recurring cost
- The first private-cloud or sovereign customer is contracted (§28)
- You have hired an SRE / platform engineer (§52's Platform capability)

Take K8s deliberately, as a funded piece of work — not by drifting into it.
