package com.nexora.gateway.security;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class WebClientConfig {
    @LoadBalanced
    @Bean
    public WebClient webClient() {
        return WebClient.builder().build();
    }
}
