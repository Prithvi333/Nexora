package com.nexora.orders.security;

import com.nexora.orders.utility.constants.IRoles;
import com.nexora.orders.utility.constants.IUrls;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

@Configuration
public class SecurityConfiguration {

    @Autowired
    private JwtValidator jwtValidator;

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http
    ) throws Exception {

        http
                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )

                .cors(cors ->
                        cors.configurationSource(request -> {

                            CorsConfiguration config =
                                    new CorsConfiguration();
                            config.setAllowCredentials(true);
                            config.setAllowedOrigins(List.of("*"));

                            config.setAllowedMethods(
                                    List.of("*")
                            );

                            config.setAllowedHeaders(
                                    List.of("Authorization")
                            );

                            return config;
                        })
                )

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(IUrls.USER + "/**").hasAnyRole(IRoles.ROLE_ADMIN, IRoles.ROLE_USER)
                        .requestMatchers(IUrls.ADMIN + "/**").hasRole(IRoles.ROLE_ADMIN)
                        .anyRequest().authenticated()
                ).addFilterBefore(jwtValidator, BasicAuthenticationFilter.class)

                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable);

        return http.build();
    }

}
