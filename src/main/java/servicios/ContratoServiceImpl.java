package servicios;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

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
import jakarta.validation.Valid;
import presentacion.ContratoBuscarForm;

@Service
@Validated
public class ContratoServiceImpl {

    @Autowired
    private ContratoRepo contratoRepo;

    @Autowired
    private PropiedadRepo propiedadRepo;

    @Autowired
    private PersonaRepo personaRepo;

    public List<Contrato> getAll() {
		return contratoRepo.findByEliminadaFalse();
	}

	public Contrato getById(Long id) {
		return contratoRepo.findById(id).orElse(null);
	}

	public List<Contrato> filter(@Valid ContratoBuscarForm formBean) {
		return contratoRepo.findByEliminadaFalse();
	}
	
	// Eliminación
    
    @Transactional
    public void deleteById(Long id) throws Excepcion {
        Contrato c = contratoRepo.findById(id).orElse(null);
        if (c == null || Boolean.TRUE.equals(c.getEliminada())) {
            throw new Excepcion("Contrato inexistente o ya eliminado");
        }

        if (c.getEstadoContrato() != EstadoContrato.BORRADOR) {
            throw new Excepcion("Sólo se pueden eliminar contratos en estado BORRADOR");
        }

        c.setEliminada(true);
        contratoRepo.save(c);
    }

