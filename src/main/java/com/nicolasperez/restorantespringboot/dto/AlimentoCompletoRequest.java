package com.nicolasperez.restorantespringboot.dto;

import java.math.BigDecimal;
import java.util.List;

public class AlimentoCompletoRequest {

    private String      nombreAlimento;
    private BigDecimal  precio;
    private String      tipo;
    private String      nombreReceta;
    private String      descripcionProceso;
    private String      chefCedula;
    private List<String> ingredientesDescripciones;

    // ── Getters y Setters ──────────────────────────────────────

    public String getNombreAlimento()          { return nombreAlimento; }
    public void   setNombreAlimento(String v)  { this.nombreAlimento = v; }

    public BigDecimal getPrecio()              { return precio; }
    public void       setPrecio(BigDecimal v)  { this.precio = v; }

    public String getTipo()                    { return tipo; }
    public void   setTipo(String v)            { this.tipo = v; }

    public String getNombreReceta()            { return nombreReceta; }
    public void   setNombreReceta(String v)    { this.nombreReceta = v; }

    public String getDescripcionProceso()      { return descripcionProceso; }
    public void   setDescripcionProceso(String v){ this.descripcionProceso = v; }

    public String getChefCedula()              { return chefCedula; }
    public void   setChefCedula(String v)      { this.chefCedula = v; }

    public List<String> getIngredientesDescripciones()         { return ingredientesDescripciones; }
    public void         setIngredientesDescripciones(List<String> v){ this.ingredientesDescripciones = v; }
}