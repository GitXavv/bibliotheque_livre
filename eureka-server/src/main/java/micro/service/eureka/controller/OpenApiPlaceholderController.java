package micro.service.eureka.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Point d'entrée minimal pour SpringDoc ; la console Eureka reste sur la racine du serveur.
 */
@RestController
@RequestMapping("/api/eureka-infos")
@Tag(name = "Infos Eureka Server")
public class OpenApiPlaceholderController {

    @GetMapping
    @Operation(summary = "Statut du serveur Eureka")
    public ResponseEntity<Map<String, String>> status() {
        return ResponseEntity.ok(Map.of(
                "component", "eureka-server",
                "hint", "Console Eureka sur http://localhost:8761/"
        ));
    }
}
