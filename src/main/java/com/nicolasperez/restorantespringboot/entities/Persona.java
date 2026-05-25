package com.nicolasperez.restorantespringboot.entities;


import jakarta.persistence.*;
import java.io.Serializable;

/**
 * Entidad JPA Persona
 * Clase base para Gerente, Chef, Mesero, Cliente
 * Usa estrategia de herencia JOINED (cada subclase tiene su propia tabla)
 * 
 * CORRECCIÓN: Removido @DiscriminatorColumn (solo se usa para SINGLE_TABLE)
 * 
 * @author Nicolás Perez
 */
@Entity
@Table(name = "personas")
@Inheritance(strategy = InheritanceType.JOINED)
public class Persona implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    protected Integer id;
    
    @Column(name = "nombre", nullable = false, length = 100)
    protected String nombre;
    
    @Column(name = "cedula", nullable = false, unique = true, length = 20)
    protected String cedula;
    
    @Column(name = "telefono", length = 20)
    protected String telefono;
    
    @Column(name = "correo", length = 100)
    protected String correo;
    
    @Column(name = "usuario", length = 50)
    protected String usuario;
    
    @Column(name = "contrasenia", length = 100)
    protected String contrasenia;
    
    // Constructores
    
    public Persona() {
    }

    public Persona(String nombre, String cedula, String telefono, String correo) {
        this.nombre = nombre;
        this.cedula = cedula;
        this.telefono = telefono;
        this.correo = correo;
    }

    public Persona(String nombre, String cedula, String telefono, String correo, 
                   String usuario, String contrasenia) {
        this.nombre = nombre;
        this.cedula = cedula;
        this.telefono = telefono;
        this.correo = correo;
        this.usuario = usuario;
        this.contrasenia = contrasenia;
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

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }
    
    // Métodos de negocio
    
    /**
     * Valida los datos básicos de la persona
     */
    public boolean esValido() {
        return nombre != null && !nombre.trim().isEmpty() && nombre.length() <= 100
            && cedula != null && !cedula.trim().isEmpty() && cedula.length() >= 7 && cedula.length() <= 20
            && (telefono == null || telefono.length() <= 20)
            && (correo == null || correo.length() <= 100);
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
        if (!(object instanceof Persona)) {
            return false;
        }
        Persona other = (Persona) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Persona{" + 
               "id=" + id + 
               ", nombre=" + nombre + 
               ", cedula=" + cedula + 
               ", telefono=" + telefono + 
               ", correo=" + correo + 
               '}';
    }
}