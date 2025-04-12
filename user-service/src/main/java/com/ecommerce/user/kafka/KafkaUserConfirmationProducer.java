package com.ecommerce.user.kafka;

import com.ecommerce.shared.dto.UserEvent;
import lombok.AllArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class KafkaUserConfirmationProducer {
    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaUserConfirmationProducer.class);

    private NewTopic topic;
    private KafkaTemplate<String, UserEvent> kafkaTemplate;

    public void sendMessage(UserEvent userEvent) {
        LOGGER.info(String.format("#### -> Sending message -> %s", userEvent.toString()));

        // create the message
        Message<UserEvent> message = MessageBuilder
                .withPayload(userEvent)
                .setHeader(KafkaHeaders.TOPIC, topic.name())
                .build();

        // send the message
        kafkaTemplate.send(message);
    }

}
