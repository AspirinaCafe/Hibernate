package principal;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import org.hibernate.Session;

import clases.Cliente;
import clases.Coche;
import clases.Revision;
import clases.RevisionException;
import clases.TipoCombustible;
import dao.ClienteDao;
import dao.CocheDao;
import dao.RevisionDao;
import persistencia.HibernateUtil;

/**
 * 
 * @author JMBJ
 *
 */

public class PrincipalRevision {

	private static Scanner teclado = new Scanner(System.in);
	private static Session session;
	private static ClienteDao clienteDao = new ClienteDao();
	private static CocheDao cocheDao = new CocheDao();
	private static RevisionDao revisionDao = new RevisionDao();

	public static void main(String[] args) {

		int opc;

		do {
			opc = mostrarMenu();
			tratarMenu(opc);
		} while (opc != 4);

	}

	private static void tratarMenu(int opc) {

		int opcCliente, opcCoche, opcRevision;

		switch (opc) {
		case 1:
			do {
				opcCliente = verMenuCliente();
				tratarMenuCliente(opcCliente);
			} while (opcCliente != 6);

			break;

		case 2:
			do {
				opcCoche = verMenuCoche();
				tratarMenuCoche(opcCoche);
			} while (opcCoche != 7);
			break;
		case 3:

			do {
				opcRevision = verMenuRevision();
				tratarMenuRevision(opcRevision);
			} while (opcRevision != 6);
			break;
		}

	}

	/**
	 * 
	 * Tratamos la distinta opciones que tiene la revision 1 Dar de alta a una
	 * revision. 2 Modificar fecha de una revision. 3.Borrar revision por su id.
	 * 4.Consultar revision de un cliente. 5.Consultar todas las revisiones. 6.salir
	 * de revision
	 * 
	 * @param opcRevision la opcion a tratar
	 * @throws RevisionException
	 */
	private static void tratarMenuRevision(int opcRevision) {

		String dni, matricula, descripcionRevision;
		int year, month, day, idRevision;
		boolean isDate;
		configurarSesion();

		try {
			switch (opcRevision) {
			case 1:
				System.out.println("|" + opcRevision + "| Dar de alta a una revision.");

				do {
					day = solicitarEntero("Ingresa el dia de la revision:");
					month = solicitarEntero("Ingresa el mes de la revision:");
					year = solicitarEntero("Ingresa el aï¿½o de la revision:");
					isDate = esFechaValida(year, day, month);
					if (!isDate) {
						System.err.println("Error...Parece que algun dato de la fecha no es valido." + "\n"
								+ "Intentalo otra vez");
					}
				} while (!isDate);
				descripcionRevision = solicitarCadena("Ingresa la descripcion de la revision:");
				matricula = solicitarCadena("Ingresa la matricula del coche de la revision:");
				altaRevision(day, month, year, descripcionRevision, matricula);
				break;

			case 2:
				System.out.println("|" + opcRevision + "| Modificar fecha de una revision.");
				matricula = solicitarCadena("Ingresa la matricula del coche:");
				modificarFechaDeUnaRevision(matricula);
				break;
			case 3:

				System.out.println("|" + opcRevision + "| Borrar revision por su matricula.");
				matricula = solicitarCadena("Ingrese la matricula del coche:");
				borrarLaRevisionPorSuMatricula(matricula);
				break;

			case 4:
				System.out.println("|" + opcRevision + "| Consultar revision de un cliente.");
				dni = solicitarCadena("Ingrese el dni que desea consultar la revision:");
				consultarRevisionPorDNICliente(dni);
				break;
			case 5:
				System.out.println("|" + opcRevision + "| Consultar todas las revisiones.");
				consultarTodasLasRevisiones();
				break;
			}
		} catch (RevisionException e) {
			System.out.println(e.getMessage());
		}
		cerrarSesion();

	}

	private static boolean esFechaValida(int year, int day, int month) {
		boolean esFechaValida = true;
		try {
			LocalDate.of(year, month, day);
		} catch (DateTimeException e) {
			esFechaValida = false;
		}
		return esFechaValida;
	}

