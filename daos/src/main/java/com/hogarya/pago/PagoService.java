package com.hogarya.pago;

import com.hogarya.common.exception.ConflictoException;
import com.hogarya.common.exception.RecursoNoEncontradoException;
import com.hogarya.common.exception.ReglaNegocioException;
import com.hogarya.factura.Factura;
import com.hogarya.factura.FacturaRepository;
import com.hogarya.factura.FacturaService;
import com.hogarya.pago.dto.PagoFiltro;
import com.hogarya.pago.dto.PagoResponse;
import com.hogarya.pago.dto.RegistrarPagoRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PagoService {

    private final PagoRepository pagoRepository;
    private final FacturaRepository facturaRepository;
    private final FacturaService facturaService;
    private final Clock clock;

    // ---------------------------------------------------------------- POST

    /**
     * Registra el pago y marca la factura como PAGADA en UNA MISMA transaccion.
     * Si algo falla (validacion, indice unico, @Version de la factura) se hace rollback de todo.
     */
    @Transactional
    public PagoResponse registrar(RegistrarPagoRequest req) {
        LocalDate hoy = LocalDate.now(clock);

        Factura factura = facturaService.buscar(req.facturaId());

        switch (factura.getEstado(hoy)) {
            case ANULADA -> throw new ReglaNegocioException("No se pueden registrar pagos de facturas anuladas");
            case PAGADA -> throw new ReglaNegocioException("La factura ya se encuentra pagada");
            default -> { /* PENDIENTE o VENCIDA: ok */ }
        }

        if (pagoRepository.existsByFacturaId(factura.getId())) {
            throw new ConflictoException("La factura %d ya tiene un pago registrado".formatted(factura.getId()));
        }

        // Importe pagado - interes = importe factura (no se admiten pagos parciales)
        BigDecimal neto = req.importePagado().subtract(req.interes());
        if (neto.compareTo(factura.getImporte()) != 0) {
            throw new ReglaNegocioException(
                    "Importe pagado (%s) - interes (%s) = %s debe ser igual al importe de la factura (%s). No se admiten pagos parciales."
                            .formatted(req.importePagado(), req.interes(), neto, factura.getImporte()));
        }

        LocalDate fechaPago = req.fechaPago() != null ? req.fechaPago() : hoy;
        if (fechaPago.isAfter(hoy)) {
            throw new ReglaNegocioException("La fecha de pago no puede ser futura");
        }
        if (fechaPago.isBefore(factura.getFechaEmision())) {
            throw new ReglaNegocioException("La fecha de pago (%s) no puede ser anterior a la emision de la factura (%s)"
                    .formatted(fechaPago, factura.getFechaEmision()));
        }

        Pago pago = new Pago();
        pago.setFactura(factura);
        pago.setFechaPago(fechaPago);
        pago.setMedioPago(req.medioPago());
        pago.setImportePagado(req.importePagado());
        pago.setInteres(req.interes());
        pago.setFechaRegistro(LocalDateTime.now(clock));

        factura.marcarPagada();

        pagoRepository.save(pago);
        // flush dentro de la transaccion: detecta duplicado (uk_pago_factura) o anulacion concurrente (@Version) -> 409
        facturaRepository.flush();

        return toResponse(pago);
    }

    // ---------------------------------------------------------------- GET

    @Transactional(readOnly = true)
    public PagoResponse obtener(Long id) {
        return toResponse(pagoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Pago", id)));
    }

    @Transactional(readOnly = true)
    public List<PagoResponse> listar(PagoFiltro filtro) {
        return pagoRepository.findAll(PagoSpecifications.conFiltro(filtro), Sort.by("id"))
                .stream().map(this::toResponse).toList();
    }

    private PagoResponse toResponse(Pago p) {
        return new PagoResponse(p.getId(), p.getFactura().getId(), p.getFechaPago(), p.getMedioPago(),
                p.getImportePagado(), p.getInteres(), p.getFechaRegistro());
    }
}
