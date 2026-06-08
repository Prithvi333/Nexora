package com.nexora.orders.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JwtValidator extends OncePerRequestFilter {

    @Value("${JWT_SECRET}")
    private String secretKey;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {


        String token = response.getHeader("Authorization").substring(7);
        try {
            Claims claims = Jwts.parserBuilder().setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes())).build().parseClaimsJws(token).getBody();
            setAuthenticationContext(claims);
            filterChain.doFilter(request, response);
        } catch (JwtException exception) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
        }
    }

    private void setAuthenticationContext(Claims claims) {
        String username = (String) claims.get("username");
        String authorities = (String) claims.get("authorities");
        List<GrantedAuthority> authorityList = generateAuthentication(authorities);
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(username, null, authorityList));
    }

    private List<GrantedAuthority> generateAuthentication(String authorities) {
        String[] roles = authorities.split(",");

        List<GrantedAuthority> authorityList = new ArrayList<>();
        for (String role : roles) {
            authorityList.add(new SimpleGrantedAuthority(role));
        }
        return authorityList;
    }
}