	/**
	 * Metodo para consultar las revisiones que tiene un cliente por su dni
	 * 
	 * 
	 * 
	 * @param dni del cliente que quiere consultar su revision.
	 * @throws RevisionException si listaRevisionDeClientePorDNI esta vacia esque
	 *                           ese cliente no tiene revision.
	 */
	private static void consultarRevisionPorDNICliente(String dni) throws RevisionException {

		List<Object[]> listaRevisionDeClientePorDNI;

		listaRevisionDeClientePorDNI = revisionDao.consultaLaRevisionDeUnClientePorSuDNI(dni);

		// Si la la listaRevisionDeClientePorDNI
		if (listaRevisionDeClientePorDNI.isEmpty()) {
			throw new RevisionException("Vaya. Parece que ese cliente con dni:" + dni + " Todavia no tiene revision");
		}

		for (Object[] objects : listaRevisionDeClientePorDNI) {
			System.out.println("DNI:" + objects[0] + "\n Matricula:" + objects[1] + "\n Fecha revision " + objects[2]
					+ "\n Descripcion:" + objects[3]);
		}
	}

	/**
	 * Metodo para cambiar la fecha de una revision.
	 * 
	 * Una ves solicite el dia el mes y el aï¿½o llamara al metodo
	 * esFechaValida(int,int,int) que recibe 3 enteros validando cada uno de los
	 * datos de la fecha si eso devuelve false quiere decir que algo en la fecha
	 * esta mal.
	 * 
	 * Se controla Para que no salte la exception DateTimeException.
	 * 
	 * si devuelve true continuara con transformar la fecha que se a introducido en
	 * una nuevaFecha que luegeo se modificara y se salvara los cambios. LocalDate
	 * fechaNew = LocalDate.of(year, month, day); Transforma la fecha que se a
	 * introducido
	 * 
	 * revisionDAO.setFechaRevision(fechaNew); modifica la fecha por la nueva.
	 * 
	 * revisionDao.guardar(revisionDAO); salvamos los cambios.
	 * 
	 * @param matricula del coche
	 * @throws RevisionException Si la listaRevision tiene un tamaï¿½o 0 saltara la
	 *                           exception
	 */
	private static void modificarFechaDeUnaRevision(String matricula) throws RevisionException {

		boolean isDate;
		int day, month, year, idRevision;

		boolean fechaCambiada = false;

		List<Revision> listRevisionDAO = revisionDao.consultarRevisionPorMatricula(matricula);

		if (listRevisionDAO.size() == 0) {
			throw new RevisionException("Vaya. Parece que no existe ese coche con esa matricula " + matricula);
		}

		System.out.println("Lista de revision de la matricula: " + matricula);
		Iterator<Revision> it = listRevisionDAO.iterator();
		while (it.hasNext()) {
			Revision revision = (Revision) it.next();
			System.out.println("Id revision:" + revision.getIdRevision());
			System.out.println("Matricula " + revision.getCoche().getMatricula());
			System.out.println("Fecha de la revision:" + revision.getFechaRevision());
		}

		idRevision = solicitarEntero("Introduce el id que deseas modificar la fecha: ");

		for (Revision revision : listRevisionDAO) {

			if (revision.getIdRevision() == idRevision) {
				System.out.println("Procedemos a cambiar la fecha");

				do {
					day = solicitarEntero("Ingresa el dia [NUEVA FECHA]");
					month = solicitarEntero("Ingresa el mes [NUEVA FECHA]");
					year = solicitarEntero("Ingresa el año [NUEVA FECHA]");
					isDate = esFechaValida(year, day, month);
					if (!isDate) {
						System.err.println("Error...Parece que algun dato de la fecha no es valido." + "\n"
								+ "Intentalo otra vez");
					}

				} while (!isDate);
				LocalDate fechaNew = LocalDate.of(year, month, day);
				revision.setFechaRevision(fechaNew);
				revisionDao.guardar(revision);
				fechaCambiada = true;
			} 

		}

		if (fechaCambiada) {
			System.out.println("Fecha de la revision: " + idRevision + " cambiada correctamente.");

		}else {
			throw new RevisionException("Vaya. Parece que el id no es correcto " + idRevision);

		}
	}

