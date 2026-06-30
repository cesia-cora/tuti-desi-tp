package entidades;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "contrato")
public class Contrato {
	
	public Contrato() {
		
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "propiedad_id")
	@NotNull(message = "Debe seleccionar una propiedad")
	private Propiedad propiedad;
	
	@ManyToOne
	@JoinColumn(name = "inquilino_id")
	@NotNull(message = "Debe seleccionar un inquilino")
	private Persona inquilino;
	
	@NotNull(message = "Debe haber una fecha de inicio")
	@Column(nullable = false)
	private LocalDateTime fechaInicio;
	
	@Positive(message = "La duración debe ser un valor positivo")
	@Column(nullable = false)
	private int duracionMeses;
	
	@Positive(message = "El importe debe ser un valor positivo")
	@NotNull(message = "El importe no puede estar vacío")
	@Column(nullable = false)
	private double importeMensual;
	
	@Column(nullable = false)
	private LocalDate diaVencMensual;
	
	@NotBlank(message = "La descripción es obligatoria")
	@Column(nullable = false)
	private String descripcion;
	
	@Enumerated(EnumType.STRING)
	private EstadoContrato estadoContrato;
	
	@OneToMany(mappedBy = "contrato", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<HistorialEstadoContrato> historialEstados = new ArrayList<>();
	
	private Boolean eliminada = false;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Propiedad getPropiedad() {
		return propiedad;
	}

	public void setPropiedad(Propiedad propiedad) {
		this.propiedad = propiedad;
	}

	public Persona getInquilino() {
		return inquilino;
	}

	public void setInquilino(Persona inquilino) {
		this.inquilino = inquilino;
	}

	public LocalDateTime getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(LocalDateTime fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public int getDuracionMeses() {
		return duracionMeses;
	}

	public void setDuracionMeses(int duracionMeses) {
		this.duracionMeses = duracionMeses;
	}

	public double getImporteMensual() {
		return importeMensual;
	}

	public void setImporteMensual(double importeMensual) {
		this.importeMensual = importeMensual;
	}

	public LocalDate getDiaVencMensual() {
		return diaVencMensual;
	}

	public void setDiaVencMensual(LocalDate diaVencMensual) {
		this.diaVencMensual = diaVencMensual;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public EstadoContrato getEstadoContrato() {
		return estadoContrato;
	}

	public void setEstadoContrato(EstadoContrato estadoContrato) {
		this.estadoContrato = estadoContrato;
	}
	
	public List<HistorialEstadoContrato> getHistorialEstados() {
		return historialEstados;
	}

	public void setHistorialEstados(List<HistorialEstadoContrato> historialEstados) {
		this.historialEstados = historialEstados;
	}

	public Boolean getEliminada() {
		return eliminada;
	}

	public void setEliminada(Boolean eliminada) {
		this.eliminada = eliminada;
	}

	@Override
	public String toString() {
		return "Contrato [id=" + id + ", propiedad=" + propiedad + ", inquilino=" + inquilino + ", fechaInicio="
				+ fechaInicio + ", duracionMeses=" + duracionMeses + ", importeMensual=" + importeMensual
				+ ", diaVencMensual=" + diaVencMensual + ", descripcion=" + descripcion + ", estadoContrato="
				+ estadoContrato + "]";
	}
	
	
}
