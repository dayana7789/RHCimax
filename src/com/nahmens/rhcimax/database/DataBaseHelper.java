package com.nahmens.rhcimax.database;

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
															+ "direccionFiscal TEXT, "
															+ "direccionComercial TEXT);";
	
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
															+ "FOREIGN KEY(idEmpresa) REFERENCES " + TABLA_EMPRESA + "(_id));";
	
	private static final String DATABASE_CREATE_COTIZACION = "CREATE table " + TABLA_COTIZACION + " ("
															+"_id INTEGER PRIMARY KEY AUTOINCREMENT, "
															+ "fechaEnvio TEXT NOT NULL, "
															+ "fechaLeido TEXT, "
															+ "enviado INTEGER NOT NULL, "
															+ "recibido INTEGER NOT NULL, "
															+ "idUsuario INTEGER NOT NULL, "
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
															+ "descripcion TEXT NOT NULL);";
	
	private static final String DATABASE_CREATE_COTIZACION_SERVICIO = "CREATE table " + TABLA_COTIZACION_SERVICIO + " ("
																	+ "idCotizacion INTEGER NOT NULL, "
																	+ "idServicio INTEGER NOT NULL, "
																	+ "primary key (idCotizacion, idServicio), "
																	+ "FOREIGN KEY(idCotizacion) REFERENCES " + TABLA_COTIZACION + "(_id), " 
																	+ "FOREIGN KEY(idServicio) REFERENCES " + TABLA_SERVICIO + "(_id));";

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

		this.insertarRegistros(database);
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
	}

}
