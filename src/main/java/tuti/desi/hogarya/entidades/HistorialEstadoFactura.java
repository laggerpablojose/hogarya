package tuti.desi.hogarya.entidades;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "historiales_estado_factura")
public class HistorialEstadoFactura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "factura_id", nullable = false)
    private Factura factura;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_anterior", length = 30)
    private EstadoFactura estadoAnterior;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_nuevo", nullable = false, length = 30)
    private EstadoFactura estadoNuevo;

    @Column(nullable = false)
    private LocalDateTime fecha;

    @Size(max = 500, message = "La observación no puede superar los 500 caracteres")
    @Column(length = 500)
    private String observacion;

    public HistorialEstadoFactura() {
    }

    public HistorialEstadoFactura(Factura factura, EstadoFactura estadoAnterior, EstadoFactura estadoNuevo,
            String observacion) {
        this.factura = factura;
        this.estadoAnterior = estadoAnterior;
        this.estadoNuevo = estadoNuevo;
        this.fecha = LocalDateTime.now();
        this.observacion = observacion;
    }

    public Long getId() {
        return id;
    }

    public Factura getFactura() {
        return factura;
    }

    public EstadoFactura getEstadoAnterior() {
        return estadoAnterior;
    }

    public EstadoFactura getEstadoNuevo() {
        return estadoNuevo;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setFactura(Factura factura) {
        this.factura = factura;
    }

    public void setEstadoAnterior(EstadoFactura estadoAnterior) {
        this.estadoAnterior = estadoAnterior;
    }

    public void setEstadoNuevo(EstadoFactura estadoNuevo) {
        this.estadoNuevo = estadoNuevo;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }
}