# 19. Analytics, Dashboards and Benchmarking

> **Source:** PSSTEC ESG360 FDD v1.0, section 19. **Indicative release:** R1 (basic) / R4 (advanced).
> Read `CLAUDE.md` (root) before implementing. Cross-functional rules BR-001–BR-020 (spec 24) apply to this module.


Provides role-based analytics, self-service exploration and governed external benchmarking.

| **Req. ID** | **Requirement**        | **Functional Description**                                                | **Priority** | **Acceptance Criteria**                                 |
|-------------|------------------------|---------------------------------------------------------------------------|--------------|---------------------------------------------------------|
| 19-001      | Executive scorecard    | Display approved KPIs, targets, risks and trends.                         | Must         | User can drill to data lineage.                         |
| 19-002      | Operational dashboards | Provide energy, water, waste, safety, supplier and collection dashboards. | Must         | Dashboard respects scope security.                      |
| 19-003      | Ad hoc analysis        | Allow authorised users to slice governed datasets.                        | Should       | Personal queries cannot bypass row security.            |
| 19-004      | Benchmark datasets     | Load licensed internal/external benchmarks with metadata.                 | Should       | Benchmark source and peer group are displayed.          |
| 19-005      | Alerts                 | Trigger threshold, trend, anomaly and deadline alerts.                    | Must         | Alert shows rule and contributing records.              |
| 19-006      | Forecasting            | Provide transparent statistical/ML forecasts.                             | Should       | Model version, confidence and features are documented.  |
| 19-007      | Natural-language query | Allow governed questions over approved semantic model.                    | Could        | Responses cite source visual/metric and enforce access. |
| 19-008      | Scheduled distribution | Email or publish dashboard snapshots to authorised users.                 | Should       | Distribution follows classification policy.             |
| 19-009      | Board presentation     | Generate editable board charts and commentary.                            | Should       | Output uses approved reporting snapshot.                |
| 19-010      | Data export            | Export filtered data with watermark/classification and audit log.         | Must         | Export reflects user permissions.                       |

