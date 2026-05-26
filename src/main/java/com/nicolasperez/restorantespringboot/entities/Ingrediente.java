
package com.nicolasperez.restorantespringboot.entities;



import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad JPA Ingrediente
 * Tiene relación ManyToMany con Receta
 * 
 * @author Nicolás Perez
 */
@Entity
@Table(name = "ingredientes")
@NamedQueries({
    @NamedQuery(name = "Ingrediente.findAll", query = "SELECT i FROM Ingrediente i"),
    @NamedQuery(name = "Ingrediente.findByDescripcion", 
                query = "SELECT i FROM Ingrediente i WHERE i.descripcion = :descripcion"),
    @NamedQuery(name = "Ingrediente.findConStockBajo", 
                query = "SELECT i FROM Ingrediente i WHERE i.cantidadStock <= :umbral")
})
public class Ingrediente implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    
    @Column(name = "descripcion", nullable = false, length = 100)
    private String descripcion;
    
    @Column(name = "cantidad_stock", nullable = false)
    private Integer cantidadStock;
    
    // Relaciones
    @JsonIgnore
    @ManyToMany(mappedBy = "ingredientes")
    private List<Receta> recetas = new ArrayList<>();

    @JsonIgnore
    @ManyToMany(mappedBy = "ingredientes")
    private List<Despensa> despensas = new ArrayList<>();
    
    // Constructores
    
    public Ingrediente() {
    }

    public Ingrediente(String descripcion, Integer cantidadStock) {
        this.descripcion = descripcion;
        this.cantidadStock = cantidadStock;
    }
    
    // Getters y Setters
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getCantidadStock() {
        return cantidadStock;
    }

    public void setCantidadStock(Integer cantidadStock) {
        this.cantidadStock = cantidadStock;
    }

    public List<Receta> getRecetas() {
        return recetas;
    }

    public void setRecetas(List<Receta> recetas) {
        this.recetas = recetas;
    }

    public List<Despensa> getDespensas() {
        return despensas;
    }

    public void setDespensas(List<Despensa> despensas) {
        this.despensas = despensas;
    }
    
    // Métodos de negocio
    
    /**
     * Valida los datos del ingrediente
     */
    public boolean esValido() {
        return descripcion != null && !descripcion.trim().isEmpty() 
            && descripcion.length() <= 100
            && cantidadStock != null && cantidadStock >= 0;
    }
    
    /**
     * Verifica si el ingrediente tiene stock disponible
     */
    public boolean tieneStock() {
        return cantidadStock != null && cantidadStock > 0;
    }
    
    /**
     * Verifica si el stock está bajo
     */
    public boolean stockBajo(int umbral) {
        return cantidadStock != null && cantidadStock < umbral;
    }
    
    /**
     * Incrementa el stock
     */
    public void incrementarStock(int cantidad) {
        if (cantidad > 0) {
            this.cantidadStock += cantidad;
        }
    }
    
    /**
     * Decrementa el stock
     */
    public boolean decrementarStock(int cantidad) {
        if (cantidad > 0 && this.cantidadStock >= cantidad) {
            this.cantidadStock -= cantidad;
            return true;
        }
        return false;
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
        if (!(object instanceof Ingrediente)) {
            return false;
        }
        Ingrediente other = (Ingrediente) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Ingrediente{" + 
               "id=" + id + 
               ", descripcion=" + descripcion + 
               ", cantidadStock=" + cantidadStock + 
               '}';
    }
}
