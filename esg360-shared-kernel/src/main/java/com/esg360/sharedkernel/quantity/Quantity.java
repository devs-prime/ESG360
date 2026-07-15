package com.esg360.sharedkernel.quantity;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * An exact decimal amount paired with its unit. The only way domain code does arithmetic.
 *
 * <p>Rules this type enforces (ADR-0006, ADR-0018, BR-001/BR-002):
 *
 * <ul>
 *   <li>All arithmetic is decimal — construction from {@code float}/{@code double} is impossible.
 *   <li>Division is pinned to scale {@value #DIVISION_SCALE}, {@link RoundingMode#HALF_EVEN}.
 *       Callers never choose a rounding mode; presentation-layer rounding happens elsewhere.
 *   <li>Unit-mixing is an error, not a coercion: {@code add}/{@code subtract} require the same
 *       unit. Unit conversion is the catalogue's job (normalised value/unit pairs, BR-001), not
 *       silent arithmetic.
 *   <li>Value equality: {@code 1.0 kg} equals {@code 1.00 kg} (scale-insensitive), because two
 *       representations of the same physical quantity must not diverge in sets or maps.
 * </ul>
 *
 * <p>Domain code must never use raw {@link BigDecimal} arithmetic or binary floating point — both
 * are banned by ArchUnit/Checkstyle rules.
 */
public final class Quantity implements Comparable<Quantity> {

    /** Fixed intermediate scale for division; rounding to fewer digits is presentation-only. */
    static final int DIVISION_SCALE = 12;

    private final BigDecimal value;
    private final String unit;

    private Quantity(BigDecimal value, String unit) {
        this.value = Objects.requireNonNull(value, "value");
        this.unit = requireUnit(unit);
    }

    /** Creates a quantity from an exact decimal string, e.g. {@code Quantity.of("12.5", "kWh")}. */
    public static Quantity of(String value, String unit) {
        Objects.requireNonNull(value, "value");
        return new Quantity(new BigDecimal(value), unit);
    }

    public static Quantity of(BigDecimal value, String unit) {
        return new Quantity(value, unit);
    }

    public static Quantity of(long value, String unit) {
        return new Quantity(BigDecimal.valueOf(value), unit);
    }

    public BigDecimal value() {
        return value;
    }

    public String unit() {
        return unit;
    }

    public Quantity add(Quantity other) {
        return new Quantity(value.add(sameUnit(other).value), unit);
    }

    public Quantity subtract(Quantity other) {
        return new Quantity(value.subtract(sameUnit(other).value), unit);
    }

    /** Multiplies by a dimensionless exact factor (e.g. an emission factor's numeric part). */
    public Quantity multiply(BigDecimal factor) {
        Objects.requireNonNull(factor, "factor");
        return new Quantity(value.multiply(factor), unit);
    }

    /**
     * Divides by a dimensionless exact divisor at the pinned scale/rounding (ADR-0006). This is
     * the only division domain code is allowed to perform.
     */
    public Quantity divide(BigDecimal divisor) {
        Objects.requireNonNull(divisor, "divisor");
        return new Quantity(value.divide(divisor, DIVISION_SCALE, RoundingMode.HALF_EVEN), unit);
    }

    public Quantity negate() {
        return new Quantity(value.negate(), unit);
    }

    public boolean isNegative() {
        return value.signum() < 0;
    }

    public boolean isZero() {
        return value.signum() == 0;
    }

    @Override
    public int compareTo(Quantity other) {
        return value.compareTo(sameUnit(other).value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Quantity other)) {
            return false;
        }
        return unit.equals(other.unit) && value.compareTo(other.value) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value.stripTrailingZeros(), unit);
    }

    @Override
    public String toString() {
        return value.toPlainString() + " " + unit;
    }

    private Quantity sameUnit(Quantity other) {
        Objects.requireNonNull(other, "other");
        if (!unit.equals(other.unit)) {
            throw new IllegalArgumentException(
                    "Unit mismatch: '%s' vs '%s' — convert via the catalogue before combining"
                            .formatted(unit, other.unit));
        }
        return other;
    }

    private static String requireUnit(String unit) {
        Objects.requireNonNull(unit, "unit");
        if (unit.isBlank()) {
            throw new IllegalArgumentException("unit must not be blank");
        }
        return unit;
    }
}
