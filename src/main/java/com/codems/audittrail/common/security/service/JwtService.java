package com.codems.audittrail.common.security.service;

import com.codems.audittrail.common.security.model.SecurityUser;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;

import com.codems.audittrail.config.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtProperties jwtProperties;
    private  SecretKey secretKey;

    @PostConstruct
    public void init(){
        secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtProperties.secret()));
    }

    public String generateToken(SecurityUser user) {
        Instant now = Instant.now();
        Duration expiration = jwtProperties.expiration() == null ? Duration.ofHours(1) : jwtProperties.expiration();

        return Jwts.builder()
                .subject(user.getUsername())
                .claim("userId", user.id())
                .claim("role", resolveRole(user))
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plus(expiration)))
                .signWith(secretKey)
                .compact();
    }

    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    public boolean isTokenValid(String token, SecurityUser user) {
        return user.getUsername().equals(extractUsername(token)) && extractClaims(token).getExpiration().after(new Date());
    }

    private Claims extractClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private String resolveRole(SecurityUser user) {
        return user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElse("ROLE_USER");
    }
}
