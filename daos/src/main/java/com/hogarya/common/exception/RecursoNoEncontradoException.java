package com.hogarya.common.exception;

public class RecursoNoEncontradoException extends RuntimeException {
    public RecursoNoEncontradoException(String recurso, Object id) {
        super("%s con id %s no encontrado".formatted(recurso, id));
    }
}
