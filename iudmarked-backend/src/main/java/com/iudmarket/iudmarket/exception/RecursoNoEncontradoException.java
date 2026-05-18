package com.iudmarket.iudmarket.exception;

import org.springframework.http.HttpStatus;

/**
 * Recurso solicitado no existe en el sistema (cliente, cajera, producto, compra, etc.).
 */
public class RecursoNoEncontradoException extends NegocioException {

    public RecursoNoEncontradoException(String message) {
        super(message, HttpStatus.NOT_FOUND.value());
    }
}
