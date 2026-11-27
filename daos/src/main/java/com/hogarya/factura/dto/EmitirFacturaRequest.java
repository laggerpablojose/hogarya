package com.hogarya.factura.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

public record EmitirFacturaRequest(

        @NotNull(message = "El contrato es obligatorio")
        Long contratoId,

        @NotBlank(message = "El periodo es obligatorio")
        @Pattern(regexp = "^\\d{4}-(0[1-9]|1[0-2])$", message = "El periodo debe tener formato AAAA-MM")
        String periodo,

        @Size(max = 500, message = "El concepto no puede superar 500 caracteres")
        String concepto,

        /* Opcional: si es null se usa la fecha actual */
        LocalDate fechaEmision,

        /* Opcional: si es null se calcula con el dia de vencimiento del contrato */
        LocalDate fechaVencimiento,

        /* Opcional: si es null -> facturacion MENSUAL con el importe del contrato */
        @Positive(message = "El importe debe ser mayor a cero")
        @Digits(integer = 10, fraction = 2, message = "El importe admite hasta 10 enteros y 2 decimales")
        BigDecimal importe
) {
}
