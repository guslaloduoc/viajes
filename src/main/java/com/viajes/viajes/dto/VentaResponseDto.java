package com.viajes.viajes.dto; // Ubicado en src/main/java/com/viajes/viajes/dto

import java.time.LocalDate;
import java.math.BigDecimal;

// --- Clases necesarias para HATEOAS ---
import org.springframework.hateoas.RepresentationModel;
// Puedes necesitar Link si lo usas directamente, pero RepresentationModel ya lo maneja
// import org.springframework.hateoas.Link;
// ------------------------------------

// Asumiendo que usas Lombok para getters/setters y constructores
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;


@Data // Genera getters, setters, equals, hashCode, toString
@NoArgsConstructor // Genera constructor sin argumentos
@AllArgsConstructor // Genera constructor con todos los argumentos
// --- ¡IMPORTANTE: Extender de RepresentationModel para HATEOAS! ---
public class VentaResponseDto extends RepresentationModel<VentaResponseDto> {

    private Long id; // ID de la venta
    private String producto;
    private LocalDate fecha;
    private BigDecimal monto;

    // No añadas un campo para los enlaces (_links) aquí,
    // RepresentationModel ya lo maneja internamente.

    // Si NO usas Lombok, debes tener getters y setters manuales aquí:
    /*
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    // ... otros getters y setters para producto, fecha, monto ...
    */
}