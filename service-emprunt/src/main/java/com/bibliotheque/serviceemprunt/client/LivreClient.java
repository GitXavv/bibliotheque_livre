package com.bibliotheque.serviceemprunt.client;

import com.bibliotheque.serviceemprunt.dto.LivreDTO;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class LivreClient {

    private final RestClient restClient;

    public LivreClient(RestClient.Builder builder) {
        this.restClient = builder
                .baseUrl("http://service-livre")
                .build();
    }

    // Récupérer un livre par son id
    public LivreDTO getLivreById(Long id) {
        return restClient.get()
                .uri("/api/livres/{id}", id)
                .retrieve()
                .body(LivreDTO.class);
    }

    // Mettre à jour la disponibilité d'un livre
    public void updateDisponibilite(Long id, Boolean disponible) {
        restClient.put()
                .uri("/api/livres/{id}/disponibilite", id)
                .body(disponible)
                .retrieve()
                .toBodilessEntity();
    }
}