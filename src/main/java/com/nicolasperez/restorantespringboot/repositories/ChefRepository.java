package com.nicolasperez.restorantespringboot.repositories;

import com.nicolasperez.restorantespringboot.entities.Chef;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ChefRepository extends JpaRepository<Chef, Integer> {

    Optional<Chef> findByCedula(String cedula);

    Optional<Chef> findByNombre(String nombre);

    List<Chef> findAllByOrderByNombreAsc();

    List<Chef> findBySalarioGreaterThanEqualOrderBySalarioDesc(
            BigDecimal salario
    );

    List<Chef> findByRecetasIsEmpty();

    @Query("""
    SELECT c
    FROM Chef c
    WHERE LOWER(c.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))
""")
    Optional<Chef> buscarPorNombre(String nombre);

    @Query("""
    SELECT c
    FROM Chef c
    ORDER BY c.nombre
""")
    List<Chef> buscarTodosOrdenados();

}