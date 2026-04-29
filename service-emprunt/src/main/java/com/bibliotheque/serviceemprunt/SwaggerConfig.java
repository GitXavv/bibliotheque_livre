package com.bibliotheque.serviceemprunt;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Service Emprunt API")
                        .version("1.0.0")
                        .description("API de gestion des emprunts de livres - Système de bibliothèque en microservices")
                        .contact(new Contact()
                                .name("Bibliothèque Microservices")
                                .email("contact@bibliotheque.com")));
    }
}