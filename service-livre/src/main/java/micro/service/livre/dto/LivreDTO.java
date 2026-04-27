package micro.service.livre.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LivreDTO {
    private Long id;
    private String titre;
    private String auteur;
    private String isbn;
    private boolean disponible;
}
