package servicios;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import accesoDatos.PropiedadRepo;
import entidades.EstadoDisponibilidad;
import entidades.HistorialEstadoPropiedad;
import entidades.Propiedad;
import excepciones.Excepcion;
import presentacion.PropiedadBuscarForm;

@Service
public class PropiedadServiceImpl implements PropiedadService {

    @Autowired
    private PropiedadRepo propiedadRepo;

    @Override
    public List<Propiedad> getAll() {
        return propiedadRepo.findByEliminadaFalse();
    }

    @Override
    public Propiedad getById(Long id) {
        return propiedadRepo.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public void save(Propiedad propiedad) throws Excepcion {

        if (propiedad.getEstadoDisponibilidad() == null) {
            propiedad.setEstadoDisponibilidad(EstadoDisponibilidad.DISPONIBLE);
        }

        boolean esAlta = propiedad.getId() == null;
        boolean existe;

        if (esAlta) {
            existe = propiedadRepo.existsByDireccionAndCiudadIdAndEliminadaFalse(
                    propiedad.getDireccion(),
                    propiedad.getCiudad().getId());

            if (existe) {
                throw new Excepcion("Ya existe una propiedad activa con la misma dirección y ciudad.");
            }

            HistorialEstadoPropiedad historial = new HistorialEstadoPropiedad();
            historial.setPropiedad(propiedad);
            historial.setEstadoDisponibilidad(propiedad.getEstadoDisponibilidad());
            historial.setFechaCambio(LocalDateTime.now());

            propiedad.getHistorialEstados().add(historial);

            propiedadRepo.save(propiedad);

        } else {

            Propiedad propiedadActual = propiedadRepo.findById(propiedad.getId()).orElse(null);

            if (propiedadActual == null) {
                throw new Excepcion("No se encontró la propiedad a modificar.");
            }

            existe = propiedadRepo.existsByDireccionAndCiudadIdAndEliminadaFalseAndIdNot(
                    propiedad.getDireccion(),
                    propiedad.getCiudad().getId(),
                    propiedad.getId());

            if (existe) {
                throw new Excepcion("Ya existe una propiedad activa con la misma dirección y ciudad.");
            }

            EstadoDisponibilidad estadoAnterior = propiedadActual.getEstadoDisponibilidad();

            propiedadActual.setDireccion(propiedad.getDireccion());
            propiedadActual.setCiudad(propiedad.getCiudad());
            propiedadActual.setTipoPropiedad(propiedad.getTipoPropiedad());
            propiedadActual.setCantidadAmbientes(propiedad.getCantidadAmbientes());
            propiedadActual.setMetrosCuadrados(propiedad.getMetrosCuadrados());
            propiedadActual.setDescripcion(propiedad.getDescripcion());
            propiedadActual.setPropietario(propiedad.getPropietario());
            propiedadActual.setEstadoDisponibilidad(propiedad.getEstadoDisponibilidad());

            if (estadoAnterior != propiedad.getEstadoDisponibilidad()) {
                HistorialEstadoPropiedad historial = new HistorialEstadoPropiedad();
                historial.setPropiedad(propiedadActual);
                historial.setEstadoDisponibilidad(propiedad.getEstadoDisponibilidad());
                historial.setFechaCambio(LocalDateTime.now());

                propiedadActual.getHistorialEstados().add(historial);
            }

            propiedadRepo.save(propiedadActual);
        }
    }

    @Override
    public void deleteById(Long id) {

        Propiedad propiedad = getById(id);

        if (propiedad != null) {
            propiedad.setEliminada(true);
            propiedadRepo.save(propiedad);
        }
    }

    @Override
    public List<Propiedad> filter(PropiedadBuscarForm filter) throws Excepcion {

        if (filter.getDireccion() == null
                && filter.getCiudadSeleccionada() == null
                && filter.getTipoSeleccionado() == null
                && filter.getEstadoSeleccionado() == null) {

            return propiedadRepo.findByEliminadaFalse();
        }

        return propiedadRepo.filtrar(
                filter.getDireccion(),
                filter.getCiudadSeleccionada(),
                filter.getTipoSeleccionado(),
                filter.getEstadoSeleccionado());
    }
}