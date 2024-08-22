package com.netflix.testingdemo;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.intellij.lang.annotations.Language;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
public class SmokeTestWithRequest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    public void top10() throws Exception {
        @Language("GraphQL")
        var query = """                
                {
                    top10(countryCode: "NLD") {
                        title
                        categories
                    }
                }
                """;

        mockMvc.perform(post("/graphql")
                        .secure(true)
                        .content(objectMapper.writeValueAsBytes(new GraphqlRequest(query)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("data.top10").isArray())
                .andExpect(jsonPath("data.top10[0].title").exists());
    }

    record GraphqlRequest(String query){}
}
