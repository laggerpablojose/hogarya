package tuti.desi.hogarya.servicios;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tuti.desi.hogarya.accesoDatos.CambioEstadoPublicacionRepository;
import tuti.desi.hogarya.accesoDatos.PropiedadRepository;
import tuti.desi.hogarya.accesoDatos.PublicacionRepository;
import tuti.desi.hogarya.entidades.CambioEstadoPublicacion;
import tuti.desi.hogarya.entidades.EstadoPropiedad;
import tuti.desi.hogarya.entidades.EstadoPublicacion;
import tuti.desi.hogarya.entidades.Propiedad;
import tuti.desi.hogarya.entidades.Publicacion;
import tuti.desi.hogarya.excepciones.NegocioException;
import tuti.desi.hogarya.presentacion.formularios.PublicacionForm;

@Service
public class PublicacionService {

    @Autowired
    private PublicacionRepository publicacionRepository;

    @Autowired
    private PropiedadRepository propiedadRepository;

    @Autowired
    private CambioEstadoPublicacionRepository cambioEstadoPublicacionRepository;

    public List<Publicacion> listarPublicaciones() {
        return publicacionRepository.findByEliminadoFalse();
    }

    public Publicacion buscarPorId(Long id) {
        if (id == null) {
            throw new NegocioException("Debe indicar una publicación.");
        }

        Publicacion publicacion = publicacionRepository.findById(id)
                .orElseThrow(() -> new NegocioException("No se encontró la publicación solicitada."));

        if (Boolean.TRUE.equals(publicacion.getEliminado())) {
            throw new NegocioException("La publicación solicitada se encuentra eliminada.");
        }

        return publicacion;
    }

    public Publicacion crearPublicacion(PublicacionForm form) {
        validarFormBasico(form);

        Propiedad propiedad = buscarPropiedadPublicable(form.getPropiedadId());

        validarPrecio(form.getPrecioMensualAlquiler());
        validarQueNoExistaPublicacionActiva(propiedad.getId(), null);

        EstadoPublicacion estadoInicial = form.getEstadoPublicacion();
        if (estadoInicial == null) {
            estadoInicial = EstadoPublicacion.ACTIVA;
        }

        Publicacion publicacion = new Publicacion();
        publicacion.setPropiedad(propiedad);
        publicacion.setPrecioMensualAlquiler(form.getPrecioMensualAlquiler());
        publicacion.setCondiciones(form.getCondiciones());
        publicacion.setDescripcion(form.getDescripcion());
        publicacion.setFechaPublicacion(form.getFechaPublicacion());
        publicacion.setEstadoPublicacion(estadoInicial);
        publicacion.setEliminado(false);

        Publicacion publicacionGuardada = publicacionRepository.save(publicacion);

        registrarCambioEstado(publicacionGuardada, publicacionGuardada.getEstadoPublicacion());

        return publicacionGuardada;
    }

    public Publicacion modificarPublicacion(Long id, PublicacionForm form) {
        Publicacion publicacion = buscarPorId(id);

        validarFormBasico(form);

        Propiedad propiedad = buscarPropiedadPublicable(form.getPropiedadId());

        validarPrecio(form.getPrecioMensualAlquiler());

        EstadoPublicacion estadoAnterior = publicacion.getEstadoPublicacion();
        EstadoPublicacion estadoNuevo = form.getEstadoPublicacion();

        if (estadoNuevo == null) {
            estadoNuevo = publicacion.getEstadoPublicacion();
        }

        if (estadoNuevo == EstadoPublicacion.ACTIVA) {
            validarQueNoExistaPublicacionActiva(propiedad.getId(), publicacion.getId());
        }

        publicacion.setPropiedad(propiedad);
        publicacion.setPrecioMensualAlquiler(form.getPrecioMensualAlquiler());
        publicacion.setCondiciones(form.getCondiciones());
        publicacion.setDescripcion(form.getDescripcion());
        publicacion.setFechaPublicacion(form.getFechaPublicacion());
        publicacion.setEstadoPublicacion(estadoNuevo);

        Publicacion publicacionGuardada = publicacionRepository.save(publicacion);

        if (estadoAnterior != estadoNuevo) {
        	registrarCambioEstado(publicacion, estadoNuevo);
        }

        return publicacionGuardada;
    }

