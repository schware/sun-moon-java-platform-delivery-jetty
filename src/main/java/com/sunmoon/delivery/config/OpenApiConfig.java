package com.sunmoon.delivery.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI deliveryOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("sun-moon-java-platform-delivery API")
                        .description("Delivery assignment/tracking for the sun-moon-java-platform family.")
                        .version("0.1.0"))
                .servers(List.of(new Server().url("/delivery")));
    }
}
