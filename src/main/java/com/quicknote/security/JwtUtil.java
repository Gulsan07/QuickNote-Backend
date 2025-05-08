package com.quicknote.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtUtil {

    private static final String SECRET_KEY = "3G5nT0B+MWtD8A+vW5MzQ4OepCdfgt45q6L5XjLhfh9sY3L4M="; // At least 32 characters long!

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
//    private static SecretKey generateSecretKey(String secret) {
//        byte[] decodedKey = Base64.getDecoder().decode(secret);
//        return new SecretKeySpec(decodedKey, "HmacSHA256");
//    }
//
//    private static final SecretKey SECRET_KEY = generateSecretKey("3G5nT0B+MWtD8A+vW5MzQ4OepCq6L5XjLhfh9sY3L4M=");



    // Generating JWT Token
    public String generateToken(String email){
        System.out.println("generating token");
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 *60 * 60)) //1 hours
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // Extract username (email) from token
    public String extractUsername(String token) {
        System.out.println("Extrecting username");
        return extractClaim(token, Claims::getSubject);
    }

    // Extract expiration date from token
    public Date extractExpiration(String token) {
        System.out.println("Cheaking Expiration");
        return extractClaim(token, Claims::getExpiration);
    }

    // Validate token
    public boolean validateToken(String token, String username) {
        System.out.println("Cheaking validateToken");

        try{
            extractUsername(token);
            return !isTokenExpired(token);
        }catch (Exception e){
            return false;
        }
    }

    // Helper methods
    private boolean isTokenExpired(String token) {
        System.out.println("Cheaking isTokenExpired");
        return extractExpiration(token).before(new Date());
    }
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver){
        System.out.println("Extrecting extractClaim");
        Claims claims= Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claimsResolver.apply(claims);
    }
}
