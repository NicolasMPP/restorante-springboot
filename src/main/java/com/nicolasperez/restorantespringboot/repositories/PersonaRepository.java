// ============================================================
// PersonaRepository
// ============================================================

package com.nicolasperez.restorantespringboot.repositories;

import com.nicolasperez.restorantespringboot.entities.Persona;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PersonaRepository extends JpaRepository<Persona, Integer> {

    Optional<Persona> findByCedula(String cedula);

    List<Persona> findAllByOrderByNombreAsc();

    List<Persona> findByNombreContainingIgnoreCaseOrderByNombreAsc(
            String nombre
    );

    boolean existsByCedula(String cedula);

    boolean existsByCorreo(String correo);

    Optional<Persona> findByCorreo(String correo);
}