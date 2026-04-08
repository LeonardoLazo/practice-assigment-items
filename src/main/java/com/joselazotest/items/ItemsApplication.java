package com.joselazotest.items;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main application class for Items Service.
 */
@SpringBootApplication
public class ItemsApplication {

    /**
     * Main starter for the Spring Boot application.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(ItemsApplication.class, args);
    }
}
