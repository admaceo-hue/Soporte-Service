package com.mariluz.soporte.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MensajeRequest {
    
    private String nuevoEstado; 
    
    @NotBlank(message = "El contenido del mensaje no puede estar vacío")
    private String mensaje;
}