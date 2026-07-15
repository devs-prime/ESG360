# 26. Solution Context

> **Source:** PSSTEC ESG360 v1.0, section 26 (SDD).


ESG360 is designed as a cloud-native, multi-tenant product with optional dedicated-tenant and private deployment profiles. The logical architecture separates transactional ESG records, time-series activity data, documents/evidence, analytical models, search/vector indexes and immutable audit events.

| **Actor / System**       | **Interaction**                                                                   |
|--------------------------|-----------------------------------------------------------------------------------|
| Enterprise users         | Browser-based application and optional mobile-responsive capture.                 |
| Suppliers and assurers   | External portals with isolated identity and scoped permissions.                   |
| ERP / HR / Procurement   | Master data, financial activity, workforce, supplier and initiative actuals.      |
| IoT / Utility / BMS      | Meter and operational activity data.                                              |
| External data providers  | Emission factors, climate hazards, water stress, biodiversity and risk screening. |
| BI and regulator systems | Governed semantic datasets, structured disclosures and exports.                   |
| Identity provider        | Federated authentication, MFA and lifecycle provisioning.                         |
| Notification providers   | Email, Teams/collaboration and optional SMS.                                      |

