package com.nicolasperez.restorantespringboot.controllers;

import com.nicolasperez.restorantespringboot.entities.Alimento;
import com.nicolasperez.restorantespringboot.services.AlimentoService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alimentos")
@CrossOrigin("*")
public class AlimentoController {

    private final AlimentoService alimentoService;

    public AlimentoController(
            AlimentoService alimentoService
    ) {
        this.alimentoService = alimentoService;
    }

    @GetMapping
    public ResponseEntity<List<Alimento>> obtenerTodos() {

        return ResponseEntity.ok(
                alimentoService.obtenerTodos()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Alimento> obtenerPorId(
            @PathVariable Integer id
    ) {

        Alimento alimento =
                alimentoService.obtenerAlimento(id);

        if (alimento == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(alimento);
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<Alimento>> buscarPorNombre(
            @RequestParam String nombre
    ) {

        return ResponseEntity.ok(
                alimentoService.buscarPorNombre(nombre)
        );
    }

    @PostMapping
    public ResponseEntity<Alimento> crear(
            @RequestBody Alimento alimento
    ) {

        Alimento nuevo =
                alimentoService.actualizar(alimento);

        return ResponseEntity.ok(nuevo);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(
            @PathVariable Integer id
    ) {

        boolean eliminado =
                alimentoService.eliminar(id);

        if (!eliminado) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok("Alimento eliminado");
    }
}