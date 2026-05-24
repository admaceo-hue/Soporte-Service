package com.mariluz.soporte.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mariluz.soporte.dto.TicketRequest;
import com.mariluz.soporte.dto.TicketResponse;
import com.mariluz.soporte.service.SoporteService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/soporte")
public class SoporteController {

    @Autowired
    private SoporteService soporteService;

    // 1. CREAR TICKET
    @PostMapping
    public ResponseEntity<TicketResponse> crearTicket(@Valid @RequestBody TicketRequest request) {
        TicketResponse nuevoTicket = soporteService.crearTicket(request);
        return new ResponseEntity<>(nuevoTicket, HttpStatus.CREATED);
    }

    // 2. LISTAR MIS TICKETS (USER)
    @GetMapping("/usuario/{userId}")
    public ResponseEntity<List<TicketResponse>> listarTicketsUsuario(@PathVariable Integer userId) {
        List<TicketResponse> tickets = soporteService.listarTicketsUsuario(userId);
        return ResponseEntity.ok(tickets);
    }

    // 3. LISTAR TODOS LOS TICKETS (ADMIN)
    @GetMapping("/admin/todos")
    public ResponseEntity<List<TicketResponse>> listarTodosLosTickets() {
        List<TicketResponse> tickets = soporteService.listarTodosLosTickets();
        return ResponseEntity.ok(tickets);
    }

    // 4. ACTUALIZAR ESTADO DEL TICKET (ADMIN)
    
    @PutMapping("/admin/{ticketId}/estado")
    public ResponseEntity<TicketResponse> actualizarEstadoTicket(
            @PathVariable Integer ticketId,
            @RequestParam String nuevoEstado) {
        
        TicketResponse ticketActualizado = soporteService.actualizarEstadoTicket(ticketId, nuevoEstado);
        return ResponseEntity.ok(ticketActualizado);
    }
}