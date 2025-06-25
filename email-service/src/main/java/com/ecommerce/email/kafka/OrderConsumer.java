package com.ecommerce.email.kafka;

import com.ecommerce.email.dto.EmailDto;
import com.ecommerce.email.service.EmailService;
import com.ecommerce.shared.dto.OrderEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class OrderConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderConsumer.class);

    private final EmailService emailService;

    public OrderConsumer(EmailService emailService) {
        this.emailService = emailService;
    }

    @KafkaListener(topics = "${kafka.topic.order.placed.name}", groupId = "order_placed_group_id")
    public void consumeOrderPlacedEvent(OrderEvent orderEvent) {
        LOGGER.info("#### -> OrderPlacedEvent: Received message -> {}", orderEvent);

        // send email notification
        EmailDto emailDto = new EmailDto();
        emailDto.setTo(orderEvent.getUser().getEmail());
        emailDto.setSubject("Commande créée avec succès");

        Map<String, Object> variables = Map.of(
                "subject", "Commande créée avec succès",
                "name", orderEvent.getUser().getName(),
                "message", "Votre commande a été passée avec succès. Veuillez consulter votre compte pour plus de détails."
        );

        emailService.sendEmail(emailDto, variables, "email-template");
    }

    @KafkaListener(topics = "${kafka.topic.order.status.changed.name}", groupId = "order_status_changed_group_id")
    public void consumeOrderStatusChangedEvent(OrderEvent orderEvent) {
        LOGGER.info("#### -> OrderStatusChangedEvent: Received message -> {}", orderEvent);


        // send email notification
        EmailDto emailDto = new EmailDto();
        emailDto.setTo(orderEvent.getUser().getEmail());
        emailDto.setSubject("Nouvelle Statut de Commande");

        Map<String, Object> variables = Map.of(
                "subject", "Nouvelle Statut de Commande",
                "name", orderEvent.getUser().getName(),
                "message", "Le statut de votre commande a été mise à jour.",
                "status", orderEvent.getStatus()
        );
        emailService.sendEmail(emailDto, variables, "email-template");
    }
}
