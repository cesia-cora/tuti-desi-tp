
package presentacion;

import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import entidades.Publicacion;
import entidades.EstadoPublicacion;
import entidades.Propiedad;
import entidades.Ciudad;
import excepciones.Excepcion;
import jakarta.validation.Valid;
import servicios.PublicacionService;
import servicios.PropiedadService;
import servicios.CiudadService;

@Controller
@RequestMapping("/publicaciones")
public class PublicacionController {

    @Autowired
    private PublicacionService publicacionService;

    @Autowired
    private PropiedadService propiedadService;

    @Autowired
    private CiudadService ciudadService;

    @ModelAttribute("allPropiedades")
    public List<Propiedad> getAllPropiedades() {
        return propiedadService.getAll(); // devuelve las no eliminadas automaticamente
    }

    @ModelAttribute("allCiudades")
    public List<Ciudad> getAllCiudades() {
        return ciudadService.getAll();
    }

    @ModelAttribute("allEstadosPublicacion")
    public EstadoPublicacion[] getAllEstadosPublicacion() {
        return EstadoPublicacion.values();
    }

    // lista y pantalla de buscaa integrada
    @GetMapping
    public String listarYBuscar(Model model) {
        model.addAttribute("formBean", new PublicacionBuscarForm());
        model.addAttribute("publicaciones", publicacionService.getAll());
        return "Publicaciones/listado";
    }

    @PostMapping("/filtrar")
    public String filtrar(@ModelAttribute("formBean") PublicacionBuscarForm formBean, Model model) {
        try {
            List<Publicacion> filtradas = publicacionService.filter(formBean);
            model.addAttribute("publicaciones", filtradas);
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        model.addAttribute("formBean", formBean);
        return "Publicaciones/listado";
    }

    // alta de publicaciones
    @GetMapping("/nueva")
    public String mostrarFormularioAlta(Model model) {
        Publicacion publicacion = new Publicacion();
        publicacion.setFechaPublicacion(LocalDate.now());
        model.addAttribute("publicacion", publicacion);
        return "Publicaciones/alta";
    }

    // edición de publicaciones
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        Publicacion publicacion = publicacionService.getById(id);
        model.addAttribute("publicacion", publicacion);
        return "Publicaciones/editar";
    }

    // procesamiento unificado de persistencia guarda y actualiza
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

    // elimina logica
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, Model model) {
        try {
            publicacionService.deleteById(id);
        } catch (Excepcion e) {
            // si hay error de negocio, pinta el listado va la explicación del motivo
            model.addAttribute("error", e.getMessage());
            model.addAttribute("formBean", new PublicacionBuscarForm());
            model.addAttribute("publicaciones", publicacionService.getAll());
            return "Publicaciones/listado";
        }
        return "redirect:/publicaciones";
    }
}