package com.ecommerce.user.security;

import com.ecommerce.user.entity.Token;
import com.ecommerce.user.repository.TokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class LogoutService implements LogoutHandler {

	@Autowired
	private TokenRepository tokenRepository;

	@Override
	public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
		String authorizationHeader = request.getHeader("Authorization");
		if(StringUtils.hasText(authorizationHeader) && authorizationHeader.startsWith("Bearer ")) {
			String bearerToken = authorizationHeader.substring(7);
			Token token = tokenRepository.findByToken(bearerToken).orElse(null);
			if(token == null) return;
			token.setExpired(true);
			token.setRevoked(true);
			tokenRepository.save(token);
		}
	}
	
}
