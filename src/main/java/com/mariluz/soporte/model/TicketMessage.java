package com.mariluz.soporte.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ticket_mensajes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID) 
    private String id; 

    @Column(name = "ticket_id", nullable = false)
    private Integer ticketId;

  
    @Column(name = "remitente_tipo", nullable = false)
    @Enumerated(EnumType.STRING)
    private RemitenteTipo remitenteTipo; 

    @Column(columnDefinition = "TEXT", nullable = false)
    private String mensaje;

    @CreationTimestamp 
    @Column(name = "fecha_envio", updatable = false)
    private LocalDateTime fechaEnvio;
}