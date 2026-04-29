package com.bibliotheque.serviceemprunt.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LivreDTO {
    private Long id;
    private String titre;
    private String auteur;
    private String isbn;
    private Boolean disponible;
}