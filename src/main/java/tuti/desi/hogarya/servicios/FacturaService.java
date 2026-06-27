package tuti.desi.hogarya.servicios;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tuti.desi.hogarya.accesoDatos.ContratoRepository;
import tuti.desi.hogarya.accesoDatos.FacturaRepository;
import tuti.desi.hogarya.entidades.Contrato;
import tuti.desi.hogarya.entidades.EstadoContrato;
import tuti.desi.hogarya.entidades.EstadoFactura;
import tuti.desi.hogarya.entidades.Factura;
import tuti.desi.hogarya.excepciones.NegocioException;
import tuti.desi.hogarya.presentacion.formularios.FacturaForm;

@Service
public class FacturaService {

    @Autowired
    private FacturaRepository facturaRepository;

    @Autowired
    private ContratoRepository contratoRepository;

    public List<Factura> listarFacturas() {
        return facturaRepository.findByEliminadoFalse();
    }

    public Factura buscarPorId(Long id) {
        if (id == null) {
            throw new NegocioException("Debe indicar una factura.");
        }

        Factura factura = facturaRepository.findById(id)
                .orElseThrow(() -> new NegocioException("No se encontró la factura solicitada."));

        if (Boolean.TRUE.equals(factura.getEliminado())) {
            throw new NegocioException("La factura solicitada se encuentra eliminada.");
        }

        return factura;
    }

    public Factura crearFactura(FacturaForm form) {
        validarFormBasico(form);

        if (form.getEstado() != null && form.getEstado() != EstadoFactura.PENDIENTE) {
            throw new NegocioException("Una factura nueva debe iniciar en estado pendiente.");
        }

        Contrato contrato = buscarContratoActivo(form.getContratoId());

        validarFechas(form);
        validarImporte(form.getImporte());
        validarDatosPagoVacios(form);

        Factura factura = new Factura();
        factura.setContrato(contrato);
        factura.setConcepto(form.getConcepto());
        factura.setFechaEmision(form.getFechaEmision());
        factura.setFechaVencimiento(form.getFechaVencimiento());
        factura.setImporte(form.getImporte());
        factura.setEstado(EstadoFactura.PENDIENTE);
        factura.setEliminado(false);
        limpiarDatosPago(factura);

        return facturaRepository.save(factura);
    }

    public Factura modificarFactura(Long id, FacturaForm form) {
        Factura factura = buscarPorId(id);

        validarFacturaModificable(factura);
        validarFormBasico(form);

        Contrato contrato = buscarContratoActivo(form.getContratoId());

        validarFechas(form);
        validarImporte(form.getImporte());

        EstadoFactura nuevoEstado = form.getEstado();
        if (nuevoEstado == null) {
            nuevoEstado = factura.getEstado();
        }

        if (nuevoEstado == EstadoFactura.PAGADA) {
            validarDatosPagoCompletos(form);
        } else {
            validarDatosPagoVacios(form);
        }

        factura.setContrato(contrato);
        factura.setConcepto(form.getConcepto());
        factura.setFechaEmision(form.getFechaEmision());
        factura.setFechaVencimiento(form.getFechaVencimiento());
        factura.setImporte(form.getImporte());
        factura.setEstado(nuevoEstado);

        if (nuevoEstado == EstadoFactura.PAGADA) {
            factura.setFechaPago(form.getFechaPago());
            factura.setMedioPago(form.getMedioPago());
            factura.setImportePagado(form.getImportePagado());
            factura.setInteresPagado(form.getInteresPagado());
        } else {
            limpiarDatosPago(factura);
        }

        return facturaRepository.save(factura);
    }

    public void eliminarFactura(Long id) {
        Factura factura = buscarPorId(id);

        if (factura.getEstado() == EstadoFactura.PAGADA) {
            throw new NegocioException("No se puede eliminar una factura pagada.");
        }

        factura.setEliminado(true);
        facturaRepository.save(factura);
    }

    public void marcarComoPagada(Long id, FacturaForm form) {
        Factura factura = buscarPorId(id);

        validarFacturaModificable(factura);
        validarDatosPagoCompletos(form);

        factura.setEstado(EstadoFactura.PAGADA);
        factura.setFechaPago(form.getFechaPago());
        factura.setMedioPago(form.getMedioPago());
        factura.setImportePagado(form.getImportePagado());
        factura.setInteresPagado(form.getInteresPagado());

        facturaRepository.save(factura);
    }

