package com.nicolasperez.restorantespringboot.repositories;

import com.nicolasperez.restorantespringboot.dto.AlimentoDetalleDTO;
import com.nicolasperez.restorantespringboot.entities.Alimento;
import com.nicolasperez.restorantespringboot.entities.Menu;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 * Repository Spring Data JPA para Menu
 *
 * @author Nicolás Perez
 */
public interface MenuRepository extends JpaRepository<Menu, Integer> {

    // ============================================================
    // FETCH COMPLETO
    // ============================================================

    @Query("""
            SELECT DISTINCT m
            FROM Menu m
            LEFT JOIN FETCH m.alimentos
            LEFT JOIN FETCH m.gerente
            WHERE m.id = :menuId
            """)
    Optional<Menu> obtenerMenuCompleto(Integer menuId);


    // ============================================================
    // ALIMENTOS CON RECETA Y CHEF
    // ============================================================

    @Query("""
    SELECT DISTINCT a
    FROM Menu m
    JOIN m.alimentos a
    LEFT JOIN FETCH a.receta r
    LEFT JOIN FETCH r.chef
    LEFT JOIN FETCH r.ingredientes
    WHERE m.id = :menuId
    """)
    List<Alimento> obtenerAlimentosDelMenuConDetalles(Integer menuId);

    // ============================================================
    // RELACIONES
    // ============================================================

    List<Menu> findByGerente_Id(Integer gerenteId);

    Optional<Menu> findByNombreMenu(String nombreMenu);

    List<Menu> findAllByOrderByNombreMenuAsc();

    // ============================================================
    // ALIMENTOS DEL MENU
    // ============================================================

    @Query("""
            SELECT a
            FROM Menu m
            JOIN m.alimentos a
            WHERE m.id = :menuId
            ORDER BY a.nombre
            """)
    List<Alimento> obtenerAlimentosDelMenu(Integer menuId);

    @Query("""
            SELECT COUNT(a)
            FROM Menu m
            JOIN m.alimentos a
            WHERE m.id = :menuId
            """)
    Long contarAlimentos(Integer menuId);

    // ============================================================
    // ESTADISTICAS
    // ============================================================

    class MenuEstadisticasDTO {

        private Integer menuId;
        private String nombreMenu;
        private String nombreGerente;
        private Long totalAlimentos;
        private Double precioPromedio;

        public MenuEstadisticasDTO(
                Integer menuId,
                String nombreMenu,
                String nombreGerente,
                Long totalAlimentos,
                Double precioPromedio
        ) {
            this.menuId = menuId;
            this.nombreMenu = nombreMenu;
            this.nombreGerente = nombreGerente;
            this.totalAlimentos = totalAlimentos;
            this.precioPromedio = precioPromedio;
        }

        public Integer getMenuId() {
            return menuId;
        }

        public String getNombreMenu() {
            return nombreMenu;
        }

        public String getNombreGerente() {
            return nombreGerente;
        }

        public Long getTotalAlimentos() {
            return totalAlimentos;
        }

        public Double getPrecioPromedio() {
            return precioPromedio;
        }
    }

    @Query("""
            SELECT new com.nicolasperez.restorantespringboot.repositories.MenuRepository$MenuEstadisticasDTO(
                 m.id,
                 m.nombreMenu,
                 g.nombre,
                 COUNT(a),
                 AVG(a.precio)
            )
            FROM Menu m
            JOIN m.gerente g
            LEFT JOIN m.alimentos a
            GROUP BY m.id, m.nombreMenu, g.nombre
            ORDER BY COUNT(a) DESC
            """)
    List<MenuEstadisticasDTO> obtenerMenusConMasAlimentos();

    // ============================================================
    // MENUS VACIOS
    // ============================================================

    @Query("""
            SELECT m
            FROM Menu m
            WHERE m.alimentos IS EMPTY
            """)
    List<Menu> buscarMenusVacios();
}