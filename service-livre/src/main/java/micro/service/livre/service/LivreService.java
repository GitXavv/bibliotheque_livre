package micro.service.livre.service;

import lombok.RequiredArgsConstructor;
import micro.service.livre.repository.LivreRepository;
import micro.service.livre.dto.LivreDTO;
import micro.service.livre.model.Livre;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LivreService {

    private final LivreRepository livreRepository;

    public List<LivreDTO> findAll() {
        return livreRepository.findAll().stream().map(this::toDto).toList();
    }

    public Optional<LivreDTO> findById(Long id) {
        return livreRepository.findById(id).map(this::toDto);
    }

    public LivreDTO save(LivreDTO dto) {
        Livre livre = toEntity(dto);
        Livre savedLivre = livreRepository.save(livre);
        return toDto(savedLivre);
    }

    public void delete(Long id) {
        livreRepository.deleteById(id);
    }

    public Optional<LivreDTO> updateDisponibilite(Long id, boolean disponible) {
        return livreRepository.findById(id)
                .map(livre -> {
                    livre.setDisponible(disponible);
                    return toDto(livreRepository.save(livre));
                });
    }

    private LivreDTO toDto(Livre livre) {
        return LivreDTO.builder()
                .id(livre.getId())
                .titre(livre.getTitre())
                .auteur(livre.getAuteur())
                .isbn(livre.getIsbn())
                .disponible(livre.isDisponible())
                .build();
    }

    private Livre toEntity(LivreDTO dto) {
        return Livre.builder()
                .id(dto.getId())
                .titre(dto.getTitre())
                .auteur(dto.getAuteur())
                .isbn(dto.getIsbn())
                .disponible(dto.isDisponible())
                .build();
    }
}
