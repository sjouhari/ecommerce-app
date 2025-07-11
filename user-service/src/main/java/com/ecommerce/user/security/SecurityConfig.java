package com.ecommerce.user.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
	private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint, JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

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

		httpSecurity.authorizeHttpRequests(auth -> auth
						.requestMatchers(
								"/api/auth/login",
								"/api/auth/register",
								"/users/verify",
								"/api/users/forgot-password",
								"/users/reset-password").permitAll()

						.requestMatchers(HttpMethod.GET, "/api/auth/current-user").authenticated()

						.requestMatchers(HttpMethod.GET, "/api/stores/**").permitAll()

						.requestMatchers(HttpMethod.GET, "/api/profils/**").hasAuthority("PROFIL_READ")
						.requestMatchers(HttpMethod.POST, "/api/profils/**").hasAuthority("PROFIL_CREATE")
						.requestMatchers(HttpMethod.PUT, "/api/profils/**").hasAuthority("PROFIL_UPDATE")
						.requestMatchers(HttpMethod.DELETE, "/api/profils/**").hasAuthority("PROFIL_DELETE")

						.requestMatchers(HttpMethod.GET, "/api/features/**").hasAuthority("FEATURE_READ")
						.requestMatchers(HttpMethod.POST, "/api/features/**").hasAuthority("FEATURE_CREATE")
						.requestMatchers(HttpMethod.PUT, "/api/features/**").hasAuthority("FEATURE_UPDATE")
						.requestMatchers(HttpMethod.DELETE, "/api/features/**").hasAuthority("FEATURE_DELETE")

						.requestMatchers(HttpMethod.GET, "/api/users/**").hasAuthority("USER_READ")
						.requestMatchers(HttpMethod.POST, "/api/users/**").hasAuthority("USER_CREATE")
						.requestMatchers(HttpMethod.DELETE, "/api/users/**").hasAuthority("USER_DELETE")
						.anyRequest().authenticated()
				).exceptionHandling(exception -> exception
						.authenticationEntryPoint(jwtAuthenticationEntryPoint)
				).sessionManagement(session -> session
						.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				).logout(logout -> logout
						.logoutUrl("/api/auth/logout")
						.logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext())
				);

		httpSecurity.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
		httpSecurity.csrf(AbstractHttpConfigurer::disable);
		return httpSecurity.build();
	}

}
