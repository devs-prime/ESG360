package com.esg360.web.error;

import org.springframework.http.HttpStatus;

/**
 * Base for errors that map to a Problem Details response (spec 37, ADR-0014). Carries the HTTP
 * status, a stable machine-readable {@code code}, and whether the client may safely retry.
 */
public class ApiException extends RuntimeException {

    private final transient HttpStatus status;
    private final String code;
    private final boolean retryable;

    public ApiException(HttpStatus status, String code, String message, boolean retryable) {
        super(message);
        this.status = status;
        this.code = code;
        this.retryable = retryable;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getCode() {
        return code;
    }

    public boolean isRetryable() {
        return retryable;
    }
}
