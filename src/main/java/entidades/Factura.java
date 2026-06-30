package entidades;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
public class Factura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "contrato_id")
    @NotNull(message = "Debe seleccionar un contrato asociado")
    private Contrato contrato;

    @NotBlank(message = "El concepto facturado es obligatorio")
    private String conceptoFacturado;

    @NotNull(message = "La fecha de emisión es obligatoria")
    private LocalDate fechaEmision;

    @NotNull(message = "La fecha de vencimiento es obligatoria")
    private LocalDate fechaVencimiento;

    @NotNull(message = "El importe es obligatorio")
    @Positive(message = "El importe debe ser un número positivo")
    private BigDecimal importe;

    @Enumerated(EnumType.STRING)
    private EstadoFactura estado;

    private Boolean eliminada = false;

    // --- Atributos de Pago integrados en la Factura por requerimiento de la consigna ---
    private LocalDate fechaPago;

    @Enumerated(EnumType.STRING)
    private MedioPago medioPago;

    @Positive(message = "El importe pagado debe ser positivo")
    private BigDecimal importePagado;

    private BigDecimal interesPagado;

    @OneToMany(mappedBy = "factura", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HistorialEstadoFactura> historialEstados = new ArrayList<>();

    public Factura() {
    }

    // --- Getters y Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Contrato getContrato() { return contrato; }
    public void setContrato(Contrato contrato) { this.contrato = contrato; }

    public String getConceptoFacturado() { return conceptoFacturado; }
    public void setConceptoFacturado(String conceptoFacturado) { this.conceptoFacturado = conceptoFacturado; }

    public LocalDate getFechaEmision() { return fechaEmision; }
    public void setFechaEmision(LocalDate fechaEmision) { this.fechaEmision = fechaEmision; }

    public LocalDate getFechaVencimiento() { return fechaVencimiento; }
    public void setFechaVencimiento(LocalDate fechaVencimiento) { this.fechaVencimiento = fechaVencimiento; }

    public BigDecimal getImporte() { return importe; }
    public void setImporte(BigDecimal importe) { this.importe = importe; }

    public EstadoFactura getEstado() { return estado; }
    public void setEstado(EstadoFactura estado) { this.estado = estado; }

    public Boolean getEliminada() { return eliminada; }
    public void setEliminada(Boolean eliminada) { this.eliminada = eliminada; }

    public LocalDate getFechaPago() { return fechaPago; }
    public void setFechaPago(LocalDate fechaPago) { this.fechaPago = fechaPago; }

    public MedioPago getMedioPago() { return medioPago; }
    public void setMedioPago(MedioPago medioPago) { this.medioPago = medioPago; }

    public BigDecimal getImportePagado() { return importePagado; }
    public void setImportePagado(BigDecimal importePagado) { this.importePagado = importePagado; }

    public BigDecimal getInteresPagado() { return interesPagado; }
    public void setInteresPagado(BigDecimal interesPagado) { this.interesPagado = interesPagado; }

    public List<HistorialEstadoFactura> getHistorialEstados() { return historialEstados; }
    public void setHistorialEstados(List<HistorialEstadoFactura> historialEstados) { this.historialEstados = historialEstados; }
}