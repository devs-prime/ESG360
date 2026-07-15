package com.esg360.web.error;

/** One field-level validation error in a Problem Details {@code fieldErrors} array. */
public record ApiFieldError(String field, String message) {}
