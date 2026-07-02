package presentacion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model; // <-- ESTA IMPORTACIÓN ES CRÍTICA PARA RECONOCER EL OBJETO MODEL
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import entidades.EstadoDisponibilidad;
import entidades.Propiedad;
import entidades.TipoPropiedad;
import excepciones.Excepcion;
import jakarta.validation.Valid;
import servicios.CiudadService;
import servicios.PersonaService;
import servicios.PropiedadService;
import org.springframework.web.bind.annotation.RequestParam;
import entidades.Ciudad;

@Controller
public class PropiedadController {

    @Autowired
    private CiudadService ciudadService;

    @Autowired
    private PersonaService personaService;

    @Autowired
    private PropiedadService propiedadService;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/propiedades/nueva")
    public String mostrarFormularioAlta(Model model) {

        Propiedad propiedad = new Propiedad();
        propiedad.setEstadoDisponibilidad(EstadoDisponibilidad.DISPONIBLE);

        model.addAttribute("propiedad", propiedad);
        model.addAttribute("ciudades", ciudadService.getAll());
        model.addAttribute("propietarios", personaService.getAll());
        model.addAttribute("tiposPropiedad", TipoPropiedad.values());
        model.addAttribute("estadosDisponibilidad", EstadoDisponibilidad.values());

        return "Propiedades/alta";
    }

    @GetMapping("/propiedades")
    public String listar(Model model) {

        model.addAttribute("propiedades", propiedadService.getAll());

        return "Propiedades/listado";
    }

    @GetMapping("/propiedades/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {

        Propiedad propiedad = propiedadService.getById(id);

        model.addAttribute("propiedad", propiedad);
        model.addAttribute("ciudades", ciudadService.getAll());
        model.addAttribute("propietarios", personaService.getAll());
        model.addAttribute("tiposPropiedad", TipoPropiedad.values());
        model.addAttribute("estadosDisponibilidad", EstadoDisponibilidad.values());

        return "Propiedades/editar";
    }

    @GetMapping("/propiedades/eliminar/{id}")
    public String eliminar(@PathVariable Long id, Model model) {

        try {
            propiedadService.deleteById(id);
        } catch (Excepcion e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("propiedades", propiedadService.getAll());
            return "Propiedades/listado";
        }

        return "redirect:/propiedades";
    }

    @PostMapping("/propiedades/guardar")
    public String guardarPropiedad(
            @Valid @ModelAttribute("propiedad") Propiedad propiedad,
            BindingResult result,
            Model model) {

        if (result.hasErrors()) {
            model.addAttribute("ciudades", ciudadService.getAll());
            model.addAttribute("propietarios", personaService.getAll());
            model.addAttribute("tiposPropiedad", TipoPropiedad.values());
            model.addAttribute("estadosDisponibilidad", EstadoDisponibilidad.values());

            return "Propiedades/alta";
        }

        try {
            propiedadService.save(propiedad);
        } catch (Excepcion e) {

            model.addAttribute("error", e.getMessage());
            model.addAttribute("ciudades", ciudadService.getAll());
            model.addAttribute("propietarios", personaService.getAll());
            model.addAttribute("tiposPropiedad", TipoPropiedad.values());
            model.addAttribute("estadosDisponibilidad", EstadoDisponibilidad.values());

            if (propiedad.getId() != null) {
                return "Propiedades/editar";
            }

            return "Propiedades/alta";
        }

        return "redirect:/propiedades";
    }
}
