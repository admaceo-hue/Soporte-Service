package com.mariluz.soporte.model;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor; // Importante para generar el ID en texto

@Entity
@Table(name = "ticket_mensajes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketMessage {

    @Id
    private String id; // Se queda como String, tal como querías 

    @Column(name = "ticket_id", nullable = false)
    private Integer ticketId;

    @Column(name = "remitente_tipo", nullable = false)
    private String remitenteTipo; 

    @Column(columnDefinition = "TEXT", nullable = false)
    private String mensaje;

    @Column(name = "fecha_envio")
    private LocalDateTime fechaEnvio;

    // Bloque mágico: Genera el ID de texto automáticamente antes de guardar en la BD
    @PrePersist
    protected void onCreate() {
        this.fechaEnvio = LocalDateTime.now();
        if (this.id == null) {
            this.id = UUID.randomUUID().toString(); // Genera un texto único como "4f3b2a1c..."
        }
    }
}