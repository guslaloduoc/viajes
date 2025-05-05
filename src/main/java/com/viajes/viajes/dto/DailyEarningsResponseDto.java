package com.viajes.viajes.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.hateoas.RepresentationModel;

public class DailyEarningsResponseDto extends RepresentationModel<DailyEarningsResponseDto> {

private LocalDate fecha;
private BigDecimal totalGanancias;

// Constructor vacío (mantenido)
public DailyEarningsResponseDto() {
 }

// Constructor con campos (mantenido)
public DailyEarningsResponseDto(LocalDate fecha, BigDecimal totalGanancias) {
 this.fecha = fecha;
this.totalGanancias = totalGanancias;
}

// Getters y Setters (mantenidos)

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