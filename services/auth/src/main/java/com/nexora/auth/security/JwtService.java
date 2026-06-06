package com.nexora.auth.security;

import com.nexora.auth.exception.token.TokenException;
import com.nexora.auth.response.token.TokenValidationResponse;
import com.nexora.auth.token.model.RefreshTokens;
import com.nexora.auth.user.model.Users;
import com.nexora.auth.user.repository.UserRepository;
import com.nexora.auth.user.service.UserService;
import com.nexora.auth.utils.contants.ITokenConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.util.ArrayUtils;

import java.security.Key;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class JwtService {

    @Autowired
    private UserRepository userRepository;

    public Map<String, Object> generateToken(Authentication authentication) {
        if (authentication == null) {
            throw new TokenException("You should signUp first");
        }
        Key key = Keys.hmacShaKeyFor(ITokenConstants.SECURE_KEY.getBytes());
        try {
            String username = authentication.getName();
            Collection<? extends GrantedAuthority> authorityList = authentication.getAuthorities();

            String token = Jwts.builder()
                    .setSubject(username)
                    .claim("authority", convertAuthorityIntoSimpleForm(authorityList))
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

    public TokenValidationResponse validateToken(String token) {
        Key key = Keys.hmacShaKeyFor(ITokenConstants.SECURE_KEY.getBytes());
        token = token.substring(7);
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            String username = claims.getSubject();
            String authority = (String) claims.get("authority");

            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(username, null, AuthorityUtils.commaSeparatedStringToAuthorityList(authority)));
            return TokenValidationResponse.builder().valid(true).username(username).roles(authority).build();
        } catch (ExpiredJwtException e) {
            Claims claims = e.getClaims();
            String username = claims.getSubject();
            String authority = (String) claims.get("authority");

            Optional<Users> users = userRepository.findByEmailAndEnabledTrue(username);
            Optional<RefreshTokens> tokens = users.get().getRefreshTokens().stream().sorted((a, b) -> b.getExpiryDate().compareTo(a.getExpiryDate())).findFirst();
            if (tokens.isEmpty() || tokens.get().getExpiryDate().isBefore(LocalDateTime.now())) {
                throw new TokenException("Please sign in again");
            }
            Map<String, Object> generatedToken = generateToken(new UsernamePasswordAuthenticationToken(username, null, AuthorityUtils.commaSeparatedStringToAuthorityList(authority)));
            return TokenValidationResponse.builder().valid(true).username(generatedToken.get("username").toString()).newToken(generatedToken.get("accessToken").toString()).build();
        } catch (Exception ex) {
            throw new TokenException("Taken validation failed");
        }
    }

}
