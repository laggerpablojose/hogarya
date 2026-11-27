package com.hogarya.incidente.dto;

import com.hogarya.incidente.CategoriaIncidente;
import com.hogarya.incidente.EstadoIncidente;
import com.hogarya.incidente.PrioridadIncidente;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/** Query params de GET /incidentes. Todos opcionales. fechaDesde/fechaHasta filtran por fecha de alta. */
public record IncidenteFiltro(
        Long contratoId,
        EstadoIncidente estado,
        CategoriaIncidente categoria,
        PrioridadIncidente prioridad,
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaDesde,
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaHasta
) {
}