	/**
	 * Metodo que da alta la revision
	 * 
	 * fechaRevision para transformar las variables day,month,year que se habia
	 * pasado previamente. para luego aï¿½adirla a la revision nueva que se creara.
	 * 
	 * revisionNew es la revision que se dara de alta en la base de datos
	 * comprobando que el coche exista a partir de la matricula.
	 * 
	 * cocheDAO.addRevision(revisionNew); aï¿½ade la revsion a la lista de revision
	 * de la clase coche.
	 * 
	 * luego guardamos el ccohe para asi crear tambien la nueva revision y
	 * actualizar la lista de revision
	 * 
	 * @param day                 el dia que se le pasa
	 * @param month               el mes que se le pasa
	 * @param year                el aï¿½o que se le pasa
	 * @param descripcionRevision que se hara al coche
	 * @param matricula           del coche
	 * @throws RevisionException si el coche no existe no se hara nada.
	 */
	private static void altaRevision(int day, int month, int year, String descripcionRevision, String matricula)
			throws RevisionException {

		Coche cocheDAO = cocheDao.buscarCochePorMatricula(matricula);

		if (cocheDAO == null) {
			throw new RevisionException("Vaya. Parece que no existe ese coche con esa matricula " + matricula);
		}
		// Le paso la fecha que hemos puesto convertida version inglesa
		LocalDate fechaRevision = LocalDate.of(year, month, day);

		Revision revisionNew = new Revision(fechaRevision, descripcionRevision, cocheDAO);

		cocheDAO.addRevision(revisionNew);

		cocheDao.guardar(cocheDAO);

		System.out.println("Revision creada correctamente.");
		System.out.println("\n" + revisionNew);
	}

	/**
	 * Metodo para borrar la revision por su matricula
	 * 
	 * borramos de coche la revision cocheDAO.deleteRevision(revisionBorrarDAO); y
	 * borramos la revision sus datos revisionDao.borrar(revisionBorrarDAO);
	 * 
	 * 
	 * 
	 * @param matricula del coche
	 * @throws RevisionException comprueba que el coche exista con la matricula que
	 *                           se le pasa
	 */
	private static void borrarLaRevisionPorSuMatricula(String matricula) throws RevisionException {

		List<Revision> listRevisiones = revisionDao.consultarRevisionPorMatricula(matricula);
		boolean borrar = false;
		int idRevision;
		if (listRevisiones.size() == 0) {
			throw new RevisionException("Vaya. No hay revisiones con esa matricula");
		}
		// Mostrar los id de la revision por la matricula y la fecha de revision
		System.out.println("Lista de revision de la matricula: " + matricula);
		Iterator<Revision> it = listRevisiones.iterator();
		while (it.hasNext()) {
			Revision revision = (Revision) it.next();
			System.out.println("Id revision:" + revision.getIdRevision());
			System.out.println("Matricula " + revision.getCoche().getMatricula());
			System.out.println("Descripcion " + revision.getDescripcion());
			System.out.println("Fecha de la revision:" + revision.getFechaRevision());
		}
		/**
		 * solicito el id de la revision 
		 */

		idRevision = solicitarEntero("Ingrese un id:");

		for (Revision revision : listRevisiones) {
			if (revision.getIdRevision() == idRevision) {
				revision.getCoche().deleteRevision(revision);
				revisionDao.borrar(revision);
				borrar = true;
			}
		}

		if (borrar) {
			System.out.println("Revision con id :" + idRevision + " borrada correctamente.");
		}else {
			throw new RevisionException("Vaya. Parece que no hay ninguna revision con ese id: " +idRevision);
		}

	}

	/**
	 * Metodo para Consulta todas las revisiones que haya en la base de datos
	 * 
	 * @throws RevisionException si no hay ninguna saltara la excepcion
	 */
	private static void consultarTodasLasRevisiones() throws RevisionException {

		List<Revision> ListRevisionDAO = revisionDao.consultarTodasRevisiones();
		if (ListRevisionDAO.isEmpty()) {
			throw new RevisionException("Vaya. Parece que no hay ninguna revision aun.");
		}
		for (Revision revision : ListRevisionDAO) {
			System.out.println(revision);
		}
	}

	private static int verMenuRevision() {
		int opcRevision = 0;

		opcRevision = solicitarEntero("|1| Dar de alta a una revision." + "\n" + "|2| Modificar fecha de una revision."
				+ "\n" + "|3| Borrar revision por su matricula." + "\n" + "|4| Consultar revision de un cliente." + "\n"
				+ "|5| Consultar todas las revisiones." + "\n" + "|6| Salir.");

		return opcRevision;
	}

