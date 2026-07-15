package com.esg360.web.json;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import com.esg360.sharedkernel.quantity.Quantity;

class QuantityJsonModuleTest {

    private final ObjectMapper mapper = new ObjectMapper()
            .registerModule(new QuantityJsonModule())
            .enable(SerializationFeature.WRITE_BIGDECIMAL_AS_PLAIN);

    @Test
    void bigDecimalSerialisesAsAString() throws Exception {
        String json = mapper.writeValueAsString(new Holder(new BigDecimal("1234.567")));
        assertThat(json).isEqualTo("{\"amount\":\"1234.567\"}");
    }

    @Test
    void quantitySerialisesAsObjectWithStringValue() throws Exception {
        assertThat(mapper.writeValueAsString(Quantity.of("12.50", "kWh")))
                .isEqualTo("{\"value\":\"12.50\",\"unit\":\"kWh\"}");
    }

    @Test
    void quantityRoundTrips() throws Exception {
        Quantity quantity = Quantity.of("0.333333333333", "tCO2e");
        Quantity back = mapper.readValue(mapper.writeValueAsString(quantity), Quantity.class);
        assertThat(back).isEqualTo(quantity);
    }

    record Holder(BigDecimal amount) {}
}
