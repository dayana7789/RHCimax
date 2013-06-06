package com.nahmens.rhcimax.database;

import com.nahmens.rhcimax.database.modelo.Configuracion;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/*
 * Clase que define la estructura de la base de datos.
 */
public class DataBaseHelper extends SQLiteOpenHelper{

	private static final String DATABASE_NAME = "rhcimax";
	private static final int DATABASE_VERSION = 10;

	/*Tablas*/
	public static final String TABLA_USUARIO = "usuario";
	public static final String TABLA_ROL = "rol";
	public static final String TABLA_PERMISO = "permiso";
	public static final String TABLA_ROL_PERMISO = "rol_permiso";
	public static final String TABLA_EMPRESA = "empresa";
	public static final String TABLA_EMPLEADO = "empleado";
	public static final String TABLA_COTIZACION = "cotizacion";
	public static final String TABLA_EMPLEADO_COTIZACION = "empleado_cotizacion";
	public static final String TABLA_SERVICIO= "servicio";
	public static final String TABLA_COTIZACION_SERVICIO = "cotizacion_servicio";
	public static final String TABLA_CONFIGURACION= "configuracion";

	/*Creacion de tablas*/
	private static final String DATABASE_CREATE_ROL = "CREATE table " + TABLA_ROL + " ("
													+"_id INTEGER PRIMARY KEY AUTOINCREMENT, "
													+ "nombre TEXT NOT NULL, "
													+ "descripcion TEXT NOT NULL);";

	private static final String DATABASE_CREATE_USUARIO = "CREATE table " + TABLA_USUARIO + " ("
														+"_id INTEGER PRIMARY KEY AUTOINCREMENT, "
														+ "login TEXT NOT NULL, "
														+ "password TEXT NOT NULL, "
														+ "correo TEXT NOT NULL, "
														+ "idRol INTEGER, "
														+ "FOREIGN KEY(idRol) REFERENCES " + TABLA_ROL + "(_id));";
	
	private static final String DATABASE_CREATE_PERMISO = "CREATE table " + TABLA_PERMISO + " ("
														+"_id INTEGER PRIMARY KEY AUTOINCREMENT, "
														+ "nombre TEXT NOT NULL, "
														+ "descripcion TEXT NOT NULL);";
	
	private static final String DATABASE_CREATE_ROL_PERMISO = "CREATE table " + TABLA_ROL_PERMISO + " ("
															+ "idRol INTEGER NOT NULL, "
															+ "idPermiso INTEGER NOT NULL, "
															+ "primary key (idRol, idPermiso), "
															+ "FOREIGN KEY(idRol) REFERENCES " + TABLA_ROL + "(_id), " 
															+ "FOREIGN KEY(idPermiso) REFERENCES " + TABLA_PERMISO + "(_id));";
	
	private static final String DATABASE_CREATE_EMPRESA = "CREATE table " + TABLA_EMPRESA + " ("
															+"_id INTEGER PRIMARY KEY AUTOINCREMENT, "
															+ "nombre TEXT NOT NULL, "
															+ "telefono TEXT NOT NULL, "
															+ "web TEXT, "
															+ "rif TEXT, "
															+ "dirFiscal TEXT, "
															+ "dirComercial TEXT, "
															+ "fechaCreacion DATETIME DEFAULT (datetime('now','localtime')), "
															+ "fechaSincronizacion DATETIME DEFAULT NULL, "
															+ "modificado INTEGER DEFAULT 0, " 
															+ "idUsuario INTEGER NOT NULL, "
															+ "FOREIGN KEY(idUsuario) REFERENCES " + TABLA_USUARIO + "(_id));";
	
