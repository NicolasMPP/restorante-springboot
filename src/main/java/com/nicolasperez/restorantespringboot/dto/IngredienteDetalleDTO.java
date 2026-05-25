package com.nicolasperez.restorantespringboot.dto;

public class IngredienteDetalleDTO {

    private Integer ingredienteId;
    private String descripcion;
    private Integer cantidadStock;

    public IngredienteDetalleDTO(
            Integer ingredienteId,
            String descripcion,
            Integer cantidadStock
    ) {
        this.ingredienteId = ingredienteId;
        this.descripcion = descripcion;
        this.cantidadStock = cantidadStock;
    }

    public Integer getIngredienteId() {
        return ingredienteId;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public Integer getCantidadStock() {
        return cantidadStock;
    }
}