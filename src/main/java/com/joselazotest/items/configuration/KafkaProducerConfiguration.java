package com.joselazotest.items.configuration;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Configuration class for kafka producers configurations
 */
@Configuration
public class KafkaProducerConfiguration {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    /**
     * Creates and configures the items-related Kafka producer.
     * @return the {@link DefaultKafkaProducerFactory} instance representing the "items-topic"
     */
    @Bean
    public ProducerFactory<String, String> producerFactory() {
        Map<String, Object> configurationProperties = new HashMap<>();
        configurationProperties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configurationProperties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configurationProperties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

        configurationProperties.put(ProducerConfig.ACKS_CONFIG, "all");
        configurationProperties.put(ProducerConfig.RETRIES_CONFIG, 3);
        configurationProperties.put(ProducerConfig.RETRY_BACKOFF_MS_CONFIG, 1000);
        configurationProperties.put(ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG, 5000);

        return new DefaultKafkaProducerFactory<>(configurationProperties);
    }

    /**
     * Configures the primary template used to publish string-based domain events to the Kafka broker.
     *
     * @return a {@link KafkaTemplate} initialized with the custom resilience settings of the producer factory
     */
    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
}
