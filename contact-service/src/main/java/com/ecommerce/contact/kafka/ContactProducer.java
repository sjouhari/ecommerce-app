package com.ecommerce.contact.kafka;

import com.ecommerce.shared.dto.ContactDto;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ContactProducer {

    private static final Logger LOGGER = LoggerFactory.getLogger(ContactProducer.class);

    private KafkaTemplate<String, ContactDto> kafkaTemplate;

    public void sendMessage(ContactDto contactDto, String topic) {
        LOGGER.info(String.format("Contact message sent -> %s", contactDto.toString()));
        // Create the message
        Message<ContactDto> message = MessageBuilder
                .withPayload(contactDto)
                .setHeader(KafkaHeaders.TOPIC, topic)
                .build();
        kafkaTemplate.send(message);
    }
}
