package com.hogarya.incidente;

import java.util.Set;

public enum EstadoIncidente {
    ABIERTO,
    EN_PROCESO,
    RESUELTO,
    CANCELADO,
    REABIERTO;

    /**
     * Transiciones permitidas por el modulo de incidentes (PATCH /incidentes/{id}/estado).
     * EN_PROCESO -> RESUELTO NO esta aca: se hace exclusivamente desde S04 (informes de resolucion).
     */
    public Set<EstadoIncidente> transicionesPermitidasS03() {
        return switch (this) {
            case ABIERTO -> Set.of(EN_PROCESO, CANCELADO);
            case EN_PROCESO -> Set.of(CANCELADO);
            case RESUELTO -> Set.of(REABIERTO);
            case REABIERTO -> Set.of(EN_PROCESO);
            case CANCELADO -> Set.of();
        };
    }

    public boolean permiteS03(EstadoIncidente nuevo) {
        return transicionesPermitidasS03().contains(nuevo);
    }
}
