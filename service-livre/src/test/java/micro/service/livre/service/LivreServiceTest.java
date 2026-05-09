package micro.service.livre.service;

import micro.service.livre.dto.LivreDTO;
import micro.service.livre.model.Livre;
import micro.service.livre.repository.LivreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class LivreServiceTest {

    @Mock
    private LivreRepository livreRepository;

    @InjectMocks
    private LivreService livreService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findAll_ReturnsListOfLivreDTO() {
        Livre livre1 = Livre.builder().id(1L).titre("Titre 1").auteur("Auteur 1").isbn("ISBN1").disponible(true).build();
        Livre livre2 = Livre.builder().id(2L).titre("Titre 2").auteur("Auteur 2").isbn("ISBN2").disponible(false).build();
        when(livreRepository.findAll()).thenReturn(Arrays.asList(livre1, livre2));

        List<LivreDTO> result = livreService.findAll();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getTitre()).isEqualTo("Titre 1");
        assertThat(result.get(1).isDisponible()).isFalse();
    }

    @Test
    void findById_ExistingId_ReturnsLivreDTO() {
        Livre livre = Livre.builder().id(1L).titre("Titre").auteur("Auteur").isbn("ISBN").disponible(true).build();
        when(livreRepository.findById(1L)).thenReturn(Optional.of(livre));

        Optional<LivreDTO> result = livreService.findById(1L);

        assertThat(result).isPresent();
        assertThat(result.get().getTitre()).isEqualTo("Titre");
    }

    @Test
    void findById_NonExistingId_ReturnsEmpty() {
        when(livreRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<LivreDTO> result = livreService.findById(99L);

        assertThat(result).isEmpty();
    }

    @Test
    void save_ValidLivreDTO_ReturnsSavedLivreDTO() {
        Livre livre = Livre.builder().id(1L).titre("Titre").auteur("Auteur").isbn("ISBN").disponible(true).build();
        when(livreRepository.save(any(Livre.class))).thenReturn(livre);

        LivreDTO dto = LivreDTO.builder().titre("Titre").auteur("Auteur").isbn("ISBN").disponible(true).build();
        LivreDTO result = livreService.save(dto);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getTitre()).isEqualTo("Titre");
    }

    @Test
    void delete_ExistingId_DeletesLivre() {
        doNothing().when(livreRepository).deleteById(1L);

        livreService.delete(1L);

        verify(livreRepository, times(1)).deleteById(1L);
    }

    @Test
    void updateDisponibilite_ExistingId_UpdatesAndReturnsLivreDTO() {
        Livre livre = Livre.builder().id(1L).titre("Titre").auteur("Auteur").isbn("ISBN").disponible(false).build();
        when(livreRepository.findById(1L)).thenReturn(Optional.of(livre));
        Livre livreUpdated = Livre.builder().id(1L).titre("Titre").auteur("Auteur").isbn("ISBN").disponible(true).build();
        when(livreRepository.save(any(Livre.class))).thenReturn(livreUpdated);

        Optional<LivreDTO> result = livreService.updateDisponibilite(1L, true);

        assertThat(result).isPresent();
        assertThat(result.get().isDisponible()).isTrue();
    }

    @Test
    void updateDisponibilite_NonExistingId_ReturnsEmpty() {
        when(livreRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<LivreDTO> result = livreService.updateDisponibilite(99L, true);

        assertThat(result).isEmpty();
    }
}
