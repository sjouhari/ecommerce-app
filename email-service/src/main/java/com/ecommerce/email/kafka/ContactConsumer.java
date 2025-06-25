package com.ecommerce.email.kafka;

import com.ecommerce.email.dto.EmailDto;
import com.ecommerce.email.service.EmailService;
import com.ecommerce.shared.dto.ContactDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    @KafkaListener(topics = "${kafka.topic.contact.new.name}", groupId = "new-contact-group")
    public void consumeNewContact(ContactDto contactDto) {
        LOGGER.info("#### -> ContactMessageEvent: Message received");

        EmailDto emailDto = new EmailDto();
        emailDto.setTo(email);
        emailDto.setSubject(contactDto.getSubject());

        EmailDto userEmailDto = new EmailDto();
        userEmailDto.setTo(contactDto.getEmail());
        userEmailDto.setSubject("Confirmation de réception");

        Map<String, Object> variables = Map.of("contact", contactDto);

        LOGGER.info("#### -> ContactMessageEvent: Sending contact message to email to {}", email);
        emailService.sendEmail(emailDto, variables, "contact/contact-message-template");
        LOGGER.info("#### -> ContactMessageEvent: Message sent");

        LOGGER.info("#### -> ContactMessageEvent: Sending contact confirmation email to {}", contactDto.getEmail());
        emailService.sendEmail(userEmailDto, variables, "contact/contact-confirmation-template");
        LOGGER.info("#### -> ContactMessageEvent: Message sent");
    }

    @KafkaListener(topics = "${kafka.topic.contact.response.name}", groupId = "contact-response-group")
    public void consumeContactResponse(ContactDto contactDto) {
        LOGGER.info("#### -> ContactResponseEvent: Message received");

        EmailDto emailDto = new EmailDto();
        emailDto.setTo(contactDto.getEmail());
        emailDto.setSubject("Réponse sur votre message");

        Map<String, Object> variables = Map.of("contact", contactDto);

        LOGGER.info("#### -> ContactResponseEvent: Sending email to {}", contactDto.getEmail());
        emailService.sendEmail(emailDto, variables, "contact/contact-response-template");
        LOGGER.info("#### -> ContactResponseEvent: Message sent");
    }

}
