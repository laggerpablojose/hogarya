package tuti.desi.hogarya.servicios;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tuti.desi.hogarya.dtos.InteresVisitaRequestDTO;
import tuti.desi.hogarya.entidades.Interesado;
import tuti.desi.hogarya.entidades.InteresVisita;
import tuti.desi.hogarya.entidades.Propiedad;
import tuti.desi.hogarya.repositorios.InteresadoRepository;
import tuti.desi.hogarya.repositorios.InteresVisitaRepository;
import tuti.desi.hogarya.repositorios.PropiedadRepository;

@Service
public class InteresVisitaService {

    private final InteresVisitaRepository interesVisitaRepository;
    private final InteresadoRepository interesadoRepository;
    private final PropiedadRepository propiedadRepository;

    public InteresVisitaService(InteresVisitaRepository interesVisitaRepository,
            InteresadoRepository interesadoRepository,
            PropiedadRepository propiedadRepository) {
        this.interesVisitaRepository = interesVisitaRepository;
        this.interesadoRepository = interesadoRepository;
        this.propiedadRepository = propiedadRepository;
    }

    @Transactional(readOnly = true)
    public List<InteresVisita> listarTodas() {
        return interesVisitaRepository.findByEliminadoFalse();
    }

    @Transactional(readOnly = true)
    public InteresVisita buscarPorId(Long id) {
        InteresVisita visita = interesVisitaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No existe un interés de visita con id " + id));
        if (Boolean.TRUE.equals(visita.getEliminado())) {
            throw new IllegalArgumentException("El interés de visita con id " + id + " fue eliminado");
        }
        return visita;
    }

    @Transactional(readOnly = true)
    public List<InteresVisita> buscarPorPropiedad(Long propiedadId) {
        return interesVisitaRepository.findByPropiedad_IdAndEliminadoFalse(propiedadId);
    }

    @Transactional(readOnly = true)
    public List<InteresVisita> buscarPorInteresado(Long interesadoId) {
        return interesVisitaRepository.findByInteresado_IdAndEliminadoFalse(interesadoId);
    }

    @Transactional
    public InteresVisita crear(InteresVisitaRequestDTO dto) {
        Interesado interesado = obtenerInteresadoValido(dto.getInteresadoId());
        Propiedad propiedad = obtenerPropiedadValida(dto.getPropiedadId());

        InteresVisita visita = new InteresVisita();
        visita.setObservacion(dto.getObservacion());
        visita.setFecha(dto.getFecha());
        visita.setInteresado(interesado);
        visita.setPropiedad(propiedad);

        return interesVisitaRepository.save(visita);
    }

    @Transactional
    public InteresVisita actualizar(Long id, InteresVisitaRequestDTO dto) {
        InteresVisita visita = buscarPorId(id);
        Interesado interesado = obtenerInteresadoValido(dto.getInteresadoId());
        Propiedad propiedad = obtenerPropiedadValida(dto.getPropiedadId());

        visita.setObservacion(dto.getObservacion());
        visita.setFecha(dto.getFecha());
        visita.setInteresado(interesado);
        visita.setPropiedad(propiedad);

        return interesVisitaRepository.save(visita);
    }

    @Transactional
    public void eliminar(Long id) {
        InteresVisita visita = buscarPorId(id);
        visita.setEliminado(true);
        interesVisitaRepository.save(visita);
    }

    private Interesado obtenerInteresadoValido(Long interesadoId) {
        Interesado interesado = interesadoRepository.findById(interesadoId)
                .orElseThrow(() -> new IllegalArgumentException("No existe un interesado con id " + interesadoId));
        if (Boolean.TRUE.equals(interesado.getEliminado())) {
            throw new IllegalArgumentException("El interesado con id " + interesadoId + " fue eliminado");
        }
        return interesado;
    }

    private Propiedad obtenerPropiedadValida(Long propiedadId) {
        Propiedad propiedad = propiedadRepository.findById(propiedadId)
                .orElseThrow(() -> new IllegalArgumentException("No existe una propiedad con id " + propiedadId));
        if (Boolean.TRUE.equals(propiedad.getEliminado())) {
            throw new IllegalArgumentException("La propiedad con id " + propiedadId + " fue eliminada");
        }
        return propiedad;
    }
}
