package com.joselazotest.items.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for Open API documentations configurations
 */
@Configuration
public class OpenApiConfiguration {

    /**
     * Configures the OpenAPI (Swagger) documentation for the Items API.
     *
     * @return an {@link OpenAPI} instance containing the API title, version, and description
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                .title("Items API")
                .version("1.0.0")
                .description("Technical test implementation featuring In-Memory storage and Kafka integration."));
    }
}
