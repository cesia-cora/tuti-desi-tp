package accesoDatos;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import entidades.Contrato;
import entidades.EstadoContrato;

public interface ContratoRepo extends JpaRepository<Contrato, Long> {
	boolean existsByPropiedadIdAndEstadoContrato(Long id, EstadoContrato activo);
	
	boolean existsByPropiedadIdAndEstadoContratoAndIdNot(Long id, EstadoContrato activo, Long id2);
	
	List<Contrato> findByEliminadaFalse();

	@Query("""
        SELECT c FROM Contrato c
        WHERE c.eliminada = false
          AND (:propiedadId IS NULL OR c.propiedad.id = :propiedadId)
          AND (:inquilinoId IS NULL OR c.inquilino.id = :inquilinoId)
          AND (:estado IS NULL OR c.estadoContrato = :estado)
          AND (:fechaInicio IS NULL OR c.fechaInicio >= :fechaInicio)
        """)
    List<Contrato> filtrar(
        @Param("propiedadId") Long propiedadId,
        @Param("inquilinoId") Long inquilinoId,
        @Param("estado") EstadoContrato estado,
        @Param("fechaInicio") LocalDate fechaInicio
    );
}