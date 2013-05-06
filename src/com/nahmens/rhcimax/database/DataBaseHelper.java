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

	/*Creacion de tablas*/
	private static final String DATABASE_CREATE_ROL = "CREATE table " + TABLA_ROL + " ("
													+"_id INTEGER PRIMARY KEY AUTOINCREMENT, "
													+ "nombre TEXT NOT NULL, "
													+ "descripcion TEXT NOT NULL);";

	
	private static final String DATABASE_CREATE_USUARIO = "CREATE table " + TABLA_USUARIO + " ("
														+"_id INTEGER PRIMARY KEY AUTOINCREMENT, "
													//	+ "idRol INTEGER,"
														+ "login TEXT NOT NULL, "
														+ "password TEXT NOT NULL, "
														+ "correo TEXT NOT NULL)";
													//	+ "FOREIGN KEY(idRol) REFERENCES " + TABLA_ROL + "(_id));";
	
	private static final String DATABASE_CREATE_PERMISO = "CREATE table " + TABLA_PERMISO + " ("
														+"_id INTEGER PRIMARY KEY AUTOINCREMENT, "
														+ "nombre TEXT NOT NULL, "
														+ "descripcion TEXT NOT NULL);";
	
	private static final String DATABASE_CREATE_ROL_PERMISO = "CREATE table " + TABLA_ROL_PERMISO + " ("
															+"_id INTEGER PRIMARY KEY AUTOINCREMENT, "
															+ "idRol INTEGER, "
															+ "idPermiso INTEGER, "
															+ "FOREIGN KEY(idRol) REFERENCES " + TABLA_ROL + "(_id), " 
															+ "FOREIGN KEY(idPermiso) REFERENCES " + TABLA_PERMISO + "(_id));";
	
	

	public DataBaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	// Este método se llama al momento en el que se crea la BD
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE_ROL);
		database.execSQL(DATABASE_CREATE_USUARIO);
		database.execSQL(DATABASE_CREATE_PERMISO);
		database.execSQL(DATABASE_CREATE_ROL_PERMISO);
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
		onCreate(database);

	}

}
