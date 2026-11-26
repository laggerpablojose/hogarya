package com.hogarya.incidente.dto;

import com.hogarya.incidente.EstadoIncidente;
import org.springframework.hateoas.server.core.Relation;

import java.time.LocalDateTime;

@Relation(itemRelation = "cambioEstado", collectionRelation = "historial")
public record HistorialEstadoResponse(
        Long id,
        EstadoIncidente estadoAnterior,
        EstadoIncidente estadoNuevo,
        LocalDateTime fechaHora
) {
}
