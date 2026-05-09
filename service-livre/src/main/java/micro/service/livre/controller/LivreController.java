package micro.service.livre.controller;

import lombok.RequiredArgsConstructor;
import micro.service.livre.dto.LivreDTO;
import micro.service.livre.service.LivreService;
import org.springframework.http.ResponseEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/livres")
@RequiredArgsConstructor
@Tag(name = "Livres", description = "Opérations CRUD sur les livres de la bibliothèque")
public class LivreController {

    private final LivreService livreService;

    @GetMapping
    @Operation(summary = "Lister tous les livres", description = "Retourne la liste complète des livres disponibles dans la bibliothèque.")
    public List<LivreDTO> findAll() {
        return livreService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtenir un livre par son id", description = "Retourne un livre selon son identifiant unique. Renvoie 404 si le livre n'existe pas.")
    public ResponseEntity<LivreDTO> findById(@PathVariable Long id) {
        return livreService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Ajouter un livre", description = "Ajoute un nouveau livre à la bibliothèque.")
    public ResponseEntity<LivreDTO> save(@RequestBody LivreDTO livreDTO) {
        LivreDTO savedLivre = livreService.save(livreDTO);
        return ResponseEntity.ok(savedLivre);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un livre", description = "Supprime un livre de la bibliothèque selon son identifiant.")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        livreService.delete(id);
        return ResponseEntity.noContent().build();
    }

        @PutMapping("/{id}/disponibilite")
        @Operation(summary = "Modifier la disponibilité d'un livre", description = "Met à jour la disponibilité (empruntable ou non) d'un livre selon son identifiant.")
        public ResponseEntity<LivreDTO> updateDisponibilite(
            @PathVariable Long id,
            @RequestParam boolean disponible
        ) {
        return livreService.updateDisponibilite(id, disponible)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
        }
}
