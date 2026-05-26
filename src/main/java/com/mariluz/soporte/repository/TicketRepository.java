package com.mariluz.soporte.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mariluz.soporte.model.Ticket;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Integer> {

    // Usamos @Query para decirle explícitamente a Spring qué buscar sin que se confunda con el nombre del método
    @Query("SELECT t FROM Ticket t WHERE t.email = :email")
    List<Ticket> buscarPorEmail(@Param("email") String email);
}