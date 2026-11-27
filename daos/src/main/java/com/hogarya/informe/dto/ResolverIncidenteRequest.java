package com.hogarya.informe.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record ResolverIncidenteRequest(

        @NotNull(message = "El incidente es obligatorio")
        Long incidenteId,

        @NotBlank(message = "La descripcion del trabajo realizado es obligatoria")
        @Size(max = 2000, message = "La descripcion no puede superar 2000 caracteres")
        String descripcionTrabajo,

        @NotNull(message = "El costo es obligatorio (informar 0 si no corresponde)")
        @PositiveOrZero(message = "El costo no puede ser negativo")
        @Digits(integer = 10, fraction = 2, message = "El costo admite hasta 10 enteros y 2 decimales")
        BigDecimal costo,

        @Size(max = 2000, message = "Las observaciones no pueden superar 2000 caracteres")
        String observaciones,

        @NotBlank(message = "El responsable o tecnico es obligatorio")
        @Size(max = 150, message = "El responsable no puede superar 150 caracteres")
        String responsable
) {
}
