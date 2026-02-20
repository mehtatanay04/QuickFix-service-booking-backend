package com.tanay.bookingapp.security;

import java.io.IOException;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component 
public class JwtAuthFilter extends OncePerRequestFilter {

private final JwtUtil jwtUtil;

public JwtAuthFilter(JwtUtil jwtUtil) {
this.jwtUtil = jwtUtil;
}

@Override
protected void doFilterInternal(
HttpServletRequest request,
HttpServletResponse response,
FilterChain filterChain
) throws ServletException, IOException {

String path = request.getRequestURI();

if(path.equals("/api/auth/login") || path.equals("/api/auth/register")) {
filterChain.doFilter(request, response);
return;
}

String authHeader = request.getHeader("Authorization");

if(authHeader == null || !authHeader.startsWith("Bearer ")) {
response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
response.setContentType("application/json");
response.getWriter().write("{\"error\":\"Missing or invalid Authorization header\"}");
return;
}

String token = authHeader.substring(7);

Claims claims = jwtUtil.extractClaims(token);

if(claims == null) {
response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
response.setContentType("application/json");
response.getWriter().write("{\"error\":\"Invalid or expired token\"}");
return;
}

request.setAttribute("userId", claims.get("id"));
request.setAttribute("role", claims.get("role"));

filterChain.doFilter(request, response);
}

}