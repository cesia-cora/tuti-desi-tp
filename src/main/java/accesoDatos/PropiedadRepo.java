package accesoDatos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import entidades.EstadoDisponibilidad;
import entidades.Propiedad;
import entidades.TipoPropiedad;

public interface PropiedadRepo extends JpaRepository<Propiedad, Long> {

	List<Propiedad> findByEliminadaFalse();//propiedades activas

	List<Propiedad> findByCiudadIdAndEliminadaFalse(Long ciudadId);//listar propiedades no eliminadas

	List<Propiedad> findByEstadoDisponibilidadAndEliminadaFalse(EstadoDisponibilidad estadoDisponibilidad);//consulta por disponibilidad

	boolean existsByDireccionAndCiudadIdAndEliminadaFalse(String direccion, Long ciudadId);//no tiene misma direccion y ciudad
	
	boolean existsByDireccionAndCiudadIdAndEliminadaFalseAndIdNot(//excluye propiedad actual para no compararse consigo misma
	        String direccion,
	        Long ciudadId,
	        Long id);
	@Query("""
		       SELECT p
		       FROM Propiedad p
		       WHERE p.eliminada = false
		       AND (:direccion IS NULL OR p.direccion LIKE %:direccion%)
		       AND (:ciudadId IS NULL OR p.ciudad.id = :ciudadId)
		       AND (:tipo IS NULL OR p.tipoPropiedad = :tipo)
		       AND (:estado IS NULL OR p.estadoDisponibilidad = :estado)
		       """)
		List<Propiedad> filtrar(
		        @Param("direccion") String direccion,
		        @Param("ciudadId") Long ciudadId,
		        @Param("tipo") TipoPropiedad tipo,
		        @Param("estado") EstadoDisponibilidad estado);

}

