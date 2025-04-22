package com.viajes.viajes.dto; // Ubicado en src/main/java/com/viajes/viajes/dto

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public class VentaRequestDto {

    @NotBlank(message = "El nombre del producto no puede estar vacío")
    private String producto; // Incluimos el campo producto

    @NotNull(message = "La fecha de venta no puede ser nula")
    private LocalDate fecha;

    @NotNull(message = "El monto de la venta no puede ser nulo")
    @DecimalMin(value = "0.01", message = "El monto de la venta debe ser mayor a cero")
    private BigDecimal monto;

    // Getters y Setters (necesarios para la deserialización JSON por Spring)

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