package com.hogarya.incidente;

import com.hogarya.contrato.Contrato;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "incidentes")
@Getter
@Setter
@NoArgsConstructor
public class Incidente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "contrato_id", nullable = false)
    private Contrato contrato;

    @Column(nullable = false, length = 150)
    private String titulo;

    @Column(nullable = false, length = 2000)
    private String descripcion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private CategoriaIncidente categoria;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private PrioridadIncidente prioridad;

    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaAlta;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Setter(AccessLevel.NONE) // solo cambia via transicionar(...)
    private EstadoIncidente estado;

    @OneToMany(mappedBy = "incidente", cascade = CascadeType.PERSIST)
    @OrderBy("fechaHora ASC, id ASC")
    @Setter(AccessLevel.NONE)
    private List<HistorialEstado> historial = new ArrayList<>();

    /** Bloqueo optimista: evita que dos cambios de estado concurrentes se pisen. */
    @Version
    private Long version;

    /** Alta: estado ABIERTO + primera entrada del historial (estado anterior vacio). */
    public static Incidente nuevo(Contrato contrato, String titulo, String descripcion,
                                  CategoriaIncidente categoria, PrioridadIncidente prioridad,
                                  LocalDateTime ahora) {
        Incidente i = new Incidente();
        i.contrato = contrato;
        i.titulo = titulo;
        i.descripcion = descripcion;
        i.categoria = categoria;
        i.prioridad = prioridad;
        i.fechaAlta = ahora;
        i.estado = EstadoIncidente.ABIERTO;
        i.historial.add(new HistorialEstado(i, null, EstadoIncidente.ABIERTO, ahora));
        return i;
    }

    /**
     * Cambia el estado y registra la transicion en el historial (mismo objeto, misma transaccion).
     * La validacion de que la transicion sea legal la hace el servicio que la invoca (S03 o S04).
     * Devuelve la entrada de historial creada.
     */
    public HistorialEstado transicionar(EstadoIncidente nuevo, LocalDateTime fechaHora) {
        HistorialEstado h = new HistorialEstado(this, this.estado, nuevo, fechaHora);
        this.historial.add(h);
        this.estado = nuevo;
        return h;
    }
}
