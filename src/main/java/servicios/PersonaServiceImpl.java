package servicios;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import accesoDatos.PersonaRepo;
import entidades.Persona;

@Service
public class PersonaServiceImpl implements PersonaService {

    @Autowired
    private PersonaRepo personaRepo;

    @Override
    public List<Persona> getAll() {
        return personaRepo.findByEliminadaFalse();
    }

    
}

