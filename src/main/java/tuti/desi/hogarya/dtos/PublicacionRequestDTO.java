package tuti.desi.hogarya.dtos;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import tuti.desi.hogarya.entidades.EstadoPublicacion;

/**
 * DTO de entrada para crear o actualizar una Publicacion.
 * No incluye id ni eliminado: esos campos los maneja el servidor.
 */
public class PublicacionRequestDTO {

    @NotNull(message = "El id de la propiedad es obligatorio")
    private Long propiedadId;

    @NotNull(message = "El precio mensual de alquiler es obligatorio")
    @Positive(message = "El precio mensual de alquiler debe ser mayor a cero")
    private Double precioMensualAlquiler;

    @NotBlank(message = "Las condiciones son obligatorias")
    @Size(max = 1000, message = "Las condiciones no pueden superar los 1000 caracteres")
    private String condiciones;

    @NotBlank(message = "La descripcion es obligatoria")
    @Size(max = 1000, message = "La descripcion no puede superar los 1000 caracteres")
    private String descripcion;

    @NotNull(message = "La fecha de publicacion es obligatoria")
    private LocalDate fechaPublicacion;

    // Opcional: si no se manda, el servicio la deja en ACTIVA por defecto.
    private EstadoPublicacion estadoPublicacion;

    public Long getPropiedadId() {
        return propiedadId;
    }

    public void setPropiedadId(Long propiedadId) {
        this.propiedadId = propiedadId;
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
