package com.viajes.viajes.controller;

import com.viajes.viajes.dto.VentaRequestDto;
import com.viajes.viajes.dto.VentaResponseDto;
import com.viajes.viajes.dto.DailyEarningsResponseDto;
import com.viajes.viajes.service.VentaService;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.logging.Logger; //Para loggear errores

@RestController
@RequestMapping("/api/ventas")
public class VentaController {

    private static final Logger logger = Logger.getLogger(VentaController.class.getName());
    private final VentaService ventaService;

    public VentaController(VentaService ventaService) {
        this.ventaService = ventaService;
    }

    // Endpoint POST para crear una nueva venta
    // POST http://localhost:8080/api/ventas
    @PostMapping
    public ResponseEntity<VentaResponseDto> createVenta(@Valid @RequestBody VentaRequestDto ventaRequestDto) {
        logger.info("Recibida solicitud para crear venta: " + ventaRequestDto);
        VentaResponseDto createdVenta = ventaService.createVenta(ventaRequestDto);
        return new ResponseEntity<>(createdVenta, HttpStatus.CREATED);
    }

    // Endpoint GET para obtener todas las ventas
    // GET http://localhost:8080/api/ventas
    @GetMapping
    public List<VentaResponseDto> getAllVentas() {
        logger.info("Recibida solicitud para obtener todas las ventas.");
        return ventaService.getAllVentas();
    }

    // Endpoint GET para obtener ganancias diarias por fecha
    // GET http://localhost:8080/api/ventas/earnings/YYYY-MM-DD
    @GetMapping("/earnings/{dateStr}")
    public ResponseEntity<?> getDailyEarnings(@PathVariable String dateStr) {
        logger.info("Recibida solicitud para obtener ganancias del día: " + dateStr);
        try {
            LocalDate date = LocalDate.parse(dateStr);
            DailyEarningsResponseDto dailyEarnings = ventaService.calculateDailyEarnings(date);
            return ResponseEntity.ok(dailyEarnings);
        } catch (DateTimeParseException e) {
            logger.warning("Error al parsear la fecha '" + dateStr + "': " + e.getMessage());
            return ResponseEntity.badRequest().body("Formato de fecha inválido. Use YYYY-MM-DD.");
        } catch (Exception e) {
            logger.severe("Error inesperado al calcular ganancias diarias para " + dateStr + ": " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ocurrió un error interno.");
        }
    }

    // *** Nuevo Endpoint PUT para actualizar una venta por ID ***
    // PUT http://localhost:8080/api/ventas/{id}
    @PutMapping("/{id}") // Mapea a solicitudes PUT a /api/ventas/{id}
    public VentaResponseDto updateVenta(
            @PathVariable Long id, // El ID de la venta a actualizar
            @Valid @RequestBody VentaRequestDto ventaRequestDto) { // Los datos actualizados
        logger.info("Recibida solicitud para actualizar venta con ID: " + id);
        return ventaService.updateVenta(id, ventaRequestDto);
    }

    // DELETE http://localhost:8080/api/ventas/{id}
    @DeleteMapping("/{id}") // Mapea a solicitudes DELETE a /api/ventas/{id}
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteVenta(@PathVariable Long id) {
        logger.info("Recibida solicitud para eliminar venta con ID: " + id);
        ventaService.deleteVenta(id);

    }

}