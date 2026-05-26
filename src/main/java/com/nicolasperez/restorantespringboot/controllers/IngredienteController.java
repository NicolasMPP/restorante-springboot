package com.nicolasperez.restorantespringboot.controllers;

import com.nicolasperez.restorantespringboot.entities.Ingrediente;
import com.nicolasperez.restorantespringboot.services.IngredienteService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ingredientes")
@CrossOrigin("*")
public class IngredienteController {

    private final IngredienteService ingredienteService;

    public IngredienteController(
            IngredienteService ingredienteService
    ) {
        this.ingredienteService = ingredienteService;
    }

    @GetMapping
    public ResponseEntity<List<Ingrediente>>
    obtenerTodos() {

        return ResponseEntity.ok(
                ingredienteService.obtenerTodos()
        );
    }

    @PutMapping("/{id}/stock")
    public ResponseEntity<String>
    actualizarStock(
            @PathVariable Integer id,
            @RequestParam Integer stock
    ) {

        boolean actualizado =
                ingredienteService
                        .actualizarStock(id, stock);

        if (!actualizado) {

            return ResponseEntity.badRequest()
                    .body("No se pudo actualizar");
        }

        return ResponseEntity.ok(
                "Stock actualizado"
        );
    }

    @PostMapping
    public ResponseEntity<Ingrediente> crear(
            @RequestBody Ingrediente ingrediente
    ) {
        return ResponseEntity.ok(
                ingredienteService.guardar(ingrediente)
        );
    }

}