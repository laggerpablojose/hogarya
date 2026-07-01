package tuti.desi.hogarya.presentacion;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import tuti.desi.hogarya.entidades.EstadoPropiedad;
import tuti.desi.hogarya.entidades.Propiedad;
import tuti.desi.hogarya.entidades.TipoPropiedad;
import tuti.desi.hogarya.excepciones.NegocioException;
import tuti.desi.hogarya.presentacion.formularios.PropiedadForm;
import tuti.desi.hogarya.servicios.PropiedadService;

@Controller
@RequestMapping("/propiedades")
public class PropiedadController {

    private final PropiedadService propiedadService;

    public PropiedadController(PropiedadService propiedadService) {
        this.propiedadService = propiedadService;
    }

    // HU 1.4: Listado con filtros opcionales
    @GetMapping
    public String listar(
            @RequestParam(required = false) String direccion,
            @RequestParam(required = false) String ciudad,
            @RequestParam(required = false) TipoPropiedad tipo,
            @RequestParam(required = false) EstadoPropiedad estado,
            Model model) {

        model.addAttribute("propiedades",
                propiedadService.listarPropiedadesFiltradas(direccion, ciudad, tipo, estado));
        model.addAttribute("tiposPropiedad", TipoPropiedad.values());
        model.addAttribute("estadosPropiedad", EstadoPropiedad.values());
        model.addAttribute("filtroDireccion", direccion);
        model.addAttribute("filtroCiudad", ciudad);
        model.addAttribute("filtroTipo", tipo);
        model.addAttribute("filtroEstado", estado);
        return "propiedades/listado";
    }

    // HU 1.1: Mostrar formulario de alta
    @GetMapping("/nueva")
    public String mostrarFormularioAlta(Model model) {
        model.addAttribute("propiedadForm", new PropiedadForm());
        cargarDatosFormulario(model);
        return "propiedades/formulario";
    }

    // HU 1.1: Procesar alta
    @PostMapping("/nueva")
    public String procesarFormularioAlta(
            @Valid @ModelAttribute("propiedadForm") PropiedadForm form,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            cargarDatosFormulario(model);
            return "propiedades/formulario";
        }

        try {
            propiedadService.crearPropiedad(form);
            redirectAttributes.addFlashAttribute("mensaje", "La propiedad fue registrada correctamente.");
            return "redirect:/propiedades";
        } catch (NegocioException e) {
            model.addAttribute("error", e.getMessage());
            cargarDatosFormulario(model);
            return "propiedades/formulario";
        }
    }

    // HU 1.3: Mostrar formulario de edición
    @GetMapping("/{id}/editar")
    public String mostrarFormularioEdicion(
            @PathVariable Long id,
            Model model,
            RedirectAttributes redirectAttributes) {

        try {
            Propiedad propiedad = propiedadService.buscarPorId(id);
            PropiedadForm form = convertirAForm(propiedad);
            model.addAttribute("propiedadForm", form);
            cargarDatosFormulario(model);
            return "propiedades/formulario";
        } catch (NegocioException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/propiedades";
        }
    }

    // HU 1.3: Procesar edición
    @PostMapping("/{id}/editar")
    public String procesarFormularioEdicion(
            @PathVariable Long id,
            @Valid @ModelAttribute("propiedadForm") PropiedadForm form,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            form.setId(id);
            cargarDatosFormulario(model);
            return "propiedades/formulario";
        }

        try {
            propiedadService.modificarPropiedad(id, form);
            redirectAttributes.addFlashAttribute("mensaje", "La propiedad fue modificada correctamente.");
            return "redirect:/propiedades";
        } catch (NegocioException e) {
            form.setId(id);
            model.addAttribute("error", e.getMessage());
            cargarDatosFormulario(model);
            return "propiedades/formulario";
        }
    }

    // HU 1.2: Eliminar (baja lógica)
    @PostMapping("/{id}/eliminar")
    public String eliminar(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {

        try {
            propiedadService.eliminarPropiedad(id);
            redirectAttributes.addFlashAttribute("mensaje", "La propiedad fue eliminada correctamente.");
        } catch (NegocioException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/propiedades";
    }

    private void cargarDatosFormulario(Model model) {
        model.addAttribute("tiposPropiedad", TipoPropiedad.values());
        model.addAttribute("estadosPropiedad", EstadoPropiedad.values());
        model.addAttribute("propietarios", propiedadService.listarPropietariosDisponibles());
    }

    private PropiedadForm convertirAForm(Propiedad propiedad) {
        PropiedadForm form = new PropiedadForm();
        form.setId(propiedad.getId());
        form.setDireccion(propiedad.getDireccion());
        form.setCiudad(propiedad.getCiudad());
        form.setTipo(propiedad.getTipo());
        form.setAmbientes(propiedad.getAmbientes());
        form.setMetrosCuadrados(propiedad.getMetrosCuadrados());
        form.setDescripcion(propiedad.getDescripcion());
        form.setEstado(propiedad.getEstado());
        if (propiedad.getPropietario() != null) {
            form.setPropietarioId(propiedad.getPropietario().getId());
        }
        return form;
    }
}
