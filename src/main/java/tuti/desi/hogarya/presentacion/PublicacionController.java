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
import tuti.desi.hogarya.entidades.EstadoPublicacion;
import tuti.desi.hogarya.entidades.Publicacion;
import tuti.desi.hogarya.excepciones.NegocioException;
import tuti.desi.hogarya.presentacion.formularios.PublicacionForm;
import tuti.desi.hogarya.servicios.PublicacionService;

@Controller
@RequestMapping("/publicaciones")
public class PublicacionController {

    @Autowired
    private PublicacionService publicacionService;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("publicaciones", publicacionService.listarPublicaciones());
        return "publicaciones/listado";
    }

    @GetMapping("/nueva")
    public String mostrarFormularioAlta(Model model) {
        model.addAttribute("publicacionForm", new PublicacionForm());
        cargarDatosFormulario(model);
        return "publicaciones/formulario";
    }

    @PostMapping("/nueva")
    public String procesarFormularioAlta(
            @Valid @ModelAttribute("publicacionForm") PublicacionForm form,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            cargarDatosFormulario(model);
            return "publicaciones/formulario";
        }

        try {
            publicacionService.crearPublicacion(form);
            redirectAttributes.addFlashAttribute("mensaje", "La publicación fue creada correctamente.");
            return "redirect:/publicaciones";
        } catch (NegocioException e) {
            model.addAttribute("error", e.getMessage());
            cargarDatosFormulario(model);
            return "publicaciones/formulario";
        }
    }

    @GetMapping("/{id}/editar")
    public String mostrarFormularioEdicion(
            @PathVariable Long id,
            Model model,
            RedirectAttributes redirectAttributes) {

        try {
            Publicacion publicacion = publicacionService.buscarPorId(id);
            PublicacionForm form = convertirAPublicacionForm(publicacion);

            model.addAttribute("publicacionForm", form);
            cargarDatosFormulario(model);

            return "publicaciones/formulario";
        } catch (NegocioException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/publicaciones";
        }
    }

    @PostMapping("/{id}/editar")
    public String procesarFormularioEdicion(
            @PathVariable Long id,
            @Valid @ModelAttribute("publicacionForm") PublicacionForm form,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            form.setId(id);
            cargarDatosFormulario(model);
            return "publicaciones/formulario";
        }

        try {
            publicacionService.modificarPublicacion(id, form);
            redirectAttributes.addFlashAttribute("mensaje", "La publicación fue modificada correctamente.");
            return "redirect:/publicaciones";
        } catch (NegocioException e) {
            form.setId(id);
            model.addAttribute("error", e.getMessage());
            cargarDatosFormulario(model);
            return "publicaciones/formulario";
        }
    }

    @PostMapping("/{id}/eliminar")
    public String eliminar(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {

        try {
            publicacionService.eliminarPublicacion(id);
            redirectAttributes.addFlashAttribute("mensaje", "La publicación fue eliminada correctamente.");
        } catch (NegocioException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/publicaciones";
    }

    private void cargarDatosFormulario(Model model) {
        model.addAttribute("propiedades", publicacionService.listarPropiedadesDisponiblesParaPublicacion());
        model.addAttribute("estadosPublicacion", EstadoPublicacion.values());
    }

    private PublicacionForm convertirAPublicacionForm(Publicacion publicacion) {
        PublicacionForm form = new PublicacionForm();

        form.setId(publicacion.getId());

        if (publicacion.getPropiedad() != null) {
            form.setPropiedadId(publicacion.getPropiedad().getId());
        }

        form.setPrecioMensualAlquiler(publicacion.getPrecioMensualAlquiler());
        form.setCondiciones(publicacion.getCondiciones());
        form.setDescripcion(publicacion.getDescripcion());
        form.setFechaPublicacion(publicacion.getFechaPublicacion());
        form.setEstadoPublicacion(publicacion.getEstadoPublicacion());

        return form;
    }
}