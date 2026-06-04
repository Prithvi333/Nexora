package com.nexora.auth.security;

import com.nexora.auth.exception.token.TokenException;
import com.nexora.auth.response.token.TokenValidationResponse;
import com.nexora.auth.utils.contants.ITokenConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.util.ArrayUtils;

import java.security.Key;
import java.util.*;

@Service
public class JwtService {


    public Map<String, Object> generateToken(Authentication authentication) {
        if (authentication == null) {
            throw new TokenException("You should signUp first");
        }
        Key key = Keys.hmacShaKeyFor(ITokenConstants.SECURE_KEY.getBytes());
        try {
            String username = authentication.getName();
            Collection<? extends GrantedAuthority> authorityList = authentication.getAuthorities();

            String token = Jwts.builder().setSubject("token")
                    .setClaims(Map.of("username", username))
                    .setClaims(Map.of("authority", convertAuthorityIntoSimpleForm(authorityList)))
                    .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60)).setIssuedAt(new Date())
                    .signWith(key).compact();
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
            Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
            String username = (String) claims.get("username");
            String authority = (String) claims.get("authority");

            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(username, null, AuthorityUtils.commaSeparatedStringToAuthorityList(authority)));
            return TokenValidationResponse.builder().valid(true).username(username).roles(authority).build();
        } catch (Exception ex) {
            throw new TokenException("Taken validation failed");
        }
    }

}
