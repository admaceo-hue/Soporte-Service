package com.mariluz.soporte.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.mariluz.soporte.dto.MensajeRequest;
import com.mariluz.soporte.dto.TicketRequest;
import com.mariluz.soporte.dto.TicketResponse;
import com.mariluz.soporte.exception.ResourceNotFoundException;
import com.mariluz.soporte.security.JwtUtil;
import com.mariluz.soporte.service.SoporteService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.json.JsonMapper;

@WebMvcTest(SoporteController.class)
@AutoConfigureMockMvc(addFilters = false) // desactiva filtro JWT y seguridad para ejecutar el test
public class SoporteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JsonMapper objectMapper; // para mapear objetos/clases a json

    @MockitoBean
    private SoporteService service;

    @MockitoBean
    private JwtUtil jwtUtil; // importante para que funcione el contexto de seguridad

    // metodo de apoyo para construir una respuesta de ticket
    private TicketResponse ticketResponse() {
        return TicketResponse.builder()
            .id(1)
            .asunto("a")
            .descripcion("d")
            .estado("ABIERTO")
            .build();
    }

    // -------------- 1. CREAR TICKET --------------
    // 201
    @Test
    public void testCrearTicket() throws Exception {
        TicketRequest request = new TicketRequest();
        request.setAsunto("Problema");
        request.setDescripcion("Detalle");

        when(service.crearTicket(request)).thenReturn(ticketResponse());

        mockMvc
            .perform(
                post("/tickets/crear")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
            )
            .andExpect(status().isCreated());
    }

    // 400
    @Test
    public void testCrearTicketBadRequest() throws Exception {
        TicketRequest request = new TicketRequest();
        request.setAsunto("");
        request.setDescripcion("");

        mockMvc
            .perform(
                post("/tickets/crear")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
            )
            .andExpect(status().isBadRequest());
    }

    // -------------- 2. LISTAR MIS TICKETS --------------
    // 200
    @Test
    public void testListarMisTickets() throws Exception {
        when(service.listarTicketsUsuario()).thenReturn(
            java.util.List.of(ticketResponse())
        );

        mockMvc
            .perform(get("/tickets/mis-tickets"))
            .andExpect(status().isOk());
    }

    // -------------- 3. LISTAR TODOS LOS TICKETS --------------
    // 200
    @Test
    public void testListarTodos() throws Exception {
        when(service.listarTodosLosTickets()).thenReturn(
            java.util.List.of(ticketResponse())
        );

        mockMvc
            .perform(get("/tickets/todos"))
            .andExpect(status().isOk());
    }

    // -------------- 4. AGREGAR MENSAJE --------------
    // 200
    @Test
    public void testAgregarMensaje() throws Exception {
        MensajeRequest request = new MensajeRequest();
        request.setNuevoEstado(null);
        request.setMensaje("Hola");

        when(
            service.agregarMensajeOActualizarTicket(1, request)
        ).thenReturn(ticketResponse());

        mockMvc
            .perform(
                put("/tickets/1/mensajes")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
            )
            .andExpect(status().isOk());
    }

    // 404
    @Test
    public void testAgregarMensajeNotFound() throws Exception {
        MensajeRequest request = new MensajeRequest();
        request.setNuevoEstado(null);
        request.setMensaje("Hola");

        when(
            service.agregarMensajeOActualizarTicket(99, request)
        ).thenThrow(
            new ResourceNotFoundException("Ticket no encontrado: 99")
        );

        mockMvc
            .perform(
                put("/tickets/99/mensajes")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
            )
            .andExpect(status().isNotFound());
    }

    // 400
    @Test
    public void testAgregarMensajeBadRequest() throws Exception {
        MensajeRequest request = new MensajeRequest();
        request.setNuevoEstado(null);
        request.setMensaje("");

        mockMvc
            .perform(
                put("/tickets/1/mensajes")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
            )
            .andExpect(status().isBadRequest());
    }
}
