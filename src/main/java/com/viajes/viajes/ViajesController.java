package com.viajes.viajes;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ViajesController {

	private List<Viajes> viajes = new ArrayList<>();

	public ViajesController() {
		// agregamos datos de ejemplo
		viajes.add(
				new Viajes(
						1,
						"Daniel Rodriguez Martinez",
						"Andrés Calamaro",
						"Perro",
						"Macul 357",
						"La Granja 48"));
		viajes.add(
				new Viajes(
						2,
						"Alex Pretter Erices",
						"Johan Hernandez",
						"Gato",
						"Los Trapenses 489",
						"Calle Brasil 56"));
		viajes.add(
				new Viajes(
						3,
						"Gerardo Guzman",
						"Francisco Perez",
						"Loro",
						"Argentina 35",
						"Petorca 778"));
		viajes.add(
				new Viajes(
						4,
						"Fernando Villegas",
						"Leisla Soto",
						"Tortuga",
						"Osoro 0598",
						"Llollinco 569"));
	}

	// end point para ver todos los viajes
	@GetMapping("/viajes")
	public List<Viajes> getViajes() {
		return viajes;
	}

	// end point para ver los viajes por id
	@GetMapping("/viajes/{id}")
	public Viajes getViajesById(@PathVariable int id) {
		for (Viajes viajes : viajes) {
			if (viajes.getId() == id) {
				return viajes;
			}
		}
		return null;
	}
}