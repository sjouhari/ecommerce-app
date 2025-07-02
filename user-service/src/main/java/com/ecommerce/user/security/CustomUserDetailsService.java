package com.ecommerce.user.security;

import com.ecommerce.user.entity.User;
import com.ecommerce.user.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	private final UserRepository userRepository;
	
	PasswordEncoder passwordEncoder;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user = userRepository.findByEmail(email).orElseThrow(
				() -> new UsernameNotFoundException("User with email: " + email + " not found."));

		Set<GrantedAuthority> authorities = user.getProfils().stream()
				.flatMap(role -> role.getFeatures().stream())
				.map((feature) -> new SimpleGrantedAuthority(feature.getResourceName() + "_" + feature.getAction()))
				.collect(Collectors.toSet());

		return new org.springframework.security.core.userdetails.User(
				user.getEmail(),
				user.getPassword(),
				authorities
				);
	}

}
