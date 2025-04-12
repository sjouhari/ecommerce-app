package com.ecommerce.user.kafka;

import com.ecommerce.shared.dto.UserEvent;
import lombok.AllArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
public class KafkaUserConfirmedProducer {
    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaUserConfirmedProducer.class);

    @Value("${kafka.topic.user.confirmed.name}")
    private String topicName;

    @Autowired
    private KafkaTemplate<String, UserEvent> kafkaTemplate;

    public void sendMessage(UserEvent userEvent) {
        LOGGER.info(String.format("#### -> Sending message -> %s", userEvent.toString()));

        // create the message
        Message<UserEvent> message = MessageBuilder
                .withPayload(userEvent)
                .setHeader(KafkaHeaders.TOPIC, topicName)
                .build();

        // send the message
        kafkaTemplate.send(message);
    }

}
