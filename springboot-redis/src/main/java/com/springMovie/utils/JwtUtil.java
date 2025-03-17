package com.springMovie.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {
    private static final String SECRET_KEY = "mySecretKeymySecretKeymySecretKeymySecretKey"; // 必须至少 32 字节
    private static final long EXPIRATION_TIME = 1000 * 60 * 60; // 1小时

    // 生成 Key
    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // 生成 JWT Token
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // 1小时过期
                .signWith(getSigningKey(), SignatureAlgorithm.HS256) // 0.11.x 以上版本需要传 Key
                .compact();
    }

    // 获取用户名
    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    // 从 JWT 中提取 Claims
    private Claims extractClaims(String token) {
        return Jwts.parserBuilder() // 0.11.x 及以上版本使用 parserBuilder()
                .setSigningKey(getSigningKey()) // 需要传入 Key
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // 验证 JWT Token 是否过期
    public boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }

    // 验证 JWT Token 是否有效
    public boolean validateToken(String token, String username) {
        return (username.equals(extractUsername(token)) && !isTokenExpired(token));
    }
}
