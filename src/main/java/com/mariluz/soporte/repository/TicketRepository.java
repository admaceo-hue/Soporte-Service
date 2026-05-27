package com.mariluz.soporte.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mariluz.soporte.model.Ticket;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Integer> {

    @Query("SELECT DISTINCT t FROM Ticket t LEFT JOIN FETCH t.mensajes WHERE t.email = :email")
    List<Ticket> buscarPorEmailConMensajes(@Param("email") String email);

    @Query("SELECT DISTINCT t FROM Ticket t LEFT JOIN FETCH t.mensajes")
    List<Ticket> findAllWithMensajes();
}
