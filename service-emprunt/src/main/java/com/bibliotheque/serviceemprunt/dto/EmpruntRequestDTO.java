package com.bibliotheque.serviceemprunt.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmpruntRequestDTO {

    private Long livreId;
    private String emprunteurId;

    public EmpruntRequestDTO(long livreId, long l) {
    }
}
