package com.nexora.auth.security;

import com.nexora.auth.exception.token.RefreshTokenExpired;
import com.nexora.auth.exception.token.RefreshTokenNotFound;
import com.nexora.auth.exception.token.TokenException;
import com.nexora.auth.response.token.RefreshTokenResponse;
import com.nexora.auth.token.model.RefreshTokens;
import com.nexora.auth.token.repository.TokenRepository;
import com.nexora.auth.user.model.Users;
import com.nexora.auth.user.repository.UserRepository;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class JwtService {

    private static final Logger log = LoggerFactory.getLogger(JwtService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenRepository tokenRepository;

    @Value("${JWT_SECRET}")
    private String secretKey;

    public Map<String, Object> generateToken(Users user) {
        if (user == null) {
            log.warn("Token generation failed - user is null");
            throw new TokenException("You should signUp first");
        }
        log.debug("Generating JWT token for userUid: {}", user.getUid());
        Key key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        try {
            String username = user.getEmail();
            List<GrantedAuthority> authorities = new ArrayList<>();
            user.getRoles().forEach(roles -> authorities.add(new SimpleGrantedAuthority(roles.getRoleName())));

            String token = Jwts.builder()
                    .setSubject(user.getUid())
                    .claim("username", username)
                    .claim("roles", convertAuthorityIntoSimpleForm(authorities))
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                    .signWith(key)
                    .compact();
            log.info("JWT token generated successfully for userUid: {}", user.getUid());
            return Map.of("username", username, "accessToken", token);
        } catch (Exception exception) {
            log.error("Token generation failed for userUid: {} - {}", user.getUid(), exception.getMessage());
            exception.printStackTrace();
            throw new TokenException("Token generation exception");
        }
    }

    private String convertAuthorityIntoSimpleForm(Collection<? extends GrantedAuthority> authorities) {
        Set<String> set = new HashSet<>();
        for (GrantedAuthority authority : authorities) {
            set.add(authority.getAuthority());
        }
        return String.join(",", set);
    }

    @Transactional
    public RefreshTokenResponse validateToken(String refreshToken) {
        log.debug("Validating refresh token");
        RefreshTokens refreshTokens = tokenRepository.findByToken(refreshToken).orElseThrow(() -> {
            log.warn("Refresh token not found");
            return new RefreshTokenNotFound();
        });
        if (refreshTokens.getExpiryDate().isBefore(LocalDateTime.now())) {
            log.warn("Refresh token expired for userUid: {}, expired at: {}", refreshTokens.getUser().getUid(), refreshTokens.getExpiryDate());
            throw new RefreshTokenExpired();
        }
        log.info("Refresh token valid for userUid: {}, generating new access token", refreshTokens.getUser().getUid());
        return new RefreshTokenResponse(generateToken(refreshTokens.getUser()).get("accessToken").toString(), refreshTokens.getExpiryDate().toString());
    }
}