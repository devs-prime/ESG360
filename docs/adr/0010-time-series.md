# ADR-0010: Time-series — native PostgreSQL partitioning; defer a dedicated store

**Status:** Proposed
**Drivers:** §29 (separate time-series store), §35 ("cloud time-series service **or** PostgreSQL extension"), NFR-004 (billions of records), §28 (private cloud)

## Context

Meter, BMS, SCADA and IoT readings are high-volume and append-heavy. §29's data architecture
lists a distinct time-series store; §35 explicitly permits a PostgreSQL extension instead.
NFR-004 requires billions of activity/time-series rows.

## Decision

**Native PostgreSQL declarative partitioning + BRIN indexes**, in the same database, inside the
Integration/Environmental module schemas. Partition by `(tenant_id, time)`. **Defer** both
TimescaleDB and any dedicated time-series service.

## Rationale

1. **Per-cell volume is manageable.** "Billions" is estimate-across-the-estate. Spread over
   20–40 cells (ADR-0003), each cell holds a few hundred million rows — squarely within native
   partitioning's comfort zone.
2. **Readings must join to org hierarchy, periods and factors** for calculation. A separate
   store turns every calculation into a cross-store join — the exact complexity NFR-003 doesn't
   need.
3. **§28's private-cloud profile penalises extra infrastructure.** Every additional datastore is
   something a customer's ops team must run in their own subscription. Zero-extra-dependency is
   a genuine architectural feature here, not laziness.
4. **BRIN indexes** are near-perfect for naturally time-ordered append-only data: tiny
   (kilobytes where B-tree would be gigabytes) and fast for range scans, which is the access
   pattern.
5. **TimescaleDB stays available later.** It's an extension — adopting it is an additive change,
   not a migration. Not choosing it now costs nothing.

## Consequences

**Positive:** one datastore; joins stay local; simplest possible private/sovereign footprint;
partition-drop makes retention tiers (§29) trivial.

**Negative:**
- No built-in compression or continuous aggregates (TimescaleDB's real advantages). Roll up via
  scheduled materialized views instead.
- Manual partition lifecycle management (create-ahead, detach, archive). Automate it in R0.
- If a single tenant becomes a genuine IoT firehose, this will need revisiting.

## Note on TimescaleDB licensing

Compression and continuous aggregates are under the Timescale License (TSL), which forbids
offering *Timescale itself* as a service. Building an application on it is permitted — but in
the private-cloud profile the customer operates the database, so the licence question deserves
legal review **before** adoption, not after.

## Evolution trigger

Move to TimescaleDB (first) or a dedicated service (only if that fails) when **any** of:
- Sustained meter ingest saturates write capacity in a shared cell
- Storage cost is dominated by uncompressed time-series
- Rollup queries breach NFR-002's p95 budget despite materialized views

Take TimescaleDB before a separate service: it keeps the joins and the single-datastore benefit.
