package com.hogarya.pago.dto;

import com.hogarya.pago.MedioPago;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.time.LocalDate;

public record RegistrarPagoRequest(

        @NotNull(message = "La factura es obligatoria")
        Long facturaId,

        /* Opcional: si es null se usa la fecha actual */
        LocalDate fechaPago,

        @NotNull(message = "El medio de pago es obligatorio")
        MedioPago medioPago,

        @NotNull(message = "El importe pagado es obligatorio")
        @Positive(message = "El importe pagado debe ser mayor a cero")
        @Digits(integer = 10, fraction = 2, message = "El importe admite hasta 10 enteros y 2 decimales")
        BigDecimal importePagado,

        @NotNull(message = "El interes es obligatorio (informar 0 si no corresponde)")
        @PositiveOrZero(message = "El interes no puede ser negativo")
        @Digits(integer = 10, fraction = 2, message = "El interes admite hasta 10 enteros y 2 decimales")
        BigDecimal interes
) {
}
