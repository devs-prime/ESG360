package com.esg360.web.error;

import org.springframework.http.HttpStatus;

/** 409 — e.g. an Idempotency-Key reused with a different payload (COL-002). */
public class ConflictException extends ApiException {

    public ConflictException(String message) {
        super(HttpStatus.CONFLICT, "CONFLICT", message, false);
    }
}
