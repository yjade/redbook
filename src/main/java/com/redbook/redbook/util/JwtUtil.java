package com.redbook.redbook.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expire}")
    private Long expire;

    public String generateToken(Long userId) {
        return Jwts.builder()
                .setSubject(userId.toString())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expire * 1000))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    public Long getUserIdFromToken(String token) {
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        SecretKey secretKey = Keys.hmacShaKeyFor(keyBytes);

        Claims claims = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return Long.valueOf(claims.getSubject());
    }

    public boolean validateToken(String token) {
        try {
            byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
            SecretKey secretKey = Keys.hmacShaKeyFor(keyBytes);

            Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
