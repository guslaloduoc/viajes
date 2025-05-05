package com.viajes.viajes.dto; // Ubicado en src/main/java/com/viajes/viajes/dto

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal; // Asegúrate de tener este import
import java.time.LocalDate; // Asegúrate de tener este import

public class VentaRequestDto {

 @NotBlank(message = "El nombre del producto no puede estar vacío")
private String producto;

@NotNull(message = "La fecha de venta no puede ser nula")
private LocalDate fecha;

@NotNull(message = "El monto de la venta no puede ser nulo")
 @DecimalMin(value = "0.01", message = "El monto de la venta debe ser mayor a cero")
private BigDecimal monto;


// --- ¡Añade este Constructor con todos los argumentos! ---
// Este constructor es el que necesita la prueba unitaria y también puede ser útil
// para crear DTOs programáticamente.
 public VentaRequestDto(String producto, LocalDate fecha, BigDecimal monto) {
 this.producto = producto;
 this.fecha = fecha;
this.monto = monto;
}
 // -----------------------------------------------------

 // --- Getters y Setters (necesarios para la deserialización y acceso a campos) ---

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