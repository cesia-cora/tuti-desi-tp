
//Esto es para cargar el desplegable de Ciudad en el formulario de alta de propiedad.

package servicios;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import accesoDatos.CiudadRepo;
import entidades.Ciudad;

@Service
public class CiudadServiceImpl implements CiudadService {

    @Autowired
    private CiudadRepo ciudadRepo;

    @Override
    public List<Ciudad> getAll() {
        return ciudadRepo.findAll();
    }

    @Override
    public Ciudad save(Ciudad ciudad) {
        return ciudadRepo.save(ciudad);
    }

    @Override
    public Ciudad getById(Long id) {
        return ciudadRepo.findById(id).orElse(null);
    }
}