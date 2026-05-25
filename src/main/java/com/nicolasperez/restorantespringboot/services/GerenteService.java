package com.nicolasperez.restorantespringboot.services;

import com.nicolasperez.restorantespringboot.entities.Gerente;
import com.nicolasperez.restorantespringboot.repositories.GerenteRepository;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class GerenteService {

    private final GerenteRepository gerenteRepository;

    public GerenteService(
            GerenteRepository gerenteRepository
    ) {
        this.gerenteRepository = gerenteRepository;
    }

    // ============================================================
    // CRUD
    // ============================================================

    public Gerente guardar(Gerente gerente) {

        return gerenteRepository.save(gerente);
    }

    public Gerente obtener(Integer id) {

        return gerenteRepository
                .findById(id)
                .orElse(null);
    }

    public List<Gerente> obtenerTodos() {

        return gerenteRepository.findAll();
    }

    public boolean eliminar(Integer id) {

        if (!gerenteRepository.existsById(id)) {
            return false;
        }

        gerenteRepository.deleteById(id);

        return true;
    }

    // ============================================================
    // BUSQUEDAS
    // ============================================================

    public Gerente obtenerPorCedula(
            String cedula
    ) {

        return gerenteRepository
                .findByCedula(cedula)
                .orElse(null);
    }

    public Gerente obtenerPorNombre(
            String nombre
    ) {

        return gerenteRepository
                .buscarPorNombre(nombre)
                .orElse(null);
    }
}