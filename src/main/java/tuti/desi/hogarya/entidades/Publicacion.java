package tuti.desi.hogarya.entidades;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "publicaciones")
public class Publicacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El precio mensual de alquiler es obligatorio")
    @Positive(message = "El precio mensual de alquiler debe ser mayor a cero")
    @Column(name = "precio_mensual_alquiler", nullable = false)
    private Double precioMensualAlquiler;

    @Size(max = 1000, message = "Las condiciones no pueden superar los 1000 caracteres")
    @Column(length = 1000)
    private String condiciones;
    

    @NotBlank(message = "La descripcion es obligatoria")
    @Size(max = 1000, message = "La descripcion no puede superar los 1000 caracteres")
    @Column(nullable = false, length = 1000)
    private String descripcion;


    @NotNull(message = "La fecha de publicación es obligatoria")
    @Column(name = "fecha_publicacion", nullable = false)
    private LocalDate fechaPublicacion;

    @NotNull(message = "El estado de la publicación es obligatorio")
    @Enumerated(EnumType.STRING)
    @Column(name = "estado_publicacion", nullable = false, length = 20)
    private EstadoPublicacion estadoPublicacion = EstadoPublicacion.ACTIVA;

    @NotNull(message = "La propiedad es obligatoria")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "propiedad_id", nullable = false)
    private Propiedad propiedad;

    @Column(nullable = false)
    private Boolean eliminado = false;

    public Publicacion() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Propiedad getPropiedad() {
        return propiedad;
    }

    public void setPropiedad(Propiedad propiedad) {
        this.propiedad = propiedad;
    }

    public Boolean getEliminado() {
        return eliminado;
    }

    public void setEliminado(Boolean eliminado) {
        this.eliminado = eliminado;
    }
}
