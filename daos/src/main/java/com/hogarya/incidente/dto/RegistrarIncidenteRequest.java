package com.hogarya.incidente.dto;

import com.hogarya.incidente.CategoriaIncidente;
import com.hogarya.incidente.PrioridadIncidente;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RegistrarIncidenteRequest(

        @NotNull(message = "El contrato es obligatorio")
        Long contratoId,

        @NotBlank(message = "El titulo es obligatorio")
        @Size(max = 150, message = "El titulo no puede superar 150 caracteres")
        String titulo,

        @NotBlank(message = "La descripcion es obligatoria")
        @Size(max = 2000, message = "La descripcion no puede superar 2000 caracteres")
        String descripcion,

        @NotNull(message = "La categoria es obligatoria")
        CategoriaIncidente categoria,

        @NotNull(message = "La prioridad es obligatoria")
        PrioridadIncidente prioridad
) {
}
