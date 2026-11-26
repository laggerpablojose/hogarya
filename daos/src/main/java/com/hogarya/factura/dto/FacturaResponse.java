package com.hogarya.factura.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.hogarya.factura.EstadoFactura;
import com.hogarya.factura.TipoFactura;
import org.springframework.hateoas.server.core.Relation;

import java.math.BigDecimal;
import java.time.LocalDate;

@Relation(itemRelation = "factura", collectionRelation = "facturas")
public record FacturaResponse(
        Long id,
        Long contratoId,
        String periodo,
        String concepto,
        LocalDate fechaEmision,
        LocalDate fechaVencimiento,
        BigDecimal importe,
        TipoFactura tipo,
        EstadoFactura estado,
        @JsonInclude(JsonInclude.Include.NON_NULL) Long pagoId
) {
}
