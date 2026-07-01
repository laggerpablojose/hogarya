package tuti.desi.hogarya.servicios;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tuti.desi.hogarya.accesoDatos.HistorialEstadoPropiedadRepository;
import tuti.desi.hogarya.accesoDatos.PersonaRepository;
import tuti.desi.hogarya.accesoDatos.PropiedadRepository;
import tuti.desi.hogarya.entidades.EstadoPropiedad;
import tuti.desi.hogarya.entidades.HistorialEstadoPropiedad;
import tuti.desi.hogarya.entidades.Persona;
import tuti.desi.hogarya.entidades.Propiedad;
import tuti.desi.hogarya.entidades.TipoPropiedad;
import tuti.desi.hogarya.excepciones.NegocioException;
import tuti.desi.hogarya.presentacion.formularios.PropiedadForm;

@Service
public class PropiedadService {

    private final PropiedadRepository propiedadRepository;
    private final PersonaRepository personaRepository;
    private final HistorialEstadoPropiedadRepository historialRepository;

    public PropiedadService(PropiedadRepository propiedadRepository,
            PersonaRepository personaRepository,
            HistorialEstadoPropiedadRepository historialRepository) {
        this.propiedadRepository = propiedadRepository;
        this.personaRepository = personaRepository;
        this.historialRepository = historialRepository;
    }

    @Transactional(readOnly = true)
    public List<Propiedad> listarPropiedades() {
        return propiedadRepository.findByEliminadoFalse();
    }

    @Transactional(readOnly = true)
    public List<Propiedad> listarPropiedadesFiltradas(
            String direccion, String ciudad, TipoPropiedad tipo, EstadoPropiedad estado) {

        if (direccion != null && !direccion.isBlank()) {
            return propiedadRepository.findByEliminadoFalseAndDireccionContainingIgnoreCase(direccion);
        }
        if (ciudad != null && !ciudad.isBlank()) {
            return propiedadRepository.findByEliminadoFalseAndCiudadContainingIgnoreCase(ciudad);
        }
        if (tipo != null) {
            return propiedadRepository.findByEliminadoFalseAndTipo(tipo);
        }
        if (estado != null) {
            return propiedadRepository.findByEliminadoFalseAndEstado(estado);
        }
        return propiedadRepository.findByEliminadoFalse();
    }

    @Transactional(readOnly = true)
    public Propiedad buscarPorId(Long id) {
        return propiedadRepository.findById(id)
                .filter(p -> !Boolean.TRUE.equals(p.getEliminado()))
                .orElseThrow(() -> new NegocioException("No existe una propiedad con id " + id));
    }

    @Transactional(readOnly = true)
    public List<Persona> listarPropietariosDisponibles() {
        return personaRepository.findByEliminadoFalse();
    }

    @Transactional
    public Propiedad crearPropiedad(PropiedadForm form) {
        Persona propietario = obtenerPersonaValida(form.getPropietarioId());
        validarDireccionUnica(form.getDireccion(), form.getCiudad(), null);

        Propiedad propiedad = new Propiedad();
        propiedad.setDireccion(form.getDireccion());
        propiedad.setCiudad(form.getCiudad());
        propiedad.setTipo(form.getTipo());
        propiedad.setAmbientes(form.getAmbientes());
        propiedad.setMetrosCuadrados(form.getMetrosCuadrados());
        propiedad.setDescripcion(form.getDescripcion());
        propiedad.setPropietario(propietario);

        EstadoPropiedad estado = form.getEstado() != null ? form.getEstado() : EstadoPropiedad.DISPONIBLE;
        propiedad.setEstado(estado);

        Propiedad guardada = propiedadRepository.save(propiedad);
        registrarHistorial(guardada, estado);
        return guardada;
    }

    @Transactional
    public Propiedad modificarPropiedad(Long id, PropiedadForm form) {
        Propiedad propiedad = buscarPorId(id);
        Persona propietario = obtenerPersonaValida(form.getPropietarioId());
        validarDireccionUnica(form.getDireccion(), form.getCiudad(), id);

        EstadoPropiedad estadoAnterior = propiedad.getEstado();
        EstadoPropiedad estadoNuevo = form.getEstado() != null ? form.getEstado() : estadoAnterior;

        if (tieneContratoActivo(propiedad) &&
                (estadoNuevo == EstadoPropiedad.DISPONIBLE || estadoNuevo == EstadoPropiedad.INACTIVA)) {
            throw new NegocioException(
                    "No se puede cambiar el estado a " + estadoNuevo +
                    " porque la propiedad tiene un contrato activo vigente.");
        }

        propiedad.setDireccion(form.getDireccion());
        propiedad.setCiudad(form.getCiudad());
        propiedad.setTipo(form.getTipo());
        propiedad.setAmbientes(form.getAmbientes());
        propiedad.setMetrosCuadrados(form.getMetrosCuadrados());
        propiedad.setDescripcion(form.getDescripcion());
        propiedad.setPropietario(propietario);
        propiedad.setEstado(estadoNuevo);

        Propiedad guardada = propiedadRepository.save(propiedad);

        if (estadoNuevo != estadoAnterior) {
            registrarHistorial(guardada, estadoNuevo);
        }
        return guardada;
    }

    @Transactional
    public void eliminarPropiedad(Long id) {
        Propiedad propiedad = buscarPorId(id);

        if (tieneContratoActivo(propiedad)) {
            throw new NegocioException(
                    "No se puede eliminar la propiedad porque tiene un contrato activo vigente.");
        }

        propiedad.setEliminado(true);
        propiedadRepository.save(propiedad);
    }

    private Persona obtenerPersonaValida(Long personaId) {
        Persona persona = personaRepository.findById(personaId)
                .orElseThrow(() -> new NegocioException(
                        "No existe una persona con id " + personaId));
        if (Boolean.TRUE.equals(persona.getEliminado())) {
            throw new NegocioException("La persona seleccionada como propietario está eliminada.");
        }
        return persona;
    }

    private void validarDireccionUnica(String direccion, String ciudad, Long idExcluir) {
        propiedadRepository
                .findByDireccionIgnoreCaseAndCiudadIgnoreCaseAndEliminadoFalse(direccion, ciudad)
                .ifPresent(existente -> {
                    if (!existente.getId().equals(idExcluir)) {
                        throw new NegocioException(
                                "Ya existe una propiedad activa con la dirección \"" +
                                direccion + "\" en " + ciudad + ".");
                    }
                });
    }

    private boolean tieneContratoActivo(Propiedad propiedad) {
        return propiedad.getEstado() == EstadoPropiedad.ALQUILADA;
    }

    private void registrarHistorial(Propiedad propiedad, EstadoPropiedad estado) {
        historialRepository.save(new HistorialEstadoPropiedad(propiedad, estado));
    }
}
