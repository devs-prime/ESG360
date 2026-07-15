package com.esg360.web.concurrency;

import com.esg360.web.error.PreconditionFailedException;
import com.esg360.web.error.PreconditionRequiredException;

/**
 * ETag / If-Match optimistic concurrency (spec 37, ADR-0014), backed by each row's {@code
 * row_version} (spec 39). The ETag is the quoted row_version; a mutating request must echo it in
 * If-Match or be rejected — 428 if absent, 412 if stale.
 */
public final class ETags {

    private ETags() {}

    public static String forRowVersion(long rowVersion) {
        return "\"" + rowVersion + "\"";
    }

    /** Enforces the If-Match precondition for a mutating request against the current row_version. */
    public static void requireMatch(String ifMatchHeader, long currentRowVersion) {
        if (ifMatchHeader == null || ifMatchHeader.isBlank()) {
            throw new PreconditionRequiredException("If-Match header is required for this request");
        }
        String candidate = ifMatchHeader.trim();
        if ("*".equals(candidate)) {
            return;
        }
        if (!candidate.equals(forRowVersion(currentRowVersion))) {
            throw new PreconditionFailedException("Resource was modified; If-Match precondition failed");
        }
    }
}
