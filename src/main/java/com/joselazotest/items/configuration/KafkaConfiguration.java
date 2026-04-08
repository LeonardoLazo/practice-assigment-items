package com.joselazotest.items.configuration;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

/**
 * Configuration class for kafka topics configurations
 */
@Configuration
public class KafkaConfiguration {

    /**
     * Creates and configures the Kafka topic for item-related events.
     * @return the {@link NewTopic} instance representing the "items-topic"
     */
    @Bean
    public NewTopic itemsTopic() {
        return TopicBuilder.name("items-topic")
                .partitions(1)
                .replicas(1)
                .build();
    }
}
