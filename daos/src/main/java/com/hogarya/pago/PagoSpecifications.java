package com.hogarya.pago;

import com.hogarya.pago.dto.PagoFiltro;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public final class PagoSpecifications {

    private PagoSpecifications() {
    }

    public static Specification<Pago> conFiltro(PagoFiltro f) {
        return (root, query, cb) -> {
            List<Predicate> p = new ArrayList<>();
            if (f.facturaId() != null) {
                p.add(cb.equal(root.get("factura").get("id"), f.facturaId()));
            }
            if (f.medioPago() != null) {
                p.add(cb.equal(root.get("medioPago"), f.medioPago()));
            }
            if (f.fechaDesde() != null) {
                p.add(cb.greaterThanOrEqualTo(root.<LocalDate>get("fechaPago"), f.fechaDesde()));
            }
            if (f.fechaHasta() != null) {
                p.add(cb.lessThanOrEqualTo(root.<LocalDate>get("fechaPago"), f.fechaHasta()));
            }
            return cb.and(p.toArray(Predicate[]::new));
        };
    }
}
