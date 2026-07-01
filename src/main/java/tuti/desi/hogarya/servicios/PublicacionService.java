package tuti.desi.hogarya.servicios;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tuti.desi.hogarya.dtos.PublicacionRequestDTO;
import tuti.desi.hogarya.entidades.CambioEstadoPublicacion;
import tuti.desi.hogarya.entidades.EstadoPropiedad;
import tuti.desi.hogarya.entidades.EstadoPublicacion;
import tuti.desi.hogarya.entidades.Propiedad;
import tuti.desi.hogarya.entidades.Publicacion;
import tuti.desi.hogarya.repositorios.CambioEstadoPublicacionRepository;
import tuti.desi.hogarya.repositorios.PropiedadRepository;
import tuti.desi.hogarya.repositorios.PublicacionRepository;

@Service
public class PublicacionService {

    private final PublicacionRepository publicacionRepository;
    private final PropiedadRepository propiedadRepository;
    private final CambioEstadoPublicacionRepository cambioEstadoPublicacionRepository;

    public PublicacionService(PublicacionRepository publicacionRepository,
            PropiedadRepository propiedadRepository,
            CambioEstadoPublicacionRepository cambioEstadoPublicacionRepository) {
        this.publicacionRepository = publicacionRepository;
        this.propiedadRepository = propiedadRepository;
        this.cambioEstadoPublicacionRepository = cambioEstadoPublicacionRepository;
    }

    @Transactional(readOnly = true)
    public List<Publicacion> listar(Long propiedadId, String ciudad, EstadoPublicacion estado,
            Double precioMin, Double precioMax) {
        return publicacionRepository.buscarConFiltros(propiedadId, ciudad, estado, precioMin, precioMax);
    }

    @Transactional(readOnly = true)
    public Publicacion buscarPorId(Long id) {
        Publicacion publicacion = publicacionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No existe una publicacion con id " + id));

        if (Boolean.TRUE.equals(publicacion.getEliminado())) {
            throw new IllegalArgumentException("La publicacion con id " + id + " fue eliminada");
        }

        return publicacion;
    }

    @Transactional(readOnly = true)
    public List<Publicacion> buscarPorEstado(EstadoPublicacion estado) {
        return publicacionRepository.findByEstadoPublicacionAndEliminadoFalse(estado);
    }

    @Transactional(readOnly = true)
    public List<Publicacion> buscarPorPropiedad(Long propiedadId) {
        return publicacionRepository.findByPropiedad_IdAndEliminadoFalse(propiedadId);
    }

    @Transactional(readOnly = true)
    public List<Publicacion> buscarPorCiudad(String ciudad) {
        return publicacionRepository.findByPropiedad_CiudadIgnoreCaseAndEliminadoFalse(ciudad);
    }

    @Transactional
    public Publicacion crear(PublicacionRequestDTO dto) {
        Propiedad propiedad = obtenerPropiedadValidaParaPublicar(dto.getPropiedadId());

        validarPrecio(dto.getPrecioMensualAlquiler());
        validarQueNoExistaPublicacionActiva(propiedad.getId(), null);

        Publicacion publicacion = new Publicacion();
        publicacion.setPropiedad(propiedad);
        publicacion.setPrecioMensualAlquiler(dto.getPrecioMensualAlquiler());
        publicacion.setCondiciones(dto.getCondiciones());
        publicacion.setDescripcion(dto.getDescripcion());
        publicacion.setFechaPublicacion(dto.getFechaPublicacion());
        publicacion.setEstadoPublicacion(dto.getEstadoPublicacion() != null
                ? dto.getEstadoPublicacion()
                : EstadoPublicacion.ACTIVA);
        publicacion.setEliminado(false);

        Publicacion guardada = publicacionRepository.save(publicacion);
        registrarCambioEstado(guardada, guardada.getEstadoPublicacion());

        return guardada;
    }

