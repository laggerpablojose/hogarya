package com.hogarya.factura.dto;

import com.hogarya.factura.EstadoFactura;
import jakarta.validation.constraints.NotNull;

public record CambiarEstadoFacturaRequest(
        @NotNull(message = "El estado es obligatorio")
        EstadoFactura estado
) {
}
