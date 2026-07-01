package tuti.desi.hogarya.presentacion.formularios;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import tuti.desi.hogarya.entidades.EstadoPublicacion;

public class PublicacionForm {

    private Long id;

    @NotNull(message = "La propiedad es obligatoria")
    private Long propiedadId;

    @NotNull(message = "El precio mensual de alquiler es obligatorio")
    @Positive(message = "El precio mensual de alquiler debe ser positivo")
    private Double precioMensualAlquiler;

    @NotBlank(message = "Las condiciones son obligatorias")
    @Size(max = 500, message = "Las condiciones no pueden superar los 500 caracteres")
    private String condiciones;

    @NotBlank(message = "La descripción es obligatoria")
    @Size(max = 500, message = "La descripción no puede superar los 500 caracteres")
    private String descripcion;

    @NotNull(message = "La fecha de publicación es obligatoria")
    private LocalDate fechaPublicacion;

    private EstadoPublicacion estadoPublicacion;

    public PublicacionForm() {
    }

    public Long getId() {
        return id;
    }

    public Long getPropiedadId() {
        return propiedadId;
    }

    public Double getPrecioMensualAlquiler() {
        return precioMensualAlquiler;
    }

    public String getCondiciones() {
        return condiciones;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public LocalDate getFechaPublicacion() {
        return fechaPublicacion;
    }

    public EstadoPublicacion getEstadoPublicacion() {
        return estadoPublicacion;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setPropiedadId(Long propiedadId) {
        this.propiedadId = propiedadId;
    }

    public void setPrecioMensualAlquiler(Double precioMensualAlquiler) {
        this.precioMensualAlquiler = precioMensualAlquiler;
    }

    public void setCondiciones(String condiciones) {
        this.condiciones = condiciones;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setFechaPublicacion(LocalDate fechaPublicacion) {
        this.fechaPublicacion = fechaPublicacion;
    }

    public void setEstadoPublicacion(EstadoPublicacion estadoPublicacion) {
        this.estadoPublicacion = estadoPublicacion;
    }
}