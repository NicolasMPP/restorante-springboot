package com.nicolasperez.restorantespringboot.controllers;

import com.nicolasperez.restorantespringboot.dto.IngredienteDetalleDTO;
import com.nicolasperez.restorantespringboot.entities.Despensa;
import com.nicolasperez.restorantespringboot.entities.Ingrediente;
import com.nicolasperez.restorantespringboot.services.DespensaService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/despensa")
@CrossOrigin("*")
public class DespensaController {

    private final DespensaService despensaService;

    public DespensaController(
            DespensaService despensaService
    ) {
        this.despensaService = despensaService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Despensa>
    obtenerDespensa(
            @PathVariable Integer id
    ) {

        Despensa despensa =
                despensaService.obtenerDespensa(id);

        if (despensa == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(despensa);
    }

    @GetMapping("/{id}/ingredientes")
    public ResponseEntity<List<IngredienteDetalleDTO>>
    obtenerIngredientes(
            @PathVariable Integer id
    ) {

        return ResponseEntity.ok(
                despensaService.obtenerIngredientesDespensa(id)
        );
    }

    @GetMapping("/{id}/stock-bajo")
    public ResponseEntity<List<Ingrediente>>
    stockBajo(
            @PathVariable Integer id,
            @RequestParam Integer umbral
    ) {

        return ResponseEntity.ok(
                despensaService
                        .obtenerIngredientesConStockBajo(
                                id,
                                umbral
                        )
        );
    }

    @GetMapping("/{id}/estadisticas")
    public ResponseEntity<
            DespensaService.DespensaEstadisticas>
    estadisticas(
            @PathVariable Integer id
    ) {

        return ResponseEntity.ok(
                despensaService
                        .obtenerEstadisticasDespensa(id)
        );
    }
}