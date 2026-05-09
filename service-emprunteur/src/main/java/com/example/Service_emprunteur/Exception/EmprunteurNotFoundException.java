package com.example.Service_emprunteur.Exception;

public class EmprunteurNotFoundException extends RuntimeException {
    public EmprunteurNotFoundException(Long id) {
        super("Le id n'existe pas");
    }
}
