package com.bibliotheque.serviceemprunt.controller;

import com.bibliotheque.serviceemprunt.dto.EmpruntDTO;
import com.bibliotheque.serviceemprunt.dto.EmpruntRequestDTO;
import com.bibliotheque.serviceemprunt.service.EmpruntService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/emprunts")
@RequiredArgsConstructor
@Tag(name = "Emprunts", description = "Gestion des emprunts de livres")
public class EmpruntController {

    private final EmpruntService empruntService;

    @GetMapping
    @Operation(summary = "Lister tous les emprunts")
    public ResponseEntity<List<EmpruntDTO>> findAll() {
        return ResponseEntity.ok(empruntService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Trouver un emprunt par son id")
    public ResponseEntity<EmpruntDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(empruntService.findById(id));
    }

    @PostMapping
    @Operation(summary = "Créer un nouvel emprunt")
    public ResponseEntity<EmpruntDTO> creerEmprunt(@RequestBody EmpruntRequestDTO request) {
        EmpruntDTO created = empruntService.creerEmprunt(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}/retour")
    @Operation(summary = "Retourner un livre emprunté")
    public ResponseEntity<EmpruntDTO> retournerLivre(@PathVariable Long id) {
        return ResponseEntity.ok(empruntService.retournerLivre(id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un emprunt")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        empruntService.delete(id);
        return ResponseEntity.noContent().build();
    }
}