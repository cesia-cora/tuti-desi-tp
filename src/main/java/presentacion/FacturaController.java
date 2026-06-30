package presentacion;

import java.math.BigDecimal;
import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import entidades.Factura;
import entidades.EstadoFactura;
import entidades.MedioPago;
import excepciones.Excepcion;
import jakarta.validation.Valid;
import servicios.FacturaService;
import servicios.ContratoServiceImpl;
import servicios.PropiedadService;
import servicios.PersonaService;

@Controller
@RequestMapping("/facturas")
public class FacturaController {

    @Autowired
    private FacturaService facturaService;
    @Autowired
    private ContratoServiceImpl contratoService;
    @Autowired
    private PropiedadService propiedadService;
    @Autowired
    private PersonaService personaService;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("facturas", facturaService.getAll());
        model.addAttribute("contratos", contratoService.getAll());
        model.addAttribute("propiedades", propiedadService.getAll());
        model.addAttribute("inquilinos", personaService.getAll());
        model.addAttribute("estados", EstadoFactura.values());
        model.addAttribute("formBean", new FacturaBuscarForm());
        return "Facturas/listado";
    }

    @GetMapping("/nueva")
    public String nuevaFacturaForm(Model model) {
        Factura f = new Factura();
        f.setFechaEmision(LocalDate.now());
        model.addAttribute("factura", f);
        model.addAttribute("contratos", contratoService.getAll());
        return "Facturas/alta";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("factura") Factura factura, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("contratos", contratoService.getAll());
            return "Facturas/alta";
        }
        try {
            facturaService.save(factura);
        } catch (Excepcion e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("contratos", contratoService.getAll());
            return "Facturas/alta";
        }
        return "redirect:/facturas";
    }

    @GetMapping("/pagar/{id}")
    public String pagarForm(@PathVariable Long id, Model model) {
        Factura f = facturaService.getById(id);
        model.addAttribute("factura", f);
        model.addAttribute("mediosPago", MedioPago.values());
        return "Facturas/pagar";
    }

    @PostMapping("/pagar/confirmar")
    public String confirmarPago(@RequestParam Long id, 
                                @RequestParam String fechaPago,
                                @RequestParam MedioPago medioPago, 
                                @RequestParam BigDecimal importePagado,
                                @RequestParam(required = false) BigDecimal interesPagado, 
                                Model model) {
        try {
            LocalDate fecha = LocalDate.parse(fechaPago);
            facturaService.registrarPago(id, fecha, medioPago, importePagado, interesPagado);
        } catch (Exception e) {
            Factura f = facturaService.getById(id);
            model.addAttribute("factura", f);
            model.addAttribute("mediosPago", MedioPago.values());
            model.addAttribute("error", e.getMessage());
            return "Facturas/pagar";
        }
        return "redirect:/facturas";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, Model model) {
        try {
            facturaService.deleteById(id);
        } catch (Excepcion e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("facturas", facturaService.getAll());
            model.addAttribute("contratos", contratoService.getAll());
            model.addAttribute("propiedades", propiedadService.getAll());
            model.addAttribute("inquilinos", personaService.getAll());
            model.addAttribute("estados", EstadoFactura.values());
            model.addAttribute("formBean", new FacturaBuscarForm());
            return "Facturas/listado";
        }
        return "redirect:/facturas";
    }

    @PostMapping("/buscar")
    public String buscar(@ModelAttribute("formBean") FacturaBuscarForm formBean, Model model) {
        model.addAttribute("facturas", facturaService.filter(formBean));
        model.addAttribute("contratos", contratoService.getAll());
        model.addAttribute("propiedades", propiedadService.getAll());
        model.addAttribute("inquilinos", personaService.getAll());
        model.addAttribute("estados", EstadoFactura.values());
        return "Facturas/listado";
    }
}
