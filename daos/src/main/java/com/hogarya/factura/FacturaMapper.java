package com.hogarya.factura;

import com.hogarya.factura.dto.FacturaResponse;
import com.hogarya.pago.PagoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class FacturaMapper {

    private final PagoRepository pagoRepository;

    /** Debe invocarse dentro de una transaccion (accede al contrato LAZY). */
    public FacturaResponse toResponse(Factura f, LocalDate hoy) {
        Long pagoId = f.getEstadoRegistrado() == EstadoFactura.PAGADA
                ? pagoRepository.findIdByFacturaId(f.getId()).orElse(null)
                : null;

        return new FacturaResponse(
                f.getId(),
                f.getContrato().getId(),
                f.getPeriodo(),
                f.getConcepto(),
                f.getFechaEmision(),
                f.getFechaVencimiento(),
                f.getImporte(),
                f.getTipo(),
                f.getEstado(hoy),
                pagoId
        );
    }
}
