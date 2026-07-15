package com.esg360.sharedkernel.quantity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class QuantityTest {

    @Nested
    class Arithmetic {

        @Test
        void addsExactly() {
            var a = Quantity.of("0.1", "tCO2e");
            var b = Quantity.of("0.2", "tCO2e");
            // the canonical binary-float failure case: 0.1 + 0.2 must be exactly 0.3
            assertEquals(Quantity.of("0.3", "tCO2e"), a.add(b));
        }

        @Test
        void multipliesExactly() {
            var activity = Quantity.of("1234.567", "kWh");
            var result = activity.multiply(new BigDecimal("0.000233"));
            assertEquals(Quantity.of("0.287654111", "kWh"), result);
        }

        @Test
        void divisionIsPinnedToScale12HalfEven() {
            var q = Quantity.of("1", "t");
            var result = q.divide(new BigDecimal("3"));
            assertEquals(new BigDecimal("0.333333333333"), result.value());
            assertEquals(12, result.value().scale());
        }

        @Test
        void divisionRoundsHalfEven() {
            // 0.0000000000025 / 1 at scale 12 → tie; HALF_EVEN rounds to even digit 2
            var q = Quantity.of("0.0000000000025", "t");
            assertEquals(
                    new BigDecimal("0.000000000002"), q.divide(BigDecimal.ONE).value());
            // 0.0000000000035 → tie; rounds up to even digit 4
            var r = Quantity.of("0.0000000000035", "t");
            assertEquals(
                    new BigDecimal("0.000000000004"), r.divide(BigDecimal.ONE).value());
        }

        @Test
        void divisionOfNonTerminatingDecimalDoesNotThrow() {
            // raw BigDecimal.divide would throw ArithmeticException here — Quantity must not
            var q = Quantity.of("10", "MWh");
            assertEquals(
                    new BigDecimal("3.333333333333"),
                    q.divide(new BigDecimal("3")).value());
        }
    }

    @Nested
    class UnitSafety {

        @Test
        void rejectsUnitMismatchOnAdd() {
            var kwh = Quantity.of("1", "kWh");
            var litres = Quantity.of("1", "L");
            var ex = assertThrows(IllegalArgumentException.class, () -> kwh.add(litres));
            assertTrue(ex.getMessage().contains("kWh"));
            assertTrue(ex.getMessage().contains("L"));
        }

        @Test
        void rejectsUnitMismatchOnCompare() {
            var kwh = Quantity.of("1", "kWh");
            var litres = Quantity.of("1", "L");
            assertThrows(IllegalArgumentException.class, () -> kwh.compareTo(litres));
        }

        @Test
        void rejectsBlankUnit() {
            assertThrows(IllegalArgumentException.class, () -> Quantity.of("1", " "));
        }
    }

    @Nested
    class Equality {

        @Test
        void equalityIsScaleInsensitive() {
            assertEquals(Quantity.of("1.0", "kg"), Quantity.of("1.00", "kg"));
            assertEquals(
                    Quantity.of("1.0", "kg").hashCode(),
                    Quantity.of("1.00", "kg").hashCode());
        }

        @Test
        void differentUnitsAreNeverEqual() {
            assertNotEquals(Quantity.of("1", "kg"), Quantity.of("1", "t"));
        }
    }
}
