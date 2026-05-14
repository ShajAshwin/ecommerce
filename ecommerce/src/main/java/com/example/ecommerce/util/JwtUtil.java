package com.example.ecommerce.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

    // Secret key used to sign and verify the token — keep this safe, never expose it
    private final String SECRET_KEY = "myverysecuresecretkeyforjwtauthentication12345";
    // Creates a JWT token for the given username, valid for 10 hours
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)                          // stores username inside token
                .setIssuedAt(new Date())                       // records token creation time
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // expires in 10 hours
                .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()), SignatureAlgorithm.HS256) // signs with secret key to prevent tampering
                .compact();                                    // builds and returns the final token string
    }

    // Reads the username that was stored inside the token
    public String extractUsername(String token) {
        return extractClaims(token).getSubject();              // subject = username we set during generation
    }

    // Returns true only if token belongs to this user AND is not expired
    public boolean validateToken(String token, UserDetails userDetails) {
        return extractUsername(token).equals(userDetails.getUsername()) // username in token matches DB user
                && !isTokenExpired(token);                     // and token hasn't expired yet
    }

    // Checks if token expiry date is in the past
    private boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date()); // true if expired
    }

    // Decodes the token and returns all data stored inside it
    // Throws exception automatically if token is tampered or invalid
    private Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(SECRET_KEY.getBytes())) // uses secret key to verify signature
                .build()
                .parseClaimsJws(token)                         // decodes and verifies the token
                .getBody();                                    // returns the payload (username, expiry, etc)
    }
}