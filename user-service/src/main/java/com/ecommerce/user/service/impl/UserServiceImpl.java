package com.ecommerce.user.service.impl;

import com.ecommerce.shared.dto.UserEvent;
import com.ecommerce.shared.exception.ResourceNotFoundException;
import com.ecommerce.user.dto.ResetPasswordRequestDto;
import com.ecommerce.user.dto.UserDto;
import com.ecommerce.user.entity.User;
import com.ecommerce.user.exception.EmailVerificationException;
import com.ecommerce.user.kafka.KafkaUserProducer;
import com.ecommerce.user.mapper.UserMapper;
import com.ecommerce.user.repository.UserRepository;
import com.ecommerce.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private KafkaUserProducer kafkaUserProducer;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        return UserMapper.INSTANCE.userToUserDtos(users);
    }

    @Override
    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("User", "id", id.toString())
        );
        return UserMapper.INSTANCE.userToUserDto(user);
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        User user = UserMapper.INSTANCE.userDtoToUser(userDto);
        user.setPassword("password");
        User savedUser = userRepository.save(user);
        return UserMapper.INSTANCE.userToUserDto(savedUser);
    }

    @Override
    public UserDto updateUser(Long id, UserDto userDto) {
        getUserById(id);
        User user = UserMapper.INSTANCE.userDtoToUser(userDto);
        user.setId(id);
        User savedUser = userRepository.save(user);
        return UserMapper.INSTANCE.userToUserDto(savedUser);
    }

    @Override
    public String deleteUser(Long id) {
        getUserById(id);
        userRepository.deleteById(id);
        return "User deleted successfully";
    }

    @Override
    public boolean existsById(Long id) {
        return userRepository.existsById(id);
    }

    @Override
    public String verifyUserEmail(int verificationCode) {
        User user = userRepository.findByVerificationCode(verificationCode).orElseThrow(
                () -> new EmailVerificationException("Email not verified. The given code was expired or incorrect.")
        );
        user.setEnabled(true);
        user.setVerificationCode(0);
        userRepository.save(user);


        UserEvent userEvent = new UserEvent(user.getFirstName() + " " + user.getLastName(), user.getEmail(), 0);
        kafkaUserProducer.sendMessage(userEvent, "user-confirmed");
        return "Your email verified successfully";
    }

    @Override
    public void forgotPassword(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new ResourceNotFoundException("User", "email", email)
        );

        Random randomCode = new Random();
        int verificationCode = randomCode.nextInt(900000) + 100000;
        UserEvent userEvent = new UserEvent(user.getFirstName() + " " + user.getLastName(), user.getEmail(), verificationCode);
        kafkaUserProducer.sendMessage(userEvent, "forgot_password");
    }

    @Override
    public void resetPassword(Long id, ResetPasswordRequestDto resetPasswordRequestDto) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("User", "id", id.toString())
        );

        if(!passwordEncoder.matches(resetPasswordRequestDto.getOldPassword(), user.getPassword())) {
            throw new RuntimeException("Old password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(resetPasswordRequestDto.getNewPassword()));
        userRepository.save(user);

        UserEvent userEvent = new UserEvent(user.getFirstName() + " " + user.getLastName(), user.getEmail(), 0);
        kafkaUserProducer.sendMessage(userEvent, "reset_password");

    }

}
