package com.viajes.viajes.repository;

import com.viajes.viajes.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface VentaRepository extends JpaRepository<Venta, Long> {

    // Método personalizado para encontrar ventas por fecha (opcional, pero útil)
    List<Venta> findByFecha(LocalDate fecha);

    @Query("SELECT SUM(v.monto) FROM Venta v WHERE v.fecha = :fecha")
    BigDecimal sumMontoByFecha(@Param("fecha") LocalDate fecha);

}