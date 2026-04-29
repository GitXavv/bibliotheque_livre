package com.example.Service_emprunteur.Service;

import com.example.Service_emprunteur.DTO.EmprunteurDTO;
import com.example.Service_emprunteur.Entity.Emprunteur;
import com.example.Service_emprunteur.Repository.EmprunteurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.data.projection.EntityProjection.ProjectionType.DTO;

@Service
@RequiredArgsConstructor
public class EmprunteurService
{
    private final EmprunteurRepository repository;

    // Conversion Entité -> DTO
    private EmprunteurDTO mapToDTO(Emprunteur entity) {
        return new EmprunteurDTO(entity.getId(), entity.getNom(), entity.getEmail(), entity.getTelephone());
    }

    public List<EmprunteurDTO> getAll() {
        return repository.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public EmprunteurDTO getById(Long id) {
        return repository.findById(id).map(this::mapToDTO).orElse(null);
    }

    public EmprunteurDTO save(EmprunteurDTO dto) {
        Emprunteur entity = new Emprunteur(null, dto.getNom(), dto.getEmail(), dto.getTelephone());
        Emprunteur saved = repository.save(entity);
        return mapToDTO(saved);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}

