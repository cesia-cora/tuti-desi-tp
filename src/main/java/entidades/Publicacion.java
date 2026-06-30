

package entidades;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
public class Publicacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "propiedad_id")
    @NotNull(message = "Debe seleccionar una propiedad")
    private Propiedad propiedad;

    @NotNull(message = "El precio mensual es obligatorio")
    @Min(value = 1, message = "El precio mensual de alquiler deberá ser un número positivo")
    private Double precioMensual;

    @NotBlank(message = "Las condiciones de alquiler son obligatorias")
    @Column(columnDefinition = "TEXT")
    private String condicionesAlquiler;

    @NotBlank(message = "La descripción de la publicación es obligatoria")
    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @NotNull(message = "La fecha de publicación es obligatoria")
    private LocalDate fechaPublicacion;

    @Enumerated(EnumType.STRING)
    private EstadoPublicacion estadoPublicacion;

    private Boolean eliminada = false;

    @OneToMany(mappedBy = "publicacion", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HistorialEstadoPublicacion> historialEstados = new ArrayList<>();

    public Publicacion() {
    }

    // GetSet
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Propiedad getPropiedad() { return propiedad; }
    public void setPropiedad(Propiedad propiedad) { this.propiedad = propiedad; }

    public Double getPrecioMensual() { return precioMensual; }
    public void setPrecioMensual(Double precioMensual) { this.precioMensual = precioMensual; }

    public String getCondicionesAlquiler() { return condicionesAlquiler; }
    public void setCondicionesAlquiler(String condicionesAlquiler) { this.condicionesAlquiler = condicionesAlquiler; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public LocalDate getFechaPublicacion() { return fechaPublicacion; }
    public void setFechaPublicacion(LocalDate fechaPublicacion) { this.fechaPublicacion = fechaPublicacion; }

    public EstadoPublicacion getEstadoPublicacion() { return estadoPublicacion; }
    public void setEstadoPublicacion(EstadoPublicacion estadoPublicacion) { this.estadoPublicacion = estadoPublicacion; }

    public Boolean getEliminada() { return eliminada; }
    public void setEliminada(Boolean eliminada) { this.eliminada = eliminada; }

    public List<HistorialEstadoPublicacion> getHistorialEstados() { return historialEstados; }
    public void setHistorialEstados(List<HistorialEstadoPublicacion> historialEstados) { this.historialEstados = historialEstados; }
}