package com.nicolasperez.restorantespringboot.repositories;

import com.nicolasperez.restorantespringboot.entities.*;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AlimentoRepository extends JpaRepository<Alimento, Integer> {

    Optional<Alimento> findByNombre(String nombre);

    List<Alimento> findAllByOrderByNombreAsc();

    List<Alimento> findByPrecioBetweenOrderByPrecioAsc(
            Double min,
            Double max
    );

    List<Alimento> findByNombreContainingIgnoreCaseOrderByNombreAsc(
            String nombre
    );

    List<Alimento> findByReceta_IdOrderByNombreAsc(
            Integer recetaId
    );

    @Query("""
           SELECT AVG(a.precio)
           FROM Alimento a
           """)
    Double obtenerPrecioPromedio();

    // ============================================================
    // TIPOS
    // ============================================================

    @Query("""
           SELECT p
           FROM PlatoFuerte p
           ORDER BY p.nombre
           """)
    List<PlatoFuerte> buscarPlatosFuertes();

    @Query("""
           SELECT p
           FROM Postres p
           ORDER BY p.nombre
           """)
    List<Postres> buscarPostres();

    @Query("""
           SELECT b
           FROM Bebida b
           ORDER BY b.nombre
           """)
    List<Bebida> buscarBebidas();

    @Query("""
           SELECT a
           FROM Adicionales a
           ORDER BY a.nombre
           """)
    List<Adicionales> buscarAdicionales();

    // ============================================================
    // TOPS
    // ============================================================

    @Query("""
           SELECT a
           FROM Alimento a
           ORDER BY a.precio DESC
           """)
    List<Alimento> buscarMasCaros(Pageable pageable);

    @Query("""
           SELECT a
           FROM Alimento a
           ORDER BY a.precio ASC
           """)
    List<Alimento> buscarMasBaratos(Pageable pageable);

    // ============================================================
    // BÚSQUEDAS POR NOMBRE
    // ============================================================

    List<Alimento> findByNombreContainingIgnoreCase(
            String nombre
    );

    // ============================================================
    // BÚSQUEDAS POR PRECIO
    // ============================================================

    List<Alimento> findByPrecioLessThanEqual(
            Double precio
    );

    List<Alimento> findByPrecioGreaterThanEqual(
            Double precio
    );
    // Necesario porque con herencia SINGLE_TABLE Hibernate no cambia
// el discriminador via merge — hay que hacerlo con SQL nativo
    @Modifying
    @Transactional
    @Query(
            value  = "UPDATE alimentos SET tipo_alimento = :tipo WHERE id = :id",
            nativeQuery = true
    )
    void actualizarTipo(
            @Param("id")   Integer id,
            @Param("tipo") String  tipo
    );
}