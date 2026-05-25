// dto/AlimentoDetalleDTO.java
package com.nicolasperez.restorantespringboot.dto;

import java.math.BigDecimal;

public class AlimentoDetalleDTO {

    private Integer alimentoId;
    private String alimentoNombre;
    private BigDecimal precio;        // ← BigDecimal, no Double
    private String tipoAlimento;
    private String nombreReceta;
    private String descripcionProceso;
    private String chefNombre;
    private Long totalIngredientes;   // ← Long, SIZE() retorna Long en Hibernate 7

    public AlimentoDetalleDTO(
            Integer alimentoId,
            String alimentoNombre,
            BigDecimal precio,
            String tipoAlimento,
            String nombreReceta,
            String descripcionProceso,
            String chefNombre,
            Long totalIngredientes
    ) {
        this.alimentoId        = alimentoId;
        this.alimentoNombre    = alimentoNombre;
        this.precio            = precio;
        this.tipoAlimento      = tipoAlimento;
        this.nombreReceta      = nombreReceta;
        this.descripcionProceso = descripcionProceso;
        this.chefNombre        = chefNombre;
        this.totalIngredientes = totalIngredientes;
    }

    public Integer getAlimentoId()          { return alimentoId; }
    public String  getAlimentoNombre()      { return alimentoNombre; }
    public BigDecimal getPrecio()           { return precio; }
    public String  getTipoAlimento()        { return tipoAlimento; }
    public String  getNombreReceta()        { return nombreReceta; }
    public String  getDescripcionProceso()  { return descripcionProceso; }
    public String  getChefNombre()          { return chefNombre; }
    public Long    getTotalIngredientes()   { return totalIngredientes; }
}