package com.hogarya.incidente;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/** Entrada de solo lectura del historial: no tiene setters publicos. */
@Entity
@Table(name = "historial_estados_incidente",
        indexes = @Index(name = "idx_historial_incidente_fecha", columnList = "incidente_id, fecha_hora"))
@Getter
@NoArgsConstructor
public class HistorialEstado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "incidente_id", nullable = false, updatable = false)
    private Incidente incidente;

    /** Vacio en el registro inicial. */
    @Enumerated(EnumType.STRING)
    @Column(length = 20, updatable = false)
    private EstadoIncidente estadoAnterior;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20, updatable = false)
    private EstadoIncidente estadoNuevo;

    @Column(name = "fecha_hora", nullable = false, updatable = false)
    private LocalDateTime fechaHora;

    HistorialEstado(Incidente incidente, EstadoIncidente anterior, EstadoIncidente nuevo, LocalDateTime fechaHora) {
        this.incidente = incidente;
        this.estadoAnterior = anterior;
        this.estadoNuevo = nuevo;
        this.fechaHora = fechaHora;
    }
}
