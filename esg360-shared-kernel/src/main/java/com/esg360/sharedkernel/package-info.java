/**
 * ESG360 shared kernel (ADR-0004): the small, slow-changing, FK-able core that every feature
 * module may depend on. Contents are limited to {@code Tenant}, {@code OrganisationNode}, {@code
 * ReportingPeriod}, {@code MetricDefinition} (+ unit/conversion reference) and the {@code
 * Quantity} value type.
 *
 * <p>This package must depend on nothing but the JDK — enforced by ArchUnit. Adding anything to
 * the shared kernel requires a new ADR.
 */
package com.esg360.sharedkernel;
