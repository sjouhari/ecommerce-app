package com.ecommerce.user.service.impl;

import com.ecommerce.shared.dto.UserEvent;
import com.ecommerce.shared.exception.ResourceNotFoundException;
import com.ecommerce.user.dto.*;
import com.ecommerce.user.entity.Profil;
import com.ecommerce.user.entity.User;
import com.ecommerce.user.exception.EmailAlreadyExistsException;
import com.ecommerce.user.exception.LoginFailedException;
import com.ecommerce.user.kafka.KafkaUserProducer;
import com.ecommerce.user.mapper.UserMapper;
import com.ecommerce.user.repository.ProfilRepository;
import com.ecommerce.user.repository.UserRepository;
import com.ecommerce.user.security.JwtTokenProvider;
import com.ecommerce.user.service.AuthService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class AuthServiceImpl implements AuthService {

	private final AuthenticationManager authenticationManager;
	private final PasswordEncoder passwordEncoder;
	private final UserRepository userRepository;
	private final ProfilRepository profilRepository;
	private final JwtTokenProvider jwtTokenProvider;
	private final KafkaUserProducer kafkaUserConfirmationProducer;

	@Value("${kafka.topic.user.confirmation.name}")
	private String userConfirmationTopicName;

	private final Random random = new Random();

    public AuthServiceImpl(AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder, UserRepository userRepository, ProfilRepository profilRepository, JwtTokenProvider jwtTokenProvider, KafkaUserProducer kafkaUserConfirmationProducer) {
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.profilRepository = profilRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.kafkaUserConfirmationProducer = kafkaUserConfirmationProducer;
    }

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

			if(!currentUser.isVerified()) {
				throw new LoginFailedException("Votre compte n'a pas encore été verifié. Veuillez verifier votre boite mail.");
			}

			if(!currentUser.isEnabled()) {
				throw new LoginFailedException("Votre compte a été désactivé. Veuillez contactez l'administrateur.");
			}

			CurrentUserDto currentUserDto = UserMapper.INSTANCE.userToCurrentUserDto(currentUser);
			
			String generatedToken = jwtTokenProvider.generateToken(authentication, currentUserDto);
			
			SecurityContextHolder.getContext().setAuthentication(authentication);

            return new JWTAuthResponse(generatedToken);
		} catch (BadCredentialsException badCredentialsException) {
			throw new LoginFailedException("Adresse email ou mot de passe incorrect");
		} catch (Exception e) {
			throw new LoginFailedException(e.getMessage());
		}
	}

	@Override
	public MessageResponseDto register(RegisterDto registerDTO) {
		if(userRepository.existsByEmail(registerDTO.getEmail())) {
			throw new EmailAlreadyExistsException("Adresse email deja utilisé par un autre utilisateur.");
		}

		List<Profil> profils = new ArrayList<>();

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

		int verificationCode = random.nextInt(900000) + 100000;
		UserEvent userEvent = new UserEvent(user.getFirstName() + " " + user.getLastName(), user.getEmail(), verificationCode);
		kafkaUserConfirmationProducer.sendMessage(userEvent, userConfirmationTopicName);

		user.setEnabled(false);
		user.setVerified(false);
		user.setVerificationCode(verificationCode);
		user.setVerificationCodeExpireAt(LocalDateTime.now().plusHours(1));
        userRepository.save(user);
		return new MessageResponseDto("Votre compte a été créé avec success. Veuillez verifier votre boite mail pour confirmer votre compte.");
	}

	@Override
	public CurrentUserDto getCurrentUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication == null || !authentication.isAuthenticated()) {
			return null;
		}

		Object principal = authentication.getPrincipal();
		if (principal instanceof UserDetails userDetails) {
			String userEmail = userDetails.getUsername();

			User user = userRepository.findByEmail(userEmail).orElseThrow(
					() -> new ResourceNotFoundException("User", "email", userEmail)
			);
			return UserMapper.INSTANCE.userToCurrentUserDto(user);
		}

		return null;
	}

}
