package com.nicolasperez.restorantespringboot.services;

import com.nicolasperez.restorantespringboot.entities.Chef;
import com.nicolasperez.restorantespringboot.repositories.ChefRepository;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class ChefService {

    private final ChefRepository chefRepository;

    public ChefService(
            ChefRepository chefRepository
    ) {
        this.chefRepository = chefRepository;
    }

    // ============================================================
    // CRUD
    // ============================================================

    public Chef guardar(Chef chef) {

        return chefRepository.save(chef);
    }

    public Chef obtener(Integer id) {

        return chefRepository
                .findById(id)
                .orElse(null);
    }

    public List<Chef> obtenerTodos() {

        return chefRepository.findAll();
    }

    public boolean eliminar(Integer id) {

        if (!chefRepository.existsById(id)) {
            return false;
        }

        chefRepository.deleteById(id);

        return true;
    }

    // ============================================================
    // BUSQUEDAS
    // ============================================================

    public Chef obtenerPorNombre(String nombre) {

        return chefRepository
                .buscarPorNombre(nombre)
                .orElse(null);
    }

    public Chef obtenerPorCedula(String cedula) {

        return chefRepository
                .findByCedula(cedula)
                .orElse(null);
    }

    public List<Chef> obtenerOrdenados() {

        return chefRepository
                .buscarTodosOrdenados();
    }
}