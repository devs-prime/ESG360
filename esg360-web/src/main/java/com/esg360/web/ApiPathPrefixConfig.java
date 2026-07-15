package com.esg360.web;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerTypePredicate;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Puts every ESG360 REST controller under {@code /api/v1} (spec 37) without each controller
 * repeating the prefix, and without moving the actuator endpoints (which are not under
 * {@code com.esg360}).
 */
@Configuration
public class ApiPathPrefixConfig implements WebMvcConfigurer {

    public static final String API_V1 = "/api/v1";

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        configurer.addPathPrefix(API_V1, HandlerTypePredicate.forBasePackage("com.esg360"));
    }
}
