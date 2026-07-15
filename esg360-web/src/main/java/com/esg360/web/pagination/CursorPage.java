package com.esg360.web.pagination;

import java.util.List;

/**
 * The list-response envelope for every collection endpoint (spec 37, ADR-0014). Pagination is
 * cursor-based and forward-only; {@code nextCursor} is an opaque token (see {@link Cursor}) and is
 * {@code null} on the last page. Offset pagination is banned.
 *
 * @param items the page of results
 * @param nextCursor opaque cursor for the next page, or {@code null} if there is none
 * @param limit the page size that was applied
 */
public record CursorPage<T>(List<T> items, String nextCursor, int limit) {

    public CursorPage {
        items = items == null ? List.of() : List.copyOf(items);
    }

    public boolean hasMore() {
        return nextCursor != null;
    }
}
