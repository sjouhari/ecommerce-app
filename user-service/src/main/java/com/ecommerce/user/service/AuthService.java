package com.ecommerce.user.service;

import com.ecommerce.user.dto.JWTAuthResponse;
import com.ecommerce.user.dto.LoginDTO;
import com.ecommerce.user.dto.RegisterDto;
import com.ecommerce.user.dto.UserDto;

public interface AuthService {
	JWTAuthResponse login(LoginDTO loginDTO);
	UserDto register(RegisterDto registerDTO);
}
