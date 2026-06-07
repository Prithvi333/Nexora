package com.nexora.user.security;

import com.nexora.user.utility.constants.IRole;
import com.nexora.user.utility.constants.IUrls;
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
                                    List.of("*")
                            );

                            return config;
                        })
                )

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(IUrls.ADMIN + "/**").hasRole(IRole.ROLE_ADMIN)
                        .requestMatchers(IUrls.USER + "/**").hasAnyRole(IRole.ROLE_ADMIN, IRole.ROLE_USER)
                        .anyRequest().authenticated()
                )
                .addFilterBefore(new JwtValidator(), BasicAuthenticationFilter.class)
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable);

        return http.build();
    }

}