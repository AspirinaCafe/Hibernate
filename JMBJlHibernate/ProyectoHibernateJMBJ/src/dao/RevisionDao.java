package dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import clases.Revision;
import persistencia.HibernateUtil;
/**
 * 
 * @author JMBJ
 *
 */
public class RevisionDao extends GenericDAO<Revision> {

	
	/**
	 * Consulta de revisiones por la matricula introducida
	 * @param matricula el numero de la matricula a consultar
	 * @return  devuelve un list de revisiones de todas la revisiones que tenga esa matricula
	 */
	public  List<Revision> consultarRevisionPorMatricula (String matricula){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		
		List<Revision> listRevisionesFechaMatricula;
		
		Query sentenciaQuery = session.createQuery("SELECT r FROM Revision r WHERE Matricula_Revision = '"+matricula+"'");
		
		listRevisionesFechaMatricula = sentenciaQuery.list();
		
		return listRevisionesFechaMatricula;
		
	}
	/**
	 * 	Metodo que consulta la revision de un cliente por su dni y lo ordeno por fecha ASC 
	 * 
	 * @param dni para buscar luego en la base de datos ese dni
	 * @return devuelve una lista de Object[]  que cumpla con la sentencia 
	 */

	public List<Object[]> consultaLaRevisionDeUnClientePorSuDNI(String dni){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		List<Object[]> listRevisionClienteDAO ;
		
		Query sentenciaQuery = session.createQuery("SELECT c.dni,co.matricula,r.fechaRevision,r.descripcion FROM Revision r INNER JOIN r.coche co INNER JOIN co.cliente c WHERE co.cliente= '"+dni+"' ORDER BY r.fechaRevision asc");
			
		listRevisionClienteDAO= sentenciaQuery.list();
		
		return listRevisionClienteDAO ;
	}
	
	
	
	
	/**
	 * Consultar todas las revisiones 
	 * @return devuelve todas la revisiones que hay 
	 */
	

	public List<Revision> consultarTodasRevisiones(){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		
		List<Revision> listRevision;
		
		Query sentenciaQuery = session.createQuery("SELECT r FROM Revision r ");
		
		listRevision = sentenciaQuery.list();
		
		return listRevision;
	}
	
}
