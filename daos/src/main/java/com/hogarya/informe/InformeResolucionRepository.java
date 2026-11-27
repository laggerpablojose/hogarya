package com.hogarya.informe;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface InformeResolucionRepository
        extends JpaRepository<InformeResolucion, Long>, JpaSpecificationExecutor<InformeResolucion> {
}
