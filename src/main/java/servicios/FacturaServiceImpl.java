package servicios;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import accesoDatos.FacturaRepo;
import accesoDatos.ContratoRepo;
import entidades.Factura;
import entidades.Contrato;
import entidades.EstadoContrato;
import entidades.EstadoFactura;
import entidades.HistorialEstadoFactura;
import excepciones.Excepcion;
import presentacion.FacturaBuscarForm;

@Service
public class FacturaServiceImpl implements FacturaService {

    @Autowired
    private FacturaRepo facturaRepo;

    @Autowired
    private ContratoRepo contratoRepo;

    @Override
    public List<Factura> getAll() {
        return facturaRepo.findByEliminadaFalse();
    }

    @Override
    public Factura getById(Long id) {
        return facturaRepo.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public void save(Factura factura) throws Excepcion {
        boolean esAlta = factura.getId() == null;

        if (esAlta) {
            Contrato con = contratoRepo.findById(factura.getContrato().getId()).orElse(null);
            if (con == null || Boolean.TRUE.equals(con.getEliminada())) {
                throw new Excepcion("El contrato asociado no existe.");
            }
            if (con.getEstadoContrato() != EstadoContrato.ACTIVO) {
                throw new Excepcion("No se puede facturar sobre un contrato que no se encuentre ACTIVO.");
            }
            if (factura.getFechaVencimiento().isBefore(factura.getFechaEmision())) {
                throw new Excepcion("La fecha de vencimiento no puede ser anterior a la de emisión.");
            }

            factura.setEstado(EstadoFactura.PENDIENTE);

            HistorialEstadoFactura hist = new HistorialEstadoFactura();
            hist.setFactura(factura);
            hist.setEstado(EstadoFactura.PENDIENTE);
            hist.setFechaHora(LocalDateTime.now());
            factura.getHistorialEstados().add(hist);

            facturaRepo.save(factura);
        } else {
            Factura actual = facturaRepo.findById(factura.getId()).orElse(null);
            if (actual == null) throw new Excepcion("Factura no encontrada.");
            if (actual.getEstado() == EstadoFactura.ANULADA || actual.getEstado() == EstadoFactura.PAGADA) {
                throw new Excepcion("No se puede modificar una factura en estado " + actual.getEstado());
            }

            actual.setConceptoFacturado(factura.getConceptoFacturado());
            actual.setFechaEmision(factura.getFechaEmision());
            actual.setFechaVencimiento(factura.getFechaVencimiento());
            actual.setImporte(factura.getImporte());
            facturaRepo.save(actual);
        }
    }

    @Override
    @Transactional
    public void registrarPago(Long id, java.time.LocalDate fecha, entidades.MedioPago medio, BigDecimal importe, BigDecimal interes) throws Excepcion {
        Factura f = facturaRepo.findById(id).orElse(null);
        if (f == null) throw new Excepcion("Factura inexistente.");
        if (f.getEstado() == EstadoFactura.PAGADA) throw new Excepcion("La factura ya se encuentra pagada.");
        if (f.getEstado() == EstadoFactura.ANULADA) throw new Excepcion("La factura está anulada.");
        if (importe == null || f.getImporte().compareTo(importe) != 0) {
            throw new Excepcion("El importe pagado debe ser de exactamente: $" + f.getImporte());
        }

        f.setFechaPago(fecha);
        f.setMedioPago(medio);
        f.setImportePagado(importe);
        f.setInteresPagado(interes == null ? BigDecimal.ZERO : interes);
        f.setEstado(EstadoFactura.PAGADA);

        HistorialEstadoFactura hist = new HistorialEstadoFactura();
        hist.setFactura(f);
        hist.setEstado(EstadoFactura.PAGADA);
        hist.setFechaHora(LocalDateTime.now());
        f.getHistorialEstados().add(hist);

        facturaRepo.save(f);
    }

    @Override
    @Transactional
    public void deleteById(Long id) throws Excepcion {
        Factura f = facturaRepo.findById(id).orElse(null);
        if (f == null) throw new Excepcion("Factura no encontrada.");
        if (f.getEstado() == EstadoFactura.PAGADA) {
            throw new Excepcion("No se puede anular ni eliminar una factura que ya ha sido PAGADA.");
        }
        f.setEliminada(true);
        facturaRepo.save(f);
    }

    @Override
    public List<Factura> filter(FacturaBuscarForm filter) {
        return facturaRepo.filtrar(
            filter.getContratoId(),
            filter.getPropiedadId(),
            filter.getInquilinoId(),
            filter.getEstado(),
            filter.getFechaDesde(),
            filter.getFechaHasta()
        );
    }
}