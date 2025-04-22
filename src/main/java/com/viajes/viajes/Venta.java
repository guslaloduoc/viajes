package com.viajes.viajes;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.math.BigDecimal;

@Entity // Indica que esta clase es una entidad JPA
@Table(name = "VENTAS_MASCOTAS")
public class Venta {

	@Id // Clave primaria
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ventas_mascotas_seq") // Configura la generación//
																							// automática de ID
	@SequenceGenerator(name = "ventas_mascotas_seq", sequenceName = "VENTAS_MASCOTAS_SEQ", allocationSize = 1)
	private Long id;

	@Column(name = "PRODUCTO", nullable = false) // Nombre del producto vendido
	private String producto;

	@Column(name = "FECHA_VENTA", nullable = false)
	private LocalDate fecha; // Solo la fecha de la venta

	@Column(name = "MONTO_VENTA", nullable = false, precision = 10, scale = 2) // Monto total de la venta
	private BigDecimal monto;

	// Constructor vacío requerido por JPA
	public Venta() {
	}

	// Constructor para crear objetos Venta (sin ID, que es autogenerado)
	public Venta(String producto, LocalDate fecha, BigDecimal monto) {
		this.producto = producto;
		this.fecha = fecha;
		this.monto = monto;
	}

	// Getters y Setters (necesarios para JPA y para que Spring los use)

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getProducto() {
		return producto;
	}

	public void setProducto(String producto) {
		this.producto = producto;
	}

	public LocalDate getFecha() {
		return fecha;
	}

	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
	}

	public BigDecimal getMonto() {
		return monto;
	}

	public void setMonto(BigDecimal monto) {
		this.monto = monto;
	}

	@Override
	public String toString() {
		return "Venta{" +
				"id=" + id +
				", producto='" + producto + '\'' +
				", fecha=" + fecha +
				", monto=" + monto +
				'}';
	}
}