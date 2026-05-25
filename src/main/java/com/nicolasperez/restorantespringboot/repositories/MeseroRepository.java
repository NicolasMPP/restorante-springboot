// ============================================================
// MeseroRepository
// ============================================================

package com.nicolasperez.restorantespringboot.repositories;

import com.nicolasperez.restorantespringboot.entities.Mesero;

import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface MeseroRepository extends JpaRepository<Mesero, Integer> {

    Optional<Mesero> findByCedula(String cedula);

    Optional<Mesero> findByUsuario(String usuario);

    List<Mesero> findAllByOrderByNombreAsc();

    List<Mesero> findByNombreContainingIgnoreCaseOrderByNombreAsc(
            String nombre
    );

    List<Mesero> findBySalarioGreaterThanEqualOrderBySalarioDesc(
            BigDecimal salario
    );

    boolean existsByCedula(String cedula);

    boolean existsByUsuario(String usuario);

    Optional<Mesero> findByUsuarioAndContrasenia(
            String usuario,
            String contrasenia
    );
}