package com.hogarya.contrato;

import org.springframework.hateoas.server.core.Relation;

import java.math.BigDecimal;
import java.time.LocalDate;

@Relation(itemRelation = "contrato", collectionRelation = "contratos")
public record ContratoResponse(
        Long id,
        LocalDate fechaInicio,
        LocalDate fechaFin,
        Integer duracionMeses,
        BigDecimal importeMensual,
        Integer diaVencimiento,
        boolean activo,
        boolean eliminado
) {
    public static ContratoResponse de(Contrato c) {
        return new ContratoResponse(c.getId(), c.getFechaInicio(), c.getFechaFinEfectiva(),
                c.getDuracionMeses(), c.getImporteMensual(), c.getDiaVencimiento(),
                c.isActivo(), c.isEliminado());
    }
}
