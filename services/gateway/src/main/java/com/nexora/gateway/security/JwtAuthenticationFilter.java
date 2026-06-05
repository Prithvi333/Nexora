package com.nexora.gateway.security;

import com.nexora.gateway.utils.IConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter implements WebFilter {

    @Autowired
    private AuthClient authClient;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String path = exchange.getRequest().getPath().toString();
        if (path.startsWith(IConstants.LOGIN_URI)) {
            System.out.println("Running...");
            return chain.filter(exchange);
        }

        String token = exchange.getRequest()
                .getHeaders()
                .getFirst(HttpHeaders.AUTHORIZATION);


        if (token == null || !token.startsWith("Bearer ")) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        return authClient.validate(token)
                .flatMap(response -> {

                    if (!response.valid()) {
                        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                        return exchange.getResponse().setComplete();
                    }

                    String roles = response.roles();

                    ServerHttpRequest mutatedRequest = exchange.getRequest()
                            .mutate()
                            .header("X-USERNAME", response.userName())
                            .header("X-ROLES", roles)
                            .build();

                    return chain.filter(exchange.mutate()
                            .request(mutatedRequest)
                            .build());
                });
    }
}

