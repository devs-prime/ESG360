# 38. Calculation Engine Design

> **Source:** PSSTEC ESG360 v1.0, section 38 (TDD).


The calculation engine shall execute metadata-defined formulas against versioned inputs and reference data. It must be deterministic, explainable and suitable for replay during assurance or restatement.

24. Resolve tenant, boundary, period, metric/factor/formula versions and applicable hierarchy.

25. Load approved activity records and dimensions at unrounded precision.

26. Validate unit compatibility and convert to canonical unit.

27. Resolve factor according to configured hierarchy and validity.

28. Calculate gas-level emissions or metric result using decimal arithmetic.

29. Apply GWP set where CO2e is required.

30. Aggregate according to metric rules and consolidation boundary.

31. Create result records and lineage edges to every input, factor and formula version.

32. Run cross-checks, anomaly rules and reconciliation.

33. Publish CalculationCompleted event and retain execution manifest/hash.

## Calculation Pseudocode

result = SUM(activity.normalised_quantity × factor.value × allocation_ratio × conversion_multiplier)  
co2e = SUM(gas_mass × GWP\[gas, version\])

