package com.hogarya.incidente;

import com.hogarya.common.exception.RecursoNoEncontradoException;
import com.hogarya.common.exception.ReglaNegocioException;
import com.hogarya.contrato.Contrato;
import com.hogarya.contrato.ContratoRepository;
import com.hogarya.incidente.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class IncidenteService {

    private final IncidenteRepository incidenteRepository;
    private final HistorialEstadoRepository historialRepository;
    private final ContratoRepository contratoRepository;
    private final Clock clock;

    // ---------------------------------------------------------------- POST

    @Transactional
    public IncidenteResponse registrar(RegistrarIncidenteRequest req) {
        LocalDateTime ahora = LocalDateTime.now(clock);

        Contrato contrato = contratoRepository.findById(req.contratoId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Contrato", req.contratoId()));
        if (contrato.isEliminado()) {
            throw new ReglaNegocioException("El contrato %d esta eliminado".formatted(contrato.getId()));
        }
        if (!contrato.isActivo()) {
            throw new ReglaNegocioException("El contrato %d no esta activo".formatted(contrato.getId()));
        }
        if (!contrato.estaVigenteEn(ahora.toLocalDate())) {
            throw new ReglaNegocioException("El contrato %d no esta vigente en la fecha de alta (%s)"
                    .formatted(contrato.getId(), ahora.toLocalDate()));
        }

        // Estado inicial ABIERTO + primera entrada del historial (se persiste en cascada, misma transaccion)
        Incidente incidente = Incidente.nuevo(contrato, req.titulo().trim(), req.descripcion().trim(),
                req.categoria(), req.prioridad(), ahora);

        return toResponse(incidenteRepository.saveAndFlush(incidente));
    }

    // ---------------------------------------------------------------- PATCH

    @Transactional
    public IncidenteResponse cambiarEstado(Long id, CambiarEstadoIncidenteRequest req) {
        Incidente incidente = buscar(id);
        EstadoIncidente actual = incidente.getEstado();
        EstadoIncidente nuevo = req.estado();

        if (nuevo == EstadoIncidente.RESUELTO) {
            throw new ReglaNegocioException(
                    "Un incidente solo puede resolverse registrando un informe de resolucion (POST /informes-resolucion)");
        }
        if (actual == EstadoIncidente.CANCELADO) {
            throw new ReglaNegocioException("Los incidentes cancelados no admiten nuevas transiciones");
        }
        if (!actual.permiteS03(nuevo)) {
            throw new ReglaNegocioException("Transicion no permitida: %s -> %s. Desde %s se permite: %s"
                    .formatted(actual, nuevo, actual, actual.transicionesPermitidasS03()));
        }

        // Estado + historial en la misma transaccion
        incidente.transicionar(nuevo, LocalDateTime.now(clock));
        incidenteRepository.flush(); // detecta conflicto de @Version (-> 409)
        return toResponse(incidente);
    }

    // ---------------------------------------------------------------- GET

    @Transactional(readOnly = true)
    public IncidenteResponse obtener(Long id) {
        return toResponse(buscar(id));
    }

    @Transactional(readOnly = true)
    public List<IncidenteResponse> listar(IncidenteFiltro filtro) {
        return incidenteRepository.findAll(IncidenteSpecifications.conFiltro(filtro), Sort.by("id"))
                .stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public List<HistorialEstadoResponse> historial(Long id) {
        if (!incidenteRepository.existsById(id)) {
            throw new RecursoNoEncontradoException("Incidente", id);
        }
        return historialRepository.findByIncidenteIdOrderByFechaHoraAscIdAsc(id).stream()
                .map(h -> new HistorialEstadoResponse(h.getId(), h.getEstadoAnterior(), h.getEstadoNuevo(), h.getFechaHora()))
                .toList();
    }

    // ---------------------------------------------------------------- util (usado tambien por S04)

    public Incidente buscar(Long id) {
        return incidenteRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Incidente", id));
    }

    public IncidenteResponse toResponse(Incidente i) {
        return new IncidenteResponse(i.getId(), i.getContrato().getId(), i.getTitulo(), i.getDescripcion(),
                i.getCategoria(), i.getPrioridad(), i.getEstado(), i.getFechaAlta());
    }
}
