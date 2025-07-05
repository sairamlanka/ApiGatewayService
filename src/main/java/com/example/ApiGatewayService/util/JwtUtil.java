package com.example.ApiGatewayService.util;

import java.security.Key;
import java.util.Date;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Configuration
public class JwtUtil {
	
	private final String SECRET = "jhbhasdbasbjhsihdbsnkjAHSIDWFEFFEG";
    private final Key secretKey = Keys.hmacShaKeyFor(SECRET.getBytes());
	
	public ResponseEntity<Boolean> validateToken(String token) {
        try {	      
            String username = extractUserName(token);
            boolean isValid = validateToken(token, username);
            return ResponseEntity.ok(isValid);
        } catch (Exception e) {
        	e.printStackTrace();
            return ResponseEntity.ok(false);
        }
    }
	
    public String extractUserName(String token) {
        return extractClaims(token).getSubject();
    }

    public Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean validateToken(String token, String username) {
        final String extractedUsername = extractUserName(token);
        return (extractedUsername.equals(username) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }
}