	/**
	 * Tratamos la distinta opciones que tiene el coche 1 dar de alta 2 modificar
	 * modelo de un coche. 3.borrar coche por su matricula 4. consultar lista de
	 * revision de un coche por matricula ordenada por su fecha 5. consulta todos
	 * los coches. 6. salir de coche
	 * 
	 * @param opcCoche
	 * @throws RevisionException
	 */
	private static void tratarMenuCoche(int opcCoche) {

		String matricula, marca, modelo, tipoCombustible;
		String dniCliente;
		boolean correctoTipoCombustible = false;
		configurarSesion();

		try {
			switch (opcCoche) {
			case 1:
				System.out.println("|" + opcCoche + "| Dar de alta a un Coche.");
				dniCliente = solicitarCadena("Ingrese el dni del cliente del coche:");
				matricula = solicitarCadena("Ingrese la matricula del coche:");
				marca = solicitarCadena("Ingrese la marca del coche:");
				modelo = solicitarCadena("Ingrese el modelo del coche:");
				do {
					tipoCombustible = solicitarCadena("Ingrese el tipo de combustible [ELECTRICO|GASOLINA|DIESEL]");
					tipoCombustible = tipoCombustible.toUpperCase();

					switch (tipoCombustible) {
					case "DIESEL":
						correctoTipoCombustible = true;
						break;

					case "GASOLINA":
						correctoTipoCombustible = true;

						break;
					case "ELECTRICO":
						correctoTipoCombustible = true;

						break;
					}
					if (!correctoTipoCombustible) {

						System.out.println(
								"Error al introducir el tipo de combustible debe ser [ELECTRICO|GASOLINA|DIESEL]");

					}
				} while (!correctoTipoCombustible);
				altaCoche(dniCliente, matricula, marca, modelo, tipoCombustible);
				break;
			case 2:

				System.out.println("|" + opcCoche + "| Modificar modelo de un coche.");
				matricula = solicitarCadena("Ingrese la matricula del coche a modificar:");
				modificarModeloCoche(matricula);
				break;
			case 3:
				System.out.println("|" + opcCoche + "| Borrar coche por su matricula.");
				matricula = solicitarCadena("Ingrese la matricula del coche que se va a borrar:");
				borrarCochePorSuMatricula(matricula);
				break;
			case 4:
				System.out.println(
						"|" + opcCoche + "| Consultar lista de revision de un coche por matricula.[Ordena por fecha]");
				matricula = solicitarCadena("Ingrese la matricula del coche que desea consultar la revision.");
				consultarRevisionDeUnCochePorSuMatricula(matricula);
				break;
			case 5:
				System.out.println("|" + opcCoche + "|  Consultar todos los Coches.");
				consultarTodosLosCoches();
				break;
			case 6:
				System.out.println("|" + opcCoche + "| Cuantos coches tiene ese cliente agrupado por el tipo de combustible.");
				dniCliente = solicitarCadena("Introduce el dni del coche que deseas consultar");
				consultarCochePorDni(dniCliente);

				break;

			}
		} catch (RevisionException e) {
			System.out.println(e.getMessage());
		}
		cerrarSesion();

	}

	private static void consultarCochePorDni(String dniCliente) throws RevisionException {
		List<Object[]> listaCocheDAO = cocheDao.consularCochePorDNI(dniCliente);
		
		if (listaCocheDAO.isEmpty()) {
			throw new RevisionException("Vaya. Parece ser que ese dni: "+ dniCliente+ " No tiene ningun coche.");
		}
		
		for (Object[] objects : listaCocheDAO) {
			System.out.println("COMBUSTIBLE "+objects[0] + " TIENE  " + objects[1]);
		}
		
	}

	/**
	 * Consulta todos los coches que hay en la base de datos.
	 * 
	 * @throws RevisionException si la listTodosLosCochesDAO esta vacia saltara la
	 *                           excepciï¿½n
	 */

	private static void consultarTodosLosCoches() throws RevisionException {

		List<Coche> listTodosLosCochesDAO = cocheDao.consultarTodosLosCoches();

		if (listTodosLosCochesDAO.isEmpty()) {
			throw new RevisionException("Vaya. Parece ser que no hay coche.");
		}

		for (Coche coche : listTodosLosCochesDAO) {
			System.out.println(coche);
		}
	}

