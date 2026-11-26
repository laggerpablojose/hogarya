package com.hogarya.informe.dto;

import org.springframework.hateoas.server.core.Relation;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Relation(itemRelation = "informe", collectionRelation = "informes")
public record InformeResponse(
        Long id,
        Long incidenteId,
        String descripcionTrabajo,
        LocalDateTime fechaResolucion,
        BigDecimal costo,
        String observaciones,
        String responsable
) {
}
