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
 * Entidad JPA Despensa
 * Tiene relación ManyToOne con Gerente
 * Tiene relación ManyToMany con Ingrediente
 * 
 * @author Nicolás Perez
 */
@Entity
@Table(name = "despensa")
@NamedQueries({
    @NamedQuery(name = "Despensa.findAll", query = "SELECT d FROM Despensa d"),
    @NamedQuery(name = "Despensa.findByGerente", 
                query = "SELECT d FROM Despensa d WHERE d.gerente.id = :gerenteId")
})
public class Despensa implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    
    // Relaciones
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "gerente_id", nullable = false)
    private Gerente gerente;
    
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
        name = "despensa_ingredientes",
        joinColumns = @JoinColumn(name = "despensa_id"),
        inverseJoinColumns = @JoinColumn(name = "ingrediente_id")
    )
    private List<Ingrediente> ingredientes = new ArrayList<>();
    
    // Constructores
    
    public Despensa() {
    }

    public Despensa(Gerente gerente) {
        this.gerente = gerente;
    }
    
    // Getters y Setters
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Gerente getGerente() {
        return gerente;
    }

    public void setGerente(Gerente gerente) {
        this.gerente = gerente;
    }

    public List<Ingrediente> getIngredientes() {
        return ingredientes;
    }

    public void setIngredientes(List<Ingrediente> ingredientes) {
        this.ingredientes = ingredientes;
    }
    
    // Métodos de negocio
    
    /**
     * Agrega un ingrediente a la despensa
     */
    public void agregarIngrediente(Ingrediente ingrediente) {
        if (!ingredientes.contains(ingrediente)) {
            ingredientes.add(ingrediente);
            ingrediente.getDespensas().add(this);
        }
    }
    
    /**
     * Remueve un ingrediente de la despensa
     */
    public void removerIngrediente(Ingrediente ingrediente) {
        ingredientes.remove(ingrediente);
        ingrediente.getDespensas().remove(this);
    }
    
    /**
     * Obtiene el número total de ingredientes en la despensa
     */
    public int getTotalIngredientes() {
        return ingredientes != null ? ingredientes.size() : 0;
    }
    
    /**
     * Obtiene ingredientes con stock bajo
     */
    public List<Ingrediente> getIngredientesConStockBajo(int umbral) {
        List<Ingrediente> resultado = new ArrayList<>();
        for (Ingrediente ingrediente : ingredientes) {
            if (ingrediente.stockBajo(umbral)) {
                resultado.add(ingrediente);
            }
        }
        return resultado;
    }
    
    /**
     * Obtiene ingredientes sin stock
     */
    public List<Ingrediente> getIngredientesSinStock() {
        List<Ingrediente> resultado = new ArrayList<>();
        for (Ingrediente ingrediente : ingredientes) {
            if (!ingrediente.tieneStock()) {
                resultado.add(ingrediente);
            }
        }
        return resultado;
    }
    
    /**
     * Busca un ingrediente por descripción
     */
    public Ingrediente buscarIngredientePorDescripcion(String descripcion) {
        for (Ingrediente ingrediente : ingredientes) {
            if (ingrediente.getDescripcion().equalsIgnoreCase(descripcion)) {
                return ingrediente;
            }
        }
        return null;
    }
    
    /**
     * Calcula el valor total de stock (si los ingredientes tuvieran precio)
     */
    public int getStockTotal() {
        int total = 0;
        for (Ingrediente ingrediente : ingredientes) {
            total += ingrediente.getCantidadStock();
        }
        return total;
    }
    
    /**
     * Verifica si un ingrediente está en la despensa
     */
    public boolean contieneIngrediente(Ingrediente ingrediente) {
        return ingredientes.contains(ingrediente);
    }
    
    /**
     * Verifica si un ingrediente está en la despensa por descripción
     */
    public boolean contieneIngrediente(String descripcion) {
        return buscarIngredientePorDescripcion(descripcion) != null;
    }
    
    /**
     * Valida la despensa
     */
    public boolean esValida() {
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
        if (!(object instanceof Despensa)) {
            return false;
        }
        Despensa other = (Despensa) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Despensa{" + 
               "id=" + id + 
               ", gerente=" + (gerente != null ? gerente.getNombre() : "N/A") + 
               ", totalIngredientes=" + getTotalIngredientes() +
               ", stockTotal=" + getStockTotal() +
               '}';
    }
}
