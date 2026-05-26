package com.mariluz.soporte.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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
@RequestMapping("soporte")
public class SoporteController {

    @Autowired
    private SoporteService soporteService;

    @PostMapping("/crear")
    public ResponseEntity<TicketResponse> crearTicket(@Valid @RequestBody TicketRequest request, Authentication authentication) {
        String userId = authentication.getName();
        TicketResponse nuevoTicket = soporteService.crearTicket(request, userId);
        return new ResponseEntity<>(nuevoTicket, HttpStatus.CREATED);
    }

    @GetMapping("/mis-tickets")
    public ResponseEntity<List<TicketResponse>> listarTicketsUsuario(Authentication authentication) {
        String userId = authentication.getName();
        List<TicketResponse> tickets = soporteService.listarTicketsUsuario(userId);
        return ResponseEntity.ok(tickets);
    }

    @GetMapping("/admin/todos")
    public ResponseEntity<List<TicketResponse>> listarTodosLosTickets() {
        List<TicketResponse> tickets = soporteService.listarTodosLosTickets();
        return ResponseEntity.ok(tickets);
    }

    @PutMapping("/admin/{ticketId}/estado")
    public ResponseEntity<TicketResponse> actualizarEstadoTicket(
            @PathVariable Integer ticketId,
            @RequestParam String nuevoEstado) {
        
        TicketResponse ticketActualizado = soporteService.actualizarEstadoTicket(ticketId, nuevoEstado);
        return ResponseEntity.ok(ticketActualizado);
    }
}