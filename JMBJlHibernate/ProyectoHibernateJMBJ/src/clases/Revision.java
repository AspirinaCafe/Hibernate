package clases;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Type;

@Entity()
@Table(name = "Revision")
public class Revision implements Serializable{
	
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@Type(type = "integer")
	@Column(name = "idRevision")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int idRevision;
	@Column(name = "FechaRevision")
	private LocalDate fechaRevision;
	@Column(name = "Descripcion")
	private String descripcion;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
	@JoinColumn(name = "Matricula_Revision")
	private Coche coche;

	public Revision( LocalDate fechaRevision, String descripcion, Coche coche) {
		super();
	
		this.fechaRevision = fechaRevision;
		this.descripcion = descripcion;
		this.coche = coche;
	}
	
	public Revision() {
		
	}

	public int getIdRevision() {
		return idRevision;
	}

	public void setIdRevision(int idRevision) {
		this.idRevision = idRevision;
	}

	public LocalDate getFechaRevision() {
		return fechaRevision;
	}

	public void setFechaRevision(LocalDate fechaRevision) {
		this.fechaRevision = fechaRevision;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Coche getCoche() {
		return coche;
	}

	public void setCoche(Coche coche) {
		this.coche = coche;
	}

	/**
	 * Por el coche
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((coche == null) ? 0 : coche.hashCode());
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
		Revision other = (Revision) obj;
		if (coche == null) {
			if (other.coche != null)
				return false;
		} else if (!coche.equals(other.coche))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Revision \n\n\t idRevision=" + idRevision + "\n\t fechaRevision=" + fechaRevision + "\n\t descripcion="
				+ descripcion + "\n\n" + coche);
		return sb.toString();
	}
}
