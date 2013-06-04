package com.nahmens.rhcimax.database.sqliteDAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

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
	public Usuario buscarUsuario(Context context, String login, String password) {
		ConexionBD conexion = new ConexionBD(context);
		Cursor mCursor = null;
		Usuario usu = null;

		try{
			conexion.open();

			mCursor = conexion.getDatabase().query(DataBaseHelper.TABLA_USUARIO, null, "login=? and password=?", new String[] {login,password}, null, null, null);

			if(mCursor.getCount()>0){
				mCursor.moveToFirst();
				usu = new Usuario(mCursor.getInt(mCursor.getColumnIndex(Usuario.ID)), 
						         mCursor.getString(mCursor.getColumnIndex(Usuario.LOGIN)),
						         mCursor.getString(mCursor.getColumnIndex(Usuario.CORREO)),
						         mCursor.getString(mCursor.getColumnIndex(Usuario.PASSWORD)),
						         mCursor.getInt(mCursor.getColumnIndex(Usuario.ID_ROL)));
			}

		}finally{
			conexion.close();
		}

		return usu;

	}

	@Override
	public Cursor listarUsuarios(Context context) {
		ConexionBD conexion = new ConexionBD(context);
		Cursor mCursor = null;
		try{

			conexion.open();

			mCursor = conexion.getDatabase().rawQuery("SELECT * FROM " + DataBaseHelper.TABLA_USUARIO, null);

			if (mCursor != null) {
				mCursor.moveToFirst();
			}
			
			/*mCursor.moveToFirst();
			
			while (!mCursor.isAfterLast()) {
				String dbUsu = mCursor.getString(0);
				String dbPwd = mCursor.getString(1);
				Log.e("UsuarioSqliteDao","usu: ,"+ dbUsu + ", pass= ,"+dbPwd+",");
				Log.e("UsuarioSqliteDao","login: ,"+ login + ", password= ,"+password+",");

				if(dbUsu.equals(login) && dbPwd.equals(password)){
					entre = true;

					break;
				}

				i++;
				mCursor.moveToNext();
			}*/



		}finally{
			conexion.close();
		}

		return mCursor;		

	}
}
