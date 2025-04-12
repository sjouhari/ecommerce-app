package com.ecommerce.user.service;

import com.ecommerce.user.dto.UserDto;

import java.util.List;

public interface UserService {

    List<UserDto> getAllUsers();

    UserDto getUserById(Long id);

    UserDto createUser(UserDto userDto);

    UserDto updateUser(Long id, UserDto userDto);

    String deleteUser(Long id);

    boolean existsById(Long id);

    String verifyUserEmail(int verificationCode);

}
