package com.esg360.app.api;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.esg360.api.MetaApi;
import com.esg360.api.model.ApiMeta;

/**
 * Implements the generated {@link MetaApi} contract (ADR-0014). Serves {@code GET /api/v1/meta};
 * the {@code /api/v1} prefix is applied by {@code ApiPathPrefixConfig}, and implementing the
 * generated interface means this controller cannot drift from the published OpenAPI document.
 */
@RestController
public class MetaController implements MetaApi {

    private final String applicationName;
    private final String buildVersion;

    public MetaController(
            @Value("${spring.application.name:esg360}") String applicationName,
            @Value("${esg360.build.version:0.1.0-SNAPSHOT}") String buildVersion) {
        this.applicationName = applicationName;
        this.buildVersion = buildVersion;
    }

    @Override
    public ResponseEntity<ApiMeta> getApiMeta() {
        return ResponseEntity.ok(new ApiMeta(applicationName, buildVersion, "v1"));
    }
}
