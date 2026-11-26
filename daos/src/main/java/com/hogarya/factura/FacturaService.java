package com.hogarya.factura;

import com.hogarya.common.exception.ConflictoException;
import com.hogarya.common.exception.RecursoNoEncontradoException;
import com.hogarya.common.exception.ReglaNegocioException;
import com.hogarya.contrato.Contrato;
import com.hogarya.contrato.ContratoRepository;
import com.hogarya.factura.dto.CambiarEstadoFacturaRequest;
import com.hogarya.factura.dto.EmitirFacturaRequest;
import com.hogarya.factura.dto.FacturaFiltro;
import com.hogarya.factura.dto.FacturaResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FacturaService {

    private final FacturaRepository facturaRepository;
    private final ContratoRepository contratoRepository;
    private final FacturaMapper mapper;
    private final Clock clock;

    // ---------------------------------------------------------------- POST

    @Transactional
    public FacturaResponse emitir(EmitirFacturaRequest req) {
        LocalDate hoy = LocalDate.now(clock);

        Contrato contrato = contratoRepository.findById(req.contratoId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Contrato", req.contratoId()));
        if (contrato.isEliminado()) {
            throw new ReglaNegocioException("El contrato %d esta eliminado".formatted(contrato.getId()));
        }
        if (!contrato.isActivo()) {
            throw new ReglaNegocioException("El contrato %d no esta activo".formatted(contrato.getId()));
        }

        YearMonth periodo = YearMonth.parse(req.periodo());
        if (!contrato.intersecta(periodo.atDay(1), periodo.atEndOfMonth())) {
            throw new ReglaNegocioException(
                    "El periodo %s no tiene ningun dia dentro de la vigencia del contrato (%s a %s)"
                            .formatted(periodo, contrato.getFechaInicio(),
                                    contrato.getFechaFinEfectiva() == null ? "sin fin" : contrato.getFechaFinEfectiva()));
        }

        boolean esMensual = req.importe() == null;
        TipoFactura tipo = esMensual ? TipoFactura.MENSUAL : TipoFactura.ADICIONAL;
        String clave = esMensual ? Factura.claveMensual(contrato.getId(), req.periodo()) : null;

        if (esMensual && facturaRepository.existsByClaveMensualActiva(clave)) {
            throw new ConflictoException(
                    "Ya existe una facturacion mensual no anulada para el contrato %d y periodo %s"
                            .formatted(contrato.getId(), periodo));
        }

        LocalDate fechaEmision = req.fechaEmision() != null ? req.fechaEmision() : hoy;
        LocalDate fechaVencimiento = req.fechaVencimiento() != null
                ? req.fechaVencimiento()
                : calcularVencimiento(periodo, contrato.getDiaVencimiento());

        if (fechaEmision.isAfter(fechaVencimiento)) {
            throw new ReglaNegocioException("La fecha de emision (%s) no puede ser posterior al vencimiento (%s)"
                    .formatted(fechaEmision, fechaVencimiento));
        }

        BigDecimal importe = esMensual ? contrato.getImporteMensual() : req.importe();

        Factura f = new Factura();
        f.setContrato(contrato);
        f.setPeriodo(req.periodo());
        f.setConcepto(req.concepto() != null ? req.concepto()
                : (esMensual ? "Alquiler mensual " + periodo : null));
        f.setFechaEmision(fechaEmision);
        f.setFechaVencimiento(fechaVencimiento);
        f.setImporte(importe);
        f.setTipo(tipo);
        f.setEstadoRegistrado(EstadoFactura.PENDIENTE);
        f.setClaveMensualActiva(clave);

        // flush inmediato: si otra peticion concurrente gano, el indice unico lanza la excepcion aca (-> 409)
        return mapper.toResponse(facturaRepository.saveAndFlush(f), hoy);
    }

    /** Dia de vencimiento del contrato dentro del periodo; si no existe en el mes, ultimo dia. */
    static LocalDate calcularVencimiento(YearMonth periodo, int diaVencimiento) {
        return periodo.atDay(Math.min(diaVencimiento, periodo.lengthOfMonth()));
    }

    // ---------------------------------------------------------------- GET

    @Transactional(readOnly = true)
    public FacturaResponse obtener(Long id) {
        return mapper.toResponse(buscar(id), LocalDate.now(clock));
    }

    @Transactional(readOnly = true)
    public List<FacturaResponse> listar(FacturaFiltro filtro) {
        LocalDate hoy = LocalDate.now(clock);
        return facturaRepository
                .findAll(FacturaSpecifications.conFiltro(filtro, hoy), Sort.by("id"))
                .stream()
                .map(f -> mapper.toResponse(f, hoy))
                .toList();
    }

    // ---------------------------------------------------------------- PATCH

    @Transactional
    public FacturaResponse cambiarEstado(Long id, CambiarEstadoFacturaRequest req) {
        if (req.estado() != EstadoFactura.ANULADA) {
            throw new ReglaNegocioException(
                    "Este endpoint solo permite ANULAR. PAGADA se establece registrando un pago (POST /pagos); "
                            + "PENDIENTE y VENCIDA los determina el sistema.");
        }

        Factura f = buscar(id);
        switch (f.getEstadoRegistrado()) {
            case PAGADA -> throw new ReglaNegocioException("Una factura pagada no puede anularse");
            case ANULADA -> throw new ReglaNegocioException("La factura ya se encuentra anulada");
            default -> { /* PENDIENTE o VENCIDA: se puede anular */ }
        }

        f.anular(LocalDateTime.now(clock));
        facturaRepository.flush(); // detecta conflicto de @Version dentro de la transaccion
        return mapper.toResponse(f, LocalDate.now(clock));
    }

    // ---------------------------------------------------------------- util

    /** Publico para reutilizarlo desde S02. */
    public Factura buscar(Long id) {
        return facturaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Factura", id));
    }
}
