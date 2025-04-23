package ru.practicum.stats_client.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient webClient(WebClient.Builder builder,
                               @Value("${stats-server.url:http://localhost:9090}") String baseUrl) {
        return builder
                .baseUrl(baseUrl)
                .build();
    }
}