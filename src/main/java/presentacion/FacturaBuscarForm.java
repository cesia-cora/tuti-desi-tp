package presentacion;

import java.time.LocalDate;
import entidades.EstadoFactura;

public class FacturaBuscarForm {
    private Long contratoId;
    private Long propiedadId;
    private Long inquilinoId;
    private EstadoFactura estado;
    private LocalDate fechaDesde;
    private LocalDate fechaHasta;

    public Long getContratoId() { return contratoId; }
    public void setContratoId(Long contratoId) { this.contratoId = contratoId; }
    public Long getPropiedadId() { return propiedadId; }
    public void setPropiedadId(Long propiedadId) { this.propiedadId = propiedadId; }
    public Long getInquilinoId() { return inquilinoId; }
    public void setInquilinoId(Long inquilinoId) { this.inquilinoId = inquilinoId; }
    public EstadoFactura getEstado() { return estado; }
    public void setEstado(EstadoFactura estado) { this.estado = estado; }
    public LocalDate getFechaDesde() { return fechaDesde; }
    public void setFechaDesde(LocalDate fechaDesde) { this.fechaDesde = fechaDesde; }
    public LocalDate getFechaHasta() { return fechaHasta; }
    public void setFechaHasta(LocalDate fechaHasta) { this.fechaHasta = fechaHasta; }
}