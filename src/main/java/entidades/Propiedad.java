package entidades;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
public class Propiedad {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)

	private Long id;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@NotBlank(message = "La dirección es obligatoria")
	private String direccion;
	

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	@ManyToOne
	@JoinColumn(name = "ciudad_id")
	@NotNull(message = "Debe seleccionar una ciudad")
	private Ciudad ciudad;

	public Ciudad getCiudad() {
		return ciudad;
	}

	public void setCiudad(Ciudad ciudad) {
		this.ciudad = ciudad;
	}

	@Enumerated(EnumType.STRING)
	@NotNull(message = "Debe seleccionar un tipo")
	private TipoPropiedad tipoPropiedad;

	public TipoPropiedad getTipoPropiedad() {
		return tipoPropiedad;
	}

	public void setTipoPropiedad(TipoPropiedad tipoPropiedad) {
		this.tipoPropiedad = tipoPropiedad;
	}
	@NotNull(message = "La cantidad de ambientes es un dato obligatorio")
	@Min(value = 1, message = "La cantidad de ambientes debe ser mayor a cero")
	private Integer cantidadAmbientes;

	public Integer getCantidadAmbientes() {
		return cantidadAmbientes;
	}

	public void setCantidadAmbientes(Integer cantidadAmbientes) {
		this.cantidadAmbientes = cantidadAmbientes;
	}
	
	@NotNull(message = "Debes cargar los metros cuadrados de la propiedad")
	@Min(value = 1, message = "Los metros cuadrados deben ser positivos")
	private Double metrosCuadrados;

	public Double getMetrosCuadrados() {
		return metrosCuadrados;
	}

	public void setMetrosCuadrados(Double metrosCuadrados) {
		this.metrosCuadrados = metrosCuadrados;
	}

	@NotBlank(message = "La descripción es obligatoria")
	private String descripcion;

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	@Enumerated(EnumType.STRING)
	private EstadoDisponibilidad estadoDisponibilidad;

	public EstadoDisponibilidad getEstadoDisponibilidad() {
		return estadoDisponibilidad;
	}

	public void setEstadoDisponibilidad(EstadoDisponibilidad estadoDisponibilidad) {
		this.estadoDisponibilidad = estadoDisponibilidad;
	}

	@ManyToOne
	@JoinColumn(name = "propietario_id")
	@NotNull(message = "Debe seleccionar un propietario")
	private Persona propietario;

	public Persona getPropietario() {
		return propietario;
	}

	public void setPropietario(Persona propietario) {
		this.propietario = propietario;
	}

	private Boolean eliminada = false;

	public Boolean getEliminada() {
		return eliminada;
	}

	public void setEliminada(Boolean eliminada) {
		this.eliminada = eliminada;
	}

	@OneToMany(mappedBy = "propiedad", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<HistorialEstadoPropiedad> historialEstados = new ArrayList<>();

	public List<HistorialEstadoPropiedad> getHistorialEstados() {
		return historialEstados;
	}

	public void setHistorialEstados(List<HistorialEstadoPropiedad> historialEstados) {
		this.historialEstados = historialEstados;
	}

	public Propiedad() {
	}

}
