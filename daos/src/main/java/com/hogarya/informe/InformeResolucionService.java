package com.hogarya.informe;

import com.hogarya.common.exception.RecursoNoEncontradoException;
import com.hogarya.common.exception.ReglaNegocioException;
import com.hogarya.incidente.EstadoIncidente;
import com.hogarya.incidente.HistorialEstado;
import com.hogarya.incidente.HistorialEstadoRepository;
import com.hogarya.incidente.Incidente;
import com.hogarya.incidente.IncidenteRepository;
import com.hogarya.incidente.IncidenteService;
import com.hogarya.informe.dto.InformeFiltro;
import com.hogarya.informe.dto.InformeResponse;
import com.hogarya.informe.dto.ResolverIncidenteRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * Servicio Tarea: resolver un incidente.
 * En UNA transaccion: (1) crea el informe, (2) pasa el incidente a RESUELTO, (3) agrega la transicion al historial.
 * Si cualquiera falla, se hace rollback de las tres.
 */
@Service
@RequiredArgsConstructor
public class InformeResolucionService {

    private final InformeResolucionRepository informeRepository;
    private final IncidenteRepository incidenteRepository;
    private final HistorialEstadoRepository historialRepository;
    private final IncidenteService incidenteService;
    private final Clock clock;

    // ---------------------------------------------------------------- POST

    @Transactional
    public InformeResponse resolver(ResolverIncidenteRequest req) {
        Incidente incidente = incidenteService.buscar(req.incidenteId());

        switch (incidente.getEstado()) {
            case EN_PROCESO -> { /* ok */ }
            case RESUELTO -> throw new ReglaNegocioException(
                    "El incidente ya esta resuelto. Para generar un nuevo informe debe reabrirse y volver a EN_PROCESO");
            default -> throw new ReglaNegocioException(
                    "Solo pueden resolverse incidentes EN_PROCESO (estado actual: %s)".formatted(incidente.getEstado()));
        }

        // Misma fecha y hora para el informe y para la entrada del historial
        LocalDateTime fechaResolucion = LocalDateTime.now(clock).truncatedTo(ChronoUnit.MICROS);

        // (2) + (3): cambio de estado y entrada de historial
        HistorialEstado transicion = incidente.transicionar(EstadoIncidente.RESUELTO, fechaResolucion);
        historialRepository.save(transicion);

        // (1): informe vinculado a esa transicion puntual
        InformeResolucion informe = new InformeResolucion();
        informe.setIncidente(incidente);
        informe.setTransicion(transicion);
        informe.setDescripcionTrabajo(req.descripcionTrabajo().trim());
        informe.setFechaResolucion(fechaResolucion);
        informe.setCosto(req.costo());
        informe.setObservaciones(req.observaciones());
        informe.setResponsable(req.responsable().trim());
        informeRepository.save(informe);

        // flush dentro de la transaccion: si otra resolucion concurrente gano, falla
        // el @Version del incidente o el indice unico uk_informe_transicion -> rollback total y 409
        incidenteRepository.flush();

        return toResponse(informe);
    }

    // ---------------------------------------------------------------- GET

    @Transactional(readOnly = true)
    public InformeResponse obtener(Long id) {
        return toResponse(informeRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Informe de resolucion", id)));
    }

    @Transactional(readOnly = true)
    public List<InformeResponse> listar(InformeFiltro filtro) {
        return informeRepository.findAll(InformeSpecifications.conFiltro(filtro),
                        Sort.by("fechaResolucion", "id"))
                .stream().map(this::toResponse).toList();
    }

    private InformeResponse toResponse(InformeResolucion i) {
        return new InformeResponse(i.getId(), i.getIncidente().getId(), i.getDescripcionTrabajo(),
                i.getFechaResolucion(), i.getCosto(), i.getObservaciones(), i.getResponsable());
    }
}
