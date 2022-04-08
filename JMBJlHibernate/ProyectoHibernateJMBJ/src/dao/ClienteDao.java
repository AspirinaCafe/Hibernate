package dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import clases.Cliente;
import persistencia.HibernateUtil;
/**
 * 
 * @author JMBJ
 *
 */
public class ClienteDao extends GenericDAO<Cliente>{
	
	
	/**
	 * Metodo para buscar un cliente por el dni que se a introducido
	 * @param dni del cliente
	 * @return y devuelve el cliente que se a encontrado
	 */
	public  Cliente buscarClientePorDni(String dni) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Cliente cliente = (Cliente) session.get(Cliente.class, dni);
		return cliente;
	}

	/**
	 * Metodo para consultar todos los clientes que tenga el mismo nombre que se a introducido
	 * @param nombre del cliente a consultar en la base de datos
	 * @return lista con todos los cliente que contiene ese nombre
	 */
	public List<Cliente> consultarClientesPorNombre(String nombre){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		List<Cliente> listClienteNombres;
		
		
		Query sentenciaQuery = session.createQuery("SELECT c FROM Cliente c WHERE Nombre = '"+nombre+"'");
				
		//Nos devuelve una lista con los nombre que sea igual
		listClienteNombres = sentenciaQuery.list();
		return listClienteNombres;
	}
	/**
	 * Metodo para consultar todos los clientes 
	 * @return devuelve una lista de cliente donde estan todo los cliente ordenado por su nombre [a-z]
	 */
	
	public static List<Cliente> consultarTodosLosClientes(){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		List<Cliente> listClientes;
		
		Query sentenciaQuery = session.getNamedQuery("findAllClientes");
		
		listClientes= sentenciaQuery.list();
		return listClientes;
		
	}
}
