package com.ecommerce.email.kafka;

import com.ecommerce.email.dto.EmailDto;
import com.ecommerce.email.service.EmailService;
import com.ecommerce.shared.dto.UserEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaConsumer.class);

    @Autowired
    private EmailService emailService;

    @KafkaListener(topics = "${kafka.topic.name}", groupId = "${spring.kafka.consumer.group-id}")
    public void consume(UserEvent userEvent) {
        LOGGER.info(String.format("#### -> Received message -> %s", userEvent.toString()));
        EmailDto emailDto = new EmailDto();
        emailDto.setTo(userEvent.getEmail());
        emailDto.setSubject("Email Confirmation");

        String verificationUrl = "http://localhost:8080/api/users/verify?code=" + userEvent.getCode();
        String content = "Hello " + userEvent.getName() +
                ", \n\nYour account has been created successfully.\n" +
                "Please click the link below to verify your account.\n\n" +
                verificationUrl;
        emailDto.setBody(content);
        emailService.sendEmail(emailDto);
    }

    @KafkaListener(topics = "${kafka.topic.user.confirmed.name}", groupId = "${spring.kafka.consumer.group-id}")
    public void userConfirmed(UserEvent userEvent) {
        LOGGER.info(String.format("#### -> Received message -> %s", userEvent.toString()));

        EmailDto emailDto = new EmailDto();
        emailDto.setTo(userEvent.getEmail());
        emailDto.setSubject("Account Verified Successfully");
        String content = "Hello " + userEvent.getName() + ", \n\nYour account has been verified successfully.";
        emailDto.setBody(content);
        emailService.sendEmail(emailDto);
    }
}
