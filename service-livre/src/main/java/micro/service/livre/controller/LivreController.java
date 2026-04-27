package micro.service.livre.controller;

import lombok.RequiredArgsConstructor;
import micro.service.livre.dto.LivreDTO;
import micro.service.livre.service.LivreService;
import org.springframework.http.ResponseEntity;
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
public class LivreController {

    private final LivreService livreService;

    @GetMapping
    public List<LivreDTO> findAll() {
        return livreService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<LivreDTO> findById(@PathVariable Long id) {
        return livreService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<LivreDTO> save(@RequestBody LivreDTO livreDTO) {
        LivreDTO savedLivre = livreService.save(livreDTO);
        return ResponseEntity.ok(savedLivre);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        livreService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/disponibilite")
    public ResponseEntity<LivreDTO> updateDisponibilite(
            @PathVariable Long id,
            @RequestParam boolean disponible
    ) {
        return livreService.updateDisponibilite(id, disponible)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
