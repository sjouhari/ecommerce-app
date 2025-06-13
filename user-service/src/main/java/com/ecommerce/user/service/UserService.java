package com.ecommerce.user.service;

import com.ecommerce.user.dto.MessageResponseDto;
import com.ecommerce.user.dto.ResetPasswordRequestDto;
import com.ecommerce.user.dto.UpdateUserDto;
import com.ecommerce.user.dto.UserDto;

import java.util.List;

public interface UserService {

    List<UserDto> getAllUsers();

    UserDto getUserById(Long id);

    UserDto createUser(UserDto userDto);

    UserDto updateUser(Long id, UpdateUserDto userDto);

    void deleteUser(Long id);

    boolean existsById(Long id);

    String verifyUserEmail(int verificationCode);

    MessageResponseDto forgotPassword(String email);

    void updatePassword(Long id, ResetPasswordRequestDto resetPasswordRequestDto);

    String getUserFullName(Long id);

    UserDto subscribeToNewsletter(Long id, String email);

}