    @Transactional
    public Publicacion actualizar(Long id, PublicacionRequestDTO dto) {
        Publicacion publicacion = buscarPorId(id);

        validarPrecio(dto.getPrecioMensualAlquiler());

        EstadoPublicacion estadoAnterior = publicacion.getEstadoPublicacion();
        EstadoPublicacion estadoNuevo = dto.getEstadoPublicacion() != null
                ? dto.getEstadoPublicacion()
                : publicacion.getEstadoPublicacion();

        if (estadoNuevo == EstadoPublicacion.ACTIVA) {
            validarPropiedadDisponibleParaActivar(publicacion.getPropiedad());
            validarQueNoExistaPublicacionActiva(publicacion.getPropiedad().getId(), publicacion.getId());
        }

        if (publicacion.getEstadoPublicacion() == EstadoPublicacion.FINALIZADA) {
            throw new IllegalArgumentException("No se puede modificar una publicacion finalizada");
        }

        // La propiedad no se modifica en la actualizacion para evitar inconsistencias historicas.
        publicacion.setPrecioMensualAlquiler(dto.getPrecioMensualAlquiler());
        publicacion.setCondiciones(dto.getCondiciones());
        publicacion.setDescripcion(dto.getDescripcion());
        publicacion.setFechaPublicacion(dto.getFechaPublicacion());
        publicacion.setEstadoPublicacion(estadoNuevo);

        Publicacion actualizada = publicacionRepository.save(publicacion);

        if (estadoAnterior != estadoNuevo) {
            registrarCambioEstado(actualizada, estadoNuevo);
        }

        return actualizada;
    }

    @Transactional
    public void eliminar(Long id) {
        Publicacion publicacion = buscarPorId(id);

        if (publicacion.getEstadoPublicacion() != EstadoPublicacion.ACTIVA) {
            throw new IllegalArgumentException("Solo se pueden eliminar publicaciones activas");
        }

        publicacion.setEliminado(true);
        publicacionRepository.save(publicacion);
    }

    private Propiedad obtenerPropiedadValidaParaPublicar(Long propiedadId) {
        Propiedad propiedad = propiedadRepository.findById(propiedadId)
                .orElseThrow(() -> new IllegalArgumentException("No existe una propiedad con id " + propiedadId));

        if (Boolean.TRUE.equals(propiedad.getEliminado())) {
            throw new IllegalArgumentException("La propiedad con id " + propiedadId + " fue eliminada");
        }

        if (propiedad.getEstado() != EstadoPropiedad.DISPONIBLE) {
            throw new IllegalArgumentException("Solo se puede publicar una propiedad disponible");
        }

        return propiedad;
    }

    private void validarPropiedadDisponibleParaActivar(Propiedad propiedad) {
        if (propiedad == null) {
            throw new IllegalArgumentException("La publicacion no tiene una propiedad asociada");
        }

        if (Boolean.TRUE.equals(propiedad.getEliminado())) {
            throw new IllegalArgumentException("No se puede activar una publicacion de una propiedad eliminada");
        }

        if (propiedad.getEstado() != EstadoPropiedad.DISPONIBLE) {
            throw new IllegalArgumentException("Solo se puede activar la publicacion si la propiedad esta disponible");
        }
    }

    private void validarQueNoExistaPublicacionActiva(Long propiedadId, Long publicacionActualId) {
        publicacionRepository
                .findByPropiedad_IdAndEstadoPublicacionAndEliminadoFalse(propiedadId, EstadoPublicacion.ACTIVA)
                .filter(publicacion -> publicacionActualId == null || !publicacion.getId().equals(publicacionActualId))
                .ifPresent(publicacion -> {
                    throw new IllegalArgumentException("Ya existe una publicacion activa para esta propiedad");
                });
    }

    private void validarPrecio(Double precio) {
        if (precio == null || precio <= 0) {
            throw new IllegalArgumentException("El precio mensual de alquiler debe ser mayor a cero");
        }
    }

    private void registrarCambioEstado(Publicacion publicacion, EstadoPublicacion estado) {
        CambioEstadoPublicacion cambio = new CambioEstadoPublicacion();
        cambio.setPublicacion(publicacion);
        cambio.setEstado(estado);
        cambio.setFechaCambio(LocalDate.now());
        cambioEstadoPublicacionRepository.save(cambio);
    }
}
