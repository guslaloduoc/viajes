package com.viajes.viajes.service;

import com.viajes.viajes.Venta;
import com.viajes.viajes.dto.VentaRequestDto;
import com.viajes.viajes.dto.VentaResponseDto;
import com.viajes.viajes.dto.DailyEarningsResponseDto;
import com.viajes.viajes.exception.ResourceNotFoundException;
import com.viajes.viajes.repository.VentaRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional // para operaciones de escritura (create, update, delete)
public class VentaServiceImpl implements VentaService {

    private final VentaRepository ventaRepository;

    public VentaServiceImpl(VentaRepository ventaRepository) {
        this.ventaRepository = ventaRepository;
    }

    @Override
    public VentaResponseDto createVenta(VentaRequestDto ventaRequestDto) {
        Venta venta = new Venta(
                ventaRequestDto.getProducto(),
                ventaRequestDto.getFecha(),
                ventaRequestDto.getMonto());
        Venta savedVenta = ventaRepository.save(venta);
        return new VentaResponseDto(
                savedVenta.getId(),
                savedVenta.getProducto(),
                savedVenta.getFecha(),
                savedVenta.getMonto());
    }

    @Override
    @Transactional(readOnly = true) // Solo lectura
    public List<VentaResponseDto> getAllVentas() {
        List<Venta> ventas = ventaRepository.findAll();
        return ventas.stream()
                .map(venta -> new VentaResponseDto(
                        venta.getId(),
                        venta.getProducto(),
                        venta.getFecha(),
                        venta.getMonto()))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true) // Solo lectura
    public DailyEarningsResponseDto calculateDailyEarnings(LocalDate date) {
        BigDecimal totalGanancias = ventaRepository.sumMontoByFecha(date);
        if (totalGanancias == null) {
            totalGanancias = BigDecimal.ZERO;
        }
        return new DailyEarningsResponseDto(date, totalGanancias);
    }

    // *** Implementación del método para actualizar ***
    @Override
    public VentaResponseDto updateVenta(Long id, VentaRequestDto ventaRequestDto) {
        // Buscar la venta existente. Si no se encuentra, lanza
        // ResourceNotFoundException
        Venta existingVenta = ventaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Venta no encontrada con ID: " + id));

        // Actualizar los campos con los datos del DTO
        existingVenta.setProducto(ventaRequestDto.getProducto());
        existingVenta.setFecha(ventaRequestDto.getFecha());
        existingVenta.setMonto(ventaRequestDto.getMonto());

        // Guardar la entidad actualizada (realiza un UPDATE)
        Venta updatedVenta = ventaRepository.save(existingVenta);

        // Convertir y devolver DTO de respuesta
        return new VentaResponseDto(
                updatedVenta.getId(),
                updatedVenta.getProducto(),
                updatedVenta.getFecha(),
                updatedVenta.getMonto());
    }

    // *** Implementación del método para eliminar ***
    @Override
    public void deleteVenta(Long id) {
        // Verificar si la venta existe antes de eliminar
        if (!ventaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Venta no encontrada con ID: " + id);
        }
        // Eliminar la venta por ID
        ventaRepository.deleteById(id);
    }

}