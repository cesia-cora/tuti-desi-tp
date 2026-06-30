package presentacion;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import entidades.Ciudad;
import entidades.EstadoDisponibilidad;
import entidades.Propiedad;
import entidades.TipoPropiedad;
import excepciones.Excepcion;
import jakarta.validation.Valid;
import servicios.CiudadService;
import servicios.PropiedadService;

@Controller
@RequestMapping("/propiedades/buscar")
public class PropiedadBuscarController {

    @Autowired
    private CiudadService ciudadService;

    @Autowired
    private PropiedadService propiedadService;

    @RequestMapping(method = RequestMethod.GET)
    public String preparaForm(Model modelo) {

        PropiedadBuscarForm form = new PropiedadBuscarForm();
        form.setCiudades(ciudadService.getAll());

        modelo.addAttribute("formBean", form);

        return "Propiedades/buscar";
    }

    @ModelAttribute("allCiudades")
    public List<Ciudad> getAllCiudades() {
        return ciudadService.getAll();
    }

    @ModelAttribute("allTiposPropiedad")
    public TipoPropiedad[] getAllTiposPropiedad() {
        return TipoPropiedad.values();
    }

    @ModelAttribute("allEstadosDisponibilidad")
    public EstadoDisponibilidad[] getAllEstadosDisponibilidad() {
        return EstadoDisponibilidad.values();
    }

    @RequestMapping(method = RequestMethod.POST)
    public String submit(
            @ModelAttribute("formBean") @Valid PropiedadBuscarForm formBean,
            BindingResult result,
            ModelMap modelo,
            @RequestParam String action) throws Excepcion {

        if (action.equals("actionBuscar")) {

            try {
                List<Propiedad> propiedades = propiedadService.filter(formBean);
                modelo.addAttribute("resultados", propiedades);
            } catch (Exception e) {
                ObjectError error = new ObjectError("globalError", e.getMessage());
                result.addError(error);
            }

            modelo.addAttribute("formBean", formBean);
            return "Propiedades/buscar";
        }

        else if (action.equals("actionCancelar")) {
            modelo.clear();
            return "redirect:/";
        }

        else if (action.equals("actionRegistrar")) {
            modelo.clear();
            return "redirect:/propiedades/nueva";
        }

        return "redirect:/propiedades";
    }
}