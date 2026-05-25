package com.nicolasperez.restorantespringboot.services;

import com.nicolasperez.restorantespringboot.entities.Ingrediente;
import com.nicolasperez.restorantespringboot.repositories.IngredienteRepository;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class IngredienteService {

    private final IngredienteRepository ingredienteRepository;

    public IngredienteService(
            IngredienteRepository ingredienteRepository
    ) {
        this.ingredienteRepository = ingredienteRepository;
    }

    // ============================================================
    // CRUD
    // ============================================================

    public Ingrediente guardar(
            Ingrediente ingrediente
    ) {

        return ingredienteRepository.save(ingrediente);
    }

    public Ingrediente obtener(Integer id) {

        return ingredienteRepository
                .findById(id)
                .orElse(null);
    }

    public List<Ingrediente> obtenerTodos() {

        return ingredienteRepository.findAll();
    }

    public Ingrediente actualizar(
            Ingrediente ingrediente
    ) {

        return ingredienteRepository.save(ingrediente);
    }

    public boolean eliminar(Integer id) {

        if (!ingredienteRepository.existsById(id)) {
            return false;
        }

        ingredienteRepository.deleteById(id);

        return true;
    }

    // ============================================================
    // BUSQUEDAS
    // ============================================================

    public Ingrediente obtenerPorDescripcion(
            String descripcion
    ) {

        return ingredienteRepository
                .findByDescripcion(descripcion)
                .orElse(null);
    }

    public List<Ingrediente> buscarStockBajo(
            Integer stock
    ) {

        return ingredienteRepository
                .findByCantidadStockLessThan(stock);
    }

    // ============================================================
    // STOCK
    // ============================================================

    public boolean actualizarStock(
            Integer ingredienteId,
            Integer nuevoStock
    ) {

        if (nuevoStock < 0) {

            System.err.println(
                    "Stock no puede ser negativo"
            );

            return false;
        }

        Ingrediente ingrediente =
                obtener(ingredienteId);

        if (ingrediente == null) {

            System.err.println(
                    "Ingrediente no encontrado"
            );

            return false;
        }

        ingrediente.setCantidadStock(nuevoStock);

        ingredienteRepository.save(ingrediente);

        return true;
    }
}