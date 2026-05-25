package com.nicolasperez.restorantespringboot.repositories;

import com.nicolasperez.restorantespringboot.dto.IngredienteDetalleDTO;
import com.nicolasperez.restorantespringboot.entities.Despensa;
import com.nicolasperez.restorantespringboot.entities.Ingrediente;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DespensaRepository
        extends JpaRepository<Despensa, Integer> {

    // ============================================================
    // DESPENSA POR GERENTE
    // ============================================================

    @Query("""
        SELECT d
        FROM Despensa d
        WHERE d.gerente.id = :gerenteId
    """)
    Optional<Despensa> buscarPorGerente(
            @Param("gerenteId") Integer gerenteId
    );

    // ============================================================
    // INGREDIENTES DETALLADOS
    // ============================================================

    @Query("""
        SELECT new com.nicolasperez.restorantespringboot.dto.IngredienteDetalleDTO(
            i.id,
            i.descripcion,
            i.cantidadStock
        )
        FROM Despensa d
        JOIN d.ingredientes i
        WHERE d.id = :despensaId
        ORDER BY i.descripcion
    """)
    List<IngredienteDetalleDTO> obtenerIngredientesInfo(
            @Param("despensaId") Integer despensaId
    );

    // ============================================================
    // STOCK BAJO
    // ============================================================

    @Query("""
        SELECT i
        FROM Despensa d
        JOIN d.ingredientes i
        WHERE d.id = :despensaId
        AND i.cantidadStock < :umbral
    """)
    List<Ingrediente> obtenerIngredientesConStockBajo(
            @Param("despensaId") Integer despensaId,
            @Param("umbral") Integer umbral
    );
}