    public void anularFactura(Long id) {
        Factura factura = buscarPorId(id);

        if (factura.getEstado() == EstadoFactura.PAGADA) {
            throw new NegocioException("No se puede anular una factura pagada.");
        }

        factura.setEstado(EstadoFactura.ANULADA);
        limpiarDatosPago(factura);

        facturaRepository.save(factura);
    }

    public List<Contrato> listarContratosActivos() {
        return contratoRepository.findByEstadoAndEliminadoFalse(EstadoContrato.ACTIVO);
    }

    private Contrato buscarContratoActivo(Long contratoId) {
        if (contratoId == null) {
            throw new NegocioException("Debe indicar un contrato.");
        }

        Contrato contrato = contratoRepository.findByIdAndEliminadoFalse(contratoId)
                .orElseThrow(() -> new NegocioException("No se encontró el contrato solicitado."));

        if (contrato.getEstado() != EstadoContrato.ACTIVO) {
            throw new NegocioException("Solo se pueden registrar facturas para contratos activos.");
        }

        return contrato;
    }

    private void validarFacturaModificable(Factura factura) {
        if (factura.getEstado() == EstadoFactura.PAGADA) {
            throw new NegocioException("No se puede modificar una factura pagada.");
        }

        if (factura.getEstado() == EstadoFactura.ANULADA) {
            throw new NegocioException("No se puede modificar una factura anulada.");
        }
    }

    private void validarFormBasico(FacturaForm form) {
        if (form == null) {
            throw new NegocioException("Los datos de la factura son obligatorios.");
        }

        if (form.getContratoId() == null) {
            throw new NegocioException("El contrato es obligatorio.");
        }

        if (form.getConcepto() == null || form.getConcepto().trim().isEmpty()) {
            throw new NegocioException("El concepto es obligatorio.");
        }

        if (form.getFechaEmision() == null) {
            throw new NegocioException("La fecha de emisión es obligatoria.");
        }

        if (form.getFechaVencimiento() == null) {
            throw new NegocioException("La fecha de vencimiento es obligatoria.");
        }

        validarImporte(form.getImporte());
    }

    private void validarFechas(FacturaForm form) {
        if (form.getFechaVencimiento().isBefore(form.getFechaEmision())) {
            throw new NegocioException("La fecha de vencimiento debe ser igual o posterior a la fecha de emisión.");
        }
    }

    private void validarImporte(BigDecimal importe) {
        if (importe == null) {
            throw new NegocioException("El importe es obligatorio.");
        }

        if (importe.compareTo(BigDecimal.ZERO) <= 0) {
            throw new NegocioException("El importe debe ser positivo.");
        }
    }

    private void validarDatosPagoCompletos(FacturaForm form) {
        if (form == null) {
            throw new NegocioException("Los datos de pago son obligatorios.");
        }

        if (form.getFechaPago() == null) {
            throw new NegocioException("La fecha de pago es obligatoria para marcar una factura como pagada.");
        }

        if (form.getMedioPago() == null) {
            throw new NegocioException("El medio de pago es obligatorio para marcar una factura como pagada.");
        }

        if (form.getImportePagado() == null) {
            throw new NegocioException("El importe pagado es obligatorio para marcar una factura como pagada.");
        }

        if (form.getImportePagado().compareTo(BigDecimal.ZERO) <= 0) {
            throw new NegocioException("El importe pagado debe ser positivo.");
        }

        if (form.getInteresPagado() != null && form.getInteresPagado().compareTo(BigDecimal.ZERO) < 0) {
            throw new NegocioException("El interés pagado no puede ser negativo.");
        }
    }

    private void validarDatosPagoVacios(FacturaForm form) {
        if (form.getFechaPago() != null
                || form.getMedioPago() != null
                || form.getImportePagado() != null
                || form.getInteresPagado() != null) {
            throw new NegocioException("Los datos de pago deben estar vacíos si la factura no está pagada.");
        }
    }

    private void limpiarDatosPago(Factura factura) {
        factura.setFechaPago(null);
        factura.setMedioPago(null);
        factura.setImportePagado(null);
        factura.setInteresPagado(null);
    }
}