package com.esg360.web.pagination;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.springframework.http.HttpStatus;

import com.esg360.web.error.ApiException;

/**
 * Opaque, stable pagination cursors (ADR-0014): a cursor is the URL-safe Base64 encoding of a
 * server-defined token (typically the last row's sort key). Clients treat it as opaque; the server
 * owns its meaning, so the encoding can change without breaking the contract.
 */
public final class Cursor {

    private Cursor() {}

    public static String encode(String token) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(token.getBytes(StandardCharsets.UTF_8));
    }

    public static String decode(String cursor) {
        try {
            return new String(Base64.getUrlDecoder().decode(cursor), StandardCharsets.UTF_8);
        } catch (IllegalArgumentException ex) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "INVALID_CURSOR", "Malformed pagination cursor", false);
        }
    }
}
