package com.ecommerce.product.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Autowired
	private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

	@Autowired
	private JwtAuthenticationFilter jwtAuthenticationFilter;

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
		return configuration.getAuthenticationManager();
	}

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

		httpSecurity.cors(cors -> cors.configurationSource(request -> {
			CorsConfiguration corsConfiguration = new CorsConfiguration();
			corsConfiguration.setAllowedOrigins(List.of("*"));
			corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "OPTIONS", "DELETE"));
			corsConfiguration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
			return corsConfiguration;
		}));

		httpSecurity.authorizeHttpRequests(auth -> auth
						.requestMatchers(HttpMethod.GET, "/api/products", "/api/products/**").hasAuthority("PRODUCT_READ")
						.requestMatchers(HttpMethod.POST, "/api/products").hasAuthority("PRODUCT_CREATE")
						.requestMatchers(HttpMethod.PUT, "/api/products/**").hasAuthority("PRODUCT_UPDATE")
						.requestMatchers(HttpMethod.DELETE, "/api/products/**").hasAuthority("PRODUCT_DELETE")
						.requestMatchers(HttpMethod.GET, "/api/categories", "/api/categories/**").hasAuthority("CATEGORY_READ")
						.requestMatchers(HttpMethod.POST, "/api/categories").hasAuthority("CATEGORY_CREATE")
						.requestMatchers(HttpMethod.PUT, "/api/categories/**").hasAuthority("CATEGORY_UPDATE")
						.requestMatchers(HttpMethod.DELETE, "/api/categories/**").hasAuthority("CATEGORY_DELETE")
						.anyRequest().authenticated()
				).exceptionHandling(exception -> exception
						.authenticationEntryPoint(jwtAuthenticationEntryPoint)
				).sessionManagement(session -> session
						.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				);

		httpSecurity.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
		httpSecurity.csrf(AbstractHttpConfigurer::disable);
		return httpSecurity.build();
	}

}
