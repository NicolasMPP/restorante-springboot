/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nicolasperez.restorantespringboot.entities;


import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.math.BigDecimal;

/**
 * Entidad JPA Bebida
 * Extiende Alimento usando herencia SINGLE_TABLE
 * 
 * @author Nicolás Perez
 */
@Entity
@DiscriminatorValue("BEBIDA")
public class Bebida extends Alimento {
    
    private static final long serialVersionUID = 1L;
    
    // Las bebidas no tienen campos adicionales
    // Solo se diferencian por el discriminador tipo_alimento = 'BEBIDA'
    
    // Constructores

    public Bebida() {}

    public Bebida(String nombre, BigDecimal precio) {
        super(nombre, precio);
    }

    public Bebida(String nombre, BigDecimal precio, Receta receta) {
        super(nombre, precio, receta);
    }
    
    @Override
    public String toString() {
        return "Bebida{" + 
               "id=" + id + 
               ", nombre=" + nombre + 
               ", precio=" + precio + 
               '}';
    }
}
