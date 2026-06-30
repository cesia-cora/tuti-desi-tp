package accesoDatos;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import entidades.Factura;
import entidades.EstadoFactura;

public interface FacturaRepo extends JpaRepository<Factura, Long> {

    List<Factura> findByEliminadaFalse();

    @Query("""
           SELECT f FROM Factura f
           WHERE f.eliminada = false
           AND (:contratoId IS NULL OR f.contrato.id = :contratoId)
           AND (:propiedadId IS NULL OR f.contrato.propiedad.id = :propiedadId)
           AND (:inquilinoId IS NULL OR f.contrato.inquilino.id = :inquilinoId)
           AND (:estado IS NULL OR f.estado = :estado)
           AND (:fechaDesde IS NULL OR f.fechaVencimiento >= :fechaDesde)
           AND (:fechaHasta IS NULL OR f.fechaVencimiento <= :fechaHasta)
           """)
    List<Factura> filtrar(
            @Param("contratoId") Long contratoId,
            @Param("propiedadId") Long propiedadId,
            @Param("inquilinoId") Long inquilinoId,
            @Param("estado") EstadoFactura estado,
            @Param("fechaDesde") LocalDate fechaDesde,
            @Param("fechaHasta") LocalDate fechaHasta);
}