package com.hogarya.contrato;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "contratos")
@Getter
@Setter
@NoArgsConstructor
public class Contrato {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate fechaInicio;

    /** Puede venir explicita o calcularse a partir de duracionMeses. */
    private LocalDate fechaFin;

    private Integer duracionMeses;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal importeMensual;

    /** Dia del mes (1-31) en que vencen las facturas mensuales. */
    @Column(nullable = false)
    private Integer diaVencimiento;

    @Column(nullable = false)
    private boolean activo = true;

    @Column(nullable = false)
    private boolean eliminado = false;

    /**
     * Regla: si hay inicio + duracion en meses, fin = inicio + duracion - 1 dia.
     * Devuelve null si el contrato no tiene fin definido.
     */
    public LocalDate getFechaFinEfectiva() {
        if (fechaFin != null) return fechaFin;
        if (duracionMeses != null) return fechaInicio.plusMonths(duracionMeses).minusDays(1);
        return null;
    }

    /** true si el rango [desde, hasta] tiene al menos un dia dentro de la vigencia. */
    public boolean intersecta(LocalDate desde, LocalDate hasta) {
        LocalDate fin = getFechaFinEfectiva();
        boolean empiezaAntesDelFin = (fin == null) || !desde.isAfter(fin);
        boolean terminaDespuesDelInicio = !hasta.isBefore(fechaInicio);
        return empiezaAntesDelFin && terminaDespuesDelInicio;
    }

    public boolean estaVigenteEn(LocalDate fecha) {
        return intersecta(fecha, fecha);
    }
}
