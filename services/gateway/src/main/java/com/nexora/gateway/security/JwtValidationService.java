package com.nexora.gateway.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Service
public class JwtValidationService {

    private static final Logger log = LoggerFactory.getLogger(JwtValidationService.class);

    @Value("${JWT_SECRET}")
    private String secretKey;

    public Claims validateToken(String token) {
        log.debug("Attempting to validate JWT token");
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)))
                .build()
                .parseClaimsJws(token)
                .getBody();
        log.info("JWT token validated successfully for subject: {}", claims.getSubject());
        return claims;
    }

}