    public void eliminarPublicacion(Long id) {
        Publicacion publicacion = buscarPorId(id);

        if (publicacion.getEstadoPublicacion() != EstadoPublicacion.ACTIVA) {
            throw new NegocioException("Solo se puede eliminar una publicación activa.");
        }

        publicacion.setEliminado(true);

        Publicacion publicacionGuardada = publicacionRepository.save(publicacion);

        registrarCambioEstado(publicacionGuardada, publicacionGuardada.getEstadoPublicacion());
    }

    public List<Propiedad> listarPropiedadesDisponiblesParaPublicacion() {
        return propiedadRepository.findAll()
                .stream()
                .filter(propiedad -> !Boolean.TRUE.equals(propiedad.getEliminado()))
                .filter(propiedad -> propiedad.getEstado() == EstadoPropiedad.DISPONIBLE)
                .collect(Collectors.toList());
    }

    public List<Publicacion> buscarConFiltros(EstadoPublicacion estadoPublicacion, Long propiedadId, String ciudad) {
        return publicacionRepository.buscarConFiltros(estadoPublicacion, propiedadId, ciudad);
    }

    private Propiedad buscarPropiedadPublicable(Long propiedadId) {
        if (propiedadId == null) {
            throw new NegocioException("Debe indicar una propiedad.");
        }

        Propiedad propiedad = propiedadRepository.findById(propiedadId)
                .orElseThrow(() -> new NegocioException("No se encontró la propiedad solicitada."));

        if (Boolean.TRUE.equals(propiedad.getEliminado())) {
            throw new NegocioException("No se puede publicar una propiedad eliminada.");
        }

        if (propiedad.getEstado() != EstadoPropiedad.DISPONIBLE) {
            throw new NegocioException("Solo se pueden publicar propiedades disponibles.");
        }

        return propiedad;
    }

    private void validarQueNoExistaPublicacionActiva(Long propiedadId, Long publicacionActualId) {
        List<Publicacion> publicacionesActivas = publicacionRepository
                .findByPropiedad_IdAndEstadoPublicacionAndEliminadoFalse(
                        propiedadId,
                        EstadoPublicacion.ACTIVA
                );

        for (Publicacion publicacion : publicacionesActivas) {
            if (publicacionActualId == null || !publicacion.getId().equals(publicacionActualId)) {
                throw new NegocioException("La propiedad ya tiene una publicación activa.");
            }
        }
    }

    private void validarFormBasico(PublicacionForm form) {
        if (form == null) {
            throw new NegocioException("Los datos de la publicación son obligatorios.");
        }

        if (form.getPropiedadId() == null) {
            throw new NegocioException("La propiedad es obligatoria.");
        }

        validarPrecio(form.getPrecioMensualAlquiler());

        if (form.getCondiciones() == null || form.getCondiciones().trim().isEmpty()) {
            throw new NegocioException("Las condiciones son obligatorias.");
        }

        if (form.getDescripcion() == null || form.getDescripcion().trim().isEmpty()) {
            throw new NegocioException("La descripción es obligatoria.");
        }

        if (form.getFechaPublicacion() == null) {
            throw new NegocioException("La fecha de publicación es obligatoria.");
        }
    }

    private void validarPrecio(Double precioMensualAlquiler) {
        if (precioMensualAlquiler == null) {
            throw new NegocioException("El precio mensual de alquiler es obligatorio.");
        }

        if (precioMensualAlquiler <= 0) {
            throw new NegocioException("El precio mensual de alquiler debe ser positivo.");
        }
    }

    private void registrarCambioEstado(Publicacion publicacion, EstadoPublicacion estado) {
        CambioEstadoPublicacion cambioEstado = new CambioEstadoPublicacion();
        cambioEstado.setPublicacion(publicacion);
        cambioEstado.setEstado(estado);
        cambioEstado.setFechaCambio(LocalDate.now());
        cambioEstado.setEliminado(false);
        cambioEstadoPublicacionRepository.save(cambioEstado);
    }
}