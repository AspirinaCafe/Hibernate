package dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import clases.Coche;
import clases.RevisionException;
import persistencia.HibernateUtil;

public class CocheDao extends GenericDAO<Coche>{
	
	/**
	 * Consultar todos los coche que hay en la base de datos.
	 * @return devuelve la lista de todos los coches.
	 */
	public  List<Coche> consultarTodosLosCoches(){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		List<Coche> listCoche;
		Query sentenciaQuery = session.createQuery("SELECT c FROM Coche c");
		listCoche = sentenciaQuery.list();
		
		return listCoche;
	}
	/**
	 * Consultar en la base de datos el coche que contenga esa matricula que se le pasa
	 * @param matricula del coche
	 * @return devuelve un objecto coche con esa matricula.
	 */
	public  Coche buscarCochePorMatricula(String matricula) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		
		//Agrego uniqueResult()
		Coche coche = (Coche) session.createQuery("SELECT c FROM Coche c WHERE Matricula = '"+matricula+"'").uniqueResult();
		return coche;
	}
	
	public List<Object[]> consularCochePorDNI(String dni){
		
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		List<Object[]> listCocheDni;
		Query sentenciaQuery =session.createQuery("SELECT co.tipoCombustible, COUNT(co.tipoCombustible) FROM Coche co WHERE '"+dni+"'=co.cliente GROUP BY co.tipoCombustible");
		listCocheDni = sentenciaQuery.list();
		return listCocheDni;
	}
	
	/**
	 * Metodo para cambiar la matricula.
	 * @param coche le paso el coche que se va a cambiar
	 * @param newMatricula matricula que se va a modificar
	 * @throws RevisionException la envio para luego tratarla
	 */
	
	public  void cambiarMatricula (Coche coche, String newMatricula) throws RevisionException {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		
		Coche c = new Coche();
		c.setCliente(coche.getCliente());
		c.setMarca(coche.getMarca());
		c.setModelo(coche.getModelo());
		c.setListRevisiones(coche.getListRevisiones());
		c.setTipoCombustible(coche.getTipoCombustible());
		c.setMatricula(newMatricula);
		
		//borro el coche que se paso
		session.delete(coche);
		//guardo el coche modificado
		session.save(c);
		
		session.getTransaction().commit();
		
		
	}
	

}
