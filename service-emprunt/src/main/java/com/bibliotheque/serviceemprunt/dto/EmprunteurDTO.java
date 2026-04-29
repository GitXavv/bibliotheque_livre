package com.bibliotheque.serviceemprunt.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmprunteurDTO {
    private Long id;
    private String nom;
    private String email;
    private String telephone;
}
