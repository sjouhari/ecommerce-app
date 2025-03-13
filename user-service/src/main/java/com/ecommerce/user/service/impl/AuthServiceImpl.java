package com.ecommerce.user.service.impl;

import com.ecommerce.user.dto.JWTAuthResponse;
import com.ecommerce.user.dto.LoginDTO;
import com.ecommerce.user.dto.RegisterDto;
import com.ecommerce.user.dto.UserDto;
import com.ecommerce.user.entity.Profil;
import com.ecommerce.user.entity.User;
import com.ecommerce.user.exception.ResourceNotFoundException;
import com.ecommerce.user.mapper.UserMapper;
import com.ecommerce.user.repository.ProfilRepository;
import com.ecommerce.user.repository.UserRepository;
import com.ecommerce.user.security.JwtTokenProvider;
import com.ecommerce.user.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {
	private AuthenticationManager authenticationManager;
	private PasswordEncoder passwordEncoder;
	private UserRepository userRepository;
	private ProfilRepository profilRepository;
	private JwtTokenProvider jwtTokenProvider;

	@Override
	public JWTAuthResponse login(LoginDTO loginDTO) {
		try {
			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(
							loginDTO.getEmail(),
							loginDTO.getPassword()
					));
			
			User currentUser = userRepository.findByEmail(loginDTO.getEmail()).orElseThrow(
					() -> new ResourceNotFoundException("User", "email", loginDTO.getEmail())
			);
			
			String generatedToken = jwtTokenProvider.generateToken(authentication);
			
			SecurityContextHolder.getContext().setAuthentication(authentication);

            return new JWTAuthResponse(generatedToken, UserMapper.INSTANCE.userToUserDto(currentUser));
		} catch (BadCredentialsException badCredentialsException) {
			throw new RuntimeException("Username or password incorrect.");
		} catch (Exception e) {
			throw new RuntimeException("Something went wrong, try again later." + e.getMessage());
		}
	}

	@Override
	public UserDto register(RegisterDto registerDTO) {
		if(userRepository.existsByEmail(registerDTO.getEmail())) {
			throw new RuntimeException("Email already used by another account");
		}

		Set<Profil> profils = new HashSet<>();

		profils.add(profilRepository.findByName("ROLE_USER").orElseThrow(
				() -> new ResourceNotFoundException("Profil", "name", "ROLE_USER")
		));

		if(registerDTO.isSeller()) {
			profils.add(profilRepository.findByName("ROLE_SELLER").orElseThrow(
					() -> new ResourceNotFoundException("Profil", "name", "ROLE_SELLER")
			));
		}

		User user = UserMapper.INSTANCE.registerDtoToUser(registerDTO);
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		user.setProfils(profils);

		return UserMapper.INSTANCE.userToUserDto(userRepository.save(user));
	}

}
