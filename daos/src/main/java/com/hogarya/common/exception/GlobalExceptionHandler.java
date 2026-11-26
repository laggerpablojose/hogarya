package com.hogarya.common.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.ConcurrencyFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ProblemDetail noEncontrado(RecursoNoEncontradoException ex) {
        return problema(HttpStatus.NOT_FOUND, "Recurso no encontrado", ex.getMessage());
    }

    @ExceptionHandler(ReglaNegocioException.class)
    public ProblemDetail reglaNegocio(ReglaNegocioException ex) {
        return problema(HttpStatus.CONFLICT, "Conflicto de negocio", ex.getMessage());
    }

    @ExceptionHandler(ConflictoException.class)
    public ProblemDetail conflicto(ConflictoException ex) {
        return problema(HttpStatus.CONFLICT, "Conflicto", ex.getMessage());
    }

    /** Cubre @Valid en @RequestBody (MethodArgumentNotValidException) y en filtros @ModelAttribute. */
    @ExceptionHandler(BindException.class)
    public ProblemDetail validacion(BindException ex) {
        Map<String, String> errores = new LinkedHashMap<>();
        ex.getFieldErrors().forEach(e -> errores.putIfAbsent(e.getField(), e.getDefaultMessage()));
        ProblemDetail pd = problema(HttpStatus.BAD_REQUEST, "Datos invalidos", "Uno o mas campos son invalidos");
        pd.setProperty("errores", errores);
        return pd;
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ProblemDetail cuerpoIlegible(HttpMessageNotReadableException ex) {
        return problema(HttpStatus.BAD_REQUEST, "Cuerpo de la peticion invalido",
                "El JSON esta mal formado o contiene valores no admitidos (fechas, enums o numeros).");
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ProblemDetail tipoIncorrecto(MethodArgumentTypeMismatchException ex) {
        return problema(HttpStatus.BAD_REQUEST, "Parametro invalido",
                "El parametro '%s' tiene un valor invalido: %s".formatted(ex.getName(), ex.getValue()));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ProblemDetail integridad(DataIntegrityViolationException ex) {
        return problema(HttpStatus.CONFLICT, "Violacion de integridad",
                "La operacion viola una restriccion de la base de datos (posible registro duplicado).");
    }

    @ExceptionHandler(ConcurrencyFailureException.class)
    public ProblemDetail concurrencia(ConcurrencyFailureException ex) {
        return problema(HttpStatus.CONFLICT, "Modificacion concurrente",
                "Otra operacion concurrente impidio completar la solicitud. Consulte el recurso e intente nuevamente.");
    }

    private ProblemDetail problema(HttpStatus status, String titulo, String detalle) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(status, detalle);
        pd.setTitle(titulo);
        pd.setProperty("timestamp", Instant.now());
        return pd;
    }
}
