
package com.nicolasperez.restorantespringboot.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad JPA Alimento
 * Clase base para PlatoFuerte, Postre, Bebida, Adicionales
 * Usa estrategia de herencia SINGLE_TABLE
 * 
 * @author Nicolás Perez
 */
@Entity
@Table(name = "alimentos")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_alimento", discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue("GENERAL")
public class Alimento implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    protected Integer id;
    
    @Column(name = "nombre", nullable = false, length = 100)
    protected String nombre;
    
    @Column(name = "precio", nullable = false, precision = 10, scale = 2)
    protected BigDecimal precio;
    
    // Relaciones
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "receta_id", nullable = true)
    protected Receta receta;

    @JsonIgnore
    @ManyToMany(mappedBy = "alimentos")
    protected List<Menu> menus = new ArrayList<>();
    
    // Constructores
    
    public Alimento() {
    }

    public Alimento(String nombre, BigDecimal precio) {
        this.nombre = nombre;
        this.precio = precio;
    }

    public Alimento(String nombre, BigDecimal precio, Receta receta) {
        this.nombre = nombre;
        this.precio = precio;
        this.receta = receta;
    }
    
    // Getters y Setters
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public Receta getReceta() {
        return receta;
    }

    public void setReceta(Receta receta) {
        this.receta = receta;
    }

    public List<Menu> getMenus() {
        return menus;
    }

    public void setMenus(List<Menu> menus) {
        this.menus = menus;
    }
    
    // Métodos de negocio
    
    /**
     * Valida los datos del alimento
     */
    public boolean esValido() {
        return nombre != null && !nombre.trim().isEmpty()
                && nombre.length() <= 100
                && precio != null && precio.compareTo(BigDecimal.ZERO) > 0
                && receta != null;
    }
    
    /**
     * Verifica si el precio está dentro de un rango válido
     */
    public boolean precioEnRango(BigDecimal precioMinimo, BigDecimal precioMaximo) {
        return precio != null
                && precio.compareTo(precioMinimo) >= 0
                && precio.compareTo(precioMaximo) <= 0;
    }
    
    /**
     * Verifica si el alimento tiene una receta válida
     */
    public boolean tieneRecetaValida() {
        return receta != null && receta.esValida();
    }
    
    /**
     * Obtiene el tipo de alimento (útil para mostrar en UI)
     */
    public String getTipoAlimento() {
        return this.getClass().getSimpleName();
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
        if (!(object instanceof Alimento)) {
            return false;
        }
        Alimento other = (Alimento) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Alimento{" + 
               "id=" + id + 
               ", nombre=" + nombre + 
               ", precio=" + precio + 
               ", receta=" + (receta != null ? receta.getNombreReceta() : "N/A") +
               '}';
    }
}

