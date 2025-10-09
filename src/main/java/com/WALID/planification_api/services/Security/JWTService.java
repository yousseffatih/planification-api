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

	public String generateToken(String username, Long userId)
	{
		Map<String, Object> claims = new HashMap<>();
		
		claims.put("userId", userId);   /// For adding USER-ID to JWT
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
	
	///
	/// Get USER-ID form token
	///
	public Long extractUserId(String token) {
	    Claims claims = extractAllClaims(token);
	    Object userIdObj = claims.get("userId");
	    Long userId = null;
	    if (userIdObj instanceof Integer) {
	        userId = ((Integer) userIdObj).longValue();
	    } else if (userIdObj instanceof Long) {
	        userId = (Long) userIdObj;
	    } else if (userIdObj instanceof String) {
	        userId = Long.parseLong((String) userIdObj);
	    }
	    return userId;
	}
	
	///
	/// Get information of USER
	///
	public Map<String, Object> extractUserInfo(String token) {
		 if (token.startsWith("Bearer ")) {
		        token = token.substring(7).trim();
		    }
		Claims claims = extractAllClaims(token);
	    Map<String, Object> userInfo = new HashMap<>();
	    userInfo.put("username", claims.getSubject());
	    userInfo.put("userId", claims.get("userId"));
	    return userInfo;
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