	/**
	 * cocheDAO comprueba si la matricula de ese coche existe. listCocheRevison
	 * comprueba si ese coche tiene alguna revision.
	 * listCocheRevison.stream().sorted el lambda ordena las revisiones por su fecha
	 * 
	 * @param matricula del coche que consultaremos su revision.
	 * @throws RevisionException si hay un coche con esa matricula. y si la lista de
	 *                           revisiones del coche tiene alguna revision.
	 */
	private static void consultarRevisionDeUnCochePorSuMatricula(String matricula) throws RevisionException {

		List<Revision> listCocheRevison;
		Coche cocheDAO = cocheDao.buscarCochePorMatricula(matricula);

		if (cocheDAO == null) {
			throw new RevisionException("Vaya no hay coches con esta matricula " + matricula);
		}

		listCocheRevison = cocheDAO.getListRevisiones();

		if (listCocheRevison.isEmpty()) {
			throw new RevisionException("Vaya. el coche con matricula " + matricula + " no tiene revision.");
		}

		listCocheRevison.stream().sorted(new Comparator<Revision>() {

			@Override
			public int compare(Revision revision1, Revision revision2) {
				return revision1.getFechaRevision().compareTo(revision2.getFechaRevision());
			}
		}).forEach(revision -> System.out.println(revision));

	}

	/**
	 * Metodo que borra un coche por su matricula
	 * 
	 * @param matricula que se va borrar de la base de datos
	 * @throws RevisionException si la matricula de ese coche no existe saltara la
	 *                           excepcion.
	 */
	private static void borrarCochePorSuMatricula(String matricula) throws RevisionException {

		Coche cocheDAO = cocheDao.buscarCochePorMatricula(matricula);

		if (cocheDAO == null) {
			throw new RevisionException("Vaya no hay coches con esta matricula " + matricula);
		}

		cocheDao.borrar(cocheDAO);
		System.out.println("coche con matricula " + matricula + " borrado correctamente.");
		System.out.println("\n" + cocheDAO);
	}

	/**
	 * Metodo que modifica el modelo de un coche por su matricula
	 * 
	 * @param matricula del coche que vamos a modeficar el modelo.
	 * @throws RevisionException si no hay el coche con esa matricula saltara una
	 *                           excepcion.
	 */
	private static void modificarModeloCoche(String matricula) throws RevisionException {

		String modeloNew;

		Coche cocheDAO = cocheDao.buscarCochePorMatricula(matricula);

		if (cocheDAO == null) {
			throw new RevisionException("Vaya no hay coches con esta matricula " + matricula);
		}

		modeloNew = solicitarCadena("Ingresa el modelo del coche");

		cocheDAO.setModelo(modeloNew);

		cocheDao.guardar(cocheDAO);

		session.refresh(cocheDAO);
		System.out.println("Modelo del coche con matricula " + matricula + " se modifico correctamente.");
		System.out.println("\n" + cocheDAO);
	}

	/**
	 * cocheMatriculaDAO lo que comprobara es que si ya existe el coche con esa
	 * matricula. clienteDAO comprueba si el cliente existe.
	 * 
	 * @param dniCliente del cliente al que pertenece el coche
	 * @param matricula  del coche
	 * @param marca      del coche
	 * @param modelo     del coche
	 * @throws RevisionException si ya existe un coche con esa matricula saltara la
	 *                           excepcion. o si el dniCliente no existe en la base
	 *                           de datos
	 */
	private static void altaCoche(String dniCliente, String matricula, String marca, String modelo,
			String tipoCombustible) throws RevisionException {
		TipoCombustible tipoCombustibleE;
		Coche cocheMatriculaDAO = cocheDao.buscarCochePorMatricula(matricula);

		if (cocheMatriculaDAO != null) {
			throw new RevisionException("Vaya. Ya existe un coche con esa matricula");
		}

		Cliente clienteDAO = clienteDao.buscarClientePorDni(dniCliente);
		if (clienteDAO == null) {
			throw new RevisionException("Vaya. No hay un cliente con ese dni " + dniCliente);
		}

		tipoCombustibleE = TipoCombustible.valueOf(tipoCombustible.toUpperCase());

		// Creamos un nuevo coche con su cliente.
		Coche cocheNuevo = new Coche(matricula, marca, modelo, tipoCombustibleE, clienteDAO);

		// Agregamos el coche a la lista de coche cliente.
		clienteDAO.addCoche(cocheNuevo);

		// Guardamos el clienteDAO con el coche ya metido en la list
		clienteDao.guardar(clienteDAO);

		System.out.println("El coche creado correctamente.");
		System.out.println("\n" + cocheNuevo);

	}

