package com.viajes.viajes.service;
// --- Imports para JUnit 5 ---
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
// --- Imports para Mockito ---
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
// --- Imports de tu código (asegúrate de que las rutas del paquete sean correctas) ---
import com.viajes.viajes.Venta;
import com.viajes.viajes.dto.VentaRequestDto;
import com.viajes.viajes.dto.VentaResponseDto;
import com.viajes.viajes.dto.DailyEarningsResponseDto;
import com.viajes.viajes.exception.ResourceNotFoundException; // Asegúrate de importar tu excepción
import com.viajes.viajes.repository.VentaRepository; // Importa tu repositorio
// --- Otros imports necesarios ---
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
// --- Imports para aserciones y verificaciones de Mockito ---
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*; // Importa los métodos estáticos when y verify
// Indica que MockitoExtension debe ser usado para inicializar mocks antes de cada test
@ExtendWith(MockitoExtension.class)
public class VentaServiceImplTest {
	// Crea una instancia mock del repositorio (la dependencia de tu servicio)
	@Mock
	private VentaRepository ventaRepository;
	// Inyecta los mocks (@Mock) en una instancia real de tu servicio (VentaServiceImpl)
	@InjectMocks
	private VentaServiceImpl ventaService; // Asumo que esta es tu implementación de VentaService
	// --- Variables de prueba comunes ---
	private VentaRequestDto ventaRequestDto;
	private Venta venta;
	private Venta savedVenta;
	private VentaResponseDto ventaResponseDto;
	@BeforeEach // Este método se ejecuta antes de cada test (@Test)
	void setUp() {
		// Inicializa objetos de prueba comunes que se usarán en varios tests
		LocalDate testDate = LocalDate.of(2025, 5, 5);
		BigDecimal testMonto = new BigDecimal("100.50");
		Long testId = 1L;
		// Request DTO de ejemplo para crear/actualizar
		ventaRequestDto = new VentaRequestDto("Producto A", testDate, testMonto);
		// Entidad Venta de ejemplo (tal como podría venir del repositorio ANTES de guardar, sin ID)
		venta = new Venta("Producto A", testDate, testMonto);
		// Entidad Venta de ejemplo (tal como podría venir del repositorio DESPUES de guardar, con ID asignado)
		savedVenta = new Venta("Producto A", testDate, testMonto);
		savedVenta.setId(testId); // Asigna un ID como si ya se hubiera guardado en la BD
		// Response DTO de ejemplo (tal como debería ser retornado por el servicio)
		ventaResponseDto = new VentaResponseDto(testId, "Producto A", testDate, testMonto);
	}
	// --- Tests para el método createVenta ---
	@Test // Marca este método como una prueba unitaria
	void testCreateVenta_Success() {
		// Arrange (Preparar el escenario para el test)
		// Configura el mock ventaRepository: cuando se llame save() con CUALQUIER objeto Venta, retorna savedVenta
		when(ventaRepository.save(any(Venta.class))).thenReturn(savedVenta);
		// Act (Ejecutar la acción que queremos probar)
		VentaResponseDto result = ventaService.createVenta(ventaRequestDto);
		// Assert (Verificar que el resultado es el esperado)
		// Verifica que el DTO retornado no sea nulo
		assertNotNull(result, "El DTO de respuesta no debe ser nulo");
		// Verifica que los campos del DTO de respuesta coincidan con los de savedVenta o ventaRequestDto
		assertEquals(savedVenta.getId(), result.getId(), "El ID del DTO de respuesta debe coincidir con el de la venta guardada");
		assertEquals(ventaRequestDto.getProducto(), result.getProducto(), "El producto en el DTO de respuesta debe coincidir con el del request");
		assertEquals(ventaRequestDto.getFecha(), result.getFecha(), "La fecha en el DTO de respuesta debe coincidir con el del request");
		assertEquals(ventaRequestDto.getMonto(), result.getMonto(), "El monto en el DTO de respuesta debe coincidir con el del request");
		// Verifica las interacciones con los mocks: asegura que save() fue llamado exactamente 1 vez con un objeto Venta
		verify(ventaRepository, times(1)).save(any(Venta.class));
	}
	// --- Tests para el método getAllVentas ---
	@Test
	void testGetAllVentas_Success() {
		// Arrange
		// Prepara una lista de entidades Venta simulando que el repositorio la retorna
		Venta venta1 = new Venta("Prod1", LocalDate.of(2025, 5, 1), new BigDecimal("50"));
		venta1.setId(1L);
		Venta venta2 = new Venta("Prod2", LocalDate.of(2025, 5, 2), new BigDecimal("75"));
		venta2.setId(2L);
		List < Venta > ventasList = Arrays.asList(venta1, venta2);
		// Configura el mock: cuando se llame findAll(), retorna ventasList
		when(ventaRepository.findAll()).thenReturn(ventasList);
		// Act
		List < VentaResponseDto > result = ventaService.getAllVentas();
		// Assert
		// Verifica que la lista retornada no sea nula y tenga el tamaño esperado
		assertNotNull(result, "La lista de DTOs no debe ser nula");
		assertEquals(2, result.size(), "La lista debe contener 2 elementos");
		// Verifica los contenidos de los DTOs en la lista (mapeo de entidad a DTO)
		assertEquals(venta1.getId(), result.get(0).getId(), "El ID del primer DTO debe coincidir");
		assertEquals(venta1.getProducto(), result.get(0).getProducto(), "El producto del primer DTO debe coincidir");
		assertEquals(venta2.getId(), result.get(1).getId(), "El ID del segundo DTO debe coincidir");
		assertEquals(venta2.getProducto(), result.get(1).getProducto(), "El producto del segundo DTO debe coincidir");
		// Verifica que findAll() del repositorio fue llamado exactamente 1 vez
		verify(ventaRepository, times(1)).findAll();
	}
	@Test // Test para el caso de lista vacía
	void testGetAllVentas_EmptyList() {
		// Arrange
		// Configura el mock: cuando se llame findAll(), retorna una lista vacía
		when(ventaRepository.findAll()).thenReturn(Arrays.asList());
		// Act
		List < VentaResponseDto > result = ventaService.getAllVentas();
		// Assert
		// Verifica que la lista retornada no sea nula pero esté vacía
		assertNotNull(result, "La lista de DTOs no debe ser nula");
		assertTrue(result.isEmpty(), "La lista debe estar vacía");
		// Verifica que findAll() fue llamado
		verify(ventaRepository, times(1)).findAll();
	}
	// --- Tests para el método calculateDailyEarnings ---
	@Test // Test para el caso de que haya ganancias para la fecha
	void testCalculateDailyEarnings_Success() {
		// Arrange
		LocalDate targetDate = LocalDate.of(2025, 5, 5);
		BigDecimal expectedEarnings = new BigDecimal("250.75");
		// Configura el mock: cuando se llame sumMontoByFecha() con la fecha, retorna el monto esperado
		when(ventaRepository.sumMontoByFecha(targetDate)).thenReturn(expectedEarnings);
		// Act
		DailyEarningsResponseDto result = ventaService.calculateDailyEarnings(targetDate);
		// Assert
		assertNotNull(result, "El DTO de respuesta no debe ser nulo");
		assertEquals(targetDate, result.getFecha(), "La fecha en el DTO de respuesta debe coincidir");
		assertEquals(expectedEarnings, result.getTotalGanancias(), "El total de ganancias debe coincidir");
		// Verifica que sumMontoByFecha() fue llamado con la fecha correcta
		verify(ventaRepository, times(1)).sumMontoByFecha(targetDate);
	}
	@Test // Test para el caso de que NO haya ganancias para la fecha (retorna null)
	void testCalculateDailyEarnings_NoSalesForDate() {
		// Arrange
		LocalDate targetDate = LocalDate.of(2025, 5, 6);
		// Configura el mock: cuando se llame sumMontoByFecha() con la fecha, retorna null
		when(ventaRepository.sumMontoByFecha(targetDate)).thenReturn(null);
		// Act
		DailyEarningsResponseDto result = ventaService.calculateDailyEarnings(targetDate);
		// Assert
		assertNotNull(result, "El DTO de respuesta no debe ser nulo");
		assertEquals(targetDate, result.getFecha(), "La fecha en el DTO de respuesta debe coincidir");
		assertEquals(BigDecimal.ZERO, result.getTotalGanancias(), "El total de ganancias debe ser cero si no hay ventas");
		// Verifica que sumMontoByFecha() fue llamado
		verify(ventaRepository, times(1)).sumMontoByFecha(targetDate);
	}
	// --- Tests para el método updateVenta ---
	@Test // Test para el caso de actualización exitosa
	void testUpdateVenta_Success() {
		// Arrange
		Long ventaId = 1L;
		LocalDate newDate = LocalDate.of(2025, 5, 7);
		BigDecimal newMonto = new BigDecimal("150.00");
		VentaRequestDto updateRequestDto = new VentaRequestDto("Producto B Actualizado", newDate, newMonto);
		// Simula la venta existente que findById retornará
		Venta existingVenta = new Venta("Producto A Anterior", LocalDate.of(2025, 5, 5), new BigDecimal("100.50"));
		existingVenta.setId(ventaId);
		// Simula la venta después de ser actualizada por el servicio y guardada por el repositorio
		Venta updatedAndSavedVenta = new Venta("Producto B Actualizado", newDate, newMonto);
		updatedAndSavedVenta.setId(ventaId); // Debe mantener el mismo ID
		// Configura mocks:
		// 1. Cuando se llame findById() con el ID, retorna Optional.of(existingVenta)
		when(ventaRepository.findById(ventaId)).thenReturn(Optional.of(existingVenta));
		// 2. Cuando se llame save() con CUALQUIER objeto Venta, retorna updatedAndSavedVenta
		when(ventaRepository.save(any(Venta.class))).thenReturn(updatedAndSavedVenta);
		// Act
		VentaResponseDto result = ventaService.updateVenta(ventaId, updateRequestDto);
		// Assert
		assertNotNull(result, "El DTO de respuesta no debe ser nulo");
		assertEquals(ventaId, result.getId(), "El ID del DTO de respuesta debe coincidir");
		assertEquals(updateRequestDto.getProducto(), result.getProducto(), "El producto actualizado en el DTO debe coincidir");
		assertEquals(updateRequestDto.getFecha(), result.getFecha(), "La fecha actualizada en el DTO debe coincidir");
		assertEquals(updateRequestDto.getMonto(), result.getMonto(), "El monto actualizado en el DTO debe coincidir");
		// Verifica las llamadas a los mocks:
		// 1. Verifica que findById() fue llamado exactamente 1 vez con el ID correcto
		verify(ventaRepository, times(1)).findById(ventaId);
		// 2. Verifica que save() fue llamado exactamente 1 vez con una instancia de Venta
		verify(ventaRepository, times(1)).save(any(Venta.class));
	}
	@Test // Test para el caso de que la venta a actualizar NO exista
	void testUpdateVenta_NotFound() {
		// Arrange
		Long nonExistentId = 99L;
		// Configura el mock: cuando se llame findById() con el ID, retorna Optional.empty() (no encontrado)
		when(ventaRepository.findById(nonExistentId)).thenReturn(Optional.empty());
		// Act & Assert
		// Verifica que se lanza la excepción ResourceNotFoundException cuando se llama al método updateVenta
		ResourceNotFoundException thrown = assertThrows(ResourceNotFoundException.class, // Esperamos esta excepción
			() -> ventaService.updateVenta(nonExistentId, ventaRequestDto), // Llama al método que debería lanzar la excepción
			"Se esperaba ResourceNotFoundException al intentar actualizar una venta inexistente" // Mensaje si no se lanza
		);
		// Opcional: Verifica el mensaje de la excepción lanzada
		assertTrue(thrown.getMessage().contains("Venta no encontrada con ID: " + nonExistentId), "El mensaje de la excepción debe contener el ID de la venta no encontrada");
		// Verifica las interacciones con los mocks: findById() fue llamado, pero save() NUNCA debería ser llamado
		verify(ventaRepository, times(1)).findById(nonExistentId);
		verify(ventaRepository, never()).save(any(Venta.class));
	}
	// --- Tests para el método deleteVenta ---
	@Test // Test para el caso de eliminación exitosa
	void testDeleteVenta_Success() {
		// Arrange
		Long ventaId = 1L;
		// Configura el mock: cuando se llame existsById() con el ID, retorna true (existe)
		when(ventaRepository.existsById(ventaId)).thenReturn(true);
		// Para métodos void como deleteById, no necesitas configurar 'when'. Mockito simplemente no hace nada por defecto.
		// Act
		// Llama al método del servicio. assertDoesNotThrow verifica que no se lance ninguna excepción.
		assertDoesNotThrow(() -> ventaService.deleteVenta(ventaId), "No se esperaba excepción al eliminar una venta existente");
		// Assert
		// Verifica las interacciones con los mocks:
		// 1. Verifica que existsById() fue llamado para verificar la existencia
		verify(ventaRepository, times(1)).existsById(ventaId);
		// 2. Verifica que deleteById() fue llamado exactamente 1 vez con el ID correcto
		verify(ventaRepository, times(1)).deleteById(ventaId);
	}
	@Test // Test para el caso de que la venta a eliminar NO exista
	void testDeleteVenta_NotFound() {
		// Arrange
		Long nonExistentId = 99L;
		// Configura el mock: cuando se llame existsById() con el ID, retorna false (no existe)
		when(ventaRepository.existsById(nonExistentId)).thenReturn(false);
		// Act & Assert
		// Verifica que se lanza la excepción ResourceNotFoundException
		ResourceNotFoundException thrown = assertThrows(ResourceNotFoundException.class, // Esperamos esta excepción
			() -> ventaService.deleteVenta(nonExistentId), // Llama al método que debería lanzar la excepción
			"Se esperaba ResourceNotFoundException al intentar eliminar una venta inexistente" // Mensaje si no se lanza
		);
		// Opcional: Verifica el mensaje de la excepción lanzada
		assertTrue(thrown.getMessage().contains("Venta no encontrada con ID: " + nonExistentId), "El mensaje de la excepción debe contener el ID de la venta no encontrada");
		// Verifica las interacciones con los mocks: existsById() fue llamado, pero deleteById() NUNCA debería ser llamado
		verify(ventaRepository, times(1)).existsById(nonExistentId);
		verify(ventaRepository, never()).deleteById(anyLong()); // O any() si el ID es Long
	}
	// Puedes añadir más tests si hay lógica compleja adicional en el servicio (ej. validaciones específicas, cálculos, etc.)
}