package com.iudmarket.iudmarket.exception;

import org.springframework.http.HttpStatus;

/**
 * La cajera no está en estado ACTIVA (libre) y no puede atender una nueva compra.
 */
public class CajeraNoDisponibleException extends NegocioException {

    public CajeraNoDisponibleException(String nombreCajera, Long id) {
        super("La cajera '" + nombreCajera + "' (id=" + id + ") no está disponible para cobrar", HttpStatus.CONFLICT.value());
    }
}
