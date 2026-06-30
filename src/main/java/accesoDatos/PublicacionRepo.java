
package accesoDatos;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import entidades.Publicacion;
import entidades.EstadoPublicacion;

public interface PublicacionRepo extends JpaRepository<Publicacion, Long> {

    List<Publicacion> findByEliminadaFalse();

    // valida si ya existe una publicacion activa para una propiedad 
    boolean existsByPropiedadIdAndEstadoPublicacionAndEliminadaFalse(Long propiedadId, EstadoPublicacion estado);

    // valida exclusion para cuando va la edicion o modificacion
    boolean existsByPropiedadIdAndEstadoPublicacionAndEliminadaFalseAndIdNot(Long propiedadId, EstadoPublicacion estado, Long id);

    @Query("""
       SELECT p 
       FROM Publicacion p 
       WHERE p.eliminada = false
       AND (:propiedadId IS NULL OR p.propiedad.id = :propiedadId)
       AND (:ciudadId IS NULL OR p.propiedad.ciudad.id = :ciudadId)
       AND (:estado IS NULL OR p.estadoPublicacion = :estado)
       AND (:precioMin IS NULL OR p.precioMensual >= :precioMin)
       AND (:precioMax IS NULL OR p.precioMensual <= :precioMax)
       """)
    List<Publicacion> filtrar(
        @Param("propiedadId") Long propiedadId,
        @Param("ciudadId") Long ciudadId,
        @Param("estado") EstadoPublicacion estado,
        @Param("precioMin") Double precioMin,
        @Param("precioMax") Double precioMax
    );
}