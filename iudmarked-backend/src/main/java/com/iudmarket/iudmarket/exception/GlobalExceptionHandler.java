package com.iudmarket.iudmarket.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Manejo centralizado de excepciones para respuestas HTTP consistentes y seguras.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(RecursoNoEncontradoException.class)
  public ResponseEntity<ErrorResponse> handleRecursoNoEncontrado(
      RecursoNoEncontradoException ex, HttpServletRequest request) {
    return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage(), request);
  }

  @ExceptionHandler(CajeraNoDisponibleException.class)
  public ResponseEntity<ErrorResponse> handleCajeraNoDisponible(
      CajeraNoDisponibleException ex, HttpServletRequest request) {
    return buildResponse(HttpStatus.CONFLICT, ex.getMessage(), request);
  }

  @ExceptionHandler(SolicitudInvalidaException.class)
  public ResponseEntity<ErrorResponse> handleSolicitudInvalida(
      SolicitudInvalidaException ex, HttpServletRequest request) {
    return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
  }

  @ExceptionHandler(NegocioException.class)
  public ResponseEntity<ErrorResponse> handleNegocio(NegocioException ex, HttpServletRequest request) {
    HttpStatus status = HttpStatus.resolve(ex.getHttpStatus());
    if (status == null) {
      status = HttpStatus.BAD_REQUEST;
    }
    return buildResponse(status, ex.getMessage(), request);
  }

  /** Errores de validación @Valid en DTOs. */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleValidacion(
      MethodArgumentNotValidException ex, HttpServletRequest request) {
    Map<String, String> errores = new LinkedHashMap<>();
    for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
      errores.put(fieldError.getField(), fieldError.getDefaultMessage());
    }
    ErrorResponse body = new ErrorResponse(
        HttpStatus.BAD_REQUEST.value(),
        "Solicitud inválida",
        "Revise los campos enviados",
        request.getRequestURI());
    body.setErrores(errores);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
  }

  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<ErrorResponse> handleAccesoDenegado(
      AccessDeniedException ex, HttpServletRequest request) {
    return buildResponse(HttpStatus.FORBIDDEN, "No tiene permisos para realizar esta operación", request);
  }

  @ExceptionHandler(AuthenticationException.class)
  public ResponseEntity<ErrorResponse> handleAutenticacion(
      AuthenticationException ex, HttpServletRequest request) {
    return buildResponse(HttpStatus.UNAUTHORIZED, "Credenciales inválidas o no proporcionadas", request);
  }

  /** Fallback: no exponer detalles internos al cliente. */
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleGenerica(Exception ex, HttpServletRequest request) {
    return buildResponse(
        HttpStatus.INTERNAL_SERVER_ERROR,
        "Error interno del servidor. Contacte al administrador.",
        request);
  }

  private ResponseEntity<ErrorResponse> buildResponse(
      HttpStatus status, String message, HttpServletRequest request) {
    ErrorResponse body = new ErrorResponse(
        status.value(),
        status.getReasonPhrase(),
        message,
        request.getRequestURI());
    return ResponseEntity.status(status).body(body);
  }
}
