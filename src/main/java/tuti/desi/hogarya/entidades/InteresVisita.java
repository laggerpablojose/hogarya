package tuti.desi.hogarya.entidades;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "intereses_visitas")
public class InteresVisita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 500, message = "La observación no puede superar los 500 caracteres")
    @Column(length = 500)
    private String observacion;

    @NotNull(message = "La fecha es obligatoria")
    @Column(nullable = false)
    private LocalDate fecha;

    @NotNull(message = "El interesado es obligatorio")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "interesado_id", nullable = false)
    private Interesado interesado;

    @NotNull(message = "La propiedad es obligatoria")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "propiedad_id", nullable = false)
    private Propiedad propiedad;

    @Column(nullable = false)
    private Boolean eliminado = false;

    public InteresVisita() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public Interesado getInteresado() {
        return interesado;
    }

    public void setInteresado(Interesado interesado) {
        this.interesado = interesado;
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
