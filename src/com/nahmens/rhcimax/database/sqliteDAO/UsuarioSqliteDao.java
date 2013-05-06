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
			
			if(value!=-1){
				insertado = true;
			}

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
	public Cursor listarUsuarios(Context context) {
		ConexionBD conexion = new ConexionBD(context);
		Cursor c = null;
		try{
			
			conexion.open();

			c = conexion.getDatabase().rawQuery("SELECT * FROM " + DataBaseHelper.TABLA_USUARIO, null);
			Log.e("UsuarioSqliteDao","path: "+ conexion.getDatabase().getPath());

		}finally{
			conexion.close();
		}
		
		return c;		
		
	}
}
