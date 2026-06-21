package com.mariluz.soporte.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.mariluz.soporte.dto.ErrorResponse;
import com.mariluz.soporte.dto.MensajeRequest;
import com.mariluz.soporte.dto.TicketRequest;
import com.mariluz.soporte.dto.TicketResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(
    name = "Tickets de Soporte",
    description = "Gestión de tickets de soporte y mensajes del usuario autenticado"
)
public interface SoporteApi {

    // 1. crear ticket
    @Operation(
        summary = "Crear ticket",
        description = "Crea un nuevo ticket de soporte para el usuario autenticado."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "201",
            description = "Ticket creado correctamente."
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Datos inválidos (campos obligatorios faltantes o formato incorrecto).",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @ExampleObject(
                    value = """
                    {
                        "timestamp": "2026-06-12T05:11:58",
                        "status": 400,
                        "message": "Datos inválidos",
                        "errors": { "asunto": "El asunto no puede estar vacío" },
                        "endpoint": "/tickets/crear"
                    }
                    """
                )
            )
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Token JWT inválido o expirado.",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @ExampleObject(
                    value = """
                    {
                        "timestamp": "2026-06-12T05:11:58",
                        "status": 401,
                        "message": "No autenticado",
                        "errors": { "error": "Se requiere token de autenticación" },
                        "endpoint": "/tickets/crear"
                    }
                    """
                )
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Error interno del servidor.",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @ExampleObject(
                    value = """
                    {
                        "timestamp": "2026-06-12T05:11:58",
                        "status": 500,
                        "message": "Error interno del servidor",
                        "errors": { "error": "Error inesperado" },
                        "endpoint": "/tickets/crear"
                    }
                    """
                )
            )
        )
    })
    public ResponseEntity<TicketResponse> crearTicket(
        @Valid TicketRequest request
    );

    // 2. listar tickets del usuario
    @Operation(
        summary = "Listar mis tickets",
        description = "Devuelve la lista de tickets del usuario autenticado."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Lista de tickets obtenida correctamente."
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Token JWT inválido o expirado.",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @ExampleObject(
                    value = """
                    {
                        "timestamp": "2026-06-12T05:11:58",
                        "status": 401,
                        "message": "No autenticado",
                        "errors": { "error": "Se requiere token de autenticación" },
                        "endpoint": "/tickets/mis-tickets"
                    }
                    """
                )
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Error interno del servidor.",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @ExampleObject(
                    value = """
                    {
                        "timestamp": "2026-06-12T05:11:58",
                        "status": 500,
                        "message": "Error interno del servidor",
                        "errors": { "error": "Error inesperado" },
                        "endpoint": "/tickets/mis-tickets"
                    }
                    """
                )
            )
        )
    })
    public ResponseEntity<List<TicketResponse>> listarTicketsUsuario();

    // 3. listar todos los tickets (solo admin)
    @Operation(
        summary = "Listar todos los tickets",
        description = "Devuelve la lista de todos los tickets. Solo accesible por administradores."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Lista de tickets obtenida correctamente."
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Token JWT inválido o expirado.",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @ExampleObject(
                    value = """
                    {
                        "timestamp": "2026-06-12T05:11:58",
                        "status": 401,
                        "message": "No autenticado",
                        "errors": { "error": "Se requiere token de autenticación" },
                        "endpoint": "/tickets/todos"
                    }
                    """
                )
            )
        ),
        @ApiResponse(
            responseCode = "403",
            description = "El usuario autenticado no tiene permisos de administrador.",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @ExampleObject(
                    value = """
                    {
                        "timestamp": "2026-06-12T05:11:58",
                        "status": 403,
                        "message": "Acceso denegado",
                        "errors": { "error": "Solo los administradores pueden listar todos los tickets." },
                        "endpoint": "/tickets/todos"
                    }
                    """
                )
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Error interno del servidor.",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @ExampleObject(
                    value = """
                    {
                        "timestamp": "2026-06-12T05:11:58",
                        "status": 500,
                        "message": "Error interno del servidor",
                        "errors": { "error": "Error inesperado" },
                        "endpoint": "/tickets/todos"
                    }
                    """
                )
            )
        )
    })
    public ResponseEntity<List<TicketResponse>> listarTodosLosTickets();

    // 4. agregar mensaje / actualizar ticket
    @Operation(
        summary = "Agregar mensaje al ticket",
        description = "Agrega un mensaje a un ticket existente y opcionalmente cambia su estado (cambio de estado solo para administradores)."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Mensaje agregado correctamente."
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Datos inválidos (mensaje vacío o estado inválido).",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @ExampleObject(
                    value = """
                    {
                        "timestamp": "2026-06-12T05:11:58",
                        "status": 400,
                        "message": "Datos inválidos",
                        "errors": { "mensaje": "El contenido del mensaje no puede estar vacío" },
                        "endpoint": "/tickets/1/mensajes"
                    }
                    """
                )
            )
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Token JWT inválido o expirado.",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @ExampleObject(
                    value = """
                    {
                        "timestamp": "2026-06-12T05:11:58",
                        "status": 401,
                        "message": "No autenticado",
                        "errors": { "error": "Se requiere token de autenticación" },
                        "endpoint": "/tickets/1/mensajes"
                    }
                    """
                )
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "El ticket indicado no existe.",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @ExampleObject(
                    value = """
                    {
                        "timestamp": "2026-06-12T05:11:58",
                        "status": 404,
                        "message": "Ticket no encontrado: 1",
                        "errors": null,
                        "endpoint": "/tickets/1/mensajes"
                    }
                    """
                )
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Error interno del servidor.",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @ExampleObject(
                    value = """
                    {
                        "timestamp": "2026-06-12T05:11:58",
                        "status": 500,
                        "message": "Error interno del servidor",
                        "errors": { "error": "Error inesperado" },
                        "endpoint": "/tickets/1/mensajes"
                    }
                    """
                )
            )
        )
    })
    public ResponseEntity<TicketResponse> agregarMensaje(
        Integer ticketId,
        @Valid MensajeRequest request
    );
}
