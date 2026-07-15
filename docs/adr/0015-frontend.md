# ADR-0015: Frontend — Next.js self-hosted; separate supplier/assurer portal application

**Status:** Proposed
**Drivers:** §35, §26, NFR-009 (WCAG 2.2 AA), NFR-010 (i18n/RTL), NFR-011, BR-008, SUP-001, §28

## Context

§35 recommends React/Next.js + TypeScript. §26 identifies three distinct user surfaces: the
enterprise application, the supplier portal, and the assurance workspace. BR-008 and SUP-001
require that supplier users can never reach another supplier's records or the buyer's internal
notes — a Critical-severity boundary (§50).

## Decision

1. **Next.js (App Router) + TypeScript**, self-hosted in containers. **No platform-specific
   features** (no Vercel-only primitives) — §28's private cloud forbids them.
2. **The supplier/assurer portal is a separate deployable application** with its own auth realm,
   sharing a design-system package with the internal app.
3. **WCAG 2.2 AA, i18n, and RTL from the first component** — not a later hardening pass.
4. **All authorization is server-side** (§35). The client is never the enforcement point.

## Rationale

**Why a separate portal app — the important call.** The alternative (one app, `isSupplier` role
flags) means a single authorization mistake anywhere leaks buyer internal notes to a supplier.
Separation buys:
- **Blast radius**: an internal-app authz bug cannot be reached by a supplier session at all
- **Smaller attack surface**: the portal ships only supplier routes and supplier scopes
- **Independent pen-testing** of the externally-exposed surface
- **Token scoping**: a portal session can never hold internal scopes, structurally

The cost is a shared design system and some duplicated shell code. Against a Critical-severity
leak risk from your least-trusted user population, that is cheap.

**Why WCAG/i18n/RTL from day one.** NFR-009/NFR-010 are product requirements (public-sector and
EU ESG buyers will test them in procurement). Retrofitting accessibility and RTL across ~25
modules is famously more expensive than the entire original build. ACC-001 (keyboard-only task
completion) is an explicit test case — treat it as a build-time constraint, not a QA finding.

**Why self-hosted, no platform lock-in.** §28's private-cloud and sovereign profiles mean the app
must run in a customer's Kubernetes. Any Vercel-specific feature would fork the product.

## Consequences

**Positive:** bounded blast radius for the external surface; portable to all five profiles;
accessibility/localisation built in rather than bolted on.

**Negative:**
- Two frontend deployables to build, test and release. Mitigated by a shared design-system package.
- Self-hosting Next.js means owning the Node runtime, caching and image optimisation yourself.
- A design system is upfront investment before the first feature ships. It pays back by module 5.

## Deliberately not decided here

Component library choice (shadcn/ui, MUI, Fluent, etc.) — reversible, and a Release 0 detail.
The constraint that matters: whatever is chosen must meet WCAG 2.2 AA and support RTL.
