package com.example.auth.backend.security;

import com.example.auth.backend.entity.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;

@Service
public class JwtService {
    private final SecretKey key;
    private final long expirationSeconds;
    public JwtService(@Value("${jwt.secret}") String secret,
                      @Value("${jwt.expiration-seconds}") long expirationSeconds) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expirationSeconds = expirationSeconds;
    }
    public String generateToken(User user) {
        Instant now=Instant.now();
        return Jwts.builder().subject(user.getUsername())
            .claim("role", user.getRole().name())
            .issuedAt(Date.from(now))
            .expiration(Date.from(now.plusSeconds(expirationSeconds)))
            .signWith(key).compact();
    }
    public Claims extractClaims(String token) {
        return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
    }
    public String extractUsername(String token){ return extractClaims(token).getSubject(); }
    public boolean isValid(String token) {
        try { return extractClaims(token).getExpiration().after(new Date()); }
        catch (JwtException|IllegalArgumentException ex) { return false; }
    }
    public long getExpirationSeconds(){return expirationSeconds;}
}
