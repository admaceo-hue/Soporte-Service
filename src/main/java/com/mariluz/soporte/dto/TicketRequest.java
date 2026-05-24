package com.mariluz.soporte.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TicketRequest {
    
    @NotNull(message = "El ID de usuario es obligatorio")
    private Integer userId;

    @NotBlank(message = "El asunto no puede estar vacío")
    private String asunto;

    @NotBlank(message = "La descripción no puede estar vacía")
    private String descripcion;
}