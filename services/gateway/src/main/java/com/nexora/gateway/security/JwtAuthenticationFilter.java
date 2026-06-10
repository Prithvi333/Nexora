package com.nexora.gateway.security;

import com.nexora.gateway.utils.IConstants;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Set;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter implements WebFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Autowired
    private JwtValidationService jwtValidationService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {

        String path = exchange.getRequest()
                .getURI()
                .getPath();

        log.debug("Incoming request path: {}", path);

        boolean isPublic = IConstants.allowedUrls.stream()
                .anyMatch(path::startsWith);
        if (isPublic) {
            log.debug("Path '{}' is public, skipping JWT validation", path);
            return chain.filter(exchange);
        }

        String authHeader = exchange.getRequest()
                .getHeaders()
                .getFirst(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.warn("Missing or malformed Authorization header for path '{}'", path);
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        String token = authHeader.substring(7);

        try {
            jwtValidationService.validateToken(token);

            String correlationId = UUID.randomUUID().toString().substring(0, 6);
            log.info("JWT validated successfully for path '{}', assigned correlationId: {}", path, correlationId);

            ServerHttpRequest mutatedRequest = exchange.getRequest()
                    .mutate()
                    .header("X-Correlation-Id", correlationId)
                    .build();
            return chain.filter(
                    exchange.mutate()
                            .request(mutatedRequest)
                            .build()
            );

        } catch (Exception e) {
            log.error("JWT validation failed for path '{}': {}", path, e.getMessage());
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
    }
}