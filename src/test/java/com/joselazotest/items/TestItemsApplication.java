package com.joselazotest.items;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

/**
 * Class used to leverage the testcontainers to automatically provision and connect to an ephemeral Kafka broker.
 */
@TestConfiguration(proxyBeanMethods = false)
public class TestItemsApplication {

    /**
     * Provisions a Dockerized Kafka container and automatically injects its
     * connection properties into the Spring context via @ServiceConnection.
     */
    @Bean
    @ServiceConnection
    KafkaContainer kafkaContainer() {
        return new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.5.0"));
    }

    /**
     * Main starter for the kafka broker.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        SpringApplication.from(ItemsApplication::main)
                .with(TestItemsApplication.class)
                .run(args);
    }
}
