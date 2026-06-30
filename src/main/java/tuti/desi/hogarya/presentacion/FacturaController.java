package tuti.desi.hogarya.presentacion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import tuti.desi.hogarya.entidades.EstadoFactura;
import tuti.desi.hogarya.entidades.Factura;
import tuti.desi.hogarya.entidades.MedioPago;
import tuti.desi.hogarya.excepciones.NegocioException;
import tuti.desi.hogarya.presentacion.formularios.FacturaForm;
import tuti.desi.hogarya.servicios.FacturaService;

@Controller
@RequestMapping("/facturas")
public class FacturaController {

    @Autowired
    private FacturaService facturaService;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("facturas", facturaService.listarFacturas());
        return "facturas/listado";
    }

    @GetMapping("/nueva")
    public String mostrarFormularioAlta(Model model) {
        model.addAttribute("facturaForm", new FacturaForm());
        cargarDatosFormulario(model);
        return "facturas/formulario";
    }

    @PostMapping("/nueva")
    public String procesarFormularioAlta(
            @Valid @ModelAttribute("facturaForm") FacturaForm form,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            cargarDatosFormulario(model);
            return "facturas/formulario";
        }

        try {
            facturaService.crearFactura(form);
            redirectAttributes.addFlashAttribute("mensaje", "La factura fue creada correctamente.");
            return "redirect:/facturas";
        } catch (NegocioException e) {
            model.addAttribute("error", e.getMessage());
            cargarDatosFormulario(model);
            return "facturas/formulario";
        }
    }

    @GetMapping("/{id}/editar")
    public String mostrarFormularioEdicion(
            @PathVariable Long id,
            Model model,
            RedirectAttributes redirectAttributes) {

        try {
            Factura factura = facturaService.buscarPorId(id);
            FacturaForm form = convertirAFacturaForm(factura);

            model.addAttribute("facturaForm", form);
            cargarDatosFormulario(model);

            return "facturas/formulario";
        } catch (NegocioException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/facturas";
        }
    }

    @PostMapping("/{id}/editar")
    public String procesarFormularioEdicion(
            @PathVariable Long id,
            @Valid @ModelAttribute("facturaForm") FacturaForm form,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            form.setId(id);
            cargarDatosFormulario(model);
            return "facturas/formulario";
        }

        try {
            facturaService.modificarFactura(id, form);
            redirectAttributes.addFlashAttribute("mensaje", "La factura fue modificada correctamente.");
            return "redirect:/facturas";
        } catch (NegocioException e) {
            form.setId(id);
            model.addAttribute("error", e.getMessage());
            cargarDatosFormulario(model);
            return "facturas/formulario";
        }
    }

    @PostMapping("/{id}/eliminar")
    public String eliminar(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {

        try {
            facturaService.eliminarFactura(id);
            redirectAttributes.addFlashAttribute("mensaje", "La factura fue eliminada correctamente.");
        } catch (NegocioException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/facturas";
    }

    @PostMapping("/{id}/anular")
    public String anular(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {

        try {
            facturaService.anularFactura(id);
            redirectAttributes.addFlashAttribute("mensaje", "La factura fue anulada correctamente.");
        } catch (NegocioException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/facturas";
    }

    @GetMapping("/{id}/pagar")
    public String mostrarFormularioPago(
            @PathVariable Long id,
            Model model,
            RedirectAttributes redirectAttributes) {

        try {
            Factura factura = facturaService.buscarPorId(id);
            FacturaForm form = convertirAFacturaForm(factura);

            model.addAttribute("facturaForm", form);
            model.addAttribute("mediosPago", MedioPago.values());

            return "facturas/pago";
        } catch (NegocioException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/facturas";
        }
    }

    @PostMapping("/{id}/pagar")
    public String procesarFormularioPago(
            @PathVariable Long id,
            @ModelAttribute("facturaForm") FacturaForm form,
            Model model,
            RedirectAttributes redirectAttributes) {

        try {
            facturaService.marcarComoPagada(id, form);
            redirectAttributes.addFlashAttribute("mensaje", "La factura fue marcada como pagada correctamente.");
            return "redirect:/facturas";
        } catch (NegocioException e) {
            form.setId(id);
            model.addAttribute("error", e.getMessage());
            model.addAttribute("mediosPago", MedioPago.values());
            return "facturas/pago";
        }
    }

    private void cargarDatosFormulario(Model model) {
        model.addAttribute("contratos", facturaService.listarContratosActivos());
        model.addAttribute("estadosFactura", EstadoFactura.values());
        model.addAttribute("mediosPago", MedioPago.values());
    }

    private FacturaForm convertirAFacturaForm(Factura factura) {
        FacturaForm form = new FacturaForm();

        form.setId(factura.getId());

        if (factura.getContrato() != null) {
            form.setContratoId(factura.getContrato().getId());
        }

        form.setConcepto(factura.getConcepto());
        form.setFechaEmision(factura.getFechaEmision());
        form.setFechaVencimiento(factura.getFechaVencimiento());
        form.setImporte(factura.getImporte());
        form.setEstado(factura.getEstado());
        form.setFechaPago(factura.getFechaPago());
        form.setMedioPago(factura.getMedioPago());
        form.setImportePagado(factura.getImportePagado());
        form.setInteresPagado(factura.getInteresPagado());

        return form;
    }
}