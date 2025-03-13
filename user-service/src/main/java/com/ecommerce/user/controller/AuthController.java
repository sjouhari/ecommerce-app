package com.ecommerce.user.controller;

import com.ecommerce.user.dto.JWTAuthResponse;
import com.ecommerce.user.dto.LoginDTO;
import com.ecommerce.user.dto.RegisterDto;
import com.ecommerce.user.dto.UserDto;
import com.ecommerce.user.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@Validated
public class AuthController {
	
	@Autowired
	private AuthService authService;
	
	@PostMapping("/login")
	public ResponseEntity<JWTAuthResponse> login(@RequestBody @Valid LoginDTO loginDTO) {
		return ResponseEntity.ok(authService.login(loginDTO));
	}
	
	@PostMapping("/register")
	public ResponseEntity<UserDto> register(@RequestBody @Valid RegisterDto registerDTO) {
		return new ResponseEntity<>(authService.register(registerDTO), HttpStatus.CREATED);
	}

}
