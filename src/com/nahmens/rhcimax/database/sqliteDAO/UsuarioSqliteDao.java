package com.nahmens.rhcimax.database.sqliteDAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.nahmens.rhcimax.database.ConexionBD;
import com.nahmens.rhcimax.database.DataBaseHelper;
import com.nahmens.rhcimax.database.DAO.UsuarioDAO;
import com.nahmens.rhcimax.database.modelo.Usuario;

/*
 * Clase que contendrá toda la funcionalidad para realizar consultas 
 * a la BD de la clase usuario.
 */
public class UsuarioSqliteDao implements UsuarioDAO{

	@Override
	public Boolean insertarUsuario(Context context, Usuario usuario) {
		Boolean insertado = false;
		ConexionBD conexion = new ConexionBD(context);
		try{
			
			conexion.open();

			ContentValues values = new ContentValues();

			values.put("login",usuario.getLogin());
			values.put("password",usuario.getPassword());
			values.put("correo",usuario.getCorreo());

			long value = conexion.getDatabase().insert(DataBaseHelper.TABLA_USUARIO, null,values);
			insertado = true;
			
			Log.e("UsuarioSqliteDao","result: "+ value);
		}finally{
			conexion.close();
		}
		
		return insertado;

	}

	@Override
	public void modificarUsuario(Context context, Usuario usuario) {
	}
	
	@Override
	public Usuario buscarUsuario(Context context, String nombre) {
		return null;
	}

	@Override
	public void listarUsuarios(Context context) {
		ConexionBD conexion = new ConexionBD(context);
		try{
			
			conexion.open();


			Cursor c = conexion.getDatabase().rawQuery("SELECT * FROM usuario", null);
			Log.e("UsuarioSqliteDao","path: "+ conexion.getDatabase().getPath());
			int id[]=new int[c.getCount()];
			int i=0;
			if (c.getCount() > 0) 
			{               
			    c.moveToFirst();
			    do {

			           id[i]=c.getInt(c.getColumnIndex("field_name"));
			           i++;
			    } while (c.moveToNext());
			    c.close();
			}

			
			
		}finally{
			conexion.close();
		}
		
	}
}
