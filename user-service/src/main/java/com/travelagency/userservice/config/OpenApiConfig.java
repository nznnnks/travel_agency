package com.travelagency.userservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("User Service API")
                        .description("API для управления пользователями в Travel Agency")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Travel Agency Team")
                                .email("support@travelagency.com")
                                .url("https://travelagency.com"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8084")
                                .description("Development Server"),
                        new Server()
                                .url("http://localhost:8080/api/users")
                                .description("Production Server (via Gateway)")
                ));
    }
}
