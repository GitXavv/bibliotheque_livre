package com.bibliotheque.serviceemprunt.repository;

import com.bibliotheque.serviceemprunt.entity.Emprunt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EmpruntRepository extends JpaRepository<Emprunt, Long> {

    // Trouver tous les emprunts d'un emprunteur
    List<Emprunt> findByEmprunteurId(Long emprunteurId);

    // Trouver tous les emprunts d'un livre
    List<Emprunt> findByLivreId(Long livreId);

    // Trouver tous les emprunts par statut (EN_COURS, RENDU, RETARD)
    List<Emprunt> findByStatut(String statut);
}
