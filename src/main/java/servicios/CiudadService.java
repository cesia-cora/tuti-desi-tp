package servicios;

import java.util.List;

import entidades.Ciudad;

public interface CiudadService {

	List<Ciudad> getAll();
	Ciudad save(Ciudad ciudad);
	Ciudad getById(Long id);
}
