package com.joselazotest.items.messaging;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * Kafka consumer responsible for processing item-related events.
 */
@Slf4j
@Component
public class ItemEventConsumer {

    /**
     * Listens to the "topics" and logs incoming messages.
     *
     * @param message the event payload (expected format: "hello world: <itemId>")
     */
    @KafkaListener(topics = "items-topic", groupId = "items-group")
    public void consume(String message) {
        log.info("Received message from Kafka: {}", message);
    }
}
