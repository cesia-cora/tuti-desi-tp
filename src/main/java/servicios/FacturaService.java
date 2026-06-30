package servicios;

import java.util.List;
import entidades.Factura;
import excepciones.Excepcion;
import presentacion.FacturaBuscarForm;

public interface FacturaService {
    List<Factura> getAll();
    Factura getById(Long id);
    void save(Factura factura) throws Excepcion;
    void registrarPago(Long id, java.time.LocalDate fecha, entidades.MedioPago medio, java.math.BigDecimal importe, java.math.BigDecimal interes) throws Excepcion;
    void deleteById(Long id) throws Excepcion;
    List<Factura> filter(FacturaBuscarForm filter);
}