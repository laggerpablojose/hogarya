package com.hogarya.pago.dto;

import com.hogarya.pago.MedioPago;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/** Query params de GET /pagos. Todos opcionales. */
public record PagoFiltro(
        Long facturaId,
        MedioPago medioPago,
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaDesde,
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaHasta
) {
}
