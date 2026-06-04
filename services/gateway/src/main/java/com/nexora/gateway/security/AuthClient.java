package com.nexora.gateway.security;

import com.nexora.gateway.response.TokenValidationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class AuthClient {

    private final WebClient webClient;

    public Mono<TokenValidationResponse> validate(String token) {
        return webClient.post()
                .uri("http://AUTH-SERVICE/internal/validate")
                .header(HttpHeaders.AUTHORIZATION, token)
                .retrieve()
                .bodyToMono(TokenValidationResponse.class);
    }
}