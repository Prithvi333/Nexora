package com.nexora.user.security;

import com.nexora.user.utility.constants.IUrls;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
        System.out.println(request.getServletPath());
        if (request.getServletPath().startsWith(IUrls.INTERNAL)) {
            filterChain.doFilter(request, response);
            return;
        }
        log.debug("Intercepting request to validate JWT token for URI: {}", request.getRequestURI());
        String token = request.getHeader("Authorization").substring(7);

        try {
            Claims claims = Jwts.parserBuilder().setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8))).build().parseClaimsJws(token).getBody();
            setAuthenticationContext(claims);
            log.info("JWT authentication context successfully set for user subject: {}", claims.getSubject());
            filterChain.doFilter(request, response);
        } catch (JwtException exception) {
            log.error("JWT validation failed for incoming request. Error: {}", exception.getMessage());
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
        }
    }

    private void setAuthenticationContext(Claims claims) {
        String username = (String) claims.get("username");
        String roles = (String) claims.get("roles");
        String userUid = claims.getSubject();
        log.debug("Extracting claims - Username: {}, Roles: {}, UserUID: {}", username, roles, userUid);
        UserPrinciple userPrinciple = new UserPrinciple(userUid, username, roles);
        List<GrantedAuthority> authorityList = generateAuthentication(roles);
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(userPrinciple, null, authorityList));
    }

    private List<GrantedAuthority> generateAuthentication(String authorities) {
        String[] roles = authorities.split(",");

        List<GrantedAuthority> authorityList = new ArrayList<>();
        for (String role : roles) {
            log.trace("Mapping authority role: {}", role);
            authorityList.add(new SimpleGrantedAuthority(role));
        }
        return authorityList;
    }
}