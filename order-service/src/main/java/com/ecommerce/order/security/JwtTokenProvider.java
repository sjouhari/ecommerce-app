package com.ecommerce.order.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class JwtTokenProvider {
	
	@Value("${app.jwt-secret}")
	private String jwtSecret;
	
	@Value("${app.jwt-expiration-milliseconds}")
	private Long jwtExpirationDate;
	
	private Key key() {
		return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
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

	// Get list of roles from JWT token
	public Set<String> getRoles(String token) {
		Object rolesObject = Jwts.parser()
				.verifyWith((SecretKey) key())
				.build()
				.parseSignedClaims(token)
				.getPayload()
				.get("roles"); // Retrieve raw object

		// Safe type casting
		if (rolesObject instanceof List<?> rawList) {
            Set<String> rolesSet = new HashSet<>();

			for (Object role : rawList) {
				if (role instanceof String) {
					rolesSet.add((String) role);
				}
			}
			return rolesSet;
		}

		return new HashSet<>(); // Return empty set if roles are missing or malformed
	}

}
