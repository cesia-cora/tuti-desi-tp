
package servicios;

import java.util.List;
import entidades.Publicacion;
import excepciones.Excepcion;
import presentacion.PublicacionBuscarForm;

public interface PublicacionService {
    List<Publicacion> getAll();
    Publicacion getById(Long id);
    void save(Publicacion publicacion) throws Excepcion;
    void deleteById(Long id) throws Excepcion;
    List<Publicacion> filter(PublicacionBuscarForm filter) throws Excepcion;
}