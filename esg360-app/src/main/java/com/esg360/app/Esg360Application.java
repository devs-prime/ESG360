package com.esg360.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * ESG360 — the single deployable of the modular monolith (ADR-0004).
 *
 * <p>Feature modules contribute their Spring configuration via {@code com.esg360} component
 * scanning as they are added (PROGRESS.md items 0.4+). Per ADR-0018's junior-trap mitigations:
 * constructor injection only, and explicit configuration over autoconfiguration in the
 * calculation and audit paths.
 */
@SpringBootApplication(scanBasePackages = "com.esg360")
public class Esg360Application {

    public static void main(String[] args) {
        SpringApplication.run(Esg360Application.class, args);
    }
}
