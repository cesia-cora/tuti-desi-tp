package presentacion;

import java.util.List;

import entidades.Ciudad;
import entidades.EstadoDisponibilidad;
import entidades.TipoPropiedad;

public class PropiedadBuscarForm {

    private String direccion;
    private Long ciudadSeleccionada;
    private TipoPropiedad tipoSeleccionado;
    private EstadoDisponibilidad estadoSeleccionado;

    private List<Ciudad> ciudades;

    public String getDireccion() {
        if (direccion != null && direccion.length() > 0)
            return direccion;
        else
            return null;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public Long getCiudadSeleccionada() {
        return ciudadSeleccionada;
    }

    public void setCiudadSeleccionada(Long ciudadSeleccionada) {
        this.ciudadSeleccionada = ciudadSeleccionada;
    }

    public TipoPropiedad getTipoSeleccionado() {
        return tipoSeleccionado;
    }

    public void setTipoSeleccionado(TipoPropiedad tipoSeleccionado) {
        this.tipoSeleccionado = tipoSeleccionado;
    }

    public EstadoDisponibilidad getEstadoSeleccionado() {
        return estadoSeleccionado;
    }

    public void setEstadoSeleccionado(EstadoDisponibilidad estadoSeleccionado) {
        this.estadoSeleccionado = estadoSeleccionado;
    }

    public List<Ciudad> getCiudades() {
        return ciudades;
    }

    public void setCiudades(List<Ciudad> ciudades) {
        this.ciudades = ciudades;
    }
}