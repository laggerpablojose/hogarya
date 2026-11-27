package com.hogarya.incidente.dto;

import com.hogarya.incidente.CategoriaIncidente;
import com.hogarya.incidente.EstadoIncidente;
import com.hogarya.incidente.PrioridadIncidente;
import org.springframework.hateoas.server.core.Relation;

import java.time.LocalDateTime;

@Relation(itemRelation = "incidente", collectionRelation = "incidentes")
public record IncidenteResponse(
        Long id,
        Long contratoId,
        String titulo,
        String descripcion,
        CategoriaIncidente categoria,
        PrioridadIncidente prioridad,
        EstadoIncidente estado,
        LocalDateTime fechaAlta
) {
}
