package servicios;

import java.util.List;

import entidades.Propiedad;

import entidades.EstadoDisponibilidad;

import excepciones.Excepcion;
import presentacion.PropiedadBuscarForm;

public interface PropiedadService {

	List<Propiedad> getAll();

	Propiedad getById(Long id);

	void save(Propiedad propiedad) throws Excepcion;

	void deleteById(Long id) throws Excepcion;

	List<Propiedad> filter(PropiedadBuscarForm filter) throws Excepcion;
}
