package com.mariluz.soporte.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mariluz.soporte.dto.TicketRequest;
import com.mariluz.soporte.dto.TicketResponse;
import com.mariluz.soporte.exception.InvalidStatusException;
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
    
    @Transactional
    // Crea un nuevo ticket de soporte para el usuario autenticado
    public TicketResponse crearTicket(TicketRequest request, String userId) {
        Ticket ticket = Ticket.builder()
                .userId(userId)
                .asunto(request.getAsunto())
                .descripcion(request.getDescripcion())
                .estado(Estado.ABIERTO)
                .build();

        Ticket ticketGuardado = ticketRepository.save(ticket);
        return mapearATicketResponse(ticketGuardado);
    }
// Lista los tickets de soporte del usuario autenticado
    @Transactional(readOnly = true)
    public List<TicketResponse> listarTicketsUsuario(String userId) {
        List<Ticket> tickets = ticketRepository.buscarPorUserId(userId);
        return tickets.stream()
                .map(this::mapearATicketResponse)
                .collect(Collectors.toList());
    }
// Lista todos los tickets de soporte (solo para administradores)
    @Transactional(readOnly = true)
    public List<TicketResponse> listarTodosLosTickets() {
        List<Ticket> tickets = ticketRepository.findAll();
        return tickets.stream()
                .map(this::mapearATicketResponse)
                .collect(Collectors.toList());
    }
// Actualiza el estado de un ticket de soporte (solo para administradores)
    @Transactional
    public TicketResponse actualizarEstadoTicket(Integer ticketId, String nuevoEstado) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket no encontrado con el ID: " + ticketId));

        try {
            ticket.setEstado(Estado.valueOf(nuevoEstado.toUpperCase()));
        } catch (IllegalArgumentException ex) {
            throw new InvalidStatusException("Estado inválido: " + nuevoEstado);
        }

        Ticket ticketActualizado = ticketRepository.save(ticket);

        return mapearATicketResponse(ticketActualizado);
    }

    private TicketResponse mapearATicketResponse(Ticket ticket) {
        List<TicketMessage> mensajesEntidad = ticketMessageRepository.buscarMensajesPorTicketId(ticket.getId());
        
        List<TicketResponse.MessageDto> mensajesDto = new ArrayList<>();
        if (mensajesEntidad != null) {
            mensajesDto = mensajesEntidad.stream()
                    .map(m -> TicketResponse.MessageDto.builder()
                            .id(m.getId())
                            .emisorId(m.getRemitenteTipo())
                            .contenido(m.getMensaje())
                            .fechaEnvio(m.getFechaEnvio())
                            .build())
                    .collect(Collectors.toList());
        }

        return TicketResponse.builder()
                .id(ticket.getId())
                .userId(ticket.getUserId())
                .asunto(ticket.getAsunto())
                .descripcion(ticket.getDescripcion())
                .estado(ticket.getEstado() != null ? ticket.getEstado().name() : "ABIERTO")
                .fechaCreacion(ticket.getFechaCreacion())
                .mensajes(mensajesDto)
                .build();
    }
}