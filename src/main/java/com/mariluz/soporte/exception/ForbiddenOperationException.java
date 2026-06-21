package com.mariluz.soporte.exception;

/**
 * Excepción para operaciones prohibidas por falta de permisos (HTTP 403 FORBIDDEN).
 * * Diferencia:
 * - 401 UNAUTHORIZED → el usuario NO está autenticado.
 * - 403 FORBIDDEN    → el usuario SÍ está autenticado, pero no tiene los privilegios necesarios.
 */
public class ForbiddenOperationException extends RuntimeException {

    public ForbiddenOperationException(String message) {
        super(message);
    }
}