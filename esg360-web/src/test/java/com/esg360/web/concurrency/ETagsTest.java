package com.esg360.web.concurrency;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

import com.esg360.web.error.PreconditionFailedException;
import com.esg360.web.error.PreconditionRequiredException;

class ETagsTest {

    @Test
    void etagIsTheQuotedRowVersion() {
        assertThat(ETags.forRowVersion(7)).isEqualTo("\"7\"");
    }

    @Test
    void missingIfMatchIsPreconditionRequired() {
        assertThatThrownBy(() -> ETags.requireMatch(null, 1)).isInstanceOf(PreconditionRequiredException.class);
        assertThatThrownBy(() -> ETags.requireMatch("  ", 1)).isInstanceOf(PreconditionRequiredException.class);
    }

    @Test
    void staleIfMatchIsPreconditionFailed() {
        assertThatThrownBy(() -> ETags.requireMatch("\"1\"", 2)).isInstanceOf(PreconditionFailedException.class);
    }

    @Test
    void matchingIfMatchPasses() {
        assertThatCode(() -> ETags.requireMatch("\"2\"", 2)).doesNotThrowAnyException();
    }

    @Test
    void wildcardMatchesAnyVersion() {
        assertThatCode(() -> ETags.requireMatch("*", 99)).doesNotThrowAnyException();
    }
}
