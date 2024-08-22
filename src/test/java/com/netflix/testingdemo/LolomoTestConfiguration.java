package com.netflix.testingdemo;

import com.netflix.testingdemo.lolomo.ai.OpenAiService;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@TestConfiguration
public class LolomoTestConfiguration {
    @Bean
    OpenAiService openAiService() {
        var mock = mock(OpenAiService.class);
        when(mock.top10("USA")).thenReturn(List.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L));
        return mock;
    }
}
