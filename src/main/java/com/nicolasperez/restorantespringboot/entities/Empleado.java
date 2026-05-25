package com.nicolasperez.restorantespringboot.entities;

import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Entidad JPA Empleado
 * CORRECCIÓN: Relación con Receta movida de Chef a Empleado
 * 
 * @author Nicolás Perez
 */
@Entity
@Table(name = "empleados")
@PrimaryKeyJoinColumn(name = "id")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_empleado", discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue("EMPLEADO")
public class Empleado extends Persona implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Column(name = "fecha_vinculacion")
    protected LocalDate fechaVinculacion;
    
    @Column(name = "hora_ingreso")
    protected LocalDateTime horaIngreso;
    
    @Column(name = "hora_salida")
    protected LocalDateTime horaSalida;
    
    @Column(name = "salario", precision = 10, scale = 2)
    protected BigDecimal salario;
    
    // Relaciones
    @OneToMany(mappedBy = "chef", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Receta> recetas = new ArrayList<>();
    
    // Constructores
    
    public Empleado() {
        super();
    }

    public Empleado(LocalDate fechaVinculacion, LocalDateTime horaIngreso, LocalDateTime horaSalida,
                    BigDecimal salario, String nombre, String cedula, String telefono, 
                    String correo, String usuario, String contrasenia) {
        super(nombre, cedula, telefono, correo, usuario, contrasenia);
        this.fechaVinculacion = fechaVinculacion;
        this.horaIngreso = horaIngreso;
        this.horaSalida = horaSalida;
        this.salario = salario;
    }
    
    // Getters y Setters


    public LocalDate getFechaVinculacion() {
        return fechaVinculacion;
    }

    public void setFechaVinculacion(LocalDate fechaVinculacion) {
        this.fechaVinculacion = fechaVinculacion;
    }

    public BigDecimal getSalario() {
        return salario;
    }

    public void setSalario(BigDecimal salario) {
        this.salario = salario;
    }
    
    public List<Receta> getRecetas() {
        return recetas;
    }

    public void setRecetas(List<Receta> recetas) {
        this.recetas = recetas;
    }
    
    // Métodos de negocio
    
    public void agregarReceta(Receta receta) {
        recetas.add(receta);
        receta.setChef(this);
    }
    
    public void removerReceta(Receta receta) {
        recetas.remove(receta);
        receta.setChef(null);
    }

    public void setHoraIngreso(LocalDateTime horaIngreso) {
        this.horaIngreso = horaIngreso;
    }

    public void setHoraSalida(LocalDateTime horaSalida) {
        this.horaSalida = horaSalida;
    }

    public LocalDateTime getHoraIngreso() {
        return horaIngreso;
    }

    public LocalDateTime getHoraSalida() {
        return horaSalida;
    }

    public int getTotalRecetas() {
        return recetas != null ? recetas.size() : 0;
    }
    
    public boolean salarioValido() {
        return salario != null && salario.compareTo(BigDecimal.ZERO) > 0;
    }
    
    @Override
    public boolean esValido() {
        return super.esValido() && salarioValido();
    }

    @Override
    public String toString() {
        return "Empleado{" + 
               super.toString() + 
               ", fechaVinculacion=" + fechaVinculacion + 
               ", salario=" + salario + 
               '}';
    }
}