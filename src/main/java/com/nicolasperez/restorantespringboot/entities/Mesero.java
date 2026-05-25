package com.nicolasperez.restorantespringboot.entities;

import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Entidad JPA Mesero
 * CORRECCIÓN: Constructor actualizado para BigDecimal
 * 
 * @author Nicolás Perez
 */
@Entity
@DiscriminatorValue("MESERO")
public class Mesero extends Empleado implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    // Constructores
    
    public Mesero() {
        super();
    }

    public Mesero(BigDecimal salario, LocalDate fechaVinculacion, LocalDateTime horaIngreso, LocalDateTime horaSalida,
                  String nombre, String cedula, String telefono, String correo,
                  String usuario, String contrasenia) {
        super(fechaVinculacion, horaIngreso, horaSalida, salario, 
              nombre, cedula, telefono, correo, usuario, contrasenia);
    }
    
    // Métodos de negocio específicos de Mesero
    
    public void tomarPedido(String[] pedido) {
        System.out.println("Pedido tomado por mesero: " + this.nombre);
    }
    
    public void cancelarPedido(String[] pedido) {
        System.out.println("Pedido cancelado por mesero: " + this.nombre);
    }
    
    public void modificarPedido(String[] pedido) {
        System.out.println("Pedido modificado por mesero: " + this.nombre);
    }
    
    public void entregarPedido(String[] pedido) {
        System.out.println("Pedido entregado por mesero: " + this.nombre);
    }
    
    @Override
    public String toString() {
        return "Mesero{" + 
               "id=" + id + 
               ", nombre=" + nombre + 
               ", cedula=" + cedula + 
               ", salario=" + salario + 
               '}';
    }
}