package com.esg360.web.json;

import java.io.IOException;
import java.math.BigDecimal;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;

import com.esg360.sharedkernel.quantity.Quantity;

/**
 * JSON rules for exact numbers (ADR-0006, ADR-0014): every {@link BigDecimal} and every {@link
 * Quantity} serialises as a string so no consumer can parse an ESG value into a binary float.
 *
 * <ul>
 *   <li>{@code BigDecimal} → its plain-string form, e.g. {@code "1234.567"}.
 *   <li>{@code Quantity} → {@code {"value":"1234.567","unit":"kWh"}}.
 * </ul>
 */
public final class QuantityJsonModule extends SimpleModule {

    public QuantityJsonModule() {
        super("esg360-quantity");
        addSerializer(BigDecimal.class, new BigDecimalAsStringSerializer());
        addSerializer(Quantity.class, new QuantitySerializer());
        addDeserializer(Quantity.class, new QuantityDeserializer());
    }

    static final class BigDecimalAsStringSerializer extends JsonSerializer<BigDecimal> {
        @Override
        public void serialize(BigDecimal value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeString(value.toPlainString());
        }
    }

    static final class QuantitySerializer extends JsonSerializer<Quantity> {
        @Override
        public void serialize(Quantity value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeStartObject();
            gen.writeStringField("value", value.value().toPlainString());
            gen.writeStringField("unit", value.unit());
            gen.writeEndObject();
        }
    }

    static final class QuantityDeserializer extends JsonDeserializer<Quantity> {
        @Override
        public Quantity deserialize(JsonParser parser, DeserializationContext context) throws IOException {
            JsonNode node = parser.readValueAsTree();
            JsonNode value = node.get("value");
            JsonNode unit = node.get("unit");
            if (value == null || unit == null) {
                throw new IOException("Quantity requires 'value' and 'unit'");
            }
            return Quantity.of(value.asText(), unit.asText());
        }
    }
}
