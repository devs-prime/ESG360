# 44. Logging, Monitoring and Operational Support

> **Source:** PSSTEC ESG360 v1.0, section 44 (TDD).


| **Telemetry**       | **Minimum Fields / Controls**                                                                               |
|---------------------|-------------------------------------------------------------------------------------------------------------|
| Application logs    | Timestamp, severity, service, environment, tenant pseudonym, correlation ID, event code; no secrets.        |
| Audit events        | Actor, action, object, result, source IP/device context, before/after hash and reason.                      |
| Metrics             | Request rate/latency/error, queue depth, job duration, DB health, storage and tenant usage.                 |
| Traces              | Distributed trace across gateway, services, queues and external calls.                                      |
| Business monitoring | Collection progress, calculation failures, stale integrations, disclosure deadlines and assurance requests. |
| Alerts              | Severity, threshold, runbook, owner, suppression and escalation.                                            |
| Support access      | Ticket-linked, time-bound, approved and fully audited tenant support session.                               |

