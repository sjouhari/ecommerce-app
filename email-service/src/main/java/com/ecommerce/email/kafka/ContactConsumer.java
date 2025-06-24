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

    private final EmailService emailService;

    @Value("${spring.mail.username}")
    private String email;

    public ContactConsumer(EmailService emailService) {
        this.emailService = emailService;
    }

    @KafkaListener(topics = "new-contact-message", groupId = "new-contact-group")
    public void consumeNewContact(ContactDto contactDto) {
        LOGGER.info(String.format("#### -> Received message -> %s", contactDto.toString()));

        EmailDto emailDto = new EmailDto();
        emailDto.setTo(email);
        emailDto.setSubject(contactDto.getSubject());

        Map<String, Object> variables = Map.of(
                "subject", contactDto.getSubject(),
                "name", contactDto.getName(),
                "message", contactDto.getMessage()
        );

        emailService.sendEmail(emailDto, variables);
    }

    @KafkaListener(topics = "contact-response-message", groupId = "contact-response-group")
    public void consumeContactResponse(ContactDto contactDto) {
        LOGGER.info(String.format("#### -> Received message -> %s", contactDto.toString()));

        EmailDto emailDto = new EmailDto();
        emailDto.setTo(contactDto.getEmail());
        emailDto.setSubject(contactDto.getSubject());

        Map<String, Object> variables = Map.of(
                "subject", contactDto.getSubject() + "  - Réponse",
                "name", contactDto.getName(),
                "message", contactDto.getMessage()
        );

        emailService.sendEmail(emailDto, variables);
    }

}
