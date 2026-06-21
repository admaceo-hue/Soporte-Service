package com.mariluz.soporte.exception;

import java.io.IOException;
import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.mariluz.soporte.dto.ErrorResponse;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.UNAUTHORIZED.value());

        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.UNAUTHORIZED.value())
                .message("Acceso denegado: Debes iniciar sesión antes de realizar esta operación.")
                .errors(null)
                .endpoint(request.getRequestURI())
                .build();

        String jsonRespuesta = """
            {
                "timestamp": "%s",
                "status": %d,
                "message": "%s",
                "errors": null,
                "endpoint": "%s"
            }
            """.formatted(
                errorResponse.getTimestamp(),
                errorResponse.getStatus(),
                errorResponse.getMessage(),
                errorResponse.getEndpoint()
        );

        response.getWriter().write(jsonRespuesta);
    }
}