package com.hogarya.common.exception;

/** Conflicto con el estado actual del recurso (duplicados, etc.) -> 409 Conflict */
public class ConflictoException extends RuntimeException {
    public ConflictoException(String mensaje) {
        super(mensaje);
    }
}
