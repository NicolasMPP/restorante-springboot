
package com.nicolasperez.restorantespringboot.entities;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad JPA Gerente
 * Extiende Persona usando herencia JOINED
 * 
 * CORRECCIÓN: Agregado @PrimaryKeyJoinColumn, tabla separada "gerentes"
 * 
 * @author Nicolás Perez
 */
@Entity
@Table(name = "gerentes")
@PrimaryKeyJoinColumn(name = "id")
public class Gerente extends Persona {
    
    private static final long serialVersionUID = 1L;
    
    // Los gerentes no tienen campos adicionales en este diseño
    // Solo se diferencian por estar en tabla separada
    
    // Relaciones
    
    @OneToMany(mappedBy = "gerente", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Menu> menus = new ArrayList<>();
    
    @OneToMany(mappedBy = "gerente", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Despensa> despensas = new ArrayList<>();
    
    // Constructores
    
    public Gerente() {
        super();
    }

    public Gerente(String nombre, String cedula, String telefono, String correo) {
        super(nombre, cedula, telefono, correo);
    }

    public Gerente(String nombre, String cedula, String telefono, String correo, 
                   String usuario, String contrasenia) {
        super(nombre, cedula, telefono, correo, usuario, contrasenia);
    }
    
    // Getters y Setters de relaciones
    
    public List<Menu> getMenus() {
        return menus;
    }

    public void setMenus(List<Menu> menus) {
        this.menus = menus;
    }

    public List<Despensa> getDespensas() {
        return despensas;
    }

    public void setDespensas(List<Despensa> despensas) {
        this.despensas = despensas;
    }
    
    // Métodos de negocio
    
    /**
     * Valida los datos del gerente
     */
    @Override
    public boolean esValido() {
        return super.esValido();
    }
    
    @Override
    public String toString() {
        return "Gerente{" + 
               "id=" + id + 
               ", nombre=" + nombre + 
               ", cedula=" + cedula + 
               '}';
    }
}