	/**
	 * Metodo que muestra las opciones de coche
	 * 
	 * @return opcionCoche recoge la opciï¿½n elegida
	 */
	private static int verMenuCoche() {
		int opcCoche = 0;

		opcCoche = solicitarEntero("|1| Dar de alta a un Coche." + "\n" + "|2| Modificar modelo de un coche." + "\n"
				+ "|3| Borrar coche por su matricula." + "\n"
				+ "|4| Consultar lista de revision de un coche por matricula.[Ordena por fecha]" + "\n"
				+ "|5| Consultar todos los Coches." + "\n" 
				+ "|6| Cuantos coches tiene ese cliente agrupado por el tipo de combustible" +"\n" 
				+ "|7| Salir.");
		return opcCoche;
	}

	/**
	 * Tratamos la distinta opciones que tiene el cliente 1 dar de alta 2 modificar
	 * el nombre del cliente 3.borrar un cliente por su dni 4.consultar un cliente
	 * por su nombre y ordenar alfabeticamente por su direccion. 5. consulta todos
	 * los clientes. 6. salir de cliente
	 * 
	 * @param opcCliente
	 * @throws RevisionException
	 */
	private static void tratarMenuCliente(int opcCliente) {

		String dni, nombreCliente, direccionCliente;
		configurarSesion();

		try {

			switch (opcCliente) {
			case 1:
				System.out.println("|" + opcCliente + "| Dar de alta a un cliente.");
				dni = solicitarCadena("Ingrese el dni del cliente:");
				nombreCliente = solicitarCadena("Ingrese el nombre del cliente:");
				direccionCliente = solicitarCadena("Ingrese la direccion del cliente:");
				altaCliente(dni, nombreCliente, direccionCliente);
				break;

			case 2:
				System.out.println("|" + opcCliente + "| Modificar el nombre de un cliente.");
				dni = solicitarCadena("Ingrese el dni del cliente que desea modificar:");
				modificarNombreCliente(dni);
				break;
			case 3:
				System.out.println("|" + opcCliente + "| Borrar a un cliente por el dni.");
				dni = solicitarCadena("Ingrese el dni del cliente que deseas borrar:");
				borrarClientePorSuDni(dni);

				break;
			case 4:
				System.out.println("|" + opcCliente
						+ "| Consultar Cliente por un nombre.[Ordenar alfabeticamente por direccion].");
				nombreCliente = solicitarCadena("Ingrese el nombre del cliente que deseas consultar:");
				consultarClientesPorSuNombre(nombreCliente);
				break;
			case 5:
				System.out.println("|" + opcCliente + "| Consultar todos los clientes.");
				consultarTodosLosClientes();

				break;
			}
		} catch (RevisionException e) {
			System.out.println(e.getMessage());
		}
		cerrarSesion();

	}

	/**
	 * Metodo para consultar todos los clientes que se llamen iguales ordenados
	 * alfabeticamente por su direcciï¿½n.
	 * 
	 * @param nombreCliente del cliente a buscar.
	 * @throws RevisionException si no se encuentra ese dni en la base de datos. si
	 *                           es Null.
	 */
	private static void consultarClientesPorSuNombre(String nombreCliente) throws RevisionException {

		List<Cliente> listClientesNombreDAO = clienteDao.consultarClientesPorNombre(nombreCliente);

		if (listClientesNombreDAO.isEmpty()) {
			throw new RevisionException("Vaya. Parece ser que no hay clientes.");
		}

		listClientesNombreDAO.stream().sorted(new Comparator<Cliente>() {

			@Override
			public int compare(Cliente cliente1, Cliente cliente2) {
				return cliente1.getDireccion().compareTo(cliente2.getDireccion());
			}
		}).forEach(cliente -> System.out.println(cliente));

	}

	private static void consultarTodosLosClientes() throws RevisionException {
		List<Cliente> listClientesDAO;

		listClientesDAO = clienteDao.consultarTodosLosClientes();

		if (listClientesDAO.isEmpty()) {
			throw new RevisionException("Vaya. Parece ser que no hay clientes.");
		}

		/**
		 * Al hacer un cliente.ToString() pegaba petardaso
		 */
		listClientesDAO.stream().forEach(cliente -> System.out.println(cliente));
	}

