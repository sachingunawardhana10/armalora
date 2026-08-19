package com.armalora.user.security;

import com.armalora.user.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtProvider {

    private final SecretKey secretKey;
    private final long expiration;

    public JwtProvider(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expiration}") long expiration
    ) {

        this.secretKey = Keys.hmacShaKeyFor(
                secret.getBytes(StandardCharsets.UTF_8)
        );

        this.expiration = expiration;
    }

    public String generateToken(User user) {

        Date now = new Date();

        Date expiryDate = new Date(
                now.getTime() + expiration
        );

        return Jwts.builder()
                .subject(user.getEmail())
                .claim("userId", user.getId())
                .claim("role", user.getRole().name())
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(secretKey)
                .compact();
    }

    public String getEmailFromToken(String token) {

        Claims claims = parseClaims(token);

        return claims.getSubject();
    }

    public Long getUserIdFromToken(String token) {

        Claims claims = parseClaims(token);

        Object userId = claims.get("userId");

        if (userId instanceof Number number) {
            return number.longValue();
        }

        return Long.valueOf(userId.toString());
    }

    public String getRoleFromToken(String token) {

        Claims claims = parseClaims(token);

        return claims.get("role", String.class);
    }

    public boolean validateToken(String token) {

        try {

            parseClaims(token);

            return true;

        } catch (Exception exception) {

            return false;
        }
    }

    private Claims parseClaims(String token) {

        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}