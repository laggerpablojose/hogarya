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
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "incidentes")
public class Incidente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El título es obligatorio")
    @Size(max = 150, message = "El título no puede superar los 150 caracteres")
    @Column(nullable = false, length = 150)
    private String titulo;

    @Size(max = 1000, message = "La descripción no puede superar los 1000 caracteres")
    @Column(length = 1000)
    private String descripcion;

    @NotBlank(message = "La categoría es obligatoria")
    @Size(max = 100, message = "La categoría no puede superar los 100 caracteres")
    @Column(nullable = false, length = 100)
    private String categoria;

    @NotNull(message = "La fecha de alta es obligatoria")
    @Column(name = "fecha_alta", nullable = false)
    private LocalDate fechaAlta;

    @NotBlank(message = "La prioridad es obligatoria")
    @Size(max = 30, message = "La prioridad no puede superar los 30 caracteres")
    @Column(nullable = false, length = 30)
    private String prioridad;

    @NotNull(message = "El contrato es obligatorio")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contrato_id", nullable = false)
    private Contrato contrato;

    // Estado actual del incidente. Se mantiene como atributo propio (no como
    // entidad), reservando CambioEstadoIncidente para el historial de cambios.
    @NotNull(message = "El estado del incidente es obligatorio")
    @Enumerated(EnumType.STRING)
    @Column(name = "estado_incidente", nullable = false, length = 20)
    private EstadoIncidente estadoIncidente = EstadoIncidente.ABIERTO;

    // Composición: el historial de cambios no existe sin el incidente (1..*).
    @OneToMany(mappedBy = "incidente", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<CambioEstadoIncidente> cambiosEstado = new ArrayList<>();

    // Composición: el informe de resolución no existe sin el incidente (0..1).
    @OneToOne(mappedBy = "incidente", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private InformeResolucion informeResolucion;

    @Column(nullable = false)
    private Boolean eliminado = false;

    public Incidente() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public LocalDate getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(LocalDate fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    public String getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(String prioridad) {
        this.prioridad = prioridad;
    }

    public Contrato getContrato() {
        return contrato;
    }

    public void setContrato(Contrato contrato) {
        this.contrato = contrato;
    }

    public EstadoIncidente getEstadoIncidente() {
        return estadoIncidente;
    }

    public void setEstadoIncidente(EstadoIncidente estadoIncidente) {
        this.estadoIncidente = estadoIncidente;
    }

    public List<CambioEstadoIncidente> getCambiosEstado() {
        return cambiosEstado;
    }

    public void setCambiosEstado(List<CambioEstadoIncidente> cambiosEstado) {
        this.cambiosEstado = cambiosEstado;
    }

    public InformeResolucion getInformeResolucion() {
        return informeResolucion;
    }

    public void setInformeResolucion(InformeResolucion informeResolucion) {
        this.informeResolucion = informeResolucion;
    }

    public Boolean getEliminado() {
        return eliminado;
    }

    public void setEliminado(Boolean eliminado) {
        this.eliminado = eliminado;
    }
}
