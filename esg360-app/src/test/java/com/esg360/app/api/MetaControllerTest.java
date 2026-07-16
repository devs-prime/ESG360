package com.esg360.app.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.esg360.modules.identity.security.SecurityConfig;
import com.esg360.web.ApiPathPrefixConfig;

/**
 * The proof endpoint is served under /api/v1 and matches the generated contract.
 *
 * <p>Imports the real {@link SecurityConfig} rather than disabling filters: /api/v1/meta being
 * reachable without a token is a deliberate rule in that config, and a test that bypassed security
 * would still pass if someone later closed the endpoint — or, worse, if someone opened every other
 * one. The {@link JwtDecoder} is mocked only because this slice has no IdP to fetch keys from; no
 * request here presents a token.
 */
@WebMvcTest(controllers = MetaController.class)
@Import({ApiPathPrefixConfig.class, SecurityConfig.class})
class MetaControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private JwtDecoder jwtDecoder;

    @Test
    void servesMetadataUnderApiV1() throws Exception {
        mvc.perform(get("/api/v1/meta"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.apiVersion").value("v1"))
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.version").exists());
    }
}
