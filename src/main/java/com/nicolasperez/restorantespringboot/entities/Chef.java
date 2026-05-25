package com.nicolasperez.restorantespringboot.entities;


import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * Entidad JPA Chef
 * CORRECCIÓN: Hereda recetas de Empleado
 * 
 * @author Nicolás Perez
 */
@Entity
@DiscriminatorValue("CHEF")
public class Chef extends Empleado implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    // Chef hereda todo de Empleado, incluida la relación con Receta
    
    // Constructores
    
    public Chef() {
        super();
    }

    public Chef(BigDecimal salario, LocalDate fechaVinculacion, LocalDateTime horaIngreso, LocalDateTime horaSalida,
                String nombre, String cedula, String telefono, String correo,
                String usuario, String contrasenia) {
        super(fechaVinculacion, horaIngreso, horaSalida, salario, 
              nombre, cedula, telefono, correo, usuario, contrasenia);
    }
    
    // Métodos específicos de Chef (si los hay)
    
    @Override
    public boolean esValido() {
        return super.esValido() && salarioValido();
    }
    
    @Override
    public String toString() {
        return "Chef{" + 
               "id=" + id + 
               ", nombre=" + nombre + 
               ", cedula=" + cedula + 
               ", salario=" + salario + 
               ", totalRecetas=" + getTotalRecetas() +
               '}';
    }
}