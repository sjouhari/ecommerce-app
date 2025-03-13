package com.ecommerce.user.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {
	
	@Value("${app.jwt-secret}")
	private String jwtSecret;
	
	@Value("${app.jwt-expiration-milliseconds}")
	private Long jwtExpirationDate;
	
	private Key key() {
		return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
	}
	
	// Generate JWT Token
	public String generateToken(Authentication authentication) {
		String username = authentication.getName();
		
		Date currentDate = new Date();
		Date expirationDate = new Date(currentDate.getTime() + jwtExpirationDate);

        return Jwts.builder()
				.subject(username)
				.issuedAt(currentDate)
				.expiration(expirationDate)
				.signWith(key())
				.compact();
	}
	
	// Get username from JWT token
	public String getUsername(String token) {
		return Jwts.parser()
				.verifyWith((SecretKey) key())
				.build()
				.parseSignedClaims(token)
				.getPayload()
				.getSubject();
	}
	
	// Validate JWT Token
	public boolean validateToken(String token) {
		try {
			Jwts.parser().verifyWith((SecretKey) key()).build().parse(token);
			return true;
		} catch (MalformedJwtException malformedJwtException) {
			System.out.println("Invalid JWT token.");
		} catch (ExpiredJwtException expiredJwtException) {
			System.out.println("Expired JWT token.");
		} catch (UnsupportedJwtException unsupportedJwtException) {
			System.out.println("Unsupported JWT token.");
		} catch (IllegalArgumentException illegalArgumentException) {
			System.out.println("JWT claims string is null or empty.");
		}
		return false;
	}
}
