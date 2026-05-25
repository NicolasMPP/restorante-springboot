package com.nicolasperez.restorantespringboot.repositories;

import com.nicolasperez.restorantespringboot.entities.Ingrediente;
import com.nicolasperez.restorantespringboot.entities.Receta;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 * Repository Spring Data JPA para Receta
 *
 * @author Nicolás Perez
 */
public interface RecetaRepository extends JpaRepository<Receta, Integer> {

    // ============================================================
    // FETCH COMPLETO
    // ============================================================

    @Query("""
           SELECT DISTINCT r
           FROM Receta r
           LEFT JOIN FETCH r.ingredientes
           LEFT JOIN FETCH r.chef
           WHERE r.nombreReceta = :nombre
           """)
    Optional<Receta> buscarPorNombre(String nombre);

    @Query("""
           SELECT DISTINCT r
           FROM Receta r
           LEFT JOIN FETCH r.ingredientes
           WHERE r.chef.id = :chefId
           """)
    List<Receta> buscarPorChef(Integer chefId);

    @Query("""
           SELECT DISTINCT r
           FROM Receta r
           LEFT JOIN FETCH r.ingredientes
           LEFT JOIN FETCH r.chef
           WHERE r.id = :id
           """)
    Optional<Receta> buscarCompletaPorId(Integer id);

    @Query("""
           SELECT DISTINCT r
           FROM Receta r
           LEFT JOIN FETCH r.ingredientes
           LEFT JOIN FETCH r.chef
           """)
    List<Receta> buscarTodasCompletas();

    // ============================================================
    // DERIVED METHODS
    // ============================================================

    Optional<Receta> findByNombreReceta(String nombreReceta);

    List<Receta> findByChef_IdOrderByNombreRecetaAsc(
            Integer chefId
    );

    List<Receta> findAllByOrderByNombreRecetaAsc();

    List<Receta> findByIngredientesIsEmpty();

    // ============================================================
    // INGREDIENTES
    // ============================================================

    @Query("""
           SELECT i
           FROM Receta r
           JOIN r.ingredientes i
           WHERE r.id = :recetaId
           ORDER BY i.descripcion
           """)
    List<Ingrediente> obtenerIngredientes(Integer recetaId);

    @Query("""
           SELECT COUNT(i)
           FROM Receta r
           JOIN r.ingredientes i
           WHERE r.id = :recetaId
           """)
    Long contarIngredientes(Integer recetaId);

    // ============================================================
    // BUSQUEDAS
    // ============================================================

    @Query("""
           SELECT DISTINCT r
           FROM Receta r
           LEFT JOIN FETCH r.ingredientes i
           WHERE i.id = :ingredienteId
           """)
    List<Receta> buscarPorIngrediente(Integer ingredienteId);

    @Query("""
           SELECT DISTINCT r
           FROM Receta r
           LEFT JOIN FETCH r.ingredientes i
           WHERE i.cantidadStock >= :stockMinimo
           """)
    List<Receta> buscarConIngredientesDisponibles(
            Integer stockMinimo
    );

    // ============================================================
    // DTO COMPLEJIDAD
    // ============================================================

    class RecetaComplejidadDTO {

        private Integer id;
        private String nombreReceta;
        private String nombreChef;
        private Long totalIngredientes;

        public RecetaComplejidadDTO(
                Integer id,
                String nombreReceta,
                String nombreChef,
                Long totalIngredientes
        ) {
            this.id = id;
            this.nombreReceta = nombreReceta;
            this.nombreChef = nombreChef;
            this.totalIngredientes = totalIngredientes;
        }

        public Integer getId() {
            return id;
        }

        public String getNombreReceta() {
            return nombreReceta;
        }

        public String getNombreChef() {
            return nombreChef;
        }

        public Long getTotalIngredientes() {
            return totalIngredientes;
        }
    }

    @Query("""
           SELECT new com.nicolasperez.restorantespringboot.repositories.RecetaRepository$RecetaComplejidadDTO(
                r.id,
                r.nombreReceta,
                c.nombre,
                COUNT(i)
           )
           FROM Receta r
           JOIN r.chef c
           LEFT JOIN r.ingredientes i
           GROUP BY r.id, r.nombreReceta, c.nombre
           ORDER BY COUNT(i) DESC
           """)
    List<RecetaComplejidadDTO> obtenerRecetasPorComplejidad();
}