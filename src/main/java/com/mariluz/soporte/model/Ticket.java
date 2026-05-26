package com.mariluz.soporte.model;

import java.time.LocalDateTime;

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
@Table(name = "ticket")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "user_id", nullable = false)
    private String email;

    @Column(nullable = false)
    private String asunto;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String descripcion;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Estado estado;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

}