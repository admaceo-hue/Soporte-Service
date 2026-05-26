package com.mariluz.soporte.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TicketRequest {

    @NotBlank(message = "El asunto no puede estar vacío")
    private String asunto;

    @NotBlank(message = "La descripción no puede estar vacía")
    private String descripcion;
}