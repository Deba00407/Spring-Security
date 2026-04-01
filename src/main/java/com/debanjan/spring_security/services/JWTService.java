package com.debanjan.spring_security.services;

import com.debanjan.spring_security.dtos.DecodedUser;
import com.debanjan.spring_security.entities.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;


@Service
public class JWTService {

    @Value("${jwt.secret}")
    private String secretKey;

    @PostConstruct
    void validateSecret(){ // early fail in case secret key is not loaded in context
        if(secretKey == null || secretKey.isBlank()){
            throw new RuntimeException("Failed to load secret key string in JWT Service.");
        }
    }

    // function to generate a secret key from the string
    private SecretKey generateKey(){
        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    public String generateJWT(User user){
        return Jwts.builder()
                .subject(user.getId().toString())
                .claim("fullname", user.getFullName())
                .claim("email", user.getEmail())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 60*60*1000)) // valid for 1hr
                .signWith(generateKey())
                .compact();
    }

    public DecodedUser getUserDetails(String token){
        Claims claims = Jwts.parser()
                .verifyWith(generateKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

       return DecodedUser.builder()
               .id(Long.parseLong(claims.getSubject()))
               .fullName(claims.get("fullname").toString())
               .email(claims.get("email").toString())
               .build();
    }
}
