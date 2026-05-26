package com.mariluz.soporte.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketResponse {
    private Integer id;
    private String email;
    private String asunto;
    private String descripcion;
    private String estado;
    private LocalDateTime fechaCreacion;
    private List<MessageDto> mensajes; 

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class MessageDto {
        private String id;
        private String emisorId;
        private String contenido;
        private LocalDateTime fechaEnvio;
    }
}