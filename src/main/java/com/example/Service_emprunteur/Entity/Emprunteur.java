package com.example.Service_emprunteur.Entity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Length;

@Entity
@Table(name = "emprunteur")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor

public class Emprunteur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable=false)
    private String nom;

    @Column(unique = true, nullable = false)
    private String email;

    private String telephone;

    public Emprunteur(Long o, String nom, String email, String telephone) {
    }
}
