package com.viajes.viajes;

//Clase Viajes
public class Viajes {

	private int id;
	private String nombre_conductor;
	private String nombre_pasajero;
	private String tipo_mascota;
	private String inicio_viaje;
	private String destino_viaje;

	public Viajes(
			int id,
			String nombre_conductor,
			String nombre_pasajero,
			String tipo_mascota,
			String inicio_viaje,
			String destino_viaje) {
		this.id = id;
		this.nombre_conductor = nombre_conductor;
		this.nombre_pasajero = nombre_pasajero;
		this.tipo_mascota = tipo_mascota;
		this.inicio_viaje = inicio_viaje;
		this.destino_viaje = destino_viaje;
	}

	// metodos para acceder a los valores
	public int getId() {
		return id;
	}

	public String getConductorNombre() {
		return nombre_conductor;
	}

	public String getPasajeroNombre() {
		return nombre_pasajero;
	}

	public String getMascotaTipo() {
		return tipo_mascota;
	}

	public String getInicioViaje() {
		return inicio_viaje;
	}

	public String getDestinoViaje() {
		return destino_viaje;
	}
}