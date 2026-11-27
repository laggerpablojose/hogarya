package com.hogarya.factura.dto;

import com.hogarya.factura.EstadoFactura;
import com.hogarya.factura.TipoFactura;

/** Query params de GET /facturas. Todos opcionales. */
public record FacturaFiltro(
        Long contratoId,
        String periodo,
        EstadoFactura estado,
        TipoFactura tipo
) {
}
