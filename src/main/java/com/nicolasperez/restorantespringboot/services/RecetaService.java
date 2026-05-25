package com.nicolasperez.restorantespringboot.services;

import com.nicolasperez.restorantespringboot.entities.Chef;
import com.nicolasperez.restorantespringboot.entities.Ingrediente;
import com.nicolasperez.restorantespringboot.entities.Receta;
import com.nicolasperez.restorantespringboot.repositories.IngredienteRepository;
import com.nicolasperez.restorantespringboot.repositories.RecetaRepository;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class RecetaService {

    private final RecetaRepository recetaRepository;
    private final IngredienteRepository ingredienteRepository;

    public RecetaService(
            RecetaRepository recetaRepository,
            IngredienteRepository ingredienteRepository
    ) {
        this.recetaRepository = recetaRepository;
        this.ingredienteRepository = ingredienteRepository;
    }

    // ============================================================
    // CRUD
    // ============================================================

    public Receta crearReceta(
            String nombreReceta,
            String descripcionProceso,
            Chef chef
    ) {

        if (nombreReceta == null
                || nombreReceta.trim().isEmpty()) {

            System.err.println("Nombre obligatorio");
            return null;
        }

        if (chef == null) {

            System.err.println("Chef obligatorio");
            return null;
        }

        Receta receta = new Receta(
                nombreReceta,
                descripcionProceso,
                chef
        );

        return recetaRepository.save(receta);
    }

    public Receta obtenerPorId(Integer id) {

        return recetaRepository
                .buscarCompletaPorId(id)
                .orElse(null);
    }

    public List<Receta> obtenerTodas() {

        return recetaRepository.buscarTodasCompletas();
    }

    public Receta actualizar(Receta receta) {

        return recetaRepository.save(receta);
    }

    public boolean eliminar(Integer id) {

        if (!recetaRepository.existsById(id)) {
            return false;
        }

        recetaRepository.deleteById(id);

        return true;
    }

    // ============================================================
    // BUSQUEDAS
    // ============================================================

    public Receta obtenerPorNombre(String nombre) {

        return recetaRepository
                .buscarPorNombre(nombre)
                .orElse(null);
    }

    public List<Receta> obtenerPorChef(Integer chefId) {

        return recetaRepository
                .buscarPorChef(chefId);
    }

    public List<Receta> buscarPorIngrediente(
            Integer ingredienteId
    ) {

        return recetaRepository
                .buscarPorIngrediente(ingredienteId);
    }

    public List<Receta> obtenerConIngredientesDisponibles(
            Integer stockMinimo
    ) {

        return recetaRepository
                .buscarConIngredientesDisponibles(stockMinimo);
    }

    // ============================================================
    // INGREDIENTES
    // ============================================================

    public boolean agregarIngrediente(
            Integer recetaId,
            Integer ingredienteId
    ) {

        Optional<Receta> recetaOpt =
                recetaRepository.findById(recetaId);

        Optional<Ingrediente> ingredienteOpt =
                ingredienteRepository.findById(ingredienteId);

        if (recetaOpt.isEmpty()
                || ingredienteOpt.isEmpty()) {

            return false;
        }

        Receta receta = recetaOpt.get();

        receta.getIngredientes()
                .add(ingredienteOpt.get());

        recetaRepository.save(receta);

        return true;
    }

    public boolean removerIngrediente(
            Integer recetaId,
            Integer ingredienteId
    ) {

        Optional<Receta> recetaOpt =
                recetaRepository.findById(recetaId);

        Optional<Ingrediente> ingredienteOpt =
                ingredienteRepository.findById(ingredienteId);

        if (recetaOpt.isEmpty()
                || ingredienteOpt.isEmpty()) {

            return false;
        }

        Receta receta = recetaOpt.get();

        receta.getIngredientes()
                .remove(ingredienteOpt.get());

        recetaRepository.save(receta);

        return true;
    }

    // ============================================================
    // ESTADISTICAS
    // ============================================================

    public List<RecetaRepository.RecetaComplejidadDTO>
    obtenerRecetasPorComplejidad() {

        return recetaRepository
                .obtenerRecetasPorComplejidad();
    }
}