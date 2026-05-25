package com.nicolasperez.restorantespringboot.services;

import com.nicolasperez.restorantespringboot.entities.*;
import com.nicolasperez.restorantespringboot.repositories.AlimentoRepository;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public class AlimentoService {

    private final AlimentoRepository alimentoRepository;

    public AlimentoService(
            AlimentoRepository alimentoRepository
    ) {
        this.alimentoRepository = alimentoRepository;
    }

    // ============================================================
    // CRUD
    // ============================================================

    public Alimento crearAlimento(
            String nombre,
            BigDecimal precio,
            String tipo,
            Receta receta
    ) {

        if (nombre == null || nombre.trim().isEmpty()) {
            System.err.println("Nombre obligatorio");
            return null;
        }

        if (precio == null || precio.compareTo(BigDecimal.ZERO) <= 0) {
            System.err.println("Precio inválido");
            return null;
        }

        Alimento alimento = switch (tipo) {

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

        return alimentoRepository.save(alimento);
    }

    public Alimento obtenerAlimento(Integer id) {

        return alimentoRepository
                .findById(id)
                .orElse(null);
    }

    public List<Alimento> obtenerTodos() {
        return alimentoRepository.findAll();
    }

    public Alimento actualizar(Alimento alimento) {
        return alimentoRepository.save(alimento);
    }

    public boolean eliminar(Integer id) {

        if (!alimentoRepository.existsById(id)) {
            return false;
        }

        alimentoRepository.deleteById(id);

        return true;
    }

    // ============================================================
    // BUSQUEDAS
    // ============================================================

    public List<Alimento> buscarPorNombre(String nombre) {

        return alimentoRepository
                .findByNombreContainingIgnoreCase(nombre);
    }

    public List<Alimento> buscarPorPrecioMenor(Double precio) {

        return alimentoRepository
                .findByPrecioLessThanEqual(precio);
    }

    public List<Alimento> buscarPorPrecioMayor(Double precio) {

        return alimentoRepository
                .findByPrecioGreaterThanEqual(precio);
    }
}