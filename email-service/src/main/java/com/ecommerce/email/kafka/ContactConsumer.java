package com.ecommerce.email.kafka;

import com.ecommerce.email.dto.EmailDto;
import com.ecommerce.email.service.EmailService;
import com.ecommerce.shared.dto.ContactDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ContactConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(ContactConsumer.class);

    @Autowired
    private EmailService emailService;

    @Value("${spring.mail.username}")
    private String email;

    @KafkaListener(topics = "contact", groupId = "contact-group")
    public void consume(ContactDto contactDto) {
        LOGGER.info(String.format("#### -> Received message -> %s", contactDto.toString()));

        EmailDto emailDto = new EmailDto();
        emailDto.setTo(email);
        emailDto.setSubject("Nouveau contact message");

        Map<String, Object> variables = Map.of(
                "subject", "Nouveau contact message",
                "name", "Admin",
                "message", contactDto.getMessage() + " de " + contactDto.getEmail()
        );

        emailService.sendEmail(emailDto, variables);
    }

}
