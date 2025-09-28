package com.WALID.planification_api.services.Security;

import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;

@Service
public class JWTService{

	 private String secretkey = "";

	public JWTService() {

        try {
            KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");
            SecretKey sk = keyGen.generateKey();
            secretkey = Base64.getEncoder().encodeToString(sk.getEncoded());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

	public String generateRefreshToken(Map<String, Object> extractClaims,String username) {
		// TODO Auto-generated method stub
		return Jwts.builder()
                .claims()
                .add(extractClaims)
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 604800000))
                .and()
                .signWith(getSignKey())
                .compact();
	}

	public String generateToken(String username)
	{
		Map<String, Object> claims = new HashMap<>();
		return Jwts.builder()
                .claims()
                .add(claims)
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 86400000)) //86400000
                .and()
                .signWith(getSignKey())
                .compact();
	}
	
	public Long extractUserId(String token) {
        try {
            Claims claims = extractAllClaims(token);
            
            // Try different claim names that might contain the user ID
            Object userIdClaim = claims.get("userId");
            if (userIdClaim == null) {
                userIdClaim = claims.get("id");
            }
            if (userIdClaim == null) {
                userIdClaim = claims.get("sub"); // subject claim
            }
            
            if (userIdClaim == null) {
                throw new RuntimeException("User ID not found in token claims");
            }
            
            // Convert to Long
            if (userIdClaim instanceof Number) {
                return ((Number) userIdClaim).longValue();
            } else if (userIdClaim instanceof String) {
                return Long.parseLong((String) userIdClaim);
            } else {
                throw new RuntimeException("Invalid user ID format in token");
            }
            
        } catch (Exception e) {
            System.err.println("Error extracting user ID from token: " + e.getMessage());
            throw new RuntimeException("Failed to extract user ID from token", e);
        }
    }

	private SecretKey getSignKey() {
		byte[] keyBytes = Decoders.BASE64.decode(secretkey);
        return Keys.hmacShaKeyFor(keyBytes);
	}


	public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

	private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

	private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        final String userName = extractUserName(token);
        return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }


}
