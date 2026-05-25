/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nicolasperez.restorantespringboot.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.math.BigDecimal;

/**
 * Entidad JPA Postres
 * Extiende Alimento usando herencia SINGLE_TABLE
 * 
 * @author Nicolás Perez
 */
@Entity
@DiscriminatorValue("POSTRE")
public class Postres extends Alimento {
    
    private static final long serialVersionUID = 1L;
    
    // Los postres no tienen campos adicionales
    // Solo se diferencian por el discriminador tipo_alimento = 'POSTRE'
    
    // Constructores

    public Postres() {}

    public Postres(String nombre, BigDecimal precio, Receta receta) {
        super(nombre, precio, receta);
    }

    public Postres(String nombre, BigDecimal precio) {
        super(nombre, precio);
    }

    
    @Override
    public String toString() {
        return "Postres{" + 
               "id=" + id + 
               ", nombre=" + nombre + 
               ", precio=" + precio + 
               '}';
    }
}
