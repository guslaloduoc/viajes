package com.viajes.viajes; // Debe estar en el mismo paquete que tu clase Viajes
// --- Imports para JUnit 5 ---
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*; // Para aserciones como assertEquals
public class ViajesTest {
	@Test // Marca este método como una prueba unitaria
	void testConstructorAndGetters() {
		// Arrange (Preparar)
		int expectedId = 1;
		String expectedNombreConductor = "Juan Perez";
		String expectedNombrePasajero = "Maria Lopez";
		String expectedTipoMascota = "Perro";
		String expectedInicioViaje = "Calle A 123";
		String expectedDestinoViaje = "Avenida B 456";
		// Act (Actuar)
		// Creamos una instancia de la clase Viajes usando el constructor
		Viajes viaje = new Viajes(expectedId, expectedNombreConductor, expectedNombrePasajero, expectedTipoMascota, expectedInicioViaje, expectedDestinoViaje);
		// Assert (Verificar)
		// Verificamos que cada getter retorna el valor que se le pasó al constructor
		assertEquals(expectedId, viaje.getId(), "El ID debe coincidir");
		assertEquals(expectedNombreConductor, viaje.getConductorNombre(), "El nombre del conductor debe coincidir");
		assertEquals(expectedNombrePasajero, viaje.getPasajeroNombre(), "El nombre del pasajero debe coincidir");
		assertEquals(expectedTipoMascota, viaje.getMascotaTipo(), "El tipo de mascota debe coincidir");
		assertEquals(expectedInicioViaje, viaje.getInicioViaje(), "El inicio del viaje debe coincidir");
		assertEquals(expectedDestinoViaje, viaje.getDestinoViaje(), "El destino del viaje debe coincidir");
	}
	// Si tu clase Viajes tuviera setters, también añadirías tests para verificar
	// que los setters cambian el estado de la objeto correctamente y los getters
	// reflejan esos cambios. Pero como no tiene setters en el código que mostraste,
	// con este test del constructor y getters es suficiente para cubrir la lógica presente.
}