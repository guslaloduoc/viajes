package com.viajes.viajes.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class VentaResponseDto {

    private Long id; // Incluimos el ID generado
    private String producto; // Incluimos el campo producto
    private LocalDate fecha;
    private BigDecimal monto;

    public VentaResponseDto() {
    }

    // Constructor para mapear desde la entidad (útil en el servicio)
    public VentaResponseDto(Long id, String producto, LocalDate fecha, BigDecimal monto) {
        this.id = id;
        this.producto = producto;
        this.fecha = fecha;
        this.monto = monto;
    }

    // Getters y Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProducto() {
        return producto;
    }

    public void setProducto(String producto) {
        this.producto = producto;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }
}