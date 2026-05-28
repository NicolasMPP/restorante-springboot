package com.nicolasperez.restorantespringboot.dto;

import java.math.BigDecimal;

public class AlimentoDetalleDTO {

    private Integer    alimentoId;
    private String     alimentoNombre;
    private BigDecimal precio;
    private String     tipoAlimento;

    // receta
    private Integer recetaId;          // ← nuevo: permite GET /api/recetas/{id}
    private String  nombreReceta;
    private String  descripcionProceso;
    private Long    totalIngredientes;

    // chef
    private Integer chefId;            // ← nuevo: permite GET /api/chefs/{id}
    private String  chefNombre;

    public AlimentoDetalleDTO(
            Integer    alimentoId,
            String     alimentoNombre,
            BigDecimal precio,
            String     tipoAlimento,
            Integer    recetaId,
            String     nombreReceta,
            String     descripcionProceso,
            Long       totalIngredientes,
            Integer    chefId,
            String     chefNombre
    ) {
        this.alimentoId         = alimentoId;
        this.alimentoNombre     = alimentoNombre;
        this.precio             = precio;
        this.tipoAlimento       = tipoAlimento;
        this.recetaId           = recetaId;
        this.nombreReceta       = nombreReceta;
        this.descripcionProceso = descripcionProceso;
        this.totalIngredientes  = totalIngredientes;
        this.chefId             = chefId;
        this.chefNombre         = chefNombre;
    }

    public Integer    getAlimentoId()         { return alimentoId; }
    public String     getAlimentoNombre()     { return alimentoNombre; }
    public BigDecimal getPrecio()             { return precio; }
    public String     getTipoAlimento()       { return tipoAlimento; }
    public Integer    getRecetaId()           { return recetaId; }
    public String     getNombreReceta()       { return nombreReceta; }
    public String     getDescripcionProceso() { return descripcionProceso; }
    public Long       getTotalIngredientes()  { return totalIngredientes; }
    public Integer    getChefId()             { return chefId; }
    public String     getChefNombre()         { return chefNombre; }
}