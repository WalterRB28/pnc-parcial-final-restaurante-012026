package com.uca.pncparcialfinalrestaurante.security;

import com.uca.pncparcialfinalrestaurante.entity.enums.Role;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Getter
    @Value("${jwt.access-token-expiration-ms}")
    private long accessTokenExpirationMs;

    private Key key;

    @PostConstruct
    public void init() {
        key = Keys.hmacShaKeyFor(secret.getBytes(java.nio.charset.StandardCharsets.UTF_8));
    }

    public String generateAccessToken(String username, Long userId, Set<Role> roles, Integer tokenVersion) {
        long now = System.currentTimeMillis();
        Date issuedAt = new Date(now);
        Date expiry = new Date(now + accessTokenExpirationMs);

        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", roles.stream().map(Role::name).collect(Collectors.toList()));
        claims.put("userId", userId);
        claims.put("tokenVersion", tokenVersion);  // NUEVO

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(issuedAt)
                .setExpiration(expiry)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public Jws<Claims> parseClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
    }

    public boolean validateAccessToken(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException ex) {
            return false;
        }
    }

    public String getUsernameFromAccessToken(String token) {
        return parseClaims(token).getBody().getSubject();
    }

    public Long getUserIdFromAccessToken(String token) {
        return parseClaims(token).getBody().get("userId", Long.class);
    }

    public Integer getTokenVersionFromAccessToken(String token) {
        return parseClaims(token).getBody().get("tokenVersion", Integer.class);
    }

    @SuppressWarnings("unchecked")
    public List<String> getRolesFromAccessToken(String token) {
        Object roles = parseClaims(token).getBody().get("roles");
        if (roles instanceof List) {
            return (List<String>) roles;
        }
        return Collections.emptyList();
    }

    public String generateRefreshTokenString() {
        return UUID.randomUUID().toString() + "-" + UUID.randomUUID().toString();
    }

}