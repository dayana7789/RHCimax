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

	/** OJO: Si vas a agregar un nuevo campo a alguna tabla de tipo REAL o INTEGER,
	 * asegurate de tomar en cuenta el nombre de la nueva columna en Utils.java/cursorToJsonString()
	 *Cursor no posee un metodo para obtener el tipo de dato de la columna
	 *en los APIS viejos de android. Es por esto, que segun el nombre de
	 *la columna, obtenemos el tipo de dato, para la sincronizacion */
	
	private static final String DATABASE_NAME = "rhcimax";
	private static final int DATABASE_VERSION = 10;

	private String dirServidor = "http://dipoint.no-ip.org:8080/rhcimaxserver/";

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
	public static final String TABLA_TAREA= "tarea";
	public static final String TABLA_HISTORICO= "historico";
	public static final String TABLA_CHECKIN= "checkin";

	/*Creacion de tablas*/
	private static final String DATABASE_CREATE_ROL = "CREATE table " + TABLA_ROL + " ("
													+"_id TEXT PRIMARY KEY NOT NULL, "
													+ "nombre TEXT NOT NULL, "
													+ "fechaCreacion DATETIME DEFAULT (datetime('now','localtime')), "
													+ "fechaModificacion DATETIME DEFAULT (datetime('now','localtime')), "
													+ "fechaSincronizacion DATETIME DEFAULT NULL, "
													+ "status TEXT NOT NULL DEFAULT 'activo', "
													+ "sincronizado INTEGER NOT NULL DEFAULT 0, "
													+ "descripcion TEXT NOT NULL);";

	private static final String DATABASE_CREATE_USUARIO = "CREATE table " + TABLA_USUARIO + " ("
														+"_id TEXT PRIMARY KEY NOT NULL, "
														+ "token TEXT NOT NULL, "
														+ "login TEXT NOT NULL, "
														+ "password TEXT NOT NULL, "
														+ "correo TEXT NOT NULL, "
														+ "idRol TEXT NOT NULL, "
														+ "fechaCreacion DATETIME DEFAULT (datetime('now','localtime')), "
														+ "fechaModificacion DATETIME DEFAULT (datetime('now','localtime')), "
														+ "fechaSincronizacion DATETIME DEFAULT NULL, "
														+ "status TEXT NOT NULL DEFAULT 'activo', "
														+ "sincronizado INTEGER NOT NULL DEFAULT 0, "
														+ "FOREIGN KEY(idRol) REFERENCES " + TABLA_ROL + "(_id) ON DELETE CASCADE);";
	
	private static final String DATABASE_CREATE_PERMISO = "CREATE table " + TABLA_PERMISO + " ("
														+"_id TEXT PRIMARY KEY NOT NULL, "
														+ "nombre TEXT NOT NULL, "
														+ "fechaCreacion DATETIME DEFAULT (datetime('now','localtime')), "
														+ "fechaModificacion DATETIME DEFAULT (datetime('now','localtime')), "
														+ "fechaSincronizacion DATETIME DEFAULT NULL, "
														+ "status TEXT NOT NULL DEFAULT 'activo', "
														+ "sincronizado INTEGER NOT NULL DEFAULT 0, "
														+ "descripcion TEXT NOT NULL);";
	
	private static final String DATABASE_CREATE_ROL_PERMISO = "CREATE table " + TABLA_ROL_PERMISO + " ("
															+ "idRol TEXT NOT NULL, "
															+ "idPermiso TEXT NOT NULL, "
															+ "fechaCreacion DATETIME DEFAULT (datetime('now','localtime')), "
															+ "fechaModificacion DATETIME DEFAULT (datetime('now','localtime')), "
															+ "fechaSincronizacion DATETIME DEFAULT NULL, "
															+ "status TEXT NOT NULL DEFAULT 'activo', "
															+ "sincronizado INTEGER NOT NULL DEFAULT 0, "
															+ "primary key (idRol, idPermiso), "
															+ "FOREIGN KEY(idRol) REFERENCES " + TABLA_ROL + "(_id) ON DELETE CASCADE, " 
															+ "FOREIGN KEY(idPermiso) REFERENCES " + TABLA_PERMISO + "(_id) ON DELETE CASCADE);";
	
	private static final String DATABASE_CREATE_EMPRESA = "CREATE table " + TABLA_EMPRESA + " ("
															+"_id TEXT PRIMARY KEY NOT NULL, "
															+ "nombre TEXT NOT NULL, "
															+ "telefono TEXT NOT NULL, "
															+ "web TEXT, "
															+ "rif TEXT, "
															+ "dirFiscal TEXT, "
															+ "dirComercial TEXT, "
															+ "latitud REAL DEFAULT 0, "
															+ "longitud REAL DEFAULT 0, "
															+ "fechaCreacion DATETIME DEFAULT (datetime('now','localtime')), "
															+ "fechaModificacion DATETIME DEFAULT (datetime('now','localtime')), "
															+ "fechaSincronizacion DATETIME DEFAULT NULL, "
															+ "idUsuarioCreador TEXT NOT NULL, "
															+ "idUsuarioModificador TEXT NOT NULL, "
															+ "modificado INTEGER DEFAULT 0, "
															+ "status TEXT NOT NULL DEFAULT 'activo', "
															+ "sincronizado INTEGER NOT NULL DEFAULT 0, "
															+ "FOREIGN KEY(idUsuarioCreador) REFERENCES " + TABLA_USUARIO + "(_id) ON DELETE CASCADE, "
															+ "FOREIGN KEY(idUsuarioModificador) REFERENCES " + TABLA_USUARIO + "(_id) ON DELETE CASCADE);";
	
	private static final String DATABASE_CREATE_EMPLEADO = "CREATE table " + TABLA_EMPLEADO + " ("
															+"_id TEXT PRIMARY KEY NOT NULL, "
															+ "nombre TEXT NOT NULL, "
															+ "apellido TEXT NOT NULL, "
															+ "posicion TEXT NOT NULL, "
															+ "email TEXT NOT NULL, "
															+ "telfOficina TEXT, "
															+ "celular TEXT, "
															+ "pin TEXT, "
															+ "linkedin TEXT, "
															+ "descripcion TEXT, "
															+ "idEmpresa TEXT, "
															+ "fechaCreacion DATETIME DEFAULT (datetime('now','localtime')), "
															+ "fechaModificacion DATETIME DEFAULT (datetime('now','localtime')), "
															+ "fechaSincronizacion DATETIME DEFAULT NULL, "
															+ "status TEXT NOT NULL DEFAULT 'activo', "
															+ "sincronizado INTEGER NOT NULL DEFAULT 0, "
															+ "idUsuarioCreador TEXT NOT NULL, "
															+ "idUsuarioModificador TEXT NOT NULL, "
															+ "FOREIGN KEY(idUsuarioModificador) REFERENCES " + TABLA_USUARIO + "(_id)  ON DELETE CASCADE, "
															+ "FOREIGN KEY(idEmpresa) REFERENCES " + TABLA_EMPRESA + "(_id) ON DELETE CASCADE, "
															+ "FOREIGN KEY(idUsuarioCreador) REFERENCES " + TABLA_USUARIO + "(_id) ON DELETE CASCADE);";
	
	private static final String DATABASE_CREATE_COTIZACION = "CREATE table " + TABLA_COTIZACION + " ("
															+"_id TEXT PRIMARY KEY NOT NULL, "
															+ "numCotizacion INTEGER UNIQUE , "
															+ "fechaEnvio DATETIME DEFAULT NULL, "
															+ "fechaLeido DATETIME DEFAULT NULL, "
															+ "enviado INTEGER NOT NULL DEFAULT 0, "
															+ "recibido INTEGER NOT NULL DEFAULT 0, "
															+ "idUsuario TEXT NOT NULL, "
															+ "idEmpresa TEXT, "
															+ "descripcion TEXT, "
															+ "fechaCreacion DATETIME DEFAULT (datetime('now','localtime')), "
															+ "fechaModificacion DATETIME DEFAULT (datetime('now','localtime')), "
															+ "fechaSincronizacion DATETIME DEFAULT NULL, "
															+ "status TEXT NOT NULL DEFAULT 'activo', "
															+ "sincronizado INTEGER NOT NULL DEFAULT 0, "
															+ "FOREIGN KEY(idEmpresa) REFERENCES " + TABLA_EMPRESA + "(_id) ON DELETE CASCADE, "
															+ "FOREIGN KEY(idUsuario) REFERENCES " + TABLA_USUARIO + "(_id) ON DELETE CASCADE);";

	
	private static final String DATABASE_CREATE_EMPLEADO_COTIZACION = "CREATE table " + TABLA_EMPLEADO_COTIZACION + " ("
																	+ "idEmpleado TEXT NOT NULL, "
																	+ "idCotizacion TEXT NOT NULL, "
																	+ "fechaCreacion DATETIME DEFAULT (datetime('now','localtime')), "
																	+ "fechaModificacion DATETIME DEFAULT (datetime('now','localtime')), "
																	+ "fechaSincronizacion DATETIME DEFAULT NULL, "
																	+ "status TEXT NOT NULL DEFAULT 'activo', "
																	+ "sincronizado INTEGER NOT NULL DEFAULT 0, "
																	+ "primary key (idEmpleado, idCotizacion), "
																	+ "FOREIGN KEY(idEmpleado) REFERENCES " + TABLA_EMPLEADO + "(_id) ON DELETE CASCADE, " 
																	+ "FOREIGN KEY(idCotizacion) REFERENCES " + TABLA_COTIZACION + "(_id) ON DELETE CASCADE);";
	
	private static final String DATABASE_CREATE_SERVICIO = "CREATE table " + TABLA_SERVICIO + " ("
															+"_id TEXT PRIMARY KEY NOT NULL, "
															+ "nombre TEXT NOT NULL, "
															+ "precio REAL NOT NULL, "
															+ "descripcion TEXT NOT NULL, "
															+ "unidadMedicion TEXT NOT NULL, "
															+ "fechaCreacion DATETIME DEFAULT (datetime('now','localtime')), "
															+ "fechaModificacion DATETIME DEFAULT (datetime('now','localtime')), "
															+ "fechaSincronizacion DATETIME DEFAULT NULL, "
															+ "status TEXT NOT NULL DEFAULT 'activo', "
															+ "sincronizado INTEGER NOT NULL DEFAULT 0, "
															+ "inicial REAL NOT NULL);";
	
	private static final String DATABASE_CREATE_COTIZACION_SERVICIO = "CREATE table " + TABLA_COTIZACION_SERVICIO + " ("
																	+ "idCotizacion TEXT NOT NULL, "
																	+ "idServicio TEXT NOT NULL, "
																	+ "medida TEXT, "
																	+ "descripcion TEXT, "
																	+ "precio REAL NOT NULL, "
																	+ "inicial REAL NOT NULL, "
																	+ "fechaCreacion DATETIME DEFAULT (datetime('now','localtime')), "
																	+ "fechaModificacion DATETIME DEFAULT (datetime('now','localtime')), "
																	+ "fechaSincronizacion DATETIME DEFAULT NULL, "
																	+ "status TEXT NOT NULL DEFAULT 'activo', "
																	+ "sincronizado INTEGER NOT NULL DEFAULT 0, "
																	+ "primary key (idCotizacion, idServicio), "
																	+ "FOREIGN KEY(idCotizacion) REFERENCES " + TABLA_COTIZACION + "(_id) ON DELETE CASCADE, " 
																	+ "FOREIGN KEY(idServicio) REFERENCES " + TABLA_SERVICIO + "(_id) ON DELETE CASCADE);";

	
	private static final String DATABASE_CREATE_CONFIGURACION = "CREATE table " + TABLA_CONFIGURACION + " ("
															  + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
															  + "key TEXT NOT NULL, "
															  + "value TEXT NOT NULL);";
	
	private static final String DATABASE_CREATE_TAREA = "CREATE table " + TABLA_TAREA + " ("
													  + "_id TEXT PRIMARY KEY NOT NULL, "
													  + "nombre TEXT NOT NULL, "
													  + "fecha DATE NOT NULL, "
													  + "hora TEXT NOT NULL, "
													  + "descripcion TEXT, "
													  + "finalizada INTEGER NOT NULL DEFAULT 0, "
													  + "fechaFinalizacion DATETIME DEFAULT NULL, "
													  + "idEmpresa TEXT DEFAULT NULL, "
													  + "idEmpleado TEXT DEFAULT NULL, "
													  + "fechaCreacion DATETIME DEFAULT (datetime('now','localtime')), "
													  + "fechaModificacion DATETIME DEFAULT (datetime('now','localtime')), "
													  + "fechaSincronizacion DATETIME DEFAULT NULL, "
													  + "idUsuarioCreador TEXT NOT NULL, "
													  + "idUsuarioModificador TEXT NOT NULL, "
													  + "status TEXT NOT NULL DEFAULT 'activo', "
													  + "sincronizado INTEGER NOT NULL DEFAULT 0, "
													  + "FOREIGN KEY(idUsuarioCreador) REFERENCES " + TABLA_USUARIO + "(_id) ON DELETE CASCADE, " 
													  + "FOREIGN KEY(idUsuarioModificador) REFERENCES " + TABLA_USUARIO + "(_id) ON DELETE CASCADE, " 
													  + "FOREIGN KEY(idEmpresa) REFERENCES " + TABLA_EMPRESA + "(_id) ON DELETE CASCADE, " 
													  + "FOREIGN KEY(idEmpleado) REFERENCES " + TABLA_EMPLEADO + "(_id) ON DELETE CASCADE);";
	
	
	private static final String DATABASE_CREATE_HISTORICO = "CREATE table " + TABLA_HISTORICO + " ("
															+ "_id TEXT PRIMARY KEY NOT NULL, "
															+ "tipoRegistro TEXT NOT NULL, "
															+ "idCotizacion TEXT, "
															+ "idTarea TEXT, "
															+ "idEmpresa TEXT, "
															+ "idCheckin TEXT DEFAULT NULL, "
															+ "fechaCreacion DATETIME DEFAULT (datetime('now','localtime')), "
															+ "fechaModificacion DATETIME DEFAULT (datetime('now','localtime')), "
															+ "fechaSincronizacion DATETIME DEFAULT NULL, "
															+ "status TEXT NOT NULL DEFAULT 'activo', "
															+ "sincronizado INTEGER NOT NULL DEFAULT 0, "
															+ "idUsuarioCreador TEXT NOT NULL, "
															+ "FOREIGN KEY(idUsuarioCreador) REFERENCES " + TABLA_USUARIO + "(_id) ON DELETE CASCADE, " 
															+ "FOREIGN KEY(idCheckin) REFERENCES " + TABLA_CHECKIN + "(_id) ON DELETE CASCADE, "
															+ "FOREIGN KEY(idCotizacion) REFERENCES " + TABLA_COTIZACION + "(_id) ON DELETE CASCADE, "
															+ "FOREIGN KEY(idEmpresa) REFERENCES " + TABLA_EMPRESA + "(_id) ON DELETE CASCADE, " 
															+ "FOREIGN KEY(idTarea) REFERENCES " + TABLA_TAREA + "(_id) ON DELETE CASCADE);";
	
	private static final String DATABASE_CREATE_CHECKIN = "CREATE table " + TABLA_CHECKIN + " ("
														+"_id TEXT PRIMARY KEY NOT NULL, "
														+ "checkin DATETIME, "
														+ "latitud REAL, "
														+ "longitud REAL, "
														+ "checkout DATETIME, "
														+ "idUsuario TEXT, "
														+ "fechaCreacion DATETIME DEFAULT (datetime('now','localtime')), "
														+ "fechaModificacion DATETIME DEFAULT (datetime('now','localtime')), "
														+ "fechaSincronizacion DATETIME DEFAULT NULL, "
														+ "status TEXT NOT NULL DEFAULT 'activo', "
														+ "sincronizado INTEGER NOT NULL DEFAULT 0, "
														+ "FOREIGN KEY(idUsuario) REFERENCES " + TABLA_USUARIO + "(_id) ON DELETE CASCADE);";
	
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
		database.execSQL(DATABASE_CREATE_TAREA);
		database.execSQL(DATABASE_CREATE_CHECKIN);
		database.execSQL(DATABASE_CREATE_HISTORICO);
		
	/*	Log.e("",DATABASE_CREATE_ROL);
		Log.e("",DATABASE_CREATE_USUARIO);
		Log.e("",DATABASE_CREATE_PERMISO);
		Log.e("",DATABASE_CREATE_ROL_PERMISO);
		Log.e("",DATABASE_CREATE_EMPRESA);
		Log.e("",DATABASE_CREATE_EMPLEADO);
		Log.e("",DATABASE_CREATE_COTIZACION);
		Log.e("",DATABASE_CREATE_EMPLEADO_COTIZACION);
		Log.e("",DATABASE_CREATE_SERVICIO);
		Log.e("",DATABASE_CREATE_COTIZACION_SERVICIO);
		Log.e("",DATABASE_CREATE_CONFIGURACION);
		Log.e("",DATABASE_CREATE_TAREA);
		Log.e("",DATABASE_CREATE_CHECKIN);
		Log.e("",DATABASE_CREATE_HISTORICO);*/

		this.insertarConfiguracion(database);
	//	this.insertarRegistros(database);
	}

	
	private void insertarConfiguracion(SQLiteDatabase database) {
		String insertServer = "INSERT INTO "+ TABLA_CONFIGURACION + "("+Configuracion.KEY+", " + Configuracion.VALUE + ") VALUES('"+Configuracion.NOMBRE_SERVIDOR+"','"+dirServidor+"')";
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
		database.execSQL("DROP TABLE IF EXISTS "+TABLA_CONFIGURACION);
		database.execSQL("DROP TABLE IF EXISTS "+TABLA_TAREA);
		database.execSQL("DROP TABLE IF EXISTS "+TABLA_HISTORICO);
		database.execSQL("DROP TABLE IF EXISTS "+TABLA_CHECKIN);
		onCreate(database);

	}
	
	/*
	 * TEMPORAL: ESTA INFO. SE DEBE OBTENER DE LA NUBE.
	 */
	private void insertarRegistros(SQLiteDatabase database){
		
/*id:1*/String insertRol1 = "INSERT INTO "+ TABLA_ROL + "(_id,nombre, descripcion) VALUES('1','Administrador','Tiene acceso a la lista de todos los clientes de RHCimax y a las acciones de eliminar y modificar.')";
/*id:2*/String insertRol2 = "INSERT INTO "+ TABLA_ROL + "(_id,nombre, descripcion) VALUES('2','Usuario Avanzado','Tiene acceso a la lista de todos los clientes de RHCimax y a la accion de modificar.')";
/*id:3*/String insertRol3 = "INSERT INTO "+ TABLA_ROL + "(_id,nombre, descripcion) VALUES('3','Usuario','Tiene acceso solo a la lista de clientes de RHCimax ingresador por el y a la accion de modificar.')";
		
/*id:1*/String insertPermiso1 =  "INSERT INTO "+ TABLA_PERMISO + "(_id,nombre, descripcion) VALUES('1','ListarTodo','Se permite el acceso a todas las listas')";
/*id:2*/String insertPermiso2 =  "INSERT INTO "+ TABLA_PERMISO + "(_id,nombre, descripcion) VALUES('2','ListarPropios','Se permite el acceso a la lista de registros que pertenecen a este usuario.')";

/*id:3*/String insertPermiso3 =  "INSERT INTO "+ TABLA_PERMISO + "(_id,nombre, descripcion) VALUES('3','ModificarTodo','Se permite el acceso a la acción modificar de todo.')";
/*id:4*/String insertPermiso4 =  "INSERT INTO "+ TABLA_PERMISO + "(_id,nombre, descripcion) VALUES('4','ModificarPropios','Se permite el acceso a la acción modificar de los registros que son propios del usuario.')";

/*id:5*/String insertPermiso5 =  "INSERT INTO "+ TABLA_PERMISO + "(_id,nombre, descripcion) VALUES('5','EliminarTodo','Se permite el acceso a la acción eliminar.')";
/*id:6*/String insertPermiso6 =  "INSERT INTO "+ TABLA_PERMISO + "(_id,nombre, descripcion) VALUES('6','EliminarPropios','Se permite el acceso a la acción eliminar de los registros que son propios del usuario.')";

		String usuario = "INSERT INTO "+ TABLA_USUARIO + "(_id,idRol,login, password, correo, token) VALUES('1','1','administrador','1234','NONE','11aa22bb33cc')";
		String usuario2 = "INSERT INTO "+ TABLA_USUARIO + "(_id,idRol,login, password, correo, token) VALUES('2','2','usuario avanzado','1234','NONE','11aa22bb33cc')";
		String usuario3 = "INSERT INTO "+ TABLA_USUARIO + "(_id,idRol,login, password, correo, token) VALUES('3','3','usuario','1234','NONE','11aa22bb33cc')";

		/*Rol administrador tiene todos los permisos generales*/
		String rolPer =  "INSERT INTO "+ TABLA_ROL_PERMISO + "(idRol,idPermiso) VALUES('1','1')";
		String rolPer2 =  "INSERT INTO "+ TABLA_ROL_PERMISO + "(idRol,idPermiso) VALUES('1','3')";
		String rolPer3 =  "INSERT INTO "+ TABLA_ROL_PERMISO + "(idRol,idPermiso) VALUES('1','5')";
		
		/*Rol Usuario Avanzado tiene todos los permisos excepto eliminar y listar todo. Puede modificar todo.*/
		String rolPer4 =  "INSERT INTO "+ TABLA_ROL_PERMISO + "(idRol,idPermiso) VALUES('2','2')";
		String rolPer5 =  "INSERT INTO "+ TABLA_ROL_PERMISO + "(idRol,idPermiso) VALUES('2','3')";
		String rolPer6 =  "INSERT INTO "+ TABLA_ROL_PERMISO + "(idRol,idPermiso) VALUES('2','6')";
		
		/*Rol usuario tiene todos los permisos excepto eliminar y listar todo. Puede modificar solo sus propios registros.*/
		String rolPer7 =  "INSERT INTO "+ TABLA_ROL_PERMISO + "(idRol,idPermiso) VALUES('3','2')";
		String rolPer8 =  "INSERT INTO "+ TABLA_ROL_PERMISO + "(idRol,idPermiso) VALUES('3','4')";
		String rolPer9 =  "INSERT INTO "+ TABLA_ROL_PERMISO + "(idRol,idPermiso) VALUES('3','6')";

		database.execSQL(insertRol1);
		database.execSQL(insertRol2);
		database.execSQL(insertRol3);
		database.execSQL(insertPermiso1);
		database.execSQL(insertPermiso2);
		database.execSQL(insertPermiso3);
		database.execSQL(insertPermiso4);
		database.execSQL(insertPermiso5);
		database.execSQL(insertPermiso6);

		database.execSQL(usuario);
		database.execSQL(usuario2);
		database.execSQL(usuario3);
		database.execSQL(rolPer);
		database.execSQL(rolPer2);
		database.execSQL(rolPer3);
		database.execSQL(rolPer4);
		database.execSQL(rolPer5);
		database.execSQL(rolPer6);
		database.execSQL(rolPer7);
		database.execSQL(rolPer8);
		database.execSQL(rolPer9);
		
		String serv1 =  "INSERT INTO "+ TABLA_SERVICIO + "(_id,nombre,precio,inicial,unidadMedicion,descripcion,status) VALUES('1','Reclutamiento',0,5000,'persona','Reclutamiento:  Es un servicio para reclutar personal.','Activo')";
		String serv2 =  "INSERT INTO "+ TABLA_SERVICIO + "(_id,nombre,precio,inicial,unidadMedicion,descripcion,status) VALUES('2','Merchandiser',300,0,'horas', 'Merchandiser: Es un servicio para Merchandiser.','Activo')";
		String serv3 =  "INSERT INTO "+ TABLA_SERVICIO + "(_id,nombre,precio,inicial,unidadMedicion,descripcion,status) VALUES('3','Laptop Apple',0,20000,'ninguno', 'Laptop Apple: Laptop marque Apple 13.','Activo')";
		String serv4 =  "INSERT INTO "+ TABLA_SERVICIO + "(_id,nombre,precio,inicial,unidadMedicion,descripcion,status) VALUES('4','Mantenimiento y limpieza',800,0,'dias', 'Mantenimiento y limpieza: El nivel microestructural o local está asociado con el concepto de cohesión. Se refiere a uno de los fenómenos propios de la coherencia, el de las relaciones particulares y locales que se dan entre elementos lingüísticos, tanto los que remiten unos a otros como los que tienen la función de conectar y organizar.','Activo')";
		String serv5 =  "INSERT INTO "+ TABLA_SERVICIO + "(_id,nombre,precio,inicial,unidadMedicion,descripcion,status) VALUES('5','Canal Makro comercializadora de bienes y servicios',300,0,'semanas', 'Canal Makro comercializadora: El nivel microestructural o local está asociado con el concepto de cohesión. Se refiere a uno de los fenómenos propios de la coherencia, el de las relaciones particulares y locales que se dan entre elementos lingüísticos, tanto los que remiten unos a otros como los que tienen la función de conectar y organizar.','Activo')";
		String serv6 =  "INSERT INTO "+ TABLA_SERVICIO + "(_id,nombre,precio,inicial,unidadMedicion,descripcion,status) VALUES('6','Canales',0,40,'persona', 'Canales: El nivel microestructural o local está asociado con el concepto de cohesión. Se refiere a uno de los fenómenos propios de la coherencia, el de las relaciones particulares y locales que se dan entre elementos lingüísticos, tanto los que remiten unos a otros como los que tienen la función de conectar y organizar.','Activo')";
		String serv7 =  "INSERT INTO "+ TABLA_SERVICIO + "(_id,nombre,precio,inicial,unidadMedicion,descripcion,status) VALUES('7','Comercio',300,0,'horas', 'Comercio: El nivel microestructural o local está asociado con el concepto de cohesión. Se refiere a uno de los fenómenos propios de la coherencia, el de las relaciones particulares y locales que se dan entre elementos lingüísticos, tanto los que remiten unos a otros como los que tienen la función de conectar y organizar.','Activo')";
		String serv8 =  "INSERT INTO "+ TABLA_SERVICIO + "(_id,nombre,precio,inicial,unidadMedicion,descripcion,status) VALUES('8','servicios',300,100,'ninguno', 'servicios: El nivel microestructural o local está asociado con el concepto de cohesión. Se refiere a uno de los fenómenos propios de la coherencia, el de las relaciones particulares y locales que se dan entre elementos lingüísticos, tanto los que remiten unos a otros como los que tienen la función de conectar y organizar.','Activo')";
		String serv9 =  "INSERT INTO "+ TABLA_SERVICIO + "(_id,nombre,precio,inicial,unidadMedicion,descripcion,status) VALUES('9','industria',300,0,'dias', 'industria: El nivel microestructural o local está asociado con el concepto de cohesión. Se refiere a uno de los fenómenos propios de la coherencia, el de las relaciones particulares y locales que se dan entre elementos lingüísticos, tanto los que remiten unos a otros como los que tienen la función de conectar y organizar.','Activo')";
		String serv10 =  "INSERT INTO "+ TABLA_SERVICIO + "(_id,nombre,precio,inicial,unidadMedicion,descripcion,status) VALUES('10','nose fuego',300,0,'ninguno', 'nose fuego: El nivel microestructural o local está asociado con el concepto de cohesión. Se refiere a uno de los fenómenos propios de la coherencia, el de las relaciones particulares y locales que se dan entre elementos lingüísticos, tanto los que remiten unos a otros como los que tienen la función de conectar y organizar.','Activo')";
		
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
