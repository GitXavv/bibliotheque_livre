package com.example.Service_emprunteur.Controller;

import com.example.Service_emprunteur.DTO.EmprunteurDTO;
import com.example.Service_emprunteur.Service.EmprunteurService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/emprunteurs")
@RequiredArgsConstructor

public class EmprunteurController {
    private final EmprunteurService service;

    @GetMapping
    public List<EmprunteurDTO>getdAll(){
        return service.getAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EmprunteurDTO create(@RequestBody EmprunteurDTO emprunteurDTO){
        return service.save(emprunteurDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id){}



}
