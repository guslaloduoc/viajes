package com.viajes.viajes.dto; // Ubicado en src/main/java/com/viajes/viajes/dto

import java.math.BigDecimal;
import java.time.LocalDate;

public class DailyEarningsResponseDto {

    private LocalDate fecha;
    private BigDecimal totalGanancias;

    // Constructor vacío
    public DailyEarningsResponseDto() {
    }

    // Constructor con campos
    public DailyEarningsResponseDto(LocalDate fecha, BigDecimal totalGanancias) {
        this.fecha = fecha;
        this.totalGanancias = totalGanancias;
    }

    // Getters y Setters

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public BigDecimal getTotalGanancias() {
        return totalGanancias;
    }

    public void setTotalGanancias(BigDecimal totalGanancias) {
        this.totalGanancias = totalGanancias;
    }
}