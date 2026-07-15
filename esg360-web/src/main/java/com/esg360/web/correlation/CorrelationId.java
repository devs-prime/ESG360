package com.esg360.web.correlation;

/** Correlation-ID constants shared by the filter, logging (MDC) and the error handler. */
public final class CorrelationId {

    public static final String HEADER = "X-Correlation-Id";
    public static final String MDC_KEY = "correlationId";

    private CorrelationId() {}
}
