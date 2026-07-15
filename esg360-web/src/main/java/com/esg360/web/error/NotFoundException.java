package com.esg360.web.error;

import org.springframework.http.HttpStatus;

/** 404 — a resource does not exist (within the caller's tenant). */
public class NotFoundException extends ApiException {

    public NotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, "RESOURCE_NOT_FOUND", message, false);
    }
}
