package com.hogarya.factura;

import com.hogarya.factura.dto.FacturaFiltro;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public final class FacturaSpecifications {

    private FacturaSpecifications() {
    }

    public static Specification<Factura> conFiltro(FacturaFiltro f, LocalDate hoy) {
        return (root, query, cb) -> {
            List<Predicate> p = new ArrayList<>();

            if (f.contratoId() != null) {
                p.add(cb.equal(root.get("contrato").get("id"), f.contratoId()));
            }
            if (f.periodo() != null && !f.periodo().isBlank()) {
                p.add(cb.equal(root.get("periodo"), f.periodo()));
            }
            if (f.tipo() != null) {
                p.add(cb.equal(root.get("tipo"), f.tipo()));
            }
            if (f.estado() != null) {
                switch (f.estado()) {
                    case PENDIENTE -> {
                        p.add(cb.equal(root.get("estadoRegistrado"), EstadoFactura.PENDIENTE));
                        p.add(cb.greaterThanOrEqualTo(root.<LocalDate>get("fechaVencimiento"), hoy));
                    }
                    case VENCIDA -> {
                        p.add(cb.equal(root.get("estadoRegistrado"), EstadoFactura.PENDIENTE));
                        p.add(cb.lessThan(root.<LocalDate>get("fechaVencimiento"), hoy));
                    }
                    default -> p.add(cb.equal(root.get("estadoRegistrado"), f.estado()));
                }
            }
            return cb.and(p.toArray(Predicate[]::new));
        };
    }
}