	/**
	 * Metodo para borrar un cliente por su dni
	 * 
	 * @param dni del cliente que se va a borrar
	 * @throws RevisionException si no se encuentra ese dni en la base de datos. si
	 *                           es Null
	 */
	private static void borrarClientePorSuDni(String dni) throws RevisionException {

		Cliente clienteDAO = clienteDao.buscarClientePorDni(dni);

		if (clienteDAO == null) {
			throw new RevisionException("Vaya. No se encontro el cliente con ese dni.");
		}

		clienteDao.borrar(clienteDAO);

		System.out.println("Se a borrado correctamente el cliente con ese dni:" + dni);
		System.out.println("\n" + clienteDAO);

	}

	/**
	 * En este metodo modificamos el nombre de un cliente.
	 * 
	 * @param dni del cliente que vamos a modificar su nombre.
	 * @throws RevisionException Si el cliente no esta en la base de datos con ese
	 *                           dni aparecera este error.
	 */
	private static void modificarNombreCliente(String dni) throws RevisionException {

		String nombreNew;
		// buscamos el cliente por el dni
		Cliente clienteDAO = clienteDao.buscarClientePorDni(dni);

		if (clienteDAO == null) {
			throw new RevisionException("Vaya. No se encontro el cliente con ese dni.");
		}

		nombreNew = solicitarCadena("Introduce el nuevo nombre del cliente para el dni: " + clienteDAO.getDni());
		clienteDAO.setNombre(nombreNew);
		clienteDao.guardar(clienteDAO);

		System.out.println("Se a modificado correctamente el nombre del cliente");
		System.out.println("\n" + clienteDAO);

	}

	/**
	 * Metodo para registrar un alta de un nuevo cliente.
	 * 
	 * @param dni              del nuevo cliente
	 * @param nombreCliente    del nuevo cliente
	 * @param direccionCliente del nuevo cliente
	 * @throws RevisionException si ya existe ese cliente con el dni del nuevo
	 *                           cliente
	 */
	private static void altaCliente(String dni, String nombreCliente, String direccionCliente)
			throws RevisionException {
		Cliente clienteNew;
		Cliente clienteDAO = clienteDao.buscarClientePorDni(dni);

		if (clienteDAO != null) {
			throw new RevisionException("Vaya. Ya existe un cliente en la base de datos con ese dni.");
		} else {
			clienteNew = new Cliente(dni, nombreCliente, direccionCliente);
			clienteDao.guardar(clienteNew);
		}

		System.out.println("El cliente se a creado correctamente.");
		System.out.println("\n" + clienteNew);

	}

	private static int solicitarEntero(String msg) {
		boolean isNumber;
		int entero = 0;
		do {

			try {
				System.out.println(msg);
				entero = Integer.parseInt(teclado.nextLine());
				isNumber = true;
			} catch (NumberFormatException nfe) {
				isNumber = false;
			}
		} while (!isNumber || entero <= 0);

		return entero;
	}

	private static String solicitarCadena(String msg) {
		System.out.println(msg);
		return teclado.nextLine();
	}

	/**
	 * Muestra las opciones del menï¿½ cliente
	 * 
	 * @return recoge la opciï¿½n elegida
	 */
	private static int verMenuCliente() {
		int opcCliente = 0;

		opcCliente = solicitarEntero("|1| Dar de alta a un cliente." + "\n" + "|2| Modificar el nombre de un cliente."
				+ "\n" + "|3| Borrar a un cliente por el dni." + "\n"
				+ "|4| Consultar Cliente por un nombre.[Ordenar alfabeticamente por direccion]" + "\n"
				+ "|5| Consultar todos los clientes." + "\n" + "|6| Salir.");

		return opcCliente;
	}

	private static int mostrarMenu() {

		int opc = 0;
		boolean isNumber;
		do {
			System.out.println("1. Cliente");
			System.out.println("2. Coche");
			System.out.println("3. Revision");
			System.out.println("4. Salir");
			try {
				opc = Integer.parseInt(teclado.nextLine());
				isNumber = true;
			} catch (NumberFormatException nfe) {
				isNumber = false;
			}
		} while (opc < 1 || opc > 4 || !isNumber);
		return opc;
	}

	private static void cerrarSesion() {
		// Cerramos Hibernate.
		HibernateUtil.closeSessionFactory();

	}

	private static void configurarSesion() {
		// Inicializamos Hibernate.
		HibernateUtil.buildSessionFactory();
		// Creamos la sesiï¿½n y la enlazamos al thread actual
		HibernateUtil.openSessionAndBindToThread();
		// Obtenemos la sesiï¿½n.
		session = HibernateUtil.getSessionFactory().getCurrentSession();
	}

}
