package com.nexora.auth.security;

import com.nexora.auth.utils.contants.IConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.tomcat.util.http.parser.Authorization;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Component
public class JwtValidator extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtValidator.class);

    @Value("${JWT_SECRET}")
    private String secretKey;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String path = request.getServletPath();
        log.debug("JwtValidator processing request for path: {}", path);
        String token = request.getHeader("Authorization").substring(7);
        try {
            Claims claims = Jwts.parserBuilder().setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8))).build().parseClaimsJws(token).getBody();
            log.debug("JWT parsed successfully for path: {}", path);
            setAuthenticationContext(claims);
            filterChain.doFilter(request, response);
        } catch (JwtException exception) {
            log.warn("JWT validation failed for path: {} - {}", path, exception.getMessage());
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
        }
    }

    private void setAuthenticationContext(Claims claims) {
        String username = (String) claims.get("username");
        String roles = (String) claims.get("roles");
        String userUid = claims.getSubject();
        log.debug("Setting authentication context for userUid: {}, username: {}", userUid, username);
        UserPrinciple userPrinciple = new UserPrinciple(userUid, username, roles);
        List<GrantedAuthority> authorityList = generateAuthentication(roles);
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(userPrinciple, null, authorityList));
        log.info("Authentication context set for userUid: {} with {} role(s)", userUid, authorityList.size());
    }

    private List<GrantedAuthority> generateAuthentication(String authorities) {
        String[] roles = authorities.split(",");
        List<GrantedAuthority> authorityList = new ArrayList<>();
        for (String role : roles) {
            authorityList.add(new SimpleGrantedAuthority(role));
        }
        return authorityList;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        boolean shouldSkip = IConstants.allowedUrls.stream().anyMatch(request.getServletPath()::startsWith);
        if (shouldSkip) {
            log.debug("Skipping JWT filter for public path: {}", request.getServletPath());
        }
        return shouldSkip;
    }
}