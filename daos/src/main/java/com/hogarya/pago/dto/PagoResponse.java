package com.hogarya.pago.dto;

import com.hogarya.pago.MedioPago;
import org.springframework.hateoas.server.core.Relation;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Relation(itemRelation = "pago", collectionRelation = "pagos")
public record PagoResponse(
        Long id,
        Long facturaId,
        LocalDate fechaPago,
        MedioPago medioPago,
        BigDecimal importePagado,
        BigDecimal interes,
        LocalDateTime fechaRegistro
) {
}
