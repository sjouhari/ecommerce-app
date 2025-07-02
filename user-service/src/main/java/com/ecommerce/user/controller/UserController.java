package com.ecommerce.user.controller;

import com.ecommerce.user.dto.MessageResponseDto;
import com.ecommerce.user.dto.ResetPasswordRequestDto;
import com.ecommerce.user.dto.UpdateUserDto;
import com.ecommerce.user.dto.UserDto;
import com.ecommerce.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@Validated
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<UserDto> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody @Valid UserDto userDto) {
        return new ResponseEntity<>(userService.createUser(userDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id, @RequestBody @Valid UpdateUserDto userDto) {
        return ResponseEntity.ok(userService.updateUser(id, userDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/existsById/{id}")
    public boolean existsById(@PathVariable Long id) {
        return userService.existsById(id);
    }

    @GetMapping("/verify")
    public String verifyUserEmail(@RequestParam("code") int verificationCode) {
        return userService.verifyUserEmail(verificationCode);
    }

    @GetMapping("/forgot-password")
    public ResponseEntity<MessageResponseDto> forgotPassword(@RequestParam("email") String email) {
        return ResponseEntity.ok(userService.forgotPassword(email));
    }

    @PutMapping("/reset-password/{id}")
    public void updatePassword(@PathVariable Long id, @RequestBody @Valid ResetPasswordRequestDto resetPasswordRequestDto) {
        userService.updatePassword(id, resetPasswordRequestDto);
    }

    @GetMapping("/full-name/{id}")
    public String getUserFullName(@PathVariable Long id) {
        return userService.getUserFullName(id);
    }

    @PutMapping("/subscribe/{id}")
    public UserDto subscribeToNewsletter(@PathVariable Long id, @RequestParam("email") String email) {
        return userService.subscribeToNewsletter(id, email);
    }

}
