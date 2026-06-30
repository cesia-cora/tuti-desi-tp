package servicios;

import java.util.List;
import entidades.Contrato;
import excepciones.Excepcion;

public interface ContratoService {

    List<Contrato> getAll();

    Contrato getById(Long id);

    void save(Contrato contrato) throws Excepcion;

    void finalizar(Long id) throws Excepcion;

    void deleteById(Long id) throws Excepcion;
}