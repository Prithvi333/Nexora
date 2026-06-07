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

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenRepository tokenRepository;

    @Value("${JWT_SECRET}")
    private String secretKey;

    public Map<String, Object> generateToken(Users user) {
        if (user == null) {
            throw new TokenException("You should signUp first");
        }
        Key key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        try {
            String username = user.getEmail();
            List<GrantedAuthority> authorities = new ArrayList<>();
            user.getRoles().forEach(roles -> authorities.add(new SimpleGrantedAuthority(roles.getRoleName())));

            String token = Jwts.builder()
                    .setSubject("Access-Token")
                    .claim("username", username)
                    .claim("authorities", convertAuthorityIntoSimpleForm(authorities))
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                    .signWith(key)
                    .compact();
            return Map.of("username", username, "accessToken", token);
        } catch (Exception exception) {
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

    public RefreshTokenResponse validateToken(String refreshToken) {
        RefreshTokens refreshTokens = tokenRepository.findByToken(refreshToken).orElseThrow(RefreshTokenNotFound::new);
        if (refreshTokens.getExpiryDate().isAfter(LocalDateTime.now())) {
            throw new RefreshTokenExpired();
        }
        return new RefreshTokenResponse(generateToken(refreshTokens.getUser()).get("accessToken").toString(), refreshTokens.getExpiryDate().toString());

    }


}
