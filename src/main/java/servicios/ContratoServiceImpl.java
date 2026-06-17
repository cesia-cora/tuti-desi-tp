package servicios;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import accesoDatos.ContratoRepo;
import accesoDatos.PersonaRepo;
import accesoDatos.PropiedadRepo;
import entidades.Contrato;
import entidades.EstadoContrato;
import entidades.EstadoDisponibilidad;
import entidades.HistorialEstadoContrato;
import entidades.Persona;
import entidades.Propiedad;
import excepciones.Excepcion;

@Service
public class ContratoServiceImpl {

    @Autowired
    private ContratoRepo contratoRepo;

    @Autowired
    private PropiedadRepo propiedadRepo;

    @Autowired
    private PersonaRepo personaRepo;

    @Transactional
    public void save(Contrato contrato) throws Excepcion {
        if (contrato == null) {
            throw new Excepcion("Contrato vacío");
        }

        if (contrato.getId() != null) {
            throw new Excepcion("Este método solo crea contratos nuevos");
        }

        // Validaciones obligatorias
        if (contrato.getPropiedad() == null || contrato.getPropiedad().getId() == null) {
            throw new Excepcion("Debe seleccionar una propiedad");
        }
        if (contrato.getInquilino() == null || contrato.getInquilino().getId() == null) {
            throw new Excepcion("Debe seleccionar un inquilino");
        }
        if (contrato.getFechaInicio() == null) {
            throw new Excepcion("La fecha de inicio es obligatoria");
        }
        if (contrato.getImporteMensual() <= 0) {
            throw new Excepcion("El importe mensual debe ser un valor positivo");
        }
        if (contrato.getDuracionMeses() <= 0) {
            throw new Excepcion("La duración en meses debe ser un valor positivo");
        }
        if (contrato.getDiaVencMensual() == null) {
            throw new Excepcion("El día de vencimiento mensual es obligatorio");
        }
        int dia;
        try {
            dia = contrato.getDiaVencMensual().getDayOfMonth();
        } catch (Exception e) {
            throw new Excepcion("Formato inválido para el día de vencimiento");
        }
        if (dia < 1 || dia > 31) {
            throw new Excepcion("El día de vencimiento debe estar entre 1 y 31");
        }


        Propiedad propiedad = propiedadRepo.findById(contrato.getPropiedad().getId()).orElse(null);
        if (propiedad == null || Boolean.TRUE.equals(propiedad.getEliminada())) {
            throw new Excepcion("La propiedad seleccionada no existe o fue eliminada");
        }

        Persona inquilino = personaRepo.findById(contrato.getInquilino().getId()).orElse(null);
        if (inquilino == null || Boolean.TRUE.equals(inquilino.getEliminada())) {
            throw new Excepcion("El inquilino seleccionado no existe o fue eliminado");
        }

        if (propiedad.getEstadoDisponibilidad() != EstadoDisponibilidad.DISPONIBLE) {
            throw new Excepcion("La propiedad no está disponible para alquilar");
        }

        boolean tieneActivo = contratoRepo.existsByPropiedadIdAndEstadoContrato(propiedad.getId(), EstadoContrato.ACTIVO);
        if (tieneActivo) {
            throw new Excepcion("La propiedad ya tiene un contrato activo");
        }

        contrato.setPropiedad(propiedad);
        contrato.setInquilino(inquilino);
        contrato.setEstadoContrato(EstadoContrato.ACTIVO);

        HistorialEstadoContrato hist = new HistorialEstadoContrato();
        hist.setContrato(contrato);
        hist.setEstadoContrato(contrato.getEstadoContrato());
        hist.setFechaCambio(LocalDateTime.now());
        contrato.getHistorialEstados().add(hist);

        Contrato contratoGuardado = contratoRepo.save(contrato);

        propiedad.setEstadoDisponibilidad(EstadoDisponibilidad.ALQUILADA);
        propiedadRepo.save(propiedad);
    }
}