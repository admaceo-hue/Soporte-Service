package com.mariluz.soporte.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mariluz.soporte.model.TicketMessage;

@Repository
public interface TicketMessageRepository extends JpaRepository<TicketMessage, String> {

    
    @Query("SELECT m FROM TicketMessage m WHERE m.ticketId = :ticketId ORDER BY m.fechaEnvio ASC")
    List<TicketMessage> buscarMensajesPorTicketId(@Param("ticketId") Integer ticketId);
}