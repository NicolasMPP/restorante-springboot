/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nicolasperez.restorantespringboot.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.math.BigDecimal;

/**
 * Entidad JPA Adicionales
 * Extiende Alimento usando herencia SINGLE_TABLE
 * 
 * @author Nicolás Perez
 */
@Entity
@DiscriminatorValue("ADICIONAL")
public class Adicionales extends Alimento {
    
    private static final long serialVersionUID = 1L;
    
    // Los adicionales no tienen campos adicionales
    // Solo se diferencian por el discriminador tipo_alimento = 'ADICIONAL'
    
    // Constructores
    public Adicionales() {}

    public Adicionales(String nombre, BigDecimal precio) {
        super(nombre, precio);
    }

    public Adicionales(String nombre, BigDecimal precio, Receta receta) {
        super(nombre, precio, receta);
    }
    
    @Override
    public String toString() {
        return "Adicionales{" + 
               "id=" + id + 
               ", nombre=" + nombre + 
               ", precio=" + precio + 
               '}';
    }
}

