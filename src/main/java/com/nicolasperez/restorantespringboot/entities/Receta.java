package com.nicolasperez.restorantespringboot.entities;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad JPA Receta
 * CORRECCIÓN: Apunta a Empleado en lugar de Chef
 * 
 * @author Nicolás Perez
 */
@Entity
@Table(name = "recetas")
@NamedQueries({
    @NamedQuery(name = "Receta.findByNombre",
                query = "SELECT r FROM Receta r WHERE r.nombreReceta = :nombre"),
    @NamedQuery(name = "Receta.findByChef",
                query = "SELECT r FROM Receta r WHERE r.chef.id = :chefId")
})
public class Receta implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    
    @Column(name = "nombre_receta", nullable = false, length = 100)
    private String nombreReceta;
    
    @Column(name = "descripcion_proceso", columnDefinition = "TEXT")
    private String descripcionProceso;
    
    // Relaciones
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chef_id", nullable = false)
    private Empleado chef;  // ← CAMBIO: Chef → Empleado
    
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "receta_ingredientes",
        joinColumns = @JoinColumn(name = "receta_id"),
        inverseJoinColumns = @JoinColumn(name = "ingrediente_id")
    )
    private List<Ingrediente> ingredientes = new ArrayList<>();
    
    // Constructores
    
    public Receta() {
    }
    
    public Receta(String nombreReceta, String descripcionProceso, Empleado chef) {
        this.nombreReceta = nombreReceta;
        this.descripcionProceso = descripcionProceso;
        this.chef = chef;
    }
    
    // Getters y Setters
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombreReceta() {
        return nombreReceta;
    }

    public void setNombreReceta(String nombreReceta) {
        this.nombreReceta = nombreReceta;
    }

    public String getDescripcionProceso() {
        return descripcionProceso;
    }

    public void setDescripcionProceso(String descripcionProceso) {
        this.descripcionProceso = descripcionProceso;
    }

    public Empleado getChef() {
        return chef;
    }

    public void setChef(Empleado chef) {
        this.chef = chef;
    }

    public List<Ingrediente> getIngredientes() {
        return ingredientes;
    }

    public void setIngredientes(List<Ingrediente> ingredientes) {
        this.ingredientes = ingredientes;
    }
    
    // Métodos de negocio
    
    public void agregarIngrediente(Ingrediente ingrediente) {
        ingredientes.add(ingrediente);
    }
    
    public void removerIngrediente(Ingrediente ingrediente) {
        ingredientes.remove(ingrediente);
    }
    
    public int getTotalIngredientes() {
        return ingredientes != null ? ingredientes.size() : 0;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Receta)) {
            return false;
        }
        Receta other = (Receta) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Receta{" + 
               "id=" + id + 
               ", nombreReceta=" + nombreReceta + 
               ", chef=" + (chef != null ? chef.getNombre() : "null") + 
               ", ingredientes=" + getTotalIngredientes() +
               '}';
    }

    public boolean esValida() {
        return true;
    }
}