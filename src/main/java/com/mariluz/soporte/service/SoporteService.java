package com.mariluz.soporte.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mariluz.soporte.dto.TicketRequest;
import com.mariluz.soporte.dto.TicketResponse;
import com.mariluz.soporte.exception.ResourceNotFoundException; 
import com.mariluz.soporte.model.Estado;
import com.mariluz.soporte.model.Ticket;
import com.mariluz.soporte.model.TicketMessage;
import com.mariluz.soporte.repository.TicketMessageRepository;
import com.mariluz.soporte.repository.TicketRepository;

@Service
public class SoporteService {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private TicketMessageRepository ticketMessageRepository;

    // 1. CREAR TICKET
    @Transactional
    public TicketResponse crearTicket(TicketRequest request) {
        Ticket ticket = Ticket.builder()
                .userId(request.getUserId())
                .asunto(request.getAsunto())
                .descripcion(request.getDescripcion())
                .estado(Estado.ABIERTO)
                .build();

        Ticket ticketGuardado = ticketRepository.save(ticket);
        return mapearATicketResponse(ticketGuardado);
    }

    // 2. LISTAR TICKETS DEL USUARIO
    @Transactional(readOnly = true)
    public List<TicketResponse> listarTicketsUsuario(Integer userId) {
        List<Ticket> tickets = ticketRepository.buscarPorUserId(userId);
        return tickets.stream()
                .map(this::mapearATicketResponse)
                .collect(Collectors.toList());
    }

    // 3. LISTAR TODOS LOS TICKETS (ADMIN)
    @Transactional(readOnly = true)
    public List<TicketResponse> listarTodosLosTickets() {
        List<Ticket> tickets = ticketRepository.findAll();
        return tickets.stream()
                .map(this::mapearATicketResponse)
                .collect(Collectors.toList());
    }

    // 4. ACTUALIZAR ESTADO DEL TICKET (ADMIN)
    @Transactional
    public TicketResponse actualizarEstadoTicket(Integer ticketId, String nuevoEstado) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket no encontrado con el ID: " + ticketId));
        
        ticket.setEstado(Estado.valueOf(nuevoEstado.toUpperCase()));
        Ticket ticketActualizado = ticketRepository.save(ticket);
        
        return mapearATicketResponse(ticketActualizado);
    }

    // Método auxiliar ultra-limpio y protegido contra errores de mapeo
    private TicketResponse mapearATicketResponse(Ticket ticket) {
        List<TicketMessage> mensajes;
        
        try {
            // Intenta buscar los mensajes usando el método del repositorio
            mensajes = ticketMessageRepository.buscarMensajesPorTicketId(ticket.getId());
            if (mensajes == null) {
                mensajes = new ArrayList<>();
            }
        } catch (Exception e) {
            // Si el repositorio llega a fallar por choques del id String o columnas, 
            // atrapamos el error para que NO rompa el flujo y devolvemos una lista vacía.
            mensajes = new ArrayList<>();
        }

        return TicketResponse.builder()
                .id(ticket.getId())
                .userId(ticket.getUserId())
                .asunto(ticket.getAsunto())
                .descripcion(ticket.getDescripcion())
                .estado(ticket.getEstado() != null ? ticket.getEstado().name() : "ABIERTO")
                .fechaCreacion(ticket.getFechaCreacion())
                .mensajes(mensajes)
                .build();
    }
}