package com.joselazotest.items.messaging;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import java.util.UUID;

/**
 * Kafka producer responsible for publishing item-related events.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ItemEventProducer {

    public static final String MSG_HELLO_WORLD = "hello world: ";
    private static final String TOPIC = "items-topic";
    private final KafkaTemplate<String, String> kafkaTemplate;

    /**
     * Publishes an item creation message to the Kafka topic.
     * Exceptions are caught and logged to prevent disrupting the main business transaction.
     *
     * @param itemId the unique identifier of the newly created item
     */
    public void publishItemCreated(UUID itemId) {
        String message = MSG_HELLO_WORLD + itemId;
        try {
            kafkaTemplate.send(TOPIC, itemId.toString(), message);
            log.info("Message published to Kafka: {}", message);
        } catch (Exception e) {
            log.error("Failed to publish message to Kafka for item: {}", itemId, e);
        }
    }
}
