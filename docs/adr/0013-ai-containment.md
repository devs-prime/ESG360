# ADR-0013: AI — gateway sidecar, untrusted-content model, mandatory human review

**Status:** Proposed
**Drivers:** §42, §20, BR-006, AI-001, AI-002, AI-003, NFR-017

## Context

§42 requires a model gateway, prompt registry, grounding service, safety filters, structured
output, evaluation harness, human review and a deterministic non-AI fallback. BR-006: AI output
is labelled and never bypasses human-controlled workflow.

The threat that deserves naming: **evidence files are uploaded by suppliers** — parties outside
your trust boundary. AI-002 tests prompt injection embedded in uploaded evidence. This is not
hypothetical; it is the expected attack.

## Decision

AI is a **sidecar module behind a gateway interface**. It is **never** in a transactional path
and is **always** disable-able. Seven structural controls:

1. **Tenant content is data, never instructions.** Retrieved evidence enters a data channel; it
   is never concatenated into the system prompt.
2. **No tools granted to summarization/narrative flows.** An injected "call the export tool" has
   nothing to call.
3. **Structured output only** — JSON schema validated; non-conforming output rejected outright.
4. **Citation provenance filter** — every cited source ID must resolve to a document the
   *requesting user* is authorized for, or the entire output is rejected (AI-001).
5. **Egress classification gate** — restricted/PII data classes never leave to external models
   (AI-003). Routing by data class is the gateway's responsibility.
6. **Human review queue** — AI output lands in draft/review. No material auto-publication (BR-006).
7. **No training on tenant data by default**; per-tenant vector namespaces; encrypted embeddings.

## Rationale

**Why structural, not filter-based.** Prompt-injection *detection* is a heuristic and will be
defeated. The controls above are structural: with no tools bound, an injection that says "export
all data" has no capability to invoke. With schema-validated output, an injection producing prose
fails validation. With a provenance filter, an injection citing another tenant's document fails
resolution and the output is discarded. Defence in depth, where each layer works even if the
detector doesn't.

**Why AI can never be load-bearing.** §42 requires a deterministic non-AI fallback, and NFR-017
requires graceful degradation. If a model provider outage could block a disclosure approval, the
architecture would be wrong. Concretely: **no controlled workflow transition may depend on an
AI call.**

**Why a provider-neutral gateway.** §28's sovereign and private profiles require private or
in-country model endpoints, and some tenants will forbid external inference entirely. Routing by
tenant, use case and data class is the only way one codebase serves all five profiles.

## Consequences

**Positive:** injection has no capability surface; AI outages are cosmetic; per-tenant AI opt-out
is a config flag; auditable AI activity per §42 (purpose, model/version, prompt template ID,
sources, output hash, latency, feedback).

**Negative:**
- Structured-output-only constrains UX — no free-form streaming prose in grounded flows.
- The eval harness (golden datasets, factuality, citation, leakage, bias, regression) is real
  engineering, comparable to a test suite. Budget for it; §42 requires it.
- Provider neutrality forfeits some provider-specific features.
- Human review means AI saves drafting time, not approval time. That is the correct trade under
  BR-006, and should be set as the expectation with customers rather than oversold.

## Explicitly rejected

- **RAG over a cross-tenant corpus** — violates ADR-0003 and BR-008.
- **Fine-tuning on tenant data** — violates §42's default.
- **AI auto-approval of any material disclosure** — violates BR-006 and the product's premise.
