package com.hogarya.incidente;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/** Solo lectura: las entradas se crean exclusivamente via Incidente.transicionar(...). */
public interface HistorialEstadoRepository extends JpaRepository<HistorialEstado, Long> {

    List<HistorialEstado> findByIncidenteIdOrderByFechaHoraAscIdAsc(Long incidenteId);
}
