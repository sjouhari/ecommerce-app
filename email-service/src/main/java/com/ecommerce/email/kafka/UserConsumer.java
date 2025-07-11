package com.ecommerce.email.kafka;

import com.ecommerce.email.dto.EmailDto;
import com.ecommerce.email.service.EmailService;
import com.ecommerce.shared.dto.UserEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class UserConsumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserConsumer.class);

    private final EmailService emailService;

    public UserConsumer(EmailService emailService) {
        this.emailService = emailService;
    }

    @KafkaListener(topics = "${kafka.topic.name}", groupId = "${spring.kafka.consumer.group-id}")
    public void userConfirmation(UserEvent userEvent) {
        LOGGER.info("#### -> UserConfirmationEvent: Received message -> {}", userEvent);
        EmailDto emailDto = new EmailDto();
        emailDto.setTo(userEvent.getEmail());
        emailDto.setSubject("Confirmation de compte");

        String verificationUrl = "http://localhost:8080/users/verify?code=" + userEvent.getCode();

        Map<String, Object> variables = Map.of(
                "subject", "Confirmation de compte",
                "name", userEvent.getName(),
                "message", "Bienvenue sur notre site, pour contniuer veuillez confirmer votre compte",
                "verificationUrl", verificationUrl
        );
        emailService.sendEmail(emailDto, variables, "email-template");
    }

    @KafkaListener(topics = "${kafka.topic.user.confirmed.name}", groupId = "${spring.kafka.consumer.group-id}")
    public void userConfirmed(UserEvent userEvent) {
        LOGGER.info("#### -> UserConfirmedEvent: Received message -> {}", userEvent);

        EmailDto emailDto = new EmailDto();
        emailDto.setTo(userEvent.getEmail());
        emailDto.setSubject("Compte verifié avec succès");

        Map<String, Object> variables = Map.of(
                "subject", "Compte verifié avec succès",
                "name", userEvent.getName(),
                "message", "Votre compte a été verifié avec succès. Vous pouvez maintenant vous connecter."
        );
        emailService.sendEmail(emailDto, variables, "email-template");
    }

    @KafkaListener(topics = "${kafka.topic.forgot.password.name}", groupId = "user_forgot_password_group_id")
    public void forgotPassword(UserEvent userEvent) {
        LOGGER.info("#### -> ForgotPasswordEvent: Received message -> {}", userEvent);
        EmailDto emailDto = new EmailDto();
        emailDto.setTo(userEvent.getEmail());
        emailDto.setSubject("Mot de passe oublié");

        Map<String, Object> variables = Map.of(
                "subject", "Mot de passe oublié",
                "name", userEvent.getName(),
                "message", "Vous avez oublié votre mot de passe, c'est possible. Vous pouvez le réinitiliser !",
                "resetPasswordUrl", "http://localhost:8080/api/users/reset-password?code=" + userEvent.getCode()
        );
        emailService.sendEmail(emailDto, variables, "email-template");
    }

    @KafkaListener(topics = "${kafka.topic.reset.password.name}", groupId = "user_forgot_password_group_id")
    public void resetPassword(UserEvent userEvent) {
        LOGGER.info("#### -> ResetPasswordEvent: Received message -> {}", userEvent);
        EmailDto emailDto = new EmailDto();
        emailDto.setTo(userEvent.getEmail());
        emailDto.setSubject("Modification de mot de passe");

        Map<String, Object> variables = Map.of(
                "subject", "Modification de mot de passe",
                "name", userEvent.getName(),
                "message", "Votre mot de passe a été modifié avec succès."
        );
        emailService.sendEmail(emailDto, variables, "email-template");
    }
}
