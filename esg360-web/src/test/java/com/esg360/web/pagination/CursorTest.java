package com.esg360.web.pagination;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

import com.esg360.web.error.ApiException;

class CursorTest {

    @Test
    void roundTripsAnyToken() {
        String token = "organisation_node:2026-07-15T00:00:00Z:018f...";
        assertThat(Cursor.decode(Cursor.encode(token))).isEqualTo(token);
    }

    @Test
    void encodingIsUrlSafeAndUnpadded() {
        String cursor = Cursor.encode("a?b/c+d==e");
        assertThat(cursor).doesNotContain("=").doesNotContain("/").doesNotContain("+");
    }

    @Test
    void rejectsMalformedCursorAsBadRequest() {
        assertThatThrownBy(() -> Cursor.decode("!!! not base64 !!!"))
                .isInstanceOf(ApiException.class)
                .satisfies(e -> assertThat(((ApiException) e).getCode()).isEqualTo("INVALID_CURSOR"));
    }
}
