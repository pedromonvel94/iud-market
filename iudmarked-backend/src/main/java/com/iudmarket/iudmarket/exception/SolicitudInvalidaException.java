package com.iudmarket.iudmarket.exception;

import org.springframework.http.HttpStatus;

/**
 * Datos de entrada inválidos o regla de negocio incumplida en la solicitud.
 */
public class SolicitudInvalidaException extends NegocioException {

    public SolicitudInvalidaException(String message) {
        super(message, HttpStatus.BAD_REQUEST.value());
    }
}
