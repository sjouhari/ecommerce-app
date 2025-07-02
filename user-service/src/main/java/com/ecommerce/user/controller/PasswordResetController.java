package com.ecommerce.user.controller;

import com.ecommerce.user.entity.User;
import com.ecommerce.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

@Controller
@RequestMapping("api/users/reset-password")
public class PasswordResetController {

    private static final String RESET_PASSWORD_TEMPLATE = "reset-password-template";

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public PasswordResetController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public String resetPassword(@RequestParam("code") int verificationCode, Model model) {
        Optional<User> user = userRepository.findByVerificationCode(verificationCode);

        if(user.isEmpty()) {
            model.addAttribute("error", "Le code de verification est incorrect.");
            return RESET_PASSWORD_TEMPLATE;
        }

        if(user.get().getVerificationCodeExpireAt() != null && user.get().getVerificationCodeExpireAt().isBefore(LocalDateTime.now())) {
            model.addAttribute("error", "Le code de verification est expiré.");
            return RESET_PASSWORD_TEMPLATE;
        }

        model.addAttribute("verificationCode", verificationCode);
        return RESET_PASSWORD_TEMPLATE;
    }

    @PostMapping
    public String updatePassword(
            @RequestParam("verificationCode") int verificationCode,
            @RequestParam("password") String password,
            @RequestParam("confirmPassword") String confirmPassword,
            Model model) {
        Optional<User> user = userRepository.findByVerificationCode(verificationCode);

        if(user.isEmpty()) {
            model.addAttribute("error", "Le code de verification est incorrect.");
            return RESET_PASSWORD_TEMPLATE;
        }

        if(user.get().getVerificationCodeExpireAt() != null && user.get().getVerificationCodeExpireAt().isBefore(LocalDateTime.now())) {
            model.addAttribute("error", "Le code de verification est expiré.");
            return RESET_PASSWORD_TEMPLATE;
        }

        if(!Objects.equals(password, confirmPassword)) {
            model.addAttribute("error", "Les mots de passe ne correspondent pas.");
            model.addAttribute("verificationCode", verificationCode);
            return RESET_PASSWORD_TEMPLATE;
        }

        user.get().setPassword(passwordEncoder.encode(password));
        user.get().setVerificationCodeExpireAt(null);
        user.get().setVerificationCode(0);
        userRepository.save(user.get());

        model.addAttribute("message", "Le mot de passe a été réinitialisé avec success.");

        return RESET_PASSWORD_TEMPLATE;
    }

}
