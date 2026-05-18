package com.iudmarket.iudmarket.exception;

/**
 * Excepción base del dominio IUDMarket con código HTTP asociado.
 */
public abstract class NegocioException extends RuntimeException {

    private final int httpStatus;

    protected NegocioException(String message, int httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public int getHttpStatus() {
        return httpStatus;
    }
}