	private static final String DATABASE_CREATE_EMPLEADO = "CREATE table " + TABLA_EMPLEADO + " ("
															+"_id INTEGER PRIMARY KEY AUTOINCREMENT, "
															+ "nombre TEXT NOT NULL, "
															+ "apellido TEXT NOT NULL, "
															+ "posicion TEXT NOT NULL, "
															+ "email TEXT NOT NULL, "
															+ "telfOficina TEXT, "
															+ "celular TEXT, "
															+ "pin TEXT, "
															+ "linkedin TEXT, "
															+ "descripcion TEXT, "
															+ "idEmpresa INTEGER NOT NULL, "
															+ "fechaCreacion DATETIME DEFAULT (datetime('now','localtime')), "
															+ "fechaSincronizacion DATETIME DEFAULT NULL, "
															+ "idUsuario INTEGER NOT NULL, "
															+ "FOREIGN KEY(idEmpresa) REFERENCES " + TABLA_EMPRESA + "(_id), "
															+ "FOREIGN KEY(idUsuario) REFERENCES " + TABLA_USUARIO + "(_id));";
	
	private static final String DATABASE_CREATE_COTIZACION = "CREATE table " + TABLA_COTIZACION + " ("
															+"_id INTEGER PRIMARY KEY AUTOINCREMENT, "
															+ "fechaEnvio TEXT NOT NULL, "
															+ "fechaLeido TEXT, "
															+ "enviado INTEGER NOT NULL, "
															+ "recibido INTEGER NOT NULL, "
															+ "idUsuario INTEGER NOT NULL, "
															+ "idEmpresa INTEGER NOT NULL, "
															+ "fechaCreacion DATETIME DEFAULT (datetime('now','localtime')), "
															+ "fechaSincronizacion DATETIME DEFAULT NULL, "
															+ "FOREIGN KEY(idEmpresa) REFERENCES " + TABLA_EMPRESA + "(_id),"
															+ "FOREIGN KEY(idUsuario) REFERENCES " + TABLA_USUARIO + "(_id));";

	
	private static final String DATABASE_CREATE_EMPLEADO_COTIZACION = "CREATE table " + TABLA_EMPLEADO_COTIZACION + " ("
																	+ "idEmpleado INTEGER NOT NULL, "
																	+ "idCotizacion INTEGER NOT NULL, "
																	+ "primary key (idEmpleado, idCotizacion), "
																	+ "FOREIGN KEY(idEmpleado) REFERENCES " + TABLA_EMPLEADO + "(_id), " 
																	+ "FOREIGN KEY(idCotizacion) REFERENCES " + TABLA_COTIZACION + "(_id));";
	
	private static final String DATABASE_CREATE_SERVICIO = "CREATE table " + TABLA_SERVICIO + " ("
															+"_id INTEGER PRIMARY KEY AUTOINCREMENT, "
															+ "nombre TEXT NOT NULL, "
															+ "precio REAL NOT NULL, "
															+ "descripcion TEXT NOT NULL, "
															+ "status TEXT NOT NULL, "
															+ "unidadMedicion TEXT NOT NULL, "
															+ "medida TEXT, "
															+ "inicial REAL NOT NULL);";
	
	private static final String DATABASE_CREATE_COTIZACION_SERVICIO = "CREATE table " + TABLA_COTIZACION_SERVICIO + " ("
																	+ "idCotizacion INTEGER NOT NULL, "
																	+ "idServicio INTEGER NOT NULL, "
																	+ "primary key (idCotizacion, idServicio), "
																	+ "FOREIGN KEY(idCotizacion) REFERENCES " + TABLA_COTIZACION + "(_id), " 
																	+ "FOREIGN KEY(idServicio) REFERENCES " + TABLA_SERVICIO + "(_id));";

	
	private static final String DATABASE_CREATE_CONFIGURACION = "CREATE table " + TABLA_CONFIGURACION + " ("
															  + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
															  + "key TEXT NOT NULL, "
															  + "value TEXT NOT NULL);";
	
