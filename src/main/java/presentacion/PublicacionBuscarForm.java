package presentacion;

import entidades.EstadoPublicacion;

public class PublicacionBuscarForm {
    private Long propiedadSeleccionada;
    private Long ciudadSeleccionada;
    private EstadoPublicacion estadoSeleccionado;
    private Double precioMinimo;
    private Double precioMaximo;

    public PublicacionBuscarForm() {}

    // Getters y Setters
    public Long getPropiedadSeleccionada() { return propiedadSeleccionada; }
    public void setPropiedadSeleccionada(Long propiedadSeleccionada) { this.propiedadSeleccionada = propiedadSeleccionada; }

    public Long getCiudadSeleccionada() { return ciudadSeleccionada; }
    public void setCiudadSeleccionada(Long ciudadSeleccionada) { this.ciudadSeleccionada = ciudadSeleccionada; }

    public EstadoPublicacion getEstadoSeleccionado() { return estadoSeleccionado; }
    public void setEstadoSeleccionado(EstadoPublicacion estadoSeleccionado) { this.estadoSeleccionado = estadoSeleccionado; }

    public Double getPrecioMinimo() { return precioMinimo; }
    public void setPrecioMinimo(Double precioMinimo) { this.precioMinimo = precioMinimo; }

    public Double getPrecioMaximo() { return precioMaximo; }
    public void setPrecioMaximo(Double precioMaximo) { this.precioMaximo = precioMaximo; }
}
