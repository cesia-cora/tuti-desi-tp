package presentacion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import entidades.Contrato;
import excepciones.Excepcion;
import jakarta.validation.Valid;
import servicios.ContratoServiceImpl;
import servicios.PersonaService;
import servicios.PropiedadService;

@Controller
public class ContratoController {

    @Autowired
    private PropiedadService propiedadService;

    @Autowired
    private PersonaService personaService;

    @Autowired
    private ContratoServiceImpl contratoService;

    @GetMapping("/contratos/nuevo")
    public String mostrarFormularioAlta(Model model) {

        Contrato contrato = new Contrato();

        model.addAttribute("contrato", contrato);
        model.addAttribute("propiedades", propiedadService.getAll());
        model.addAttribute("inquilinos", personaService.getAll());

        return "Contratos/alta";
    }

    @GetMapping("/contratos")
    public String listar(Model model) {

        model.addAttribute("contratos", contratoService.getAll());
        model.addAttribute("propiedades", propiedadService.getAll());
        model.addAttribute("inquilinos", personaService.getAll());
        model.addAttribute("allEstadosContrato", entidades.EstadoContrato.values());
        model.addAttribute("formBean", new presentacion.ContratoBuscarForm());

        return "Contratos/listado";
    }

    @GetMapping("/contratos/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {

        Contrato contrato = contratoService.getById(id);

        model.addAttribute("contrato", contrato);
        model.addAttribute("propiedades", propiedadService.getAll());
        model.addAttribute("inquilinos", personaService.getAll());

        return "Contratos/editar";
    }

    @GetMapping("/contratos/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {

        try {
            contratoService.deleteById(id);
        } catch (Exception e) {
            // en este controlador simplificado redirigimos igual; la vista puede mostrar errores si lo necesita
        }

        return "redirect:/contratos";
    }

    @PostMapping("/contratos/guardar")
    public String guardarContrato(
            @Valid @ModelAttribute("contrato") Contrato contrato,
            BindingResult result,
            Model model) {

        if (result.hasErrors()) {
            model.addAttribute("propiedades", propiedadService.getAll());
            model.addAttribute("inquilinos", personaService.getAll());

            if (contrato.getId() != null) {
                return "Contratos/editar";
            }
            return "Contratos";
        }

        try {
            contratoService.save(contrato);
        } catch (Excepcion e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("propiedades", propiedadService.getAll());
            model.addAttribute("inquilinos", personaService.getAll());

            if (contrato.getId() != null) {
                return "Contratos/editar";
            }
            return "Contratos/alta";
        }

        return "redirect:/contratos/nuevo";
    }
}