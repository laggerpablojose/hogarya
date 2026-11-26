package com.hogarya.common.exception;

/** Regla de negocio incumplida -> 422 Unprocessable Entity */
public class ReglaNegocioException extends RuntimeException {
    public ReglaNegocioException(String mensaje) {
        super(mensaje);
    }
}
