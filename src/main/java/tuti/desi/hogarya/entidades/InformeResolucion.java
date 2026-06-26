package tuti.desi.hogarya.entidades;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "informes_resolucion")
public class InformeResolucion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "La fecha de resolución es obligatoria")
    @Column(name = "fecha_resolucion", nullable = false)
    private LocalDate fechaResolucion;

    @NotBlank(message = "La descripción es obligatoria")
    @Size(max = 1000, message = "La descripción no puede superar los 1000 caracteres")
    @Column(nullable = false, length = 1000)
    private String descripcion;

    @NotNull(message = "El costo es obligatorio")
    @PositiveOrZero(message = "El costo no puede ser negativo")
    @Column(nullable = false)
    private Double costo;

    @Size(max = 1000, message = "Las observaciones no pueden superar los 1000 caracteres")
    @Column(length = 1000)
    private String observaciones;

    @NotBlank(message = "El responsable técnico es obligatorio")
    @Size(max = 150, message = "El responsable técnico no puede superar los 150 caracteres")
    @Column(name = "responsable_tecnico", nullable = false, length = 150)
    private String responsableTecnico;

    // Dueño de la relación 0..1 con Incidente.
    @NotNull(message = "El incidente es obligatorio")
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "incidente_id", nullable = false, unique = true)
    private Incidente incidente;

    @Column(nullable = false)
    private Boolean eliminado = false;

    public InformeResolucion() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getFechaResolucion() {
        return fechaResolucion;
    }

    public void setFechaResolucion(LocalDate fechaResolucion) {
        this.fechaResolucion = fechaResolucion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Double getCosto() {
        return costo;
    }

    public void setCosto(Double costo) {
        this.costo = costo;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getResponsableTecnico() {
        return responsableTecnico;
    }

    public void setResponsableTecnico(String responsableTecnico) {
        this.responsableTecnico = responsableTecnico;
    }

    public Incidente getIncidente() {
        return incidente;
    }

    public void setIncidente(Incidente incidente) {
        this.incidente = incidente;
    }

    public Boolean getEliminado() {
        return eliminado;
    }

    public void setEliminado(Boolean eliminado) {
        this.eliminado = eliminado;
    }
}
