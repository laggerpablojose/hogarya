package tuti.desi.hogarya.entidades;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "contratos")
public class Contrato {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "La fecha de inicio es obligatoria")
    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate fechaInicio;

    @NotNull(message = "La fecha de fin es obligatoria")
    @Column(name = "fecha_fin", nullable = false)
    private LocalDate fechaFin;

    @NotNull(message = "El importe mensual es obligatorio")
    @Positive(message = "El importe mensual debe ser mayor a cero")
    @Column(name = "importe_mensual", nullable = false)
    private Double importeMensual;

    @NotNull(message = "El día de vencimiento mensual es obligatorio")
    @Column(name = "dia_vencimiento_mensual", nullable = false)
    private Integer diaVencimientoMensual;

    @Size(max = 1000, message = "La descripción no puede superar los 1000 caracteres")
    @Column(length = 1000)
    private String descripcion;

    @NotNull(message = "El estado del contrato es obligatorio")
    @Enumerated(EnumType.STRING)
    @Column(name = "estado_contrato", nullable = false, length = 20)
    private EstadoContrato estadoContrato = EstadoContrato.BORRADOR;

    @NotNull(message = "La propiedad es obligatoria")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "propiedad_id", nullable = false)
    private Propiedad propiedad;

    @NotNull(message = "El propietario es obligatorio")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "propietario_id", nullable = false)
    private Propietario propietario;

    @NotNull(message = "El inquilino es obligatorio")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inquilino_id", nullable = false)
    private Inquilino inquilino;

    // Composición: las facturas no tienen sentido de existencia sin el contrato.
    @OneToMany(mappedBy = "contrato", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Factura> facturas = new ArrayList<>();

    // Composición: los incidentes del contrato (0..* en el diagrama).
    @OneToMany(mappedBy = "contrato", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Incidente> incidentes = new ArrayList<>();

    @Column(nullable = false)
    private Boolean eliminado = false;

    public Contrato() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public Double getImporteMensual() {
        return importeMensual;
    }

    public void setImporteMensual(Double importeMensual) {
        this.importeMensual = importeMensual;
    }

    public Integer getDiaVencimientoMensual() {
        return diaVencimientoMensual;
    }

    public void setDiaVencimientoMensual(Integer diaVencimientoMensual) {
        this.diaVencimientoMensual = diaVencimientoMensual;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public EstadoContrato getEstadoContrato() {
        return estadoContrato;
    }

    public void setEstadoContrato(EstadoContrato estadoContrato) {
        this.estadoContrato = estadoContrato;
    }

    public Propiedad getPropiedad() {
        return propiedad;
    }

    public void setPropiedad(Propiedad propiedad) {
        this.propiedad = propiedad;
    }

    public Propietario getPropietario() {
        return propietario;
    }

    public void setPropietario(Propietario propietario) {
        this.propietario = propietario;
    }

    public Inquilino getInquilino() {
        return inquilino;
    }

    public void setInquilino(Inquilino inquilino) {
        this.inquilino = inquilino;
    }

    public List<Factura> getFacturas() {
        return facturas;
    }

    public void setFacturas(List<Factura> facturas) {
        this.facturas = facturas;
    }

    public List<Incidente> getIncidentes() {
        return incidentes;
    }

    public void setIncidentes(List<Incidente> incidentes) {
        this.incidentes = incidentes;
    }

    public Boolean getEliminado() {
        return eliminado;
    }

    public void setEliminado(Boolean eliminado) {
        this.eliminado = eliminado;
    }
}
