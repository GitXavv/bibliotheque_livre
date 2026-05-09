package micro.service.livre.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Service Livre API")
                        .version("1.0")
                        .description("API de gestion des livres de la bibliothèque. Permet de consulter, ajouter, supprimer et modifier la disponibilité des livres.")
                        .contact(new Contact()
                                .name("Equipe Microservices")
                                .email("contact@bibliotheque.fr")
                        )
                );
    }
}
