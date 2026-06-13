package com.nexora.orders.security;
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
    private static final Logger logger = LoggerFactory.getLogger(JwtValidator.class);

    @Value("${JWT_SECRET}")
    private String secretKey;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        logger.info("Entering doFilterInternal for request URI: {}", request.getRequestURI());

        String token = request.getHeader("Authorization").substring(7);
        try {
            logger.info("Validating JWT token");
            Claims claims = Jwts.parserBuilder().setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8))).build().parseClaimsJws(token).getBody();
            logger.info("JWT token validated successfully");
            setAuthenticationContext(claims);
            filterChain.doFilter(request, response);
        } catch (JwtException exception) {
            logger.error("JWT validation failed: {}", exception.getMessage());
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
        }
    }

    private void setAuthenticationContext(Claims claims) {
        logger.info("Setting authentication context");
        String username = (String) claims.get("username");
        String roles = (String) claims.get("roles");
        String userUid = claims.getSubject();
        UserPrinciple userPrinciple = new UserPrinciple(userUid, username, roles);
        List<GrantedAuthority> authorityList = generateAuthentication(roles);
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(userPrinciple, null, authorityList));
        logger.info("Authentication context set successfully for user: {}", username);
    }

    private List<GrantedAuthority> generateAuthentication(String authorities) {
        logger.info("Generating authorities from roles");
        String[] roles = authorities.split(",");

        List<GrantedAuthority> authorityList = new ArrayList<>();
        for (String role : roles) {
            authorityList.add(new SimpleGrantedAuthority(role));
        }
        logger.info("Generated {} authorities", authorityList.size());
        return authorityList;
    }

}