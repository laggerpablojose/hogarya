package com.hogarya.incidente;

import com.hogarya.incidente.dto.IncidenteFiltro;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public final class IncidenteSpecifications {

    private IncidenteSpecifications() {
    }

    public static Specification<Incidente> conFiltro(IncidenteFiltro f) {
        return (root, query, cb) -> {
            List<Predicate> p = new ArrayList<>();
            if (f.contratoId() != null) {
                p.add(cb.equal(root.get("contrato").get("id"), f.contratoId()));
            }
            if (f.estado() != null) {
                p.add(cb.equal(root.get("estado"), f.estado()));
            }
            if (f.categoria() != null) {
                p.add(cb.equal(root.get("categoria"), f.categoria()));
            }
            if (f.prioridad() != null) {
                p.add(cb.equal(root.get("prioridad"), f.prioridad()));
            }
            if (f.fechaDesde() != null) {
                p.add(cb.greaterThanOrEqualTo(root.<LocalDateTime>get("fechaAlta"), f.fechaDesde().atStartOfDay()));
            }
            if (f.fechaHasta() != null) {
                p.add(cb.lessThan(root.<LocalDateTime>get("fechaAlta"), f.fechaHasta().plusDays(1).atStartOfDay()));
            }
            return cb.and(p.toArray(Predicate[]::new));
        };
    }
}
