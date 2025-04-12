package com.ecommerce.email.kafka;

import com.ecommerce.email.dto.EmailDto;
import com.ecommerce.email.service.EmailService;
import com.ecommerce.shared.dto.OrderEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class OrderConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderConsumer.class);

    @Autowired
    private EmailService emailService;

    @KafkaListener(topics = "${kafka.topic.order.placed.name}", groupId = "order_placed_group_id")
    public void consumeOrderPlacedEvent(OrderEvent orderEvent) {
        LOGGER.info(String.format("#### -> Received message -> %s", orderEvent.toString()));

        // send email notification
        EmailDto emailDto = new EmailDto();
        emailDto.setTo(orderEvent.getUser().getEmail());
        emailDto.setSubject("Order Placed Successfully");
        String content = "Hello " + orderEvent.getUser().getName() +", " +
                "\n\nYour order has been placed successfully. " +
                "\n\nOrder ID: " + orderEvent.getOrderId();
        emailDto.setBody(content);
        emailService.sendEmail(emailDto);
    }

    @KafkaListener(topics = "${kafka.topic.order.status.changed.name}", groupId = "order_status_changed_group_id")
    public void consumeOrderStatusChangedEvent(OrderEvent orderEvent) {
        LOGGER.info(String.format("#### -> Received message -> %s", orderEvent.toString()));

        // send email notification
        EmailDto emailDto = new EmailDto();
        emailDto.setTo(orderEvent.getUser().getEmail());
        emailDto.setSubject("Order Status Update");
        String content = "Hello " + orderEvent.getUser().getName() + "," +
                "\n\nYour order status has been changed. " +
                "\n\nOrder ID: " + orderEvent.getOrderId() +
                "\n\nNew Order Status:\n\n" + orderEvent.getStatus();
        emailDto.setBody(content);
        emailService.sendEmail(emailDto);
    }
}
