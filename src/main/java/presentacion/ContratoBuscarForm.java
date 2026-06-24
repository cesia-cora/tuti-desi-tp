package presentacion;

import java.time.LocalDate;
import java.util.List;

import entidades.Contrato;
import entidades.EstadoContrato;
import entidades.Persona;
import entidades.Propiedad;

public class ContratoBuscarForm {

	private Long propiedadSeleccionada;
	private Long inquilinoSeleccionado;
	private EstadoContrato estadoSeleccionado;
	private LocalDate fechaInicio;
	
	private List<Propiedad> propiedades;
	private List<Persona> inquilinos;
	
	public Long getPropiedadSeleccionada() {
	    return propiedadSeleccionada;
	}
	
	public void setPropiedadSeleccionada(Long propiedadSeleccionada) {
	    this.propiedadSeleccionada = propiedadSeleccionada;
	}
	
	public Long getInquilinoSeleccionado() {
	    return inquilinoSeleccionado;
	}
	
	public void setInquilinoSeleccionado(Long inquilinoSeleccionado) {
	    this.inquilinoSeleccionado = inquilinoSeleccionado;
	}
	
	public EstadoContrato getEstadoSeleccionado() {
	    return estadoSeleccionado;
	}
	
	public void setEstadoSeleccionado(EstadoContrato estadoSeleccionado) {
	    this.estadoSeleccionado = estadoSeleccionado;
	}
	
	public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }
	
	public List<Propiedad> getPropiedades() {
	    return propiedades;
	}
	
	public void setPropiedades(List<Propiedad> propiedades) {
	    this.propiedades = propiedades;
	}
	
	public List<Persona> getInquilinos() {
	    return inquilinos;
	}
	
	public void setInquilinos(List<Persona> inquilinos) {
	    this.inquilinos = inquilinos;
	}
}