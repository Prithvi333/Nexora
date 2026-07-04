package com.nexora.gateway.config;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Configuration
public class RedisConfiguration {

    @Bean
    @Primary
    public KeyResolver ipKeyResolver() {
        return exchange ->
                Mono.just(
                        Objects.requireNonNull(
                                exchange.getRequest()
                                        .getRemoteAddress()
                        ).getAddress().getHostAddress()
                );
    }

    @Bean
    public KeyResolver userKeyResolver() {
        return exchange -> {

            String userId =
                    exchange.getRequest()
                            .getHeaders()
                            .getFirst("X-User-Id");

            return Mono.justOrEmpty(userId)
                    .defaultIfEmpty("anonymous");
        };
    }
}
