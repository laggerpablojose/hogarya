package com.hogarya.informe;

import com.hogarya.informe.dto.InformeFiltro;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public final class InformeSpecifications {

    private InformeSpecifications() {
    }

    public static Specification<InformeResolucion> conFiltro(InformeFiltro f) {
        return (root, query, cb) -> {
            List<Predicate> p = new ArrayList<>();
            if (f.incidenteId() != null) {
                p.add(cb.equal(root.get("incidente").get("id"), f.incidenteId()));
            }
            if (f.responsable() != null && !f.responsable().isBlank()) {
                p.add(cb.like(cb.lower(root.get("responsable")),
                        "%" + f.responsable().trim().toLowerCase() + "%"));
            }
            if (f.fechaDesde() != null) {
                p.add(cb.greaterThanOrEqualTo(root.<LocalDateTime>get("fechaResolucion"),
                        f.fechaDesde().atStartOfDay()));
            }
            if (f.fechaHasta() != null) {
                p.add(cb.lessThan(root.<LocalDateTime>get("fechaResolucion"),
                        f.fechaHasta().plusDays(1).atStartOfDay()));
            }
            return cb.and(p.toArray(Predicate[]::new));
        };
    }
}
