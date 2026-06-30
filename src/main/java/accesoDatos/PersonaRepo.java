package accesoDatos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import entidades.Persona;

public interface PersonaRepo extends JpaRepository<Persona, Long> {

    List<Persona> findByEliminadaFalse();

    //boolean existsByDniCuit(String dniCuit);//verifica si existe ese CUit cuando se da el alta

    //boolean existsByDniCuitAndIdNot(String dniCuit, Long id);//verifica duplicado cuando se modifica persona

}