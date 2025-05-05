package com.viajes.viajes.controller;

// --- Imports existentes ---
import com.viajes.viajes.dto.VentaRequestDto;
import com.viajes.viajes.dto.VentaResponseDto; // Asegúrate de que extienda RepresentationModel
import com.viajes.viajes.dto.DailyEarningsResponseDto; // Asegúrate de que extienda RepresentationModel (opcional para HATEOAS)
import com.viajes.viajes.service.VentaService;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity; // Necesario para ResponseEntity
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.logging.Logger;

// --- ¡Añade o asegúrate de tener estos imports para HATEOAS! ---
import org.springframework.hateoas.Link;
import org.springframework.hateoas.CollectionModel; // Necesario para envolver listas de DTOs con enlaces de colección
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
// Importa tu propio controlador para usarlo en methodOn()
import com.viajes.viajes.controller.VentaController; // Auto-referencia
// Imports estáticos para linkTo() y methodOn() - facilitan la escritura
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
// ------------------------------------------------------------


@RestController
@RequestMapping("/api/ventas")
public class VentaController {

    private static final Logger logger = Logger.getLogger(VentaController.class.getName());
    private final VentaService ventaService;

    public VentaController(VentaService ventaService) {
        this.ventaService = ventaService;
    }

    // Endpoint POST para crear una nueva venta (CON HATEOAS)
    // POST http://localhost:8080/api/ventas
    @PostMapping
    public ResponseEntity<VentaResponseDto> createVenta(@Valid @RequestBody VentaRequestDto ventaRequestDto) {
        logger.info("Recibida solicitud para crear venta: " + ventaRequestDto);
        VentaResponseDto createdVenta = ventaService.createVenta(ventaRequestDto);

        // --- Añadir enlaces HATEOAS ---
        // 1. Enlace "self" al recurso recién creado (GET /api/ventas/{id})
        // Usamos methodOn(VentaController.class) para referenciar métodos de este controlador de forma segura
        Link selfLink = linkTo(methodOn(VentaController.class).getVentaById(createdVenta.getId()))
                            .withSelfRel(); // Relación estándar "self"
        createdVenta.add(selfLink); // Añade el enlace al DTO

        // 2. Enlace a la colección de ventas (GET /api/ventas)
        Link collectionLink = linkTo(methodOn(VentaController.class).getAllVentas())
                                .withRel("collection"); // Relación personalizada o estándar "collection"
        createdVenta.add(collectionLink); // Añade el enlace al DTO
        // ------------------------------

        // Retorna 201 Created e incluye el DTO con los enlaces
        return new ResponseEntity<>(createdVenta, HttpStatus.CREATED);
    }

    // Endpoint GET para obtener todas las ventas (CON HATEOAS)
    // GET http://localhost:8080/api/ventas
    @GetMapping
    // El retorno es un ResponseEntity que envuelve un CollectionModel de VentaResponseDto
    // CollectionModel es necesario para añadir enlaces a la colección misma (no a cada ítem individual)
    public ResponseEntity<CollectionModel<VentaResponseDto>> getAllVentas() {
        logger.info("Recibida solicitud para obtener todas las ventas.");
        List<VentaResponseDto> ventas = ventaService.getAllVentas();

        // --- Añadir enlaces HATEOAS a cada ítem de la lista ---
        for (VentaResponseDto venta : ventas) {
            // 1. Enlace "self" para CADA venta individual en la lista (GET /api/ventas/{id})
             Link selfLink = linkTo(methodOn(VentaController.class).getVentaById(venta.getId()))
                                .withSelfRel();
            venta.add(selfLink);

            // Puedes añadir otros enlaces a cada venta individual si aplica (ej. link a usuario si existiera)
        }

        // --- Añadir enlaces HATEOAS a la colección misma ---
        // 2. Enlace "self" a la colección (GET /api/ventas)
        Link selfCollectionLink = linkTo(methodOn(VentaController.class).getAllVentas())
                                    .withSelfRel(); // El self link de la colección

        // 3. Enlace para crear una nueva venta (POST /api/ventas)
         Link createLink = linkTo(methodOn(VentaController.class).createVenta(null)) // Usamos null ya que no podemos saber el RequestBody aquí
                            .withRel("create"); // Relación personalizada "create"


        // Envuelve la lista de ventas en un CollectionModel y añade los enlaces de la colección
        // La lista va primero, seguida de los enlaces de la colección
        CollectionModel<VentaResponseDto> collectionModel = CollectionModel.of(ventas, selfCollectionLink, createLink);
        // ------------------------------------------------------------

        return ResponseEntity.ok(collectionModel); // Retorna 200 OK con el CollectionModel (lista + enlaces)
    }


    // Endpoint GET para obtener una venta por su ID (Añadido y CON HATEOAS)
    // GET http://localhost:8080/api/ventas/{id}
    @GetMapping("/{id}") // Mapea solicitudes GET a /api/ventas/{id}
    public ResponseEntity<VentaResponseDto> getVentaById(@PathVariable Long id) {
        logger.info("Recibida solicitud para obtener venta con ID: " + id);
        VentaResponseDto venta = ventaService.getVentaById(id); // Llama al servicio para obtener el DTO

        // --- Añadir enlaces HATEOAS ---
        // 1. Enlace "self" al recurso actual (GET /api/ventas/{id})
        Link selfLink = linkTo(methodOn(VentaController.class).getVentaById(id))
                            .withSelfRel();
        venta.add(selfLink); // Añade el enlace al DTO

        // 2. Enlace a la colección de ventas (GET /api/ventas)
        Link collectionLink = linkTo(methodOn(VentaController.class).getAllVentas())
                                .withRel("collection"); // Relación a la colección
        venta.add(collectionLink);

        // 3. Enlace para actualizar esta venta (PUT /api/ventas/{id})
        // Nota: Usamos el ID en methodOn para construir la URL /api/ventas/{id}
        Link updateLink = linkTo(methodOn(VentaController.class).updateVenta(id, null)) // Referencia al método PUT, RequestBody es null
                            .withRel("update"); // Relación "update"
        venta.add(updateLink);

        // 4. Enlace para eliminar esta venta (DELETE /api/ventas/{id})
        // Nota: Ahora deleteVenta retorna ResponseEntity<Void>, compatible con methodOn
         Link deleteLink = linkTo(methodOn(VentaController.class).deleteVenta(id)) // Referencia al método DELETE
                            .withRel("delete"); // Relación "delete"
         venta.add(deleteLink);
        // ------------------------------

        return ResponseEntity.ok(venta); // Devuelve 200 OK con el DTO y enlaces
    }


