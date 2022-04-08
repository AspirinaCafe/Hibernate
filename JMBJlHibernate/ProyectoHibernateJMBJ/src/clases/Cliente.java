package clases;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;


@Entity()
@Table (name = "Cliente")
/**
 * Buscas todos los cliente y los ordena alfabeticamente desde la A-Z
 */
@NamedQueries ({
    @NamedQuery ( name = "findAllClientes",
                 query = "SELECT c FROM Cliente c ORDER BY Nombre ASC")
                
})
public class Cliente implements Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "DNI")
	private String dni;
	
	@Column(name = "Nombre")
	private String nombre;
	@Column(name = "Direccion")
	private String direccion;

	@OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
	@JoinColumn(name = "Cliente_Pertenece")
	private List<Coche> listCoches;
	
	public Cliente() {
		this.listCoches = new ArrayList<Coche>();
	}

	public Cliente(String dni, String nombre, String direccion) {
		super();
		this.dni = dni;
		this.nombre = nombre;
		this.direccion = direccion;
		
		this.listCoches = new ArrayList<Coche>();
	}
	/**
	 * Agregar un coche al List
	 * @param coche a�ade el coche a la lista
	 */
	public void addCoche(Coche coche) {
		this.listCoches.add(coche);
	}

	public String getDni() {
		return dni;
	}

	public void setDni(String dni) {
		this.dni = dni;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setCiudad(String direccion) {
		this.direccion = direccion;
	}

	public List<Coche> getListCoches() {
		return listCoches;
	}

	public void setListCoches(List<Coche> listCoches) {
		this.listCoches = listCoches;
	}

	/**
	 * POR EL DNI DEL CLIENTE
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dni == null) ? 0 : dni.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Cliente other = (Cliente) obj;
		if (dni == null) {
			if (other.dni != null)
				return false;
		} else if (!dni.equals(other.dni))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Cliente \n\tdni=" + this.dni + "\n\tnombre=" + this.nombre + ""
				+ "\n\tdireccion=" + this.direccion + ""
				+ "\n\tNumero de Coches=" + this.listCoches.size()
+ "\n");
		
		return sb.toString();
	}
	
	
	
}
