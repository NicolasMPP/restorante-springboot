package com.nicolasperez.restorantespringboot.controllers;

import com.nicolasperez.restorantespringboot.dto.AlimentoCompletoRequest;
import com.nicolasperez.restorantespringboot.dto.AlimentoDetalleDTO;
import com.nicolasperez.restorantespringboot.entities.Alimento;
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
// ── Tarea 3 ────────────────────────────────────────────────────
// PUT /api/menu/{menuId}/alimento-completo/{alimentoId}
// Body: AlimentoCompletoRequest (mismo DTO que el POST)
    @PutMapping("/{menuId}/alimento-completo/{alimentoId}")
    public ResponseEntity<String> actualizarAlimentoCompleto(
            @PathVariable Integer menuId,
            @PathVariable Integer alimentoId,
            @RequestBody  AlimentoCompletoRequest request
    ) {
        boolean ok = menuService.actualizarAlimentoCompleto(
                alimentoId,
                request.getNombreAlimento(),
                request.getPrecio(),
                request.getTipo(),
                request.getNombreReceta(),
                request.getDescripcionProceso(),
                request.getChefCedula(),
                request.getIngredientesDescripciones()
        );

        return ok
                ? ResponseEntity.ok("Alimento actualizado correctamente")
                : ResponseEntity.badRequest()
                .body("No se pudo actualizar el alimento");
    }

// ── Tarea 2: GET /api/menu/{menuId}/alimentos/{alimentoId} ────
// Devuelve alimento completo (receta + chef + ingredientes)
// para pre-poblar el modal de edición en el frontend
    @GetMapping("/{menuId}/alimentos/{alimentoId}")
    public ResponseEntity<Alimento> obtenerAlimentoCompleto(
            @PathVariable Integer menuId,
            @PathVariable Integer alimentoId
    ) {
        Alimento alimento =
                menuService.obtenerAlimentoDelMenu(
                        menuId,
                        alimentoId
                );

        return alimento != null
                ? ResponseEntity.ok(alimento)
                : ResponseEntity.notFound().build();
    }

// ── Tarea 1: DELETE /api/menu/{menuId}/alimentos/{alimentoId} ─
// Desvincula el alimento del menú (borra fila en menu_alimentos)
// El alimento sigue existiendo en la tabla alimentos
    @DeleteMapping("/{menuId}/alimentos/{alimentoId}")
    public ResponseEntity<String> removerAlimento(
            @PathVariable Integer menuId,
            @PathVariable Integer alimentoId
    ) {
        boolean ok =
                menuService.removerAlimentoDelMenu(
                        menuId,
                        alimentoId
                );

        return ok
                ? ResponseEntity.ok("Alimento removido del menú")
                : ResponseEntity.badRequest()
                .body("No se pudo remover el alimento");
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