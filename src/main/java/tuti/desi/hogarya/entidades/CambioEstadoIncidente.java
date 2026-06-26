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

@Entity
@Table(name = "cambios_estado_incidente")
public class CambioEstadoIncidente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El estado es obligatorio")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoIncidente estado;

    @NotNull(message = "La fecha de cambio es obligatoria")
    @Column(name = "fecha_cambio", nullable = false)
    private LocalDate fechaCambio;

    @NotNull(message = "El incidente es obligatorio")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "incidente_id", nullable = false)
    private Incidente incidente;

    @Column(nullable = false)
    private Boolean eliminado = false;

    public CambioEstadoIncidente() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EstadoIncidente getEstado() {
        return estado;
    }

    public void setEstado(EstadoIncidente estado) {
        this.estado = estado;
    }

    public LocalDate getFechaCambio() {
        return fechaCambio;
    }

    public void setFechaCambio(LocalDate fechaCambio) {
        this.fechaCambio = fechaCambio;
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