    // Endpoint GET para obtener ganancias diarias por fecha (CON HATEOAS - opcional en DailyEarningsResponseDto)
    // GET http://localhost:8080/api/ventas/earnings/YYYY-MM-DD
    @GetMapping("/earnings/{dateStr}")
    // El retorno es un ResponseEntity que puede contener DailyEarningsResponseDto (si extiende RepresentationModel) o String (en caso de error)
    public ResponseEntity<?> getDailyEarnings(@PathVariable String dateStr) {
        logger.info("Recibida solicitud para obtener ganancias del día: " + dateStr);
        try {
            LocalDate date = LocalDate.parse(dateStr);
            DailyEarningsResponseDto dailyEarnings = ventaService.calculateDailyEarnings(date);

            // --- Añadir enlaces HATEOAS a DailyEarningsResponseDto (opcional y si extiende RepresentationModel) ---
            // Si DailyEarningsResponseDto extiende RepresentationModel<DailyEarningsResponseDto>
            // Enlace "self" al resultado específico (GET /api/ventas/earnings/YYYY-MM-DD)
            // Usamos dateStr porque es el tipo que recibe el método del controlador
            Link selfLink = linkTo(methodOn(VentaController.class).getDailyEarnings(dateStr))
                                .withSelfRel();
            dailyEarnings.add(selfLink);

            // Enlace a la colección de ventas (GET /api/ventas) - puede ser útil para navegar
             Link collectionLink = linkTo(methodOn(VentaController.class).getAllVentas())
                                .withRel("ventas_collection"); // Relación personalizada

            dailyEarnings.add(collectionLink);
            // --------------------------------------------------------------------

            // Retorna 200 OK con el DTO (y enlaces, si se añadieron)
            return ResponseEntity.ok(dailyEarnings);
        } catch (DateTimeParseException e) {
            logger.warning("Error al parsear la fecha '" + dateStr + "': " + e.getMessage());
            // En las respuestas de error típicamente NO se añaden enlaces HATEOAS
            return ResponseEntity.badRequest().body("Formato de fecha inválido. UseYYYY-MM-DD.");
        } catch (Exception e) {
            logger.severe("Error inesperado al calcular ganancias diarias para " + dateStr + ": " + e.getMessage());
            // En las respuestas de error típicamente NO se añaden enlaces HATEOAS
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ocurrió un error interno.");
        }
    }


    // *** Endpoint PUT para actualizar una venta por ID (CON HATEOAS) ***
    // PUT http://localhost:8080/api/ventas/{id}
    @PutMapping("/{id}") // Mapea a solicitudes PUT a /api/ventas/{id}
    // Retorna ResponseEntity<VentaResponseDto> para poder incluir el DTO actualizado con enlaces y estado 200 OK
    public ResponseEntity<VentaResponseDto> updateVenta(
            @PathVariable Long id, // El ID de la venta a actualizar
            @Valid @RequestBody VentaRequestDto ventaRequestDto) { // Los datos actualizados
        logger.info("Recibida solicitud para actualizar venta con ID: " + id);
        VentaResponseDto updatedVenta = ventaService.updateVenta(id, ventaRequestDto);

        // --- Añadir enlaces HATEOAS ---
        // 1. Enlace "self" al recurso actualizado (GET /api/ventas/{id})
        Link selfLink = linkTo(methodOn(VentaController.class).getVentaById(id))
                            .withSelfRel();
        updatedVenta.add(selfLink);

        // 2. Enlace a la colección de ventas (GET /api/ventas)
        Link collectionLink = linkTo(methodOn(VentaController.class).getAllVentas())
                                .withRel("collection");
        updatedVenta.add(collectionLink);

        // 3. Enlace para eliminar esta venta (DELETE /api/ventas/{id})
        // Nota: Ahora deleteVenta retorna ResponseEntity<Void>, compatible con methodOn
        Link deleteLink = linkTo(methodOn(VentaController.class).deleteVenta(id))
                            .withRel("delete");
        updatedVenta.add(deleteLink);



        return ResponseEntity.ok(updatedVenta); // Retorna 200 OK con el DTO actualizado y enlaces
    }

    // DELETE http://localhost:8080/api/ventas/{id}
    @DeleteMapping("/{id}") // Mapea a solicitudes DELETE a /api/ventas/{id}
    @ResponseStatus(HttpStatus.NO_CONTENT) // Retorna 204 No Content (sin cuerpo de respuesta)
    // --- ¡Cambiado de 'void' a 'ResponseEntity<Void>' para compatibilidad con methodOn! ---
    public ResponseEntity<Void> deleteVenta(@PathVariable Long id) {
        logger.info("Recibida solicitud para eliminar venta con ID: " + id);
        ventaService.deleteVenta(id);
        // Retorna ResponseEntity.noContent() para estado 204 y compatibilidad con methodOn
        return ResponseEntity.noContent().build();
    }

}