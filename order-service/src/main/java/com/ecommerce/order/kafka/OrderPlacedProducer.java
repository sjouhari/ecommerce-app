package com.ecommerce.order.kafka;

import com.ecommerce.shared.dto.OrderEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
public class OrderPlacedProducer {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderPlacedProducer.class);

    @Autowired
    private KafkaTemplate<String, OrderEvent> kafkaTemplate;

    public void sendMessage(OrderEvent orderEvent, String orderTopic) {
        LOGGER.info(String.format("Order placed successfully : %s", orderEvent.toString()));

        // create the message
        Message<OrderEvent> message = MessageBuilder
                .withPayload(orderEvent)
                .setHeader(KafkaHeaders.TOPIC, orderTopic)
                .build();

        // send the message
        kafkaTemplate.send(message);
    }
}
