
package entidades;

import java.time.LocalDateTime;
import jakarta.persistence.*;

@Entity
public class HistorialEstadoPublicacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "publicacion_id")
    private Publicacion publicacion;

    @Enumerated(EnumType.STRING)
    private EstadoPublicacion estadoPublicacion;

    private LocalDateTime fechaCambio;

    public HistorialEstadoPublicacion() {
    }

    // Get y Set
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Publicacion getPublicacion() { return publicacion; }
    public void setPublicacion(Publicacion publicacion) { this.publicacion = publicacion; }

    public EstadoPublicacion getEstadoPublicacion() { return estadoPublicacion; }
    public void setEstadoPublicacion(EstadoPublicacion estadoPublicacion) { this.estadoPublicacion = estadoPublicacion; }

    public LocalDateTime getFechaCambio() { return fechaCambio; }
    public void setFechaCambio(LocalDateTime fechaCambio) { this.fechaCambio = fechaCambio; }

	public void setFechaChange(LocalDateTime now) {
		// todoautogenera
		
	}
}