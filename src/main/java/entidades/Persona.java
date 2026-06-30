package entidades;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "persona")
public class Persona {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = "El nombre es obligatorio")
	@Size(max = 100)
	private String nombre;

	@NotBlank(message = "El apellido es obligatorio")
	@Size(max = 100)
	private String apellido;

	@NotBlank(message = "El DNI/CUIT es obligatorio")
	@Pattern(regexp = "\\d{7,11}", message = "El DNI/CUIT debe contener entre 7 y 11 dígitos")
	@Column(nullable = false, unique = true, length = 11)
	private String dniCuit;

	@NotBlank(message = "El teléfono es obligatorio")
	@Size(max = 30)
	private String telefono;

	@Email(message = "El email no tiene un formato válido")
	@Size(max = 150)
	private String email;

	@NotBlank(message = "El domicilio es obligatorio")
	@Size(max = 200)
	private String domicilio;

	private boolean eliminada = false;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public String getDniCuit() {
		return dniCuit;
	}

	public void setDniCuit(String dniCuit) {
		this.dniCuit = dniCuit;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getDomicilio() {
		return domicilio;
	}

	public void setDomicilio(String domicilio) {
		this.domicilio = domicilio;
	}

	public boolean isEliminada() {
		return eliminada;
	}

	public void setEliminada(boolean eliminada) {
		this.eliminada = eliminada;
	}

	public Boolean getEliminada() {
		return eliminada;
	}
}