package com.esg360.web.pagination;

/** Clamps a client-requested page size to a safe range. */
public final class PageLimit {

    public static final int DEFAULT = 50;
    public static final int MAX = 200;

    private PageLimit() {}

    public static int clamp(Integer requested) {
        if (requested == null || requested <= 0) {
            return DEFAULT;
        }
        return Math.min(requested, MAX);
    }
}
