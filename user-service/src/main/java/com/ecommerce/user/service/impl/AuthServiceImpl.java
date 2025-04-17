package com.ecommerce.user.service.impl;

import com.ecommerce.shared.dto.UserEvent;
import com.ecommerce.shared.exception.ResourceNotFoundException;
import com.ecommerce.user.dto.*;
import com.ecommerce.user.entity.Profil;
import com.ecommerce.user.entity.User;
import com.ecommerce.user.kafka.KafkaUserProducer;
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
import java.util.Random;
import java.util.Set;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {
	private AuthenticationManager authenticationManager;
	private PasswordEncoder passwordEncoder;
	private UserRepository userRepository;
	private ProfilRepository profilRepository;
	private JwtTokenProvider jwtTokenProvider;
	private KafkaUserProducer kafkaUserConfirmationProducer;

	@Override
	public JWTAuthResponse login(LoginDto loginDTO) {
		try {
			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(
							loginDTO.getEmail(),
							loginDTO.getPassword()
					));
			
			User currentUser = userRepository.findByEmail(loginDTO.getEmail()).orElseThrow(
					() -> new ResourceNotFoundException("User", "email", loginDTO.getEmail())
			);

			if(!currentUser.isEnabled()) {
				throw new RuntimeException("Your account is not verified. Please check your email.");
			}
			
			String generatedToken = jwtTokenProvider.generateToken(authentication);
			
			SecurityContextHolder.getContext().setAuthentication(authentication);

            return new JWTAuthResponse(generatedToken, UserMapper.INSTANCE.userToUserDto(currentUser));
		} catch (BadCredentialsException badCredentialsException) {
			throw new RuntimeException("Username or password incorrect.");
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
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

		Random randomCode = new Random();
		int verificationCode = randomCode.nextInt(900000) + 100000;
		UserEvent userEvent = new UserEvent(user.getFirstName() + " " + user.getLastName(), user.getEmail(), verificationCode);
		kafkaUserConfirmationProducer.sendMessage(userEvent, "user-confirmation");

		user.setEnabled(false);
		user.setVerificationCode(verificationCode);
		return UserMapper.INSTANCE.userToUserDto(userRepository.save(user));
	}

}
