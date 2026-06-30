package presentacion;

import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import entidades.Publicacion;
import entidades.EstadoPublicacion;
import entidades.Propiedad;
import excepciones.Excepcion;
import jakarta.validation.Valid;
import servicios.PublicacionService;
import servicios.PropiedadService;

@Controller
@RequestMapping("/publicaciones")
public class PublicacionController {

    @Autowired
    private PublicacionService publicacionService;

    @Autowired
    private PropiedadService propiedadService;

    @ModelAttribute("allPropiedades")
    public List<Propiedad> getAllPropiedades() {
        return propiedadService.getAll(); 
    }

    @ModelAttribute("allEstadosPublicacion")
    public EstadoPublicacion[] getAllEstadosPublicacion() {
        return EstadoPublicacion.values();
    }

    // Muestra el formulario de Alta vacío
    @GetMapping("/nueva")
    public String mostrarFormularioAlta(Model model) {
        Publicacion publicacion = new Publicacion();
        publicacion.setFechaPublicacion(LocalDate.now());
        model.addAttribute("publicacion", publicacion);
        return "Publicaciones/alta";
    }

    // Muestra el formulario de Edición con datos precargados
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        Publicacion publicacion = publicacionService.getById(id);
        model.addAttribute("publicacion", publicacion);
        return "Publicaciones/editar";
    }

    // Procesa la persistencia del Alta o la Modificación
    @PostMapping("/guardar")
    public String guardarPublicacion(
            @Valid @ModelAttribute("publicacion") Publicacion publicacion,
            BindingResult result,
            Model model) {

        if (result.hasErrors()) {
            if (publicacion.getId() != null) return "Publicaciones/editar";
            return "Publicaciones/alta";
        }

        try {
            publicacionService.save(publicacion);
        } catch (Excepcion e) {
            model.addAttribute("error", e.getMessage());
            if (publicacion.getId() != null) return "Publicaciones/editar";
            return "Publicaciones/alta";
        }

        return "redirect:/publicaciones";
    }

    // Ejecuta la baja lógica redirigiendo o volviendo con un mensaje de error
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, Model model) {
        try {
            publicacionService.deleteById(id);
        } catch (Excepcion e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("formBean", new PublicacionBuscarForm());
            model.addAttribute("publicaciones", publicacionService.getAll());
            return "Publicaciones/listado";
        }
        return "redirect:/publicaciones";
    }
}