/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nicolasperez.restorantespringboot.entities;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad JPA Menu
 * Tiene relación ManyToOne con Gerente
 * Tiene relación ManyToMany con Alimento
 * 
 * @author Nicolás Perez
 */
@Entity
@Table(name = "menu")
@NamedQueries({
    @NamedQuery(name = "Menu.findAll", query = "SELECT m FROM Menu m"),
    @NamedQuery(name = "Menu.findByGerente", 
                query = "SELECT m FROM Menu m WHERE m.gerente.id = :gerenteId")
})
public class Menu implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    
    @Column(name = "nombre_menu", length = 100)
    private String nombreMenu;
    
    // Relaciones
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "gerente_id", nullable = false)
    private Gerente gerente;
    
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
        name = "menu_alimentos",
        joinColumns = @JoinColumn(name = "menu_id"),
        inverseJoinColumns = @JoinColumn(name = "alimento_id")
    )
    private List<Alimento> alimentos = new ArrayList<>();
    
    // Constructores
    
    public Menu() {
    }

    public Menu(String nombreMenu, Gerente gerente) {
        this.nombreMenu = nombreMenu;
        this.gerente = gerente;
    }

    public Menu(Gerente gerente) {
        this.gerente = gerente;
    }
    
    // Getters y Setters
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombreMenu() {
        return nombreMenu;
    }

    public void setNombreMenu(String nombreMenu) {
        this.nombreMenu = nombreMenu;
    }

    public Gerente getGerente() {
        return gerente;
    }

    public void setGerente(Gerente gerente) {
        this.gerente = gerente;
    }

    public List<Alimento> getAlimentos() {
        return alimentos;
    }

    public void setAlimentos(List<Alimento> alimentos) {
        this.alimentos = alimentos;
    }
    
    // Métodos de negocio
    
    /**
     * Agrega un alimento al menú
     */
    public void agregarAlimento(Alimento alimento) {
        if (!alimentos.contains(alimento)) {
            alimentos.add(alimento);
            alimento.getMenus().add(this);
        }
    }
    
    /**
     * Remueve un alimento del menú
     */
    public void removerAlimento(Alimento alimento) {
        alimentos.remove(alimento);
        alimento.getMenus().remove(this);
    }
    
    /**
     * Obtiene el número total de alimentos en el menú
     */
    public int getTotalAlimentos() {
        return alimentos != null ? alimentos.size() : 0;
    }
    
    /**
     * Obtiene alimentos por tipo
     */
    public List<Alimento> getAlimentosPorTipo(Class<? extends Alimento> tipo) {
        List<Alimento> resultado = new ArrayList<>();
        for (Alimento alimento : alimentos) {
            if (tipo.isInstance(alimento)) {
                resultado.add(alimento);
            }
        }
        return resultado;
    }
    
    /**
     * Calcula el precio promedio de los alimentos del menú
     */
//    public double getPrecioPromedio() {
//        if (alimentos.isEmpty()) {
//            return 0.0;
//        }
//        double suma = 0.0;
//        for (Alimento alimento : alimentos) {
//            suma += alimento.getPrecio();
//        }
//        return suma / alimentos.size();
//    }
    
    /**
     * Obtiene el alimento más caro del menú
     */
//    public Alimento getAlimentoMasCaro() {
//        if (alimentos.isEmpty()) {
//            return null;
//        }
//        Alimento masCaro = alimentos.get(0);
//        for (Alimento alimento : alimentos) {
//            if (alimento.getPrecio() > masCaro.getPrecio()) {
//                masCaro = alimento;
//            }
//        }
//        return masCaro;
//    }
    
    /**
     * Valida el menú
     */
    public boolean esValido() {
        return gerente != null;
    }
    
    // Métodos heredados
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Menu)) {
            return false;
        }
        Menu other = (Menu) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Menu{" + 
               "id=" + id + 
               ", nombreMenu=" + nombreMenu + 
               ", gerente=" + (gerente != null ? gerente.getNombre() : "N/A") + 
               ", totalAlimentos=" + getTotalAlimentos() +
               '}';
    }
}
