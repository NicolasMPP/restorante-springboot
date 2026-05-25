/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nicolasperez.restorantespringboot.entities;


import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;

/**
 * Entidad JPA Cliente
 * Extiende Persona usando herencia JOINED
 * 
 * CORRECCIÓN: Agregado @PrimaryKeyJoinColumn, tabla separada "clientes"
 * 
 * @author Nicolás Perez
 */
@Entity
@Table(name = "clientes")
@PrimaryKeyJoinColumn(name = "id")
public class Cliente extends Persona {
    
    private static final long serialVersionUID = 1L;
    
    // Los clientes no tienen campos adicionales en este diseño
    
    // Constructores
    
    public Cliente() {
        super();
    }

    public Cliente(String nombre, String cedula, String telefono, String correo) {
        super(nombre, cedula, telefono, correo);
    }

    public Cliente(String nombre, String cedula, String telefono, String correo, 
                   String usuario, String contrasenia) {
        super(nombre, cedula, telefono, correo, usuario, contrasenia);
    }
    
    // Métodos de negocio específicos de Cliente
    
    /**
     * Registra un nuevo cliente (placeholder)
     */
    public void registrarse() {
        System.out.println("Cliente registrado: " + this.nombre);
    }
    
    /**
     * Reserva una mesa (placeholder)
     */
    public void reservarMesa() {
        System.out.println("Mesa reservada para: " + this.nombre);
    }
    
    /**
     * Cancela una reservación (placeholder)
     */
    public void cancelarReservacion() {
        System.out.println("Reservación cancelada para: " + this.nombre);
    }
    
    @Override
    public String toString() {
        return "Cliente{" + 
               "id=" + id + 
               ", nombre=" + nombre + 
               ", cedula=" + cedula + 
               '}';
    }
}
