
package presentacion;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import entidades.Publicacion;
import entidades.EstadoPublicacion;
import entidades.Propiedad;
import entidades.Ciudad;
import servicios.PublicacionService;
import servicios.PropiedadService;
import servicios.CiudadService;

@Controller
@RequestMapping("/publicaciones")
public class PublicacionBuscarController {

    @Autowired
    private PublicacionService publicacionService;

    @Autowired
    private PropiedadService propiedadService;

    @Autowired
    private CiudadService ciudadService;

    // Métodos comunes para rellenar los desplegables de los filtros
    @ModelAttribute("allPropiedades")
    public List<Propiedad> getAllPropiedades() {
        return propiedadService.getAll();
    }

    @ModelAttribute("allCiudades")
    public List<Ciudad> getAllCiudades() {
        return ciudadService.getAll();
    }

    @ModelAttribute("allEstadosPublicacion")
    public EstadoPublicacion[] getAllEstadosPublicacion() {
        return EstadoPublicacion.values();
    }

    // URL base: Muestra la pantalla con la tabla y el formulario de filtros vacío
    @GetMapping
    public String listarYBuscar(Model model) {
        model.addAttribute("formBean", new PublicacionBuscarForm());
        model.addAttribute("publicaciones", publicacionService.getAll());
        return "Publicaciones/listado";
    }

    // Procesa el envío del formulario de filtros
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
}