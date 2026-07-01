package tuti.desi.hogarya.dtos;

import java.time.LocalDate;

import tuti.desi.hogarya.entidades.EstadoPublicacion;
import tuti.desi.hogarya.entidades.Publicacion;

/**
 * DTO de salida. Expone los datos principales de la Publicacion junto con
 * datos resumidos de la Propiedad asociada.
 */
public class PublicacionResponseDTO {

    private Long id;
    private Long propiedadId;
    private String direccionPropiedad;
    private String ciudad;
    private Double precioMensualAlquiler;
    private String condiciones;
    private String descripcion;
    private LocalDate fechaPublicacion;
    private EstadoPublicacion estadoPublicacion;

    public PublicacionResponseDTO() {
    }

    public PublicacionResponseDTO(Publicacion publicacion) {
        this.id = publicacion.getId();
        this.precioMensualAlquiler = publicacion.getPrecioMensualAlquiler();
        this.condiciones = publicacion.getCondiciones();
        this.descripcion = publicacion.getDescripcion();
        this.fechaPublicacion = publicacion.getFechaPublicacion();
        this.estadoPublicacion = publicacion.getEstadoPublicacion();

        if (publicacion.getPropiedad() != null) {
            this.propiedadId = publicacion.getPropiedad().getId();
            this.direccionPropiedad = publicacion.getPropiedad().getDireccion();
            this.ciudad = publicacion.getPropiedad().getCiudad();
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPropiedadId() {
        return propiedadId;
    }

    public void setPropiedadId(Long propiedadId) {
        this.propiedadId = propiedadId;
    }

    public String getDireccionPropiedad() {
        return direccionPropiedad;
    }

    public void setDireccionPropiedad(String direccionPropiedad) {
        this.direccionPropiedad = direccionPropiedad;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public Double getPrecioMensualAlquiler() {
        return precioMensualAlquiler;
    }

    public void setPrecioMensualAlquiler(Double precioMensualAlquiler) {
        this.precioMensualAlquiler = precioMensualAlquiler;
    }

    public String getCondiciones() {
        return condiciones;
    }

    public void setCondiciones(String condiciones) {
        this.condiciones = condiciones;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDate getFechaPublicacion() {
        return fechaPublicacion;
    }

    public void setFechaPublicacion(LocalDate fechaPublicacion) {
        this.fechaPublicacion = fechaPublicacion;
    }

    public EstadoPublicacion getEstadoPublicacion() {
        return estadoPublicacion;
    }

    public void setEstadoPublicacion(EstadoPublicacion estadoPublicacion) {
        this.estadoPublicacion = estadoPublicacion;
    }
}
