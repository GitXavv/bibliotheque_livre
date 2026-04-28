package com.bibliotheque.serviceemprunt.client;

import com.bibliotheque.serviceemprunt.dto.EmprunteurDTO;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class EmprunteurClient {

    private final RestClient restClient;

    public EmprunteurClient(RestClient.Builder builder) {
        this.restClient = builder
                .baseUrl("http://service-emprunteur")
                .build();
    }

    // Vérifier qu'un emprunteur existe
    public EmprunteurDTO getEmprunteurById(Long id) {
        return restClient.get()
                .uri("/api/emprunteurs/{id}", id)
                .retrieve()
                .body(EmprunteurDTO.class);
    }
}
