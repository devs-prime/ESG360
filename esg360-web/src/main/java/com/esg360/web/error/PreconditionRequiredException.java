package com.esg360.web.error;

import org.springframework.http.HttpStatus;

/** 428 — a mutating request must carry an If-Match header (optimistic concurrency). */
public class PreconditionRequiredException extends ApiException {

    public PreconditionRequiredException(String message) {
        super(HttpStatus.PRECONDITION_REQUIRED, "PRECONDITION_REQUIRED", message, false);
    }
}
