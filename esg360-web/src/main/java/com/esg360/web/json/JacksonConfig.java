package com.esg360.web.json;

import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.SerializationFeature;

/** Registers the ESG360 JSON conventions on Spring Boot's ObjectMapper. */
@Configuration
public class JacksonConfig {

    @Bean
    Module esg360QuantityJsonModule() {
        return new QuantityJsonModule();
    }

    @Bean
    Jackson2ObjectMapperBuilderCustomizer esg360JsonCustomizer() {
        // Plain (non-scientific) decimals, and never let numbers become doubles on read.
        return builder -> builder.featuresToEnable(SerializationFeature.WRITE_BIGDECIMAL_AS_PLAIN);
    }
}
