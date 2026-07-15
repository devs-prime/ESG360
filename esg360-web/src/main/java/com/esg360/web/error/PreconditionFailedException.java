package com.esg360.web.error;

import org.springframework.http.HttpStatus;

/** 412 — an If-Match precondition did not match the current row_version (optimistic concurrency). */
public class PreconditionFailedException extends ApiException {

    public PreconditionFailedException(String message) {
        super(HttpStatus.PRECONDITION_FAILED, "PRECONDITION_FAILED", message, false);
    }
}
