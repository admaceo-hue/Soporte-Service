package com.mariluz.soporte.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class InvalidStatusException extends RuntimeException {
    public InvalidStatusException(String mensaje) {
        super(mensaje);
    }
}