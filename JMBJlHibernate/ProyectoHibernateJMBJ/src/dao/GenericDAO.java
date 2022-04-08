package dao;

import org.hibernate.Session;

import clases.RevisionException;
import persistencia.HibernateUtil;

public class GenericDAO<T> {
	
	public void guardar(T entidad) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		session.save(entidad);
		session.getTransaction().commit();
		
	}
	public void borrar(T entidad) throws RevisionException{
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		session.delete(entidad);
		session.getTransaction().commit();
	}

}


/**
 * PREGUNTAR SI HACE FALTA UN TRY CATCH
 * 
 * catch (ConstraintViolationException cve) {
	session.getTransaction().rollback();
	session.clear();
	System.err.println("No se a agregado el cliente debido a los siguientes errores:");
	
	for (ConstraintViolation constraintViolation : cve.getConstraintViolations()) {
		System.out.println("En el campo '" + constraintViolation.getPropertyPath() + "':"
				+ constraintViolation.getMessage());
	}
 */