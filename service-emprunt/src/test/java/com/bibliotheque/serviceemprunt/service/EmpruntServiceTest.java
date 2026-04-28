package com.bibliotheque.serviceemprunt.service;

import com.bibliotheque.serviceemprunt.client.EmprunteurClient;
import com.bibliotheque.serviceemprunt.client.LivreClient;
import com.bibliotheque.serviceemprunt.dto.*;
import com.bibliotheque.serviceemprunt.entity.Emprunt;
import com.bibliotheque.serviceemprunt.repository.EmpruntRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmpruntServiceTest {

    @Mock
    private EmpruntRepository empruntRepository;

    @Mock
    private LivreClient livreClient;

    @Mock
    private EmprunteurClient emprunteurClient;

    @InjectMocks
    private EmpruntService empruntService;

    private Emprunt emprunt;
    private LivreDTO livreDisponible;
    private LivreDTO livreIndisponible;
    private EmprunteurDTO emprunteur;

    @BeforeEach
    void setUp() {
        // Un emprunt de base pour les tests
        emprunt = new Emprunt();
        emprunt.setId(1L);
        emprunt.setLivreId(10L);
        emprunt.setEmprunteurId(20L);
        emprunt.setDateEmprunt(LocalDate.now());
        emprunt.setDateRetourPrevue(LocalDate.now().plusDays(15));
        emprunt.setStatut("EN_COURS");

        // Un livre disponible
        livreDisponible = new LivreDTO(10L, "Clean Code", "Robert Martin", "123456", true);

        // Un livre NON disponible
        livreIndisponible = new LivreDTO(10L, "Clean Code", "Robert Martin", "123456", false);

        // Un emprunteur
        emprunteur = new EmprunteurDTO(20L, "Hamilton", "hamilton@email.com", "650000000");
    }

    // ============================================================
    // TEST 1 : findAll() retourne bien la liste des emprunts
    // ============================================================
    @Test
    void findAll_devraitRetournerListeEmprunts() {
        // GIVEN : le repository retourne une liste avec un emprunt
        when(empruntRepository.findAll()).thenReturn(List.of(emprunt));

        // WHEN : on appelle findAll()
        List<EmpruntDTO> result = empruntService.findAll();

        // THEN : on obtient bien 1 emprunt
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
        verify(empruntRepository, times(1)).findAll();
    }

    // ============================================================
    // TEST 2 : findById() avec un id qui existe
    // ============================================================
    @Test
    void findById_devraitRetournerEmprunt_quandIdExiste() {
        // GIVEN
        when(empruntRepository.findById(1L)).thenReturn(Optional.of(emprunt));

        // WHEN
        EmpruntDTO result = empruntService.findById(1L);

        // THEN
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("EN_COURS", result.getStatut());
    }

    // ============================================================
    // TEST 3 : findById() avec un id qui n'existe pas
    // ============================================================
    @Test
    void findById_devraitLancerException_quandIdExistePas() {
        // GIVEN : le repository ne trouve rien
        when(empruntRepository.findById(99L)).thenReturn(Optional.empty());

        // WHEN + THEN : une exception doit être levée
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> empruntService.findById(99L));

        assertTrue(exception.getMessage().contains("99"));
    }

    // ============================================================
    // TEST 4 : creerEmprunt() avec tout valide
    // ============================================================
    @Test
    void creerEmprunt_devraitCreerEmprunt_quandToutEstValide() {
        // GIVEN
        EmpruntRequestDTO request = new EmpruntRequestDTO(10L, 20L);
        when(livreClient.getLivreById(10L)).thenReturn(livreDisponible);
        when(emprunteurClient.getEmprunteurById(20L)).thenReturn(emprunteur);
        when(empruntRepository.save(any(Emprunt.class))).thenReturn(emprunt);

        // WHEN
        EmpruntDTO result = empruntService.creerEmprunt(request);

        // THEN
        assertNotNull(result);
        assertEquals("EN_COURS", result.getStatut());
        assertEquals(10L, result.getLivreId());
        assertEquals(20L, result.getEmprunteurId());

        // Vérifier que le livre a bien été marqué indisponible
        verify(livreClient, times(1)).updateDisponibilite(10L, false);
        verify(empruntRepository, times(1)).save(any(Emprunt.class));
    }

    // ============================================================
    // TEST 5 : creerEmprunt() avec un livre NON disponible
    // ============================================================
    @Test
    void creerEmprunt_devraitLancerException_quandLivreIndisponible() {
        // GIVEN : le livre existe mais n'est pas disponible
        EmpruntRequestDTO request = new EmpruntRequestDTO(10L, 20L);
        when(livreClient.getLivreById(10L)).thenReturn(livreIndisponible);

        // WHEN + THEN
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> empruntService.creerEmprunt(request));

        assertTrue(exception.getMessage().contains("disponible"));

        // Vérifier qu'on n'a PAS essayé de sauvegarder
        verify(empruntRepository, never()).save(any());
    }

    // ============================================================
    // TEST 6 : creerEmprunt() avec un emprunteur inexistant
    // ============================================================
    @Test
    void creerEmprunt_devraitLancerException_quandEmprunteurInexistant() {
        // GIVEN : le livre est ok mais l'emprunteur n'existe pas
        EmpruntRequestDTO request = new EmpruntRequestDTO(10L, 99L);
        when(livreClient.getLivreById(10L)).thenReturn(livreDisponible);
        when(emprunteurClient.getEmprunteurById(99L)).thenReturn(null);

        // WHEN + THEN
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> empruntService.creerEmprunt(request));

        assertTrue(exception.getMessage().contains("Emprunteur"));
        verify(empruntRepository, never()).save(any());
    }

    // ============================================================
    // TEST 7 : retournerLivre() avec un emprunt EN_COURS
    // ============================================================
    @Test
    void retournerLivre_devraitMettreAJourStatut_quandEmpruntEnCours() {
        // GIVEN
        Emprunt empruntRetourne = new Emprunt();
        empruntRetourne.setId(1L);
        empruntRetourne.setLivreId(10L);
        empruntRetourne.setEmprunteurId(20L);
        empruntRetourne.setDateEmprunt(LocalDate.now().minusDays(5));
        empruntRetourne.setDateRetourPrevue(LocalDate.now().plusDays(10));
        empruntRetourne.setStatut("RENDU");
        empruntRetourne.setDateRetourReelle(LocalDate.now());

        when(empruntRepository.findById(1L)).thenReturn(Optional.of(emprunt));
        when(empruntRepository.save(any(Emprunt.class))).thenReturn(empruntRetourne);

        // WHEN
        EmpruntDTO result = empruntService.retournerLivre(1L);

        // THEN
        assertEquals("RENDU", result.getStatut());
        assertNotNull(result.getDateRetourReelle());

        // Vérifier que le livre a bien été remis disponible
        verify(livreClient, times(1)).updateDisponibilite(10L, true);
    }

    // ============================================================
    // TEST 8 : retournerLivre() avec un emprunt déjà RENDU
    // ============================================================
    @Test
    void retournerLivre_devraitLancerException_quandEmpruntDejaRendu() {
        // GIVEN : l'emprunt est déjà RENDU
        emprunt.setStatut("RENDU");
        when(empruntRepository.findById(1L)).thenReturn(Optional.of(emprunt));

        // WHEN + THEN
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> empruntService.retournerLivre(1L));

        assertTrue(exception.getMessage().contains("cours"));
        verify(livreClient, never()).updateDisponibilite(anyLong(), anyBoolean());
    }

    // ============================================================
    // TEST 9 : delete() avec un id valide
    // ============================================================
    @Test
    void delete_devraitSupprimerEmprunt_quandIdExiste() {
        // GIVEN
        when(empruntRepository.existsById(1L)).thenReturn(true);

        // WHEN
        empruntService.delete(1L);

        // THEN
        verify(empruntRepository, times(1)).deleteById(1L);
    }

    // ============================================================
    // TEST 10 : delete() avec un id inexistant
    // ============================================================
    @Test
    void delete_devraitLancerException_quandIdExistePas() {
        // GIVEN
        when(empruntRepository.existsById(99L)).thenReturn(false);

        // WHEN + THEN
        assertThrows(RuntimeException.class, () -> empruntService.delete(99L));
        verify(empruntRepository, never()).deleteById(anyLong());
    }
}