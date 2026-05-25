// ============================================================
// IngredienteRepository
// ============================================================

package com.nicolasperez.restorantespringboot.repositories;

import com.nicolasperez.restorantespringboot.entities.Ingrediente;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface IngredienteRepository extends JpaRepository<Ingrediente, Integer> {

    Optional<Ingrediente> findByDescripcion(String descripcion);

    List<Ingrediente> findAllByOrderByDescripcionAsc();

    List<Ingrediente> findByDescripcionContainingIgnoreCaseOrderByDescripcionAsc(
            String descripcion
    );

    List<Ingrediente> findByCantidadStockLessThanEqualOrderByCantidadStockAsc(
            Integer stock
    );

    List<Ingrediente> findByCantidadStockGreaterThanOrderByDescripcionAsc(
            Integer stock
    );

    boolean existsByDescripcion(String descripcion);

    @Query("""
           SELECT i
           FROM Ingrediente i
           JOIN i.recetas r
           WHERE r.id = :recetaId
           ORDER BY i.descripcion
           """)
    List<Ingrediente> buscarPorReceta(Integer recetaId);

    @Query("""
           SELECT SUM(i.cantidadStock)
           FROM Ingrediente i
           """)
    Long obtenerStockTotal();

    @Query("""
           SELECT AVG(i.cantidadStock)
           FROM Ingrediente i
           """)
    Double obtenerPromedioStock();

    List<Ingrediente> findByCantidadStockLessThan(
            Integer stock
    );
}