    @Transactional
    public void save(Contrato contrato) throws Excepcion {
    	
    		// Alta
    	
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
        
        boolean esAlta = contrato.getId() == null;

        // Cargar entidades relacionadas desde DB (entidades gestionadas)
        Propiedad propiedadNueva = propiedadRepo.findById(contrato.getPropiedad().getId()).orElse(null);
        if (propiedadNueva == null || Boolean.TRUE.equals(propiedadNueva.getEliminada())) {
            throw new Excepcion("La propiedad seleccionada no existe o fue eliminada");
        }

        Persona inquilino = personaRepo.findById(contrato.getInquilino().getId()).orElse(null);
        if (inquilino == null || Boolean.TRUE.equals(inquilino.getEliminada())) {
            throw new Excepcion("El inquilino seleccionado no existe o fue eliminado");
        }

        if (esAlta) {
            if (propiedadNueva.getEstadoDisponibilidad() != EstadoDisponibilidad.DISPONIBLE) {
                throw new Excepcion("La propiedad no está disponible para alquilar");
            }
            boolean tieneActivo = contratoRepo.existsByPropiedadIdAndEstadoContrato(propiedadNueva.getId(), EstadoContrato.ACTIVO);
            if (tieneActivo) {
                throw new Excepcion("La propiedad ya tiene un contrato activo");
            }

            contrato.setPropiedad(propiedadNueva);
            contrato.setInquilino(inquilino);
            contrato.setEstadoContrato(EstadoContrato.ACTIVO);

            HistorialEstadoContrato hist = new HistorialEstadoContrato();
            hist.setContrato(contrato);
            hist.setEstadoContrato(contrato.getEstadoContrato());
            hist.setFechaCambio(LocalDateTime.now());
            contrato.getHistorialEstados().add(hist);

            Contrato guardado = contratoRepo.save(contrato);

            propiedadNueva.setEstadoDisponibilidad(EstadoDisponibilidad.ALQUILADA);
            propiedadRepo.save(propiedadNueva);

            return;
        }
        
        // Modificación
        
        Contrato actual = contratoRepo.findById(contrato.getId()).orElse(null);
        if (actual == null) {
            throw new Excepcion("No se encontró el contrato a modificar");
        }
        
        boolean cambioPropiedad = !actual.getPropiedad().getId().equals(propiedadNueva.getId());
        if (cambioPropiedad) {
            // Si contrato actual estaba ACTIVO, liberar la propiedad anterior
            if (actual.getEstadoContrato() == EstadoContrato.ACTIVO) {
                Propiedad anterior = actual.getPropiedad();
                anterior.setEstadoDisponibilidad(EstadoDisponibilidad.DISPONIBLE);
                propiedadRepo.save(anterior);
            }
            // Si el contrato será o permanece ACTIVO con la nueva propiedad, validar disponibilidad y unicidad
            if (contrato.getEstadoContrato() == EstadoContrato.ACTIVO) {
                if (propiedadNueva.getEstadoDisponibilidad() != EstadoDisponibilidad.DISPONIBLE) {
                    throw new Excepcion("La nueva propiedad no está disponible para alquilar");
                }
                boolean existeActivo = contratoRepo.existsByPropiedadIdAndEstadoContratoAndIdNot(propiedadNueva.getId(), EstadoContrato.ACTIVO, actual.getId());
                if (existeActivo) {
                    throw new Excepcion("La nueva propiedad ya tiene un contrato activo");
                }
                propiedadNueva.setEstadoDisponibilidad(EstadoDisponibilidad.ALQUILADA);
                propiedadRepo.save(propiedadNueva);
            }
            actual.setPropiedad(propiedadNueva);
        }

        // Actualizar campos simples
        actual.setInquilino(inquilino);
        actual.setFechaInicio(contrato.getFechaInicio());
        actual.setDuracionMeses(contrato.getDuracionMeses());
        actual.setImporteMensual(contrato.getImporteMensual());
        actual.setDiaVencMensual(contrato.getDiaVencMensual());
        actual.setDescripcion(contrato.getDescripcion());

        // Gestión de cambios de estado con reglas de transición
        EstadoContrato estadoAnterior = actual.getEstadoContrato();
        EstadoContrato estadoNuevo = contrato.getEstadoContrato() == null ? estadoAnterior : contrato.getEstadoContrato();

        if (estadoNuevo != estadoAnterior) {
            // Validar transiciones permitidas:
            boolean permitida = false;
            if (estadoAnterior == EstadoContrato.BORRADOR && estadoNuevo == EstadoContrato.ACTIVO) permitida = true;
            if (estadoAnterior == EstadoContrato.ACTIVO && (estadoNuevo == EstadoContrato.FINALIZADO || estadoNuevo == EstadoContrato.RESCINDIDO)) permitida = true;
            // permitir BORRADOR -> BORRADOR (no cambio), ACTIVO->ACTIVO se excluye porque estadoNuevo==estadoAnterior
            if (!permitida) {
                throw new Excepcion("Transición de estado inválida: " + estadoAnterior + " -> " + estadoNuevo);
            }

            // Efectos de estado
            if (estadoNuevo == EstadoContrato.ACTIVO) {
                // No permitir activar si la propiedad no está disponible o ya tiene otro activo
                Propiedad p = actual.getPropiedad();
                if (p.getEstadoDisponibilidad() != EstadoDisponibilidad.DISPONIBLE) {
                    throw new Excepcion("La propiedad no está disponible para activar el contrato");
                }
                boolean existeActivo = contratoRepo.existsByPropiedadIdAndEstadoContratoAndIdNot(p.getId(), EstadoContrato.ACTIVO, actual.getId());
                if (existeActivo) {
                    throw new Excepcion("La propiedad ya tiene un contrato activo");
                }
                p.setEstadoDisponibilidad(EstadoDisponibilidad.ALQUILADA);
                propiedadRepo.save(p);
            } else if (estadoNuevo == EstadoContrato.FINALIZADO || estadoNuevo == EstadoContrato.RESCINDIDO) {
                // al finalizar o rescindir, la propiedad puede volver a disponible
                Propiedad p = actual.getPropiedad();
                p.setEstadoDisponibilidad(EstadoDisponibilidad.DISPONIBLE);
                propiedadRepo.save(p);
            }

            actual.setEstadoContrato(estadoNuevo);

            HistorialEstadoContrato hist = new HistorialEstadoContrato();
            hist.setContrato(actual);
            hist.setEstadoContrato(estadoNuevo);
            hist.setFechaCambio(LocalDateTime.now());
            actual.getHistorialEstados().add(hist);
        }

        contratoRepo.save(actual);
        
    }
}