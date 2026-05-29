package com.nicolasperez.restorantespringboot.services;

import com.nicolasperez.restorantespringboot.dto.AlimentoDetalleDTO;
import com.nicolasperez.restorantespringboot.entities.*;
import com.nicolasperez.restorantespringboot.repositories.*;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class MenuService {

    private final MenuRepository menuRepository;
    private final AlimentoRepository alimentoRepository;
    private final RecetaRepository recetaRepository;
    private final ChefRepository chefRepository;
    private final IngredienteRepository ingredienteRepository;

    public MenuService(
            MenuRepository menuRepository,
            AlimentoRepository alimentoRepository,
            RecetaRepository recetaRepository,
            ChefRepository chefRepository,
            IngredienteRepository ingredienteRepository
    ) {
        this.menuRepository = menuRepository;
        this.alimentoRepository = alimentoRepository;
        this.recetaRepository = recetaRepository;
        this.chefRepository = chefRepository;
        this.ingredienteRepository = ingredienteRepository;
    }

    // ============================================================
    // MENU
    // ============================================================

    public Menu obtenerMenuCompleto(Integer menuId) {
        return menuRepository.obtenerMenuCompleto(menuId)
                .orElse(null);
    }

    public List<AlimentoDetalleDTO> obtenerAlimentosConDetalles(Integer menuId) {
        return menuRepository
                .obtenerAlimentosDelMenuConDetalles(menuId)
                .stream()
                .map(this::mapearADTO)
                .sorted(Comparator.comparing(AlimentoDetalleDTO::getAlimentoNombre))
                .collect(Collectors.toList());
    }

    private AlimentoDetalleDTO mapearADTO(Alimento a) {

        Receta   r    = a.getReceta();
        Empleado chef = (r != null) ? r.getChef() : null;

        String tipo = switch (a.getClass().getSimpleName()) {
            case "PlatoFuerte"  -> "PLATO_FUERTE";
            case "Postres"      -> "POSTRE";
            case "Bebida"       -> "BEBIDA";
            case "Adicionales"  -> "ADICIONAL";
            default             -> "GENERAL";
        };

        return new AlimentoDetalleDTO(
                a.getId(),
                a.getNombre(),
                a.getPrecio(),
                tipo,
                r    != null ? r.getId()              : null,      // recetaId
                r    != null ? r.getNombreReceta()     : "Sin receta",
                r    != null ? r.getDescripcionProceso(): "No requiere preparación",
                r    != null ? (long) r.getIngredientes().size() : 0L,
                chef != null ? chef.getId()            : null,     // chefId
                chef != null ? chef.getNombre()        : "N/A"
        );
    }

    public boolean agregarAlimentoAlMenu(
            Integer menuId,
            Integer alimentoId
    ) {

        Optional<Menu> menuOpt = menuRepository.findById(menuId);

        if (menuOpt.isEmpty()) {
            System.err.println("Error: menú no existe");
            return false;
        }

        Optional<Alimento> alimentoOpt =
                alimentoRepository.findById(alimentoId);

        if (alimentoOpt.isEmpty()) {
            System.err.println("Error: alimento no existe");
            return false;
        }

        Menu menu = menuOpt.get();

        menu.getAlimentos().add(alimentoOpt.get());

        menuRepository.save(menu);

        return true;
    }

    // ============================================================
    // OPERACION COMPLEJA
    // ============================================================

    public boolean crearAlimentoCompleto(
            String nombreAlimento,
            BigDecimal precio,
            String tipo,
            String nombreReceta,
            String descripcionProceso,
            String chefCedula,
            List<String> ingredientesDescripciones,
            Integer menuId
    ) {

        try {

            Chef chef = chefRepository
                    .findByCedula(chefCedula)
                    .orElse(null);

            if (chef == null) {
                System.err.println("Chef no encontrado");
                return false;
            }

            Receta receta = new Receta(
                    nombreReceta,
                    descripcionProceso,
                    chef
            );

            receta = recetaRepository.save(receta);

            for (String descripcion : ingredientesDescripciones) {

                ingredienteRepository
                        .findByDescripcion(descripcion)
                        .ifPresent(
                                receta.getIngredientes()::add
                        );
            }

            receta = recetaRepository.save(receta);

            Alimento alimento =
                    crearAlimentoPorTipo(
                            nombreAlimento,
                            precio,
                            tipo,
                            receta
                    );

            alimento = alimentoRepository.save(alimento);

            return agregarAlimentoAlMenu(
                    menuId,
                    alimento.getId()
            );

        } catch (Exception e) {

            System.err.println(
                    "Error al crear alimento completo: "
                            + e.getMessage()
            );

            return false;
        }
    }
    // ── Tarea 4 ────────────────────────────────────────────────────
// Actualiza nombre, precio, tipo, receta, chef e ingredientes.
// Si el alimento no tenía receta, la crea.
// Si el tipo cambió, actualiza el discriminador via SQL nativo.
    public boolean actualizarAlimentoCompleto(
            Integer      alimentoId,
            String       nombreAlimento,
            BigDecimal   precio,
            String       tipo,
            String       nombreReceta,
            String       descripcionProceso,
            String       chefCedula,
            List<String> ingredientesDescripciones
    ) {
        // 1. Buscar alimento existente
        Alimento alimento = alimentoRepository
                .findById(alimentoId)
                .orElse(null);

        if (alimento == null) {
            System.err.println("Alimento no encontrado: " + alimentoId);
            return false;
        }

        // 2. Actualizar campos básicos
        alimento.setNombre(nombreAlimento);
        alimento.setPrecio(precio);
        alimentoRepository.save(alimento);

        // 3. Actualizar tipo si cambió
        //    Hibernate no toca el discriminador en merge, lo hacemos
        //    con SQL nativo dentro de la misma transacción
        String tipoActual = switch (alimento.getClass().getSimpleName()) {
            case "PlatoFuerte"  -> "PLATO_FUERTE";
            case "Postres"      -> "POSTRE";
            case "Bebida"       -> "BEBIDA";
            case "Adicionales"  -> "ADICIONAL";
            default             -> "GENERAL";
        };

        if (!tipoActual.equals(tipo)) {
            alimentoRepository.actualizarTipo(alimentoId, tipo);
        }

        // 4. Buscar chef por cédula
        Chef chef = chefRepository
                .findByCedula(chefCedula)
                .orElse(null);

        if (chef == null) {
            System.err.println("Chef no encontrado: " + chefCedula);
            return false;
        }

        // 5. Actualizar receta (o crear si el alimento no tenía)
        Receta receta = alimento.getReceta();

        if (receta == null) {

            // El alimento no tenía receta (era bebida, etc.) → crear
            receta = new Receta(nombreReceta, descripcionProceso, chef);
            receta = recetaRepository.save(receta);

            alimento.setReceta(receta);
            alimentoRepository.save(alimento);

        } else {

            // Actualizar campos de la receta existente
            receta.setNombreReceta(nombreReceta);
            receta.setDescripcionProceso(descripcionProceso);
            receta.setChef(chef);

            // 6. Sincronizar ingredientes:
            //    limpiar la lista actual y re-agregar los seleccionados
            receta.getIngredientes().clear();

            for (String descripcion : ingredientesDescripciones) {
                ingredienteRepository
                        .findByDescripcion(descripcion)
                        .ifPresent(receta.getIngredientes()::add);
            }

            recetaRepository.save(receta);
        }

        return true;
    }
    // ── Tarea 2: GET alimento completo ────────────────────────────
    public Alimento obtenerAlimentoDelMenu(
            Integer menuId,
            Integer alimentoId
    ) {
        return menuRepository
                .obtenerAlimentoCompleto(menuId, alimentoId)
                .orElse(null);
    }

    // ── Tarea 1: DELETE — desvincular del menú ────────────────────
    public boolean removerAlimentoDelMenu(
            Integer menuId,
            Integer alimentoId
    ) {
        Optional<Menu> menuOpt =
                menuRepository.obtenerMenuCompleto(menuId);

        Optional<Alimento> alimentoOpt =
                alimentoRepository.findById(alimentoId);

        if (menuOpt.isEmpty() || alimentoOpt.isEmpty()) {
            return false;
        }

        Menu menu = menuOpt.get();

        // Elimina la relación en menu_alimentos, no el alimento
        menu.getAlimentos().remove(alimentoOpt.get());

        menuRepository.save(menu);

        return true;
    }

    // ============================================================
    // UTILIDAD
    // ============================================================

    private Alimento crearAlimentoPorTipo(
            String nombre,
            BigDecimal precio,
            String tipo,
            Receta receta
    ) {

        return switch (tipo) {

            case "PLATO_FUERTE" ->
                    new PlatoFuerte(nombre, precio, receta);

            case "POSTRE" ->
                    new Postres(nombre, precio, receta);

            case "BEBIDA" ->
                    new Bebida(nombre, precio, receta);

            case "ADICIONAL" ->
                    new Adicionales(nombre, precio, receta);

            default ->
                    new Alimento(nombre, precio, receta);
        };
    }
}