	public DataBaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	// Este método se llama automaticamente al momento en el que se crea la BD.
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE_ROL);
		database.execSQL(DATABASE_CREATE_USUARIO);
		database.execSQL(DATABASE_CREATE_PERMISO);
		database.execSQL(DATABASE_CREATE_ROL_PERMISO);
		database.execSQL(DATABASE_CREATE_EMPRESA);
		database.execSQL(DATABASE_CREATE_EMPLEADO);
		database.execSQL(DATABASE_CREATE_COTIZACION);
		database.execSQL(DATABASE_CREATE_EMPLEADO_COTIZACION);
		database.execSQL(DATABASE_CREATE_SERVICIO);
		database.execSQL(DATABASE_CREATE_COTIZACION_SERVICIO);
		database.execSQL(DATABASE_CREATE_CONFIGURACION);

		this.insertarConfiguracion(database);
		this.insertarRegistros(database);
	}

	private void insertarConfiguracion(SQLiteDatabase database) {
		String insertServer = "INSERT INTO "+ TABLA_CONFIGURACION + "("+Configuracion.KEY+", " + Configuracion.VALUE + ") VALUES('"+Configuracion.NOMBRE_SERVIDOR+"','')";
		database.execSQL(insertServer);
	}

	@Override
	// Método que se llama cada vez que se actualiza la BD
	// Sirve para manejar las versiones de la misma
	public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
		Log.w(DataBaseHelper.class.getName(), "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
		database.execSQL("DROP TABLE IF EXISTS "+TABLA_USUARIO);
		database.execSQL("DROP TABLE IF EXISTS "+TABLA_ROL_PERMISO);
		database.execSQL("DROP TABLE IF EXISTS "+TABLA_ROL);
		database.execSQL("DROP TABLE IF EXISTS "+TABLA_PERMISO);
		database.execSQL("DROP TABLE IF EXISTS "+TABLA_EMPRESA);
		database.execSQL("DROP TABLE IF EXISTS "+TABLA_EMPLEADO_COTIZACION);
		database.execSQL("DROP TABLE IF EXISTS "+TABLA_EMPLEADO);
		database.execSQL("DROP TABLE IF EXISTS "+TABLA_COTIZACION_SERVICIO);
		database.execSQL("DROP TABLE IF EXISTS "+TABLA_COTIZACION);
		database.execSQL("DROP TABLE IF EXISTS "+TABLA_SERVICIO);
		onCreate(database);

	}
	
	/*
	 * TEMPORAL: ESTA INFO. SE DEBE OBTENER DE LA NUBE.
	 */
	private void insertarRegistros(SQLiteDatabase database){
		
/*id:1*/String insertRol1 = "INSERT INTO "+ TABLA_ROL + "(nombre, descripcion) VALUES('Administrador','Tiene acceso a la lista de todos los clientes de RHCimax y a las acciones de eliminar y modificar.')";
/*id:2*/String insertRol2 = "INSERT INTO "+ TABLA_ROL + "(nombre, descripcion) VALUES('Usuario Avanzado','Tiene acceso a la lista de todos los clientes de RHCimax y a la accion de modificar.')";
/*id:3*/String insertRol3 = "INSERT INTO "+ TABLA_ROL + "(nombre, descripcion) VALUES('Usuario','Tiene acceso solo a la lista de clientes de RHCimax ingresador por el y a la accion de modificar.')";
		
/*id:1*/String insertPermiso1 =  "INSERT INTO "+ TABLA_PERMISO + "(nombre, descripcion) VALUES('ListarTodos','Se permite el acceso a la lista de todos los clientes RHCimax.')";
/*id:2*/String insertPermiso2 =  "INSERT INTO "+ TABLA_PERMISO + "(nombre, descripcion) VALUES('ListarPropios','Se permite el acceso a la lista de los clientes RHCimax ingresados por el usuario.')";
/*id:3*/String insertPermiso3 =  "INSERT INTO "+ TABLA_PERMISO + "(nombre, descripcion) VALUES('Modificar','Se permite el acceso a la acción modificar.')";
/*id:4*/String insertPermiso4 =  "INSERT INTO "+ TABLA_PERMISO + "(nombre, descripcion) VALUES('Eliminar','Se permite el acceso a la acción eliminar.')";

		String usuario = "INSERT INTO "+ TABLA_USUARIO + "(idRol,login, password, correo) VALUES(1,'nahmens','1234','NONE')";
		
		/*Rol administrador tiene todos los permisos*/
		String rolPer =  "INSERT INTO "+ TABLA_ROL_PERMISO + "(idRol,idPermiso) VALUES(1,1)";
		String rolPer2 =  "INSERT INTO "+ TABLA_ROL_PERMISO + "(idRol,idPermiso) VALUES(1,2)";
		String rolPer3 =  "INSERT INTO "+ TABLA_ROL_PERMISO + "(idRol,idPermiso) VALUES(1,3)";
		String rolPer4 =  "INSERT INTO "+ TABLA_ROL_PERMISO + "(idRol,idPermiso) VALUES(1,4)";
		
		/*Rol Usuario Avanzado tiene todos los permisos excepto eliminar*/
		String rolPer5 =  "INSERT INTO "+ TABLA_ROL_PERMISO + "(idRol,idPermiso) VALUES(2,1)";
		String rolPer6 =  "INSERT INTO "+ TABLA_ROL_PERMISO + "(idRol,idPermiso) VALUES(2,2)";
		String rolPer7 =  "INSERT INTO "+ TABLA_ROL_PERMISO + "(idRol,idPermiso) VALUES(2,3)";
		
		/*Rol usuario puede ver los clientes ingresados por el y modificarlos*/
		String rolPer8 =  "INSERT INTO "+ TABLA_ROL_PERMISO + "(idRol,idPermiso) VALUES(3,2)";
		String rolPer9 =  "INSERT INTO "+ TABLA_ROL_PERMISO + "(idRol,idPermiso) VALUES(3,3)";

		database.execSQL(insertRol1);
		database.execSQL(insertRol2);
		database.execSQL(insertRol3);
		database.execSQL(insertPermiso1);
		database.execSQL(insertPermiso2);
		database.execSQL(insertPermiso3);
		database.execSQL(insertPermiso4);
		database.execSQL(usuario);
		database.execSQL(rolPer);
		database.execSQL(rolPer2);
		database.execSQL(rolPer3);
		database.execSQL(rolPer4);
		database.execSQL(rolPer5);
		database.execSQL(rolPer6);
		database.execSQL(rolPer7);
		database.execSQL(rolPer8);
		database.execSQL(rolPer9);
		
		String serv1 =  "INSERT INTO "+ TABLA_SERVICIO + "(nombre,precio,inicial,unidadMedicion,descripcion,status) VALUES('Administracion de nominas',1000,200,'persona','Administracion de nominas: Un texto es una composición de signos codificado en un sistema de escritura (como un alfabeto) que forma una unidad de sentido . Su tamaño puede ser variable. También es texto una composición de caracteres imprimibles (con grafema) generados por un algoritmo de cifrado que, aunque no tienen sentido para cualquier persona, sí puede ser descifrado por su destinatario original.','Activo')";
		String serv2 =  "INSERT INTO "+ TABLA_SERVICIO + "(nombre,precio,inicial,unidadMedicion,descripcion,status) VALUES('Reclutacion y seleccion de talento humando',500,0,'ninguno', 'Reclutacion y seleccion de talento humando: El nivel microestructural o local está asociado con el concepto de cohesión. Se refiere a uno de los fenómenos propios de la coherencia, el de las relaciones particulares y locales que se dan entre elementos lingüísticos, tanto los que remiten unos a otros como los que tienen la función de conectar y organizar.','Activo')";
		String serv3 =  "INSERT INTO "+ TABLA_SERVICIO + "(nombre,precio,inicial,unidadMedicion,descripcion,status) VALUES('Adiestramiento y capacitacion',600,0,'horas', 'Adiestramiento y capacitacion: El nivel microestructural o local está asociado con el concepto de cohesión. Se refiere a uno de los fenómenos propios de la coherencia, el de las relaciones particulares y locales que se dan entre elementos lingüísticos, tanto los que remiten unos a otros como los que tienen la función de conectar y organizar.','Activo')";
		String serv4 =  "INSERT INTO "+ TABLA_SERVICIO + "(nombre,precio,inicial,unidadMedicion,descripcion,status) VALUES('Mantenimiento y limpieza',800,0,'dias', 'Mantenimiento y limpieza: El nivel microestructural o local está asociado con el concepto de cohesión. Se refiere a uno de los fenómenos propios de la coherencia, el de las relaciones particulares y locales que se dan entre elementos lingüísticos, tanto los que remiten unos a otros como los que tienen la función de conectar y organizar.','Activo')";
		String serv5 =  "INSERT INTO "+ TABLA_SERVICIO + "(nombre,precio,inicial,unidadMedicion,descripcion,status) VALUES('Canal Makro comercializadora',300,0,'semanas', 'Canal Makro comercializadora: El nivel microestructural o local está asociado con el concepto de cohesión. Se refiere a uno de los fenómenos propios de la coherencia, el de las relaciones particulares y locales que se dan entre elementos lingüísticos, tanto los que remiten unos a otros como los que tienen la función de conectar y organizar.','Activo')";
		String serv6 =  "INSERT INTO "+ TABLA_SERVICIO + "(nombre,precio,inicial,unidadMedicion,descripcion,status) VALUES('Canales',300,40,'persona', 'Canales: El nivel microestructural o local está asociado con el concepto de cohesión. Se refiere a uno de los fenómenos propios de la coherencia, el de las relaciones particulares y locales que se dan entre elementos lingüísticos, tanto los que remiten unos a otros como los que tienen la función de conectar y organizar.','Activo')";
		String serv7 =  "INSERT INTO "+ TABLA_SERVICIO + "(nombre,precio,inicial,unidadMedicion,descripcion,status) VALUES('Comercio',300,0,'horas', 'Comercio: El nivel microestructural o local está asociado con el concepto de cohesión. Se refiere a uno de los fenómenos propios de la coherencia, el de las relaciones particulares y locales que se dan entre elementos lingüísticos, tanto los que remiten unos a otros como los que tienen la función de conectar y organizar.','Activo')";
		String serv8 =  "INSERT INTO "+ TABLA_SERVICIO + "(nombre,precio,inicial,unidadMedicion,descripcion,status) VALUES('servicios',300,0,'ninguno', 'servicios: El nivel microestructural o local está asociado con el concepto de cohesión. Se refiere a uno de los fenómenos propios de la coherencia, el de las relaciones particulares y locales que se dan entre elementos lingüísticos, tanto los que remiten unos a otros como los que tienen la función de conectar y organizar.','Activo')";
		String serv9 =  "INSERT INTO "+ TABLA_SERVICIO + "(nombre,precio,inicial,unidadMedicion,descripcion,status) VALUES('industria',300,0,'dias', 'industria: El nivel microestructural o local está asociado con el concepto de cohesión. Se refiere a uno de los fenómenos propios de la coherencia, el de las relaciones particulares y locales que se dan entre elementos lingüísticos, tanto los que remiten unos a otros como los que tienen la función de conectar y organizar.','Activo')";
		String serv10 =  "INSERT INTO "+ TABLA_SERVICIO + "(nombre,precio,inicial,unidadMedicion,descripcion,status) VALUES('nose fuego',300,0,'ninguno', 'nose fuego: El nivel microestructural o local está asociado con el concepto de cohesión. Se refiere a uno de los fenómenos propios de la coherencia, el de las relaciones particulares y locales que se dan entre elementos lingüísticos, tanto los que remiten unos a otros como los que tienen la función de conectar y organizar.','Activo')";
		
		database.execSQL(serv1);
		database.execSQL(serv2);
		database.execSQL(serv3);
		database.execSQL(serv4);
		database.execSQL(serv5);
		database.execSQL(serv6);
		database.execSQL(serv7);
		database.execSQL(serv8);
		database.execSQL(serv9);
		database.execSQL(serv10);
		
	
	}

}
