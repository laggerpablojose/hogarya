package tuti.desi.hogarya.entidades;

import java.time.LocalDate;

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
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "facturas")
public class Factura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El período facturado es obligatorio")
    @Size(max = 20, message = "El período facturado no puede superar los 20 caracteres")
    @Column(name = "periodo_facturado", nullable = false, length = 20)
    private String periodoFacturado;

    @NotNull(message = "La fecha de emisión es obligatoria")
    @Column(name = "fecha_emision", nullable = false)
    private LocalDate fechaEmision;

    @NotNull(message = "La fecha de vencimiento es obligatoria")
    @Column(name = "fecha_vencimiento", nullable = false)
    private LocalDate fechaVencimiento;

    @NotNull(message = "El importe es obligatorio")
    @Positive(message = "El importe debe ser mayor a cero")
    @Column(nullable = false)
    private Double importe;

    @NotNull(message = "El estado de la factura es obligatorio")
    @Enumerated(EnumType.STRING)
    @Column(name = "estado_factura", nullable = false, length = 20)
    private EstadoFactura estadoFactura = EstadoFactura.PENDIENTE;

    @NotNull(message = "El contrato es obligatorio")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contrato_id", nullable = false)
    private Contrato contrato;

    // Composición inversa: si se elimina la factura, se elimina el pago asociado.
    @OneToOne(mappedBy = "factura", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private PagoFactura pagoFactura;

    @Column(nullable = false)
    private Boolean eliminado = false;

    public Factura() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPeriodoFacturado() {
        return periodoFacturado;
    }

    public void setPeriodoFacturado(String periodoFacturado) {
        this.periodoFacturado = periodoFacturado;
    }

    public LocalDate getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(LocalDate fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public LocalDate getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(LocalDate fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public Double getImporte() {
        return importe;
    }

    public void setImporte(Double importe) {
        this.importe = importe;
    }

    public EstadoFactura getEstadoFactura() {
        return estadoFactura;
    }

    public void setEstadoFactura(EstadoFactura estadoFactura) {
        this.estadoFactura = estadoFactura;
    }

    public Contrato getContrato() {
        return contrato;
    }

    public void setContrato(Contrato contrato) {
        this.contrato = contrato;
    }

    public PagoFactura getPagoFactura() {
        return pagoFactura;
    }

    public void setPagoFactura(PagoFactura pagoFactura) {
        this.pagoFactura = pagoFactura;
    }

    public Boolean getEliminado() {
        return eliminado;
    }

    public void setEliminado(Boolean eliminado) {
        this.eliminado = eliminado;
    }
}
