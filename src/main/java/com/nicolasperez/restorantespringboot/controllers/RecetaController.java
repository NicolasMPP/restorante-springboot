package com.nicolasperez.restorantespringboot.controllers;

import com.nicolasperez.restorantespringboot.entities.Receta;
import com.nicolasperez.restorantespringboot.repositories.RecetaRepository;
import com.nicolasperez.restorantespringboot.services.RecetaService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recetas")
@CrossOrigin("*")
public class RecetaController {

    private final RecetaService recetaService;

    public RecetaController(
            RecetaService recetaService
    ) {
        this.recetaService = recetaService;
    }

    @GetMapping
    public ResponseEntity<List<Receta>>
    obtenerTodas() {

        return ResponseEntity.ok(
                recetaService.obtenerTodas()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Receta>
    obtenerPorId(
            @PathVariable Integer id
    ) {

        Receta receta =
                recetaService.obtenerPorId(id);

        if (receta == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(receta);
    }

    @GetMapping("/chef/{chefId}")
    public ResponseEntity<List<Receta>>
    obtenerPorChef(
            @PathVariable Integer chefId
    ) {

        return ResponseEntity.ok(
                recetaService.obtenerPorChef(chefId)
        );
    }

    @PostMapping("/{recetaId}/ingredientes/{ingredienteId}")
    public ResponseEntity<String>
    agregarIngrediente(
            @PathVariable Integer recetaId,
            @PathVariable Integer ingredienteId
    ) {

        boolean agregado =
                recetaService.agregarIngrediente(
                        recetaId,
                        ingredienteId
                );

        if (!agregado) {

            return ResponseEntity.badRequest()
                    .body("No se pudo agregar");
        }

        return ResponseEntity.ok(
                "Ingrediente agregado"
        );
    }

    @GetMapping("/complejidad")
    public ResponseEntity<
            List<RecetaRepository.RecetaComplejidadDTO>>
    obtenerComplejidad() {

        return ResponseEntity.ok(
                recetaService.obtenerRecetasPorComplejidad()
        );
    }
}