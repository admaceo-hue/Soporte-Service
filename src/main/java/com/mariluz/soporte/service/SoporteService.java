package com.mariluz.soporte.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mariluz.soporte.dto.*;
import com.mariluz.soporte.exception.*;
import com.mariluz.soporte.model.*;
import com.mariluz.soporte.repository.*;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SoporteService {

    private final TicketRepository ticketRepository;
    private final TicketMessageRepository ticketMessageRepository;

// ─── Helpers privados para validar rol de usuario ────────────────────────

    private User getCurrentUser() {
        Authentication auth =
            SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof User user)) {
            // M5: ForbiddenOperationException reemplaza a UnauthorizedOperationException
            throw new ForbiddenOperationException(
                "No hay un usuario autenticado"
            );
        }
        return user;
    }

    private boolean isAdmin(User user) {
        return user.getRole() != null &&
               user.getRole().equalsIgnoreCase("ADMIN");
    }

    private void validateAdminAccess(String message) {
        if (!isAdmin(getCurrentUser())) {
            throw new ForbiddenOperationException(message);
        }
    }

    @Transactional
    public TicketResponse crearTicket(TicketRequest request) {
        User currentUser = getCurrentUser();
        Ticket ticket = Ticket.builder()
                .email(currentUser.getEmail())
                .asunto(request.getAsunto())
                .descripcion(request.getDescripcion())
                .estado(Estado.ABIERTO)
                .fechaCreacion(LocalDateTime.now())
                .build();
        return mapearATicketResponse(ticketRepository.save(ticket));
    }

    @Transactional(readOnly = true)
    public List<TicketResponse> listarTicketsUsuario() {
        String userEmail = getCurrentUser().getEmail();
        return ticketRepository.buscarPorEmailConMensajes(userEmail).stream()
                .map(this::mapearATicketResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TicketResponse> listarTodosLosTickets() {
        validateAdminAccess("Solo los administradores pueden listar todos los tickets.");
        return ticketRepository.findAllWithMensajes().stream()
                .map(this::mapearATicketResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public TicketResponse agregarMensajeOActualizarTicket(Integer ticketId, MensajeRequest request) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket no encontrado: " + ticketId));

        User currentUser = getCurrentUser();
        boolean isAdmin = isAdmin(currentUser);
        String currentEmail = currentUser.getEmail();

        if (!isAdmin && !ticket.getEmail().equals(currentEmail)) {
            throw new ForbiddenOperationException("No tienes permiso para interactuar con este ticket.");
        }

        RemitenteTipo remitenteTipo = isAdmin ? RemitenteTipo.ADMINISTRADOR : RemitenteTipo.CLIENTE;
        boolean huboCambioEstado = false;

        if (request.getNuevoEstado() != null && !request.getNuevoEstado().trim().isEmpty()) {
            validateAdminAccess("Solo los administradores pueden cambiar el estado del ticket.");

            Estado estadoEntrante;
            try {
                estadoEntrante = Estado.valueOf(request.getNuevoEstado().toUpperCase());
            } catch (IllegalArgumentException ex) {
                throw new InvalidStatusException("Estado inválido: " + request.getNuevoEstado());
            }

            if (!ticket.getEstado().equals(estadoEntrante)) {
                ticket.setEstado(estadoEntrante);
                huboCambioEstado = true;
            }
        }

        if (ticket.getEstado() == Estado.CERRADO) {
            throw new ForbiddenOperationException("No se pueden agregar mensajes a un ticket cerrado.");
        }

        TicketMessage nuevoMensaje = TicketMessage.builder()
                .ticketId(ticket.getId())
                .remitenteTipo(remitenteTipo)
                .mensaje(request.getMensaje())
                .fechaEnvio(LocalDateTime.now())
                .build();
        ticketMessageRepository.save(nuevoMensaje);

        if (huboCambioEstado) {
            ticket = ticketRepository.save(ticket);
        }

        return mapearATicketResponse(ticket);
    }

    private TicketResponse mapearATicketResponse(Ticket ticket) {
        List<TicketMessage> mensajes = ticket.getMensajes();

        List<TicketResponse.MessageDto> mensajesDto = new ArrayList<>();
        if (mensajes != null) {
            for (TicketMessage m : mensajes) {
                mensajesDto.add(TicketResponse.MessageDto.builder()
                        .id(m.getId())
                        .remitenteTipo(m.getRemitenteTipo() != null ? m.getRemitenteTipo().name() : "CLIENTE")
                        .contenido(m.getMensaje())
                        .fechaEnvio(m.getFechaEnvio())
                        .build());
            }
        }

        return TicketResponse.builder()
                .id(ticket.getId())
                .email(ticket.getEmail())
                .asunto(ticket.getAsunto())
                .descripcion(ticket.getDescripcion())
                .estado(ticket.getEstado() != null ? ticket.getEstado().name() : "ABIERTO")
                .fechaCreacion(ticket.getFechaCreacion())
                .mensajes(mensajesDto)
                .build();
    }
}
