
package com.nicolasperez.restorantespringboot.entities;



import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.math.BigDecimal;

/**
 * Entidad JPA PlatoFuerte
 * Extiende Alimento usando herencia SINGLE_TABLE
 * 
 * @author Nicolás Perez
 */
@Entity
@DiscriminatorValue("PLATO_FUERTE")
public class PlatoFuerte extends Alimento {
    
    private static final long serialVersionUID = 1L;
    
    // Los platos fuertes no tienen campos adicionales
    // Solo se diferencian por el discriminador tipo_alimento = 'PLATO_FUERTE'
    
    // Constructores

    public PlatoFuerte(String nombre, BigDecimal precio, Receta receta) {
        super(nombre, precio, receta); // ← pasar los parámetros
    }

    public PlatoFuerte(String nombre, BigDecimal precio) {
        super(nombre, precio);
    }

    public PlatoFuerte() {

    }

    @Override
    public String toString() {
        return "PlatoFuerte{" + 
               "id=" + id + 
               ", nombre=" + nombre + 
               ", precio=" + precio + 
               '}';
    }
}