package com.example.Service_emprunteur.Repository;

import com.example.Service_emprunteur.Entity.Emprunteur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmprunteurRepository extends JpaRepository<Emprunteur, Long>
{
}
