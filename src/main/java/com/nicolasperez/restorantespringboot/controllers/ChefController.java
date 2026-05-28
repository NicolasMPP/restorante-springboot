package com.nicolasperez.restorantespringboot.controllers;

import com.nicolasperez.restorantespringboot.entities.Chef;
import com.nicolasperez.restorantespringboot.entities.Receta;
import com.nicolasperez.restorantespringboot.services.ChefService;
import com.nicolasperez.restorantespringboot.services.RecetaService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chefs")
@CrossOrigin("*")
public class ChefController {

    private final ChefService chefService;
    private final RecetaService recetaService;

    public ChefController(
            ChefService chefService,
            RecetaService recetaService
    ) {
        this.chefService  = chefService;
        this.recetaService = recetaService;
    }

    // ── GET /api/chefs ─────────────────────────────────────────
    // Usado por el modal de Agregar Alimento para poblar el select
    @GetMapping
    public ResponseEntity<List<Chef>> obtenerTodos() {
        return ResponseEntity.ok(
                chefService.obtenerOrdenados()
        );
    }

    // ── GET /api/chefs/{id} ────────────────────────────────────
    @GetMapping("/{id}")
    public ResponseEntity<Chef> obtenerPorId(
            @PathVariable Integer id
    ) {
        Chef chef = chefService.obtener(id);

        return chef != null
                ? ResponseEntity.ok(chef)
                : ResponseEntity.notFound().build();
    }

    // ── GET /api/chefs/cedula/{cedula} ─────────────────────────
    @GetMapping("/cedula/{cedula}")
    public ResponseEntity<Chef> obtenerPorCedula(
            @PathVariable String cedula
    ) {
        Chef chef = chefService.obtenerPorCedula(cedula);

        return chef != null
                ? ResponseEntity.ok(chef)
                : ResponseEntity.notFound().build();
    }

    // ── GET /api/chefs/{id}/recetas ────────────────────────────
    // Usado por el panel de detalle del chef en el menú
    @GetMapping("/{id}/recetas")
    public ResponseEntity<List<Receta>> obtenerRecetas(
            @PathVariable Integer id
    ) {
        return ResponseEntity.ok(
                recetaService.obtenerPorChef(id)
        );
    }
}