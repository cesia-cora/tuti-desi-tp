
package servicios;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import accesoDatos.PublicacionRepo;
import accesoDatos.PropiedadRepo;
import entidades.Publicacion;
import entidades.EstadoPublicacion;
import entidades.EstadoDisponibilidad;
import entidades.HistorialEstadoPublicacion;
import entidades.Propiedad;
import excepciones.Excepcion;
import presentacion.PublicacionBuscarForm;

@Service
public class PublicacionServiceImpl implements PublicacionService {

    @Autowired
    private PublicacionRepo publicacionRepo;

    @Autowired
    private PropiedadRepo propiedadRepo;

    @Override
    public List<Publicacion> getAll() {
        return publicacionRepo.findByEliminadaFalse();
    }

    @Override
    public Publicacion getById(Long id) {
        return publicacionRepo.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public void save(Publicacion publicacion) throws Excepcion {
        boolean esAlta = (publicacion.getId() == null);
        
        // aca vaentidad Propiedad completa para validar reglas
        Propiedad propiedad = propiedadRepo.findById(publicacion.getPropiedad().getId())
                .orElseThrow(() -> new Excepcion("La propiedad seleccionada no existe."));

        // aca va Solo propiedades activas y no eliminadas
        if (propiedad.getEliminada()) {
            throw new Excepcion("No se puede publicar una propiedad que ha sido eliminada.");
        }

        if (esAlta) {
            // Estado por defecto
            publicacion.setEstadoPublicacion(EstadoPublicacion.ACTIVA);

            // Propiedad debe estar DISPONIBLE obligatoriamente al dar de alta
            if (propiedad.getEstadoDisponibilidad() != EstadoDisponibilidad.DISPONIBLE) {
                throw new Excepcion("Solo se puede crear una publicación para una propiedad que se encuentre disponible.");
            }

            //  No mas de una publicación ACTIVA simultánea
            boolean tieneActiva = publicacionRepo.existsByPropiedadIdAndEstadoPublicacionAndEliminadaFalse(
                    propiedad.getId(), EstadoPublicacion.ACTIVA);
            if (tieneActiva) {
                throw new Excepcion("No puede existir más de una publicación activa para la misma propiedad.");
            }

            // Registrar Historial de Estado inicial
            HistorialEstadoPublicacion historial = new HistorialEstadoPublicacion();
            historial.setPublicacion(publicacion);
            historial.setEstadoPublicacion(publicacion.getEstadoPublicacion());
            historial.setFechaChange(LocalDateTime.now());
            publicacion.getHistorialEstados().add(historial);

            publicacionRepo.save(publicacion);

        } else {
            // Flujo de algun cambio
            Publicacion pubActual = publicacionRepo.findById(publicacion.getId())
                    .orElseThrow(() -> new Excepcion("No se encontró la publicación a modificar."));

            //  Las condiciones de alquiler podrán modificarse mientras la publicación no  finalizada.
            if (pubActual.getEstadoPublicacion() == EstadoPublicacion.FINALIZADA 
                && !pubActual.getCondicionesAlquiler().equals(publicacion.getCondicionesAlquiler())) {
                throw new Excepcion("Las condiciones de alquiler no pueden modificarse si la publicación ya está finalizada.");
            }

            //   pasa a activa verificar unicidad de estado ....activa excluyendo esta misma publicación
            if (publicacion.getEstadoPublicacion() == EstadoPublicacion.ACTIVA) {
                if (propiedad.getEstadoDisponibilidad() != EstadoDisponibilidad.DISPONIBLE) {
                    throw new Excepcion("Solo se puede activar una publicación si la propiedad asociada se encuentra disponible.");
                }
                boolean tieneActivaOtra = publicacionRepo.existsByPropiedadIdAndEstadoPublicacionAndEliminadaFalseAndIdNot(
                        propiedad.getId(), EstadoPublicacion.ACTIVA, publicacion.getId());
                if (tieneActivaOtra) {
                    throw new Excepcion("No se puede activar esta publicación porque ya existe otra publicación activa para la misma propiedad.");
                }
            }

            // conserva historial ante cambios de estado
            EstadoPublicacion estadoAnterior = pubActual.getEstadoPublicacion();
            
            pubActual.setPrecioMensual(publicacion.getPrecioMensual());
            pubActual.setCondicionesAlquiler(publicacion.getCondicionesAlquiler());
            pubActual.setDescripcion(publicacion.getDescripcion());
            pubActual.setFechaPublicacion(publicacion.getFechaPublicacion());
            pubActual.setEstadoPublicacion(publicacion.getEstadoPublicacion());

            if (estadoAnterior != publicacion.getEstadoPublicacion()) {
                HistorialEstadoPublicacion historial = new HistorialEstadoPublicacion();
                historial.setPublicacion(pubActual);
                historial.setEstadoPublicacion(publicacion.getEstadoPublicacion());
                historial.setFechaCambio(LocalDateTime.now());
                pubActual.getHistorialEstados().add(historial);
            }

            publicacionRepo.save(pubActual);
        }
    }

    @Override
    @Transactional
    public void deleteById(Long id) throws Excepcion {
        Publicacion publicacion = getById(id);
        if (publicacion == null) {
            throw new Excepcion("La publicación a eliminar no existe.");
        }
        
        //  solo pueden eliminarse publicaciones activas
        if (publicacion.getEstadoPublicacion() != EstadoPublicacion.ACTIVA) {
            throw new Excepcion("Solo pueden eliminarse publicaciones en estado ACTIVA.");
        }

        // eliminacion logica sin comprometer datos historicos ni la entidad propiedad
        publicacion.setEliminada(true);
        publicacionRepo.save(publicacion);
    }

    @Override
    public List<Publicacion> filter(PublicacionBuscarForm filter) throws Excepcion {
        if (filter.getPropiedadSeleccionada() == null 
            && filter.getCiudadSeleccionada() == null 
            && filter.getEstadoSeleccionado() == null 
            && filter.getPrecioMinimo() == null 
            && filter.getPrecioMaximo() == null) {
            return publicacionRepo.findByEliminadaFalse();
        }
        return publicacionRepo.filtrar(
                filter.getPropiedadSeleccionada(),
                filter.getCiudadSeleccionada(),
                filter.getEstadoSeleccionado(),
                filter.getPrecioMinimo(),
                filter.getPrecioMaximo()
        );
    }
}