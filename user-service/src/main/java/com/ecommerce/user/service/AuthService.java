package com.ecommerce.user.service;

import com.ecommerce.user.dto.*;

public interface AuthService {

	JWTAuthResponse login(LoginDto loginDTO);

	MessageResponseDto register(RegisterDto registerDTO);

	CurrentUserDto getCurrentUser();

}
