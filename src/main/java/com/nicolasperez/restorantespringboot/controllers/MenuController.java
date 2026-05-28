package com.nicolasperez.restorantespringboot.controllers;

import com.nicolasperez.restorantespringboot.dto.AlimentoCompletoRequest;
import com.nicolasperez.restorantespringboot.dto.AlimentoDetalleDTO;
import com.nicolasperez.restorantespringboot.entities.Menu;
import com.nicolasperez.restorantespringboot.repositories.MenuRepository;
import com.nicolasperez.restorantespringboot.services.MenuService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/menu")
@CrossOrigin("*")
public class MenuController {

    private final MenuService menuService;

    public MenuController(
            MenuService menuService
    ) {
        this.menuService = menuService;
    }

    // ============================================================
    // OBTENER MENÚ COMPLETO
    // ============================================================

    @GetMapping("/{id}")
    public ResponseEntity<Menu> obtenerMenu(
            @PathVariable Integer id
    ) {

        Menu menu = menuService.obtenerMenuCompleto(id);

        if (menu == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(menu);
    }

    // ============================================================
    // OBTENER DETALLES DE ALIMENTOS
    // ============================================================

    @GetMapping("/{id}/alimentos")
    public ResponseEntity<List<AlimentoDetalleDTO>> obtenerAlimentosConDetalles(
            @PathVariable Integer id
    ) {
        return ResponseEntity.ok(
                menuService.obtenerAlimentosConDetalles(id)
        );
    }

    // ============================================================
    // AGREGAR ALIMENTO AL MENÚ
    // ============================================================

    @PostMapping("/{menuId}/alimentos/{alimentoId}")
    public ResponseEntity<String> agregarAlimento(
            @PathVariable Integer menuId,
            @PathVariable Integer alimentoId
    ) {

        boolean resultado =
                menuService.agregarAlimentoAlMenu(
                        menuId,
                        alimentoId
                );

        if (!resultado) {

            return ResponseEntity.badRequest()
                    .body("No se pudo agregar alimento");
        }

        return ResponseEntity.ok(
                "Alimento agregado correctamente"
        );
    }
    // ── POST /api/menu/{menuId}/alimento-completo ──────────────────
// Crea receta + ingredientes + alimento y lo agrega al menú
// Body: AlimentoCompletoRequest
    @PostMapping("/{menuId}/alimento-completo")
    public ResponseEntity<String> crearAlimentoCompleto(
            @PathVariable Integer menuId,
            @RequestBody AlimentoCompletoRequest request
    ) {
        boolean ok = menuService.crearAlimentoCompleto(
                request.getNombreAlimento(),
                request.getPrecio(),
                request.getTipo(),
                request.getNombreReceta(),
                request.getDescripcionProceso(),
                request.getChefCedula(),
                request.getIngredientesDescripciones(),
                menuId
        );

        return ok
                ? ResponseEntity.ok("Alimento creado correctamente")
                : ResponseEntity.badRequest().body("No se pudo crear el alimento");
    }
    // ============================================================
    // ESTADÍSTICAS
    // ============================================================

//    @GetMapping("/estadisticas")
//    public ResponseEntity<List<MenuRepository.MenuEstadisticasDTO>>
//    obtenerEstadisticas() {
//
//        return ResponseEntity.ok(
//                menuService.obtenerMenusConMasAlimentos()
//        );
//    }
}