package com.hogarya.factura;

import com.hogarya.contrato.Contrato;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "facturas",
        indexes = @Index(name = "idx_factura_contrato_periodo", columnList = "contrato_id, periodo"),
        uniqueConstraints = @UniqueConstraint(name = "uk_factura_mensual_activa",
                columnNames = "clave_mensual_activa"))
@Getter
@Setter
@NoArgsConstructor
public class Factura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "contrato_id", nullable = false)
    private Contrato contrato;

    /** Formato AAAA-MM */
    @Column(nullable = false, length = 7)
    private String periodo;

    @Column(length = 500)
    private String concepto;

    @Column(nullable = false)
    private LocalDate fechaEmision;

    @Column(nullable = false)
    private LocalDate fechaVencimiento;

    /** Copia del importe al momento de emitir: no cambia si luego se modifica el contrato. */
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal importe;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoFactura tipo;

    /** Solo PENDIENTE, PAGADA o ANULADA. VENCIDA se deriva. */
    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 20)
    private EstadoFactura estadoRegistrado;

    /**
     * "contratoId|AAAA-MM" para facturas MENSUAL no anuladas; NULL en otro caso.
     * El indice unico garantiza en BD una sola facturacion mensual vigente por contrato y periodo.
     */
    @Column(name = "clave_mensual_activa", length = 40)
    private String claveMensualActiva;

    private LocalDateTime fechaAnulacion;

    @Version
    private Long version;

    /** Estado efectivo a una fecha dada (pendiente -> vencida desde el dia siguiente al vencimiento). */
    public EstadoFactura getEstado(LocalDate hoy) {
        if (estadoRegistrado == EstadoFactura.PENDIENTE && hoy.isAfter(fechaVencimiento)) {
            return EstadoFactura.VENCIDA;
        }
        return estadoRegistrado;
    }

    /** Pendiente o vencida (ambas se guardan como PENDIENTE). */
    public boolean estaImpaga() {
        return estadoRegistrado == EstadoFactura.PENDIENTE;
    }

    public void anular(LocalDateTime ahora) {
        this.estadoRegistrado = EstadoFactura.ANULADA;
        this.claveMensualActiva = null;
        this.fechaAnulacion = ahora;
    }

    /** Usado exclusivamente por S02 al registrar el pago. */
    public void marcarPagada() {
        this.estadoRegistrado = EstadoFactura.PAGADA;
    }

    public static String claveMensual(Long contratoId, String periodo) {
        return contratoId + "|" + periodo;
    }
}
