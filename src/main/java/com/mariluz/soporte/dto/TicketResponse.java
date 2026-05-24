package com.mariluz.soporte.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.mariluz.soporte.model.TicketMessage;

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
    private Integer userId;
    private String asunto;
    private String descripcion;
    private String estado;
    private LocalDateTime fechaCreacion;
    
    // Aquí viaja el historial de mensajes de este ticket de forma automática
    private List<TicketMessage> mensajes; 
}