package com.mariluz.soporte.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mariluz.soporte.dto.MensajeRequest;
import com.mariluz.soporte.dto.TicketRequest;
import com.mariluz.soporte.dto.TicketResponse;
import com.mariluz.soporte.service.SoporteService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/tickets")
@RequiredArgsConstructor 
public class SoporteController {

    private final SoporteService soporteService;

    // Crear ticket
    @PostMapping("/crear")
    public ResponseEntity<TicketResponse> crearTicket(@Valid @RequestBody TicketRequest request) {
        return new ResponseEntity<>(soporteService.crearTicket(request), HttpStatus.CREATED);
    }

    // Listar tickets propios
    @GetMapping("/mis-tickets")
    public ResponseEntity<List<TicketResponse>> listarTicketsUsuario() {
        return ResponseEntity.ok(soporteService.listarTicketsUsuario());
    }

    // Listar TODOS los tickets 
    @GetMapping("/todos")
    public ResponseEntity<List<TicketResponse>> listarTodosLosTickets() {
        return ResponseEntity.ok(soporteService.listarTodosLosTickets());
    }

    // Agregar mensaje y cambiar estado
    @PutMapping("/{ticketId}/mensajes")
    public ResponseEntity<TicketResponse> agregarMensaje(@PathVariable Integer ticketId, @Valid @RequestBody MensajeRequest request) {
        return ResponseEntity.ok(soporteService.agregarMensajeOActualizarTicket(ticketId, request));
    }
}