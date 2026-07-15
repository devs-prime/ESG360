package com.esg360.web.idempotency;

/**
 * The result of {@link IdempotencyService#begin}: either replay a stored response, or proceed to
 * execute the request for the first time and then call {@code complete(...)}.
 */
public sealed interface IdempotentOutcome permits IdempotentOutcome.Replay, IdempotentOutcome.Proceed {

    /** A previously completed request with the same key and payload — return this response verbatim. */
    record Replay(int status, String body) implements IdempotentOutcome {}

    /** First time this key+payload is seen — execute, then record the response. */
    record Proceed() implements IdempotentOutcome {}
}
