package accesoDatos;

import org.springframework.data.jpa.repository.JpaRepository;

import entidades.Ciudad;

public interface CiudadRepo extends JpaRepository<Ciudad, Long> {

}