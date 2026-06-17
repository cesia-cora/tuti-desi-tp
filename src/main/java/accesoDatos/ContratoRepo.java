package accesoDatos;

import org.springframework.data.jpa.repository.JpaRepository;
import entidades.Contrato;
import entidades.EstadoContrato;

public interface ContratoRepo extends JpaRepository<Contrato, Long> {
	boolean existsByPropiedadIdAndEstadoContrato(Long propiedadId, EstadoContrato estadoContrato);
}