package com.viajes.viajes.service;

import com.viajes.viajes.dto.VentaRequestDto;
import com.viajes.viajes.dto.VentaResponseDto;
import com.viajes.viajes.dto.DailyEarningsResponseDto;

import java.time.LocalDate;
import java.util.List;

public interface VentaService {

    VentaResponseDto createVenta(VentaRequestDto ventaRequestDto);

    List<VentaResponseDto> getAllVentas();

    DailyEarningsResponseDto calculateDailyEarnings(LocalDate date);

    VentaResponseDto getVentaById(Long id);

    VentaResponseDto updateVenta(Long id, VentaRequestDto ventaRequestDto);

    void deleteVenta(Long id);

}