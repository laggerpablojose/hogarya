package com.hogarya.incidente.dto;

import com.hogarya.incidente.EstadoIncidente;
import jakarta.validation.constraints.NotNull;

public record CambiarEstadoIncidenteRequest(
        @NotNull(message = "El estado es obligatorio")
        EstadoIncidente estado
) {
}
