package accesoDatos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import entidades.Contrato;
import entidades.EstadoContrato;

public interface ContratoRepo extends JpaRepository<Contrato, Long> {
	boolean existsByPropiedadIdAndEstadoContrato(Long id, EstadoContrato activo);
	
	boolean existsByPropiedadIdAndEstadoContratoAndIdNot(Long id, EstadoContrato activo, Long id2);
	
	List<Contrato> findByEliminadaFalse();
}