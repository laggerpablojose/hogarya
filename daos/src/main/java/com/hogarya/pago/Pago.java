package com.hogarya.pago;

import com.hogarya.factura.Factura;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "pagos",
        uniqueConstraints = @UniqueConstraint(name = "uk_pago_factura", columnNames = "factura_id"))
@Getter
@Setter
@NoArgsConstructor
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Un pago por factura: protegido tambien en BD con uk_pago_factura. */
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "factura_id", nullable = false, unique = true)
    private Factura factura;

    @Column(nullable = false)
    private LocalDate fechaPago;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private MedioPago medioPago;

    /** Total abonado, incluido el interes. */
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal importePagado;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal interes;

    @Column(nullable = false)
    private LocalDateTime fechaRegistro;
}
