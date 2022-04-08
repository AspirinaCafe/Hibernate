package clases;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;

@Entity()
@Table(name = "Coche")
public class Coche implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "Matricula")
	private String matricula;

	@Column(name = "Marca")
	private String marca;
	@Column(name = "Modelo")
	private String modelo;
	//fetch = FetchType.LAZY
	@ManyToOne(fetch = FetchType.LAZY)
	@Cascade(org.hibernate.annotations.CascadeType.REFRESH)
	@JoinColumn(name = "Cliente_Pertenece")
	private Cliente cliente;

	@Column(name = "Tipo_Combustible")
	@Enumerated(EnumType.STRING)
	private TipoCombustible tipoCombustible;

	/**
	 * para poder borrar el coche por su matricula
	 */
	@OneToMany
	@Cascade(org.hibernate.annotations.CascadeType.ALL)
	@JoinColumn(name = "Matricula_Revision")
	private List<Revision> listRevisiones;

	public Coche(String matricula, String marca, String modelo, TipoCombustible tipoCombustible, Cliente cliente) {
		super();
		this.matricula = matricula;
		this.marca = marca;
		this.modelo = modelo;
		this.tipoCombustible = tipoCombustible;
		this.cliente = cliente;
		this.listRevisiones = new ArrayList<Revision>();
	}

	public TipoCombustible getTipoCombustible() {
		return tipoCombustible;
	}

	public void setTipoCombustible(TipoCombustible tipoCombustible) {
		this.tipoCombustible = tipoCombustible;
	}

	public Coche() {
		this.listRevisiones = new ArrayList<Revision>();
	}

	public String getMatricula() {
		return matricula;
	}

	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}

	public String getMarca() {
		return marca;
	}

	public void setMarca(String marca) throws RevisionException {
		if (marca.length() <= 0) {
			throw new RevisionException("El nombre de la marca introducido es corto");
		}
		this.marca = marca;
	}

	public String getModelo() {
		return modelo;
	}

	public void setModelo(String modelo) throws RevisionException {
		if (modelo.length() <= 0) {
			throw new RevisionException("El nombre del modelo introducido es corto");
		}
		this.modelo = modelo;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public List<Revision> getListRevisiones() {
		return listRevisiones;
	}

	public void setListRevisiones(List<Revision> listRevisiones) {
		this.listRevisiones = listRevisiones;
	}

	/**
	 * POR LA MATRICULA DEL COCHE
	 */

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((matricula == null) ? 0 : matricula.hashCode());
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
		Coche other = (Coche) obj;
		if (matricula == null) {
			if (other.matricula != null)
				return false;
		} else if (!matricula.equals(other.matricula))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(" \nCoche \n\t matricula=" + matricula + "\n\t marca=" + marca + "\n\t modelo=" + modelo + "\n\n" + cliente
				+ "\nTipo combustible \n\t\t" + tipoCombustible + " \nlistRevisiones= " + this.listRevisiones.size() + "\n");
		return sb.toString();
				}

	/**
	 * Metodo para añadir revision al list
	 * 
	 * @param revision añade a la lista 
	 */
	public void addRevision(Revision revision) {
		this.listRevisiones.add(revision);
	}

	public void deleteRevision(Revision revision) {
		this.listRevisiones.remove(revision);
	}

}
