package tuti.desi.hogarya.servicios;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tuti.desi.hogarya.dtos.PropiedadRequestDTO;
import tuti.desi.hogarya.entidades.EstadoPropiedad;
import tuti.desi.hogarya.entidades.Persona;
import tuti.desi.hogarya.entidades.Propiedad;
import tuti.desi.hogarya.repositorios.PersonaRepository;
import tuti.desi.hogarya.repositorios.PropiedadRepository;

@Service
public class PropiedadService {

    private final PropiedadRepository propiedadRepository;
    private final PersonaRepository personaRepository;

    public PropiedadService(PropiedadRepository propiedadRepository, PersonaRepository personaRepository) {
        this.propiedadRepository = propiedadRepository;
        this.personaRepository = personaRepository;
    }

    @Transactional(readOnly = true)
    public List<Propiedad> listarTodas() {
        return propiedadRepository.findByEliminadoFalse();
    }

    @Transactional(readOnly = true)
    public Propiedad buscarPorId(Long id) {
        Propiedad propiedad = propiedadRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No existe una propiedad con id " + id));
        if (Boolean.TRUE.equals(propiedad.getEliminado())) {
            throw new IllegalArgumentException("La propiedad con id " + id + " fue eliminada");
        }
        return propiedad;
    }

    @Transactional(readOnly = true)
    public List<Propiedad> buscarPorEstado(EstadoPropiedad estado) {
        return propiedadRepository.findByEstadoAndEliminadoFalse(estado);
    }

    @Transactional(readOnly = true)
    public List<Propiedad> buscarPorCiudad(String ciudad) {
        return propiedadRepository.findByCiudadIgnoreCaseAndEliminadoFalse(ciudad);
    }

    @Transactional
    public Propiedad crear(PropiedadRequestDTO dto) {
        Persona propietario = obtenerPropietarioValido(dto.getPropietarioId());

        Propiedad propiedad = new Propiedad();
        propiedad.setDireccion(dto.getDireccion());
        propiedad.setCiudad(dto.getCiudad());
        propiedad.setTipo(dto.getTipo());
        propiedad.setAmbientes(dto.getAmbientes());
        propiedad.setMetrosCuadrados(dto.getMetrosCuadrados());
        propiedad.setDescripcion(dto.getDescripcion());
        propiedad.setEstado(dto.getEstado() != null ? dto.getEstado() : EstadoPropiedad.DISPONIBLE);
        propiedad.setPropietario(propietario);

        return propiedadRepository.save(propiedad);
    }

    @Transactional
    public Propiedad actualizar(Long id, PropiedadRequestDTO dto) {
        Propiedad propiedad = buscarPorId(id);
        Persona propietario = obtenerPropietarioValido(dto.getPropietarioId());

        propiedad.setDireccion(dto.getDireccion());
        propiedad.setCiudad(dto.getCiudad());
        propiedad.setTipo(dto.getTipo());
        propiedad.setAmbientes(dto.getAmbientes());
        propiedad.setMetrosCuadrados(dto.getMetrosCuadrados());
        propiedad.setDescripcion(dto.getDescripcion());
        if (dto.getEstado() != null) {
            propiedad.setEstado(dto.getEstado());
        }
        propiedad.setPropietario(propietario);

        return propiedadRepository.save(propiedad);
    }

    @Transactional
    public void eliminar(Long id) {
        Propiedad propiedad = buscarPorId(id);
        propiedad.setEliminado(true);
        propiedadRepository.save(propiedad);
    }

    private Persona obtenerPropietarioValido(Long propietarioId) {
        Persona propietario = personaRepository.findById(propietarioId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No existe una persona con id " + propietarioId + " para asignar como propietario"));
        if (Boolean.TRUE.equals(propietario.getEliminado())) {
            throw new IllegalArgumentException(
                    "La persona con id " + propietarioId + " está eliminada y no puede ser propietario");
        }
        return propietario;
    }
}
