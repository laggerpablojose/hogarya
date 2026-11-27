package com.hogarya.informe.dto;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/** Query params de GET /informes-resolucion. Todos opcionales. Fechas sobre la fecha de resolucion. */
public record InformeFiltro(
        Long incidenteId,
        String responsable,
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaDesde,
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaHasta
) {
}
