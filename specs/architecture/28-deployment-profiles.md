# 28. Deployment Profiles

> **Source:** PSSTEC ESG360 v1.0, section 28 (SDD).


| **Profile**                           | **Use Case**                                      | **Isolation**                                                          |
|---------------------------------------|---------------------------------------------------|------------------------------------------------------------------------|
| Shared multi-tenant SaaS              | Standard commercial cloud service                 | Logical tenant isolation, shared services and tenant-partitioned data. |
| Dedicated tenant SaaS                 | Large or regulated enterprise                     | Dedicated databases/storage and optional dedicated compute.            |
| Sovereign cloud                       | Jurisdictional residency and operator constraints | Approved in-country region and restricted operational access.          |
| Private cloud / customer subscription | Highly regulated or disconnected requirements     | Customer-controlled network, keys, identity and model endpoints.       |
| Development / test                    | Engineering and customer validation               | Synthetic or masked data; production credentials prohibited.           |

