// ============================================================
// ClienteRepository
// ============================================================

package com.nicolasperez.restorantespringboot.repositories;

import com.nicolasperez.restorantespringboot.entities.Cliente;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ClienteRepository extends JpaRepository<Cliente, Integer> {

    Optional<Cliente> findByCedula(String cedula);

    Optional<Cliente> findByCorreo(String correo);

    List<Cliente> findAllByOrderByNombreAsc();

    List<Cliente> findByNombreContainingIgnoreCaseOrderByNombreAsc(
            String nombre
    );

    boolean existsByCedula(String cedula);

    boolean existsByCorreo(String correo);
}