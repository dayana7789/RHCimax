package com.nahmens.rhcimax.database;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/*
 * Clase encargada de abrir y cerrar la conexion con la BD.
 */
public class ConexionBD {
	private Context context;
	private SQLiteDatabase database;
	private DataBaseHelper dbHelper;
	
	public ConexionBD(Context context) {
		this.context = context;
	}

	public ConexionBD open() throws SQLException {
		this.dbHelper = new DataBaseHelper(context);
		this.database = dbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		dbHelper.close();
	}

	/*Getters y setters*/
	public SQLiteDatabase getDatabase() {
		return database;
	}

	public void setDatabase(SQLiteDatabase database) {
		this.database = database;
	}

}
