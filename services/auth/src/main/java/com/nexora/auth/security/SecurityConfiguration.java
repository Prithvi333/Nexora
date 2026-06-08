package com.nexora.auth.security;

import com.nexora.auth.utils.contants.IRole;
import com.nexora.auth.utils.contants.IUrls;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
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
                                    List.of("*")
                            );

                            config.setExposedHeaders(
                                    List.of(
                                            "Authorization",
                                            "X-Refresh-Token"
                                    )
                            );

                            return config;
                        })
                )

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(IUrls.USER + "/login", IUrls.USER + "/signup", IUrls.USER + "/token").permitAll()
                        .requestMatchers(IUrls.ADMIN + "/**").hasRole(IRole.ROLE_ADMIN)
                        .requestMatchers(IUrls.USER + "/**").hasAnyRole(IRole.ROLE_ADMIN, IRole.ROLE_USER)
                        .anyRequest().authenticated()
                ).addFilterBefore(jwtValidator, BasicAuthenticationFilter.class)

                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable);

        return http.build();
    }


    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration
    ) throws Exception {
        return configuration.getAuthenticationManager();
    }
}
