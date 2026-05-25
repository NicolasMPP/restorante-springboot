package com.nicolasperez.restorantespringboot.repositories;

import com.nicolasperez.restorantespringboot.entities.Gerente;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface GerenteRepository extends JpaRepository<Gerente, Integer> {

    Optional<Gerente> findByCedula(String cedula);

    Optional<Gerente> findByUsuario(String usuario);

    Optional<Gerente> findByUsuarioAndContrasenia(
            String usuario,
            String contrasenia
    );

    boolean existsByCedula(String cedula);

    boolean existsByUsuario(String usuario);

    List<Gerente> findAllByOrderByNombreAsc();

    List<Gerente> findByNombreContainingIgnoreCase(
            String nombre
    );

    Optional<Gerente> findByMenus_Id(Integer menuId);

    Optional<Gerente> findByDespensas_Id(Integer despensaId);

    @Query("""
        SELECT g
        FROM Gerente g
        WHERE LOWER(g.nombre)
        LIKE LOWER(CONCAT('%', :nombre, '%'))
    """)
    Optional<Gerente> buscarPorNombre(
            @Param("nombre") String nombre
    );
}