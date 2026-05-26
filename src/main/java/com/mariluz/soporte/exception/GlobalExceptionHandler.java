package com.mariluz.soporte.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // toma cuando un Ticket no existe (404 Not Found)
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> manejarResourceNotFound(ResourceNotFoundException ex) {
        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("timestamp", LocalDateTime.now());
        respuesta.put("status", HttpStatus.NOT_FOUND.value());
        respuesta.put("error", "Recurso no encontrado");
        respuesta.put("message", ex.getMessage());
        return new ResponseEntity<>(respuesta, HttpStatus.NOT_FOUND);
    }

    // toma cuando mandan un estado inválido o error del Enum (400 Bad Request)
    @ExceptionHandler({InvalidStatusException.class, IllegalArgumentException.class})
    public ResponseEntity<Map<String, Object>> manejarEstadoInvalido(Exception ex) {
        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("timestamp", LocalDateTime.now());
        respuesta.put("status", HttpStatus.BAD_REQUEST.value());
        respuesta.put("error", "Petición Incorrecta");
        respuesta.put("message", "El estado proporcionado no es válido. Usa: ABIERTO, EN_PROGRESO o CERRADO.");
        return new ResponseEntity<>(respuesta, HttpStatus.BAD_REQUEST);
    }

    // toma cuando el Body de la petición viene vacío o falta por completo (400 Bad Request)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> manejarFaltaRequestBody(HttpMessageNotReadableException ex) {
        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("timestamp", LocalDateTime.now());
        respuesta.put("status", HttpStatus.BAD_REQUEST.value());
        respuesta.put("error", "Petición Incorrecta");
        respuesta.put("message", "Debe completar los campos requeridos en el cuerpo (Body) de la petición.");
        return new ResponseEntity<>(respuesta, HttpStatus.BAD_REQUEST);
    }

    // toma los errores de @Valid (Campos vacíos en el DTO) (400 Bad Request)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> manejarValidaciones(MethodArgumentNotValidException ex) {
        Map<String, Object> respuesta = new HashMap<>();
        Map<String, String> erroresCampos = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String nombreCampo = ((FieldError) error).getField();
            String mensajeError = error.getDefaultMessage();
            erroresCampos.put(nombreCampo, mensajeError);
        });

        respuesta.put("timestamp", LocalDateTime.now());
        respuesta.put("status", HttpStatus.BAD_REQUEST.value());
        respuesta.put("error", "Error de Validación");
        respuesta.put("fields", erroresCampos);

        return new ResponseEntity<>(respuesta, HttpStatus.BAD_REQUEST);
    }

    // toma cualquier otro error inesperado del sistema (500 Internal Server Error)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> manejarErroresGenerales(Exception ex) {
        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("timestamp", LocalDateTime.now());
        respuesta.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        respuesta.put("error", "Error Interno del Servidor");
        respuesta.put("message", ex.getMessage());
        return new ResponseEntity<>(respuesta, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}