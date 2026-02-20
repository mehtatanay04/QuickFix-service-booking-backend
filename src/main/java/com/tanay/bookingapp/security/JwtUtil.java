package com.tanay.bookingapp.security;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

private final SecretKey key;

public JwtUtil(@Value("${jwt.secret}") String secret) {
this.key = Keys.hmacShaKeyFor(secret.getBytes());
}

public String generateToken(Long userId, String email, String role) {

return Jwts.builder()
.setSubject(email)
.claim("id", userId)
.claim("role", role)
.setIssuedAt(new Date())
.setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 1 day
.signWith(key)
.compact();
}

public Claims extractClaims(String token) {

return Jwts.parserBuilder()
.setSigningKey(key)
.build()
.parseClaimsJws(token)
.getBody();
}
}