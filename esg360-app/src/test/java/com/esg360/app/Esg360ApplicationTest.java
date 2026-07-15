package com.esg360.app;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/** Fast unit-level smoke: the Spring context assembles without a database. */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class Esg360ApplicationTest {

    @Test
    void contextLoads() {
        // failure here means bean wiring is broken; the assertion is the successful startup
    }
}
