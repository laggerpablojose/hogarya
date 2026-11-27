package com.hogarya.informe;

import com.hogarya.incidente.HistorialEstado;
import com.hogarya.incidente.Incidente;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "informes_resolucion",
        indexes = @Index(name = "idx_informe_incidente", columnList = "incidente_id"),
        uniqueConstraints = @UniqueConstraint(name = "uk_informe_transicion", columnNames = "historial_id"))
@Getter
@Setter
@NoArgsConstructor
public class InformeResolucion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "incidente_id", nullable = false, updatable = false)
    private Incidente incidente;

    /**
     * Transicion EN_PROCESO -> RESUELTO que genero este informe.
     * El indice unico impide registrar dos informes para la misma transicion de resolucion.
     */
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "historial_id", nullable = false, unique = true, updatable = false)
    private HistorialEstado transicion;

    @Column(nullable = false, length = 2000)
    private String descripcionTrabajo;

    /** Misma fecha y hora que la entrada RESUELTO del historial. */
    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaResolucion;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal costo;

    @Column(length = 2000)
    private String observaciones;

    @Column(nullable = false, length = 150)
    private String responsable;
}
