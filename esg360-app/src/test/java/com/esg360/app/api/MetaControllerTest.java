package com.esg360.app.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import com.esg360.web.ApiPathPrefixConfig;

/** The proof endpoint is served under /api/v1 and matches the generated contract. */
@WebMvcTest(controllers = MetaController.class)
@Import(ApiPathPrefixConfig.class)
class MetaControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    void servesMetadataUnderApiV1() throws Exception {
        mvc.perform(get("/api/v1/meta"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.apiVersion").value("v1"))
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.version").exists());
    }
}
