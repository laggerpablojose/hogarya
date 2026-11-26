package com.hogarya.factura;

public enum EstadoFactura {
    PENDIENTE,
    VENCIDA,   // calculado, nunca se persiste
    PAGADA,
    ANULADA
}
