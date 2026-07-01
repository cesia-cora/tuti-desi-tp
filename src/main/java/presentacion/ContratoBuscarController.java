package presentacion;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import entidades.Contrato;
import entidades.EstadoContrato;
import entidades.Persona;
import entidades.Propiedad;
import excepciones.Excepcion;
import jakarta.validation.Valid;
import servicios.ContratoServiceImpl;
import servicios.PersonaService;
import servicios.PropiedadService;

@Controller
@RequestMapping("/contratos/buscar")
public class ContratoBuscarController {

    @Autowired
    private PropiedadService propiedadService;

    @Autowired
    private PersonaService personaService;

    @Autowired
    private ContratoServiceImpl contratoService;

    @RequestMapping(method = RequestMethod.GET)
    public String preparaForm(Model modelo) {

        ContratoBuscarForm form = new ContratoBuscarForm();
        form.setPropiedades(propiedadService.getAll());
        form.setInquilinos(personaService.getAll());

        modelo.addAttribute("formBean", form);
        modelo.addAttribute("propiedades", propiedadService.getAll());
        modelo.addAttribute("inquilinos", personaService.getAll());
        modelo.addAttribute("allEstadosContrato", entidades.EstadoContrato.values());

        modelo.addAttribute("formBean", form);

        return "Contratos/buscar";
    }

    @ModelAttribute("allPropiedades")
    public List<Propiedad> getAllPropiedades() {
        return propiedadService.getAll();
    }

    @ModelAttribute("allInquilinos")
    public List<Persona> getAllInquilinos() {
        return personaService.getAll();
    }

    @ModelAttribute("allEstadosContrato")
    public EstadoContrato[] getAllEstadosContrato() {
        return EstadoContrato.values();
    }

    @RequestMapping(method = RequestMethod.POST)
    public String submit(
            @ModelAttribute("formBean") @Valid ContratoBuscarForm formBean,
            BindingResult result,
            ModelMap modelo,
            @RequestParam String action) throws Excepcion {

        if (action.equals("actionBuscar")) {

            try {
                var resultados = contratoService.filter(formBean);
                modelo.addAttribute("resultados", resultados);
            } catch (Exception e) {
                ObjectError error = new ObjectError("globalError", e.getMessage());
                result.addError(error);
            }

            modelo.addAttribute("formBean", formBean);
            return "Contratos/buscar";
        }

        else if (action.equals("actionCancelar")) {
            modelo.clear();
            return "redirect:/";
        }

        else if (action.equals("actionRegistrar")) {
            modelo.clear();
            return "redirect:/contratos/nuevo";
        }

        return "redirect:/contratos";
    }
}