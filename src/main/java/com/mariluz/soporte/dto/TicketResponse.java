package com.mariluz.soporte.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
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
    @Builder
    public static class MessageDto {
        private String id;
        private String remitenteTipo;
        private String contenido;
        private LocalDateTime fechaEnvio;
    }
}