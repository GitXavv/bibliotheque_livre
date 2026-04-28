package com.bibliotheque.serviceemprunt.service;

import com.bibliotheque.serviceemprunt.client.EmprunteurClient;
import com.bibliotheque.serviceemprunt.client.LivreClient;
import com.bibliotheque.serviceemprunt.dto.EmpruntDTO;
import com.bibliotheque.serviceemprunt.dto.EmpruntRequestDTO;
import com.bibliotheque.serviceemprunt.dto.LivreDTO;
import com.bibliotheque.serviceemprunt.dto.EmprunteurDTO;
import com.bibliotheque.serviceemprunt.entity.Emprunt;
import com.bibliotheque.serviceemprunt.repository.EmpruntRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmpruntService {

    private final EmpruntRepository empruntRepository;
    private final LivreClient livreClient;
    private final EmprunteurClient emprunteurClient;

    // Convertir entité → DTO
    private EmpruntDTO toDTO(Emprunt emprunt) {
        return new EmpruntDTO(
                emprunt.getId(),
                emprunt.getDateEmprunt(),
                emprunt.getDateRetourPrevue(),
                emprunt.getDateRetourReelle(),
                emprunt.getLivreId(),
                emprunt.getEmprunteurId(),
                emprunt.getStatut()
        );
    }

    // Récupérer tous les emprunts
    public List<EmpruntDTO> findAll() {
        return empruntRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // Récupérer un emprunt par id
    public EmpruntDTO findById(Long id) {
        Emprunt emprunt = empruntRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Emprunt non trouvé avec l'id : " + id));
        return toDTO(emprunt);
    }

    // Créer un nouvel emprunt
    public EmpruntDTO creerEmprunt(EmpruntRequestDTO request) {

        // 1. Vérifier que le livre existe et est disponible
        LivreDTO livre = livreClient.getLivreById(request.getLivreId());
        if (livre == null) {
            throw new RuntimeException("Livre non trouvé avec l'id : " + request.getLivreId());
        }
        if (!livre.getDisponible()) {
            throw new RuntimeException("Le livre n'est pas disponible");
        }

        // 2. Vérifier que l'emprunteur existe
        EmprunteurDTO emprunteur = emprunteurClient.getEmprunteurById(Long.valueOf(request.getEmprunteurId()));
        if (emprunteur == null) {
            throw new RuntimeException("Emprunteur non trouvé avec l'id : " + request.getEmprunteurId());
        }

        // 3. Créer l'emprunt
        Emprunt emprunt = new Emprunt();
        emprunt.setLivreId(request.getLivreId());
        emprunt.setEmprunteurId(Long.valueOf(request.getEmprunteurId()));
        emprunt.setDateEmprunt(LocalDate.now());
        emprunt.setDateRetourPrevue(LocalDate.now().plusDays(15));
        emprunt.setStatut("EN_COURS");

        // 4. Sauvegarder en base
        Emprunt saved = empruntRepository.save(emprunt);

        // 5. Mettre le livre comme non disponible
        livreClient.updateDisponibilite(request.getLivreId(), false);

        return toDTO(saved);
    }

    // Retourner un livre
    public EmpruntDTO retournerLivre(Long id) {

        // 1. Trouver l'emprunt
        Emprunt emprunt = empruntRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Emprunt non trouvé avec l'id : " + id));

        // 2. Vérifier que l'emprunt est bien EN_COURS
        if (!emprunt.getStatut().equals("EN_COURS")) {
            throw new RuntimeException("Cet emprunt n'est pas en cours");
        }

        // 3. Mettre à jour l'emprunt
        emprunt.setStatut("RENDU");
        emprunt.setDateRetourReelle(LocalDate.now());

        // 4. Sauvegarder
        Emprunt saved = empruntRepository.save(emprunt);

        // 5. Remettre le livre disponible
        livreClient.updateDisponibilite(emprunt.getLivreId(), true);

        return toDTO(saved);
    }

    // Supprimer un emprunt
    public void delete(Long id) {
        if (!empruntRepository.existsById(id)) {
            throw new RuntimeException("Emprunt non trouvé avec l'id : " + id);
        }
        empruntRepository.deleteById(id);
    }
}