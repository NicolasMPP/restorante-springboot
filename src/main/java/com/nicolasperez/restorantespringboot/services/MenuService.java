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

        Receta r = a.getReceta();

        String tipo = switch (a.getClass().getSimpleName()) {
            case "PlatoFuerte"  -> "PLATO_FUERTE";
            case "Postres"      -> "POSTRE";
            case "Bebida"       -> "BEBIDA";
            case "Adicionales"  -> "ADICIONAL";
            default             -> "GENERAL";
        };

        String nombreReceta      = r != null ? r.getNombreReceta()       : "Sin receta";
        String descripcion       = r != null ? r.getDescripcionProceso() : "No requiere preparación";
        String chefNombre        = (r != null && r.getChef() != null)
                ? r.getChef().getNombre()
                : "N/A";
        long   totalIngredientes = r != null ? r.getIngredientes().size() : 0L;

        return new AlimentoDetalleDTO(
                a.getId(),
                a.getNombre(),
                a.getPrecio(),
                tipo,
                nombreReceta,
                descripcion,
                chefNombre,
                totalIngredientes
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