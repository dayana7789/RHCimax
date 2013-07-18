package com.nahmens.rhcimax.database.sqliteDAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.nahmens.rhcimax.database.ConexionBD;
import com.nahmens.rhcimax.database.DataBaseHelper;
import com.nahmens.rhcimax.database.DAO.UsuarioDAO;
import com.nahmens.rhcimax.database.modelo.Usuario;
import com.nahmens.rhcimax.utils.Formato;

/*
 * Clase que contendrá toda la funcionalidad para realizar consultas 
 * a la BD de la clase usuario.
 */
public class UsuarioSqliteDao implements UsuarioDAO{

	@Override
	public String insertarUsuario(Context context, Usuario usuario) {
		long value = -1;
		String idFila = null;

		if(usuario.getId() == null){
			idFila= new Formato().getNumeroAleatorio();
		}else{
			idFila = usuario.getId();
		}
		
		ConexionBD conexion = new ConexionBD(context);
		try{

			conexion.open();

			ContentValues values = new ContentValues();

			values.put(Usuario.ID, idFila);
			values.put(Usuario.LOGIN,usuario.getLogin());
			values.put(Usuario.PASSWORD,usuario.getPassword());
			values.put(Usuario.CORREO,usuario.getCorreo());
			values.put(Usuario.ID_ROL,usuario.getIdRol());
			values.put(Usuario.TOKEN,usuario.getToken());

			value = conexion.getDatabase().insertOrThrow(DataBaseHelper.TABLA_USUARIO, null,values);
			
			if(value==-1){
				idFila = ""+value;
			}

		}finally{
			conexion.close();
		}

		return idFila;
	}

	@Override
	public boolean modificarUsuario(Context contexto, Usuario usuario) {
		ConexionBD conexion = new ConexionBD(contexto);
		boolean modificado = false;

		try{
			conexion.open();

			ContentValues values = new ContentValues();

			values.put(Usuario.LOGIN,usuario.getLogin());
			values.put(Usuario.PASSWORD,usuario.getPassword());
			values.put(Usuario.CORREO,usuario.getCorreo());
			values.put(Usuario.ID_ROL,usuario.getIdRol());
			values.put(Usuario.TOKEN,usuario.getToken());

			int value = conexion.getDatabase().update(DataBaseHelper.TABLA_USUARIO, values, "_id=?", new String []{usuario.getId()});

			if(value!=0){
				modificado = true;
			}

		}finally{
			conexion.close();
		}

		return modificado;
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
				usu = new Usuario(mCursor.getString(mCursor.getColumnIndex(Usuario.ID)), 
				         mCursor.getString(mCursor.getColumnIndex(Usuario.LOGIN)),
				         mCursor.getString(mCursor.getColumnIndex(Usuario.CORREO)),
				         mCursor.getString(mCursor.getColumnIndex(Usuario.PASSWORD)),
				         mCursor.getString(mCursor.getColumnIndex(Usuario.ID_ROL)),
						 mCursor.getString(mCursor.getColumnIndex(Usuario.TOKEN)));
			}

		}finally{
			conexion.close();
		}

		return usu;

	}
	
	@Override
	public Usuario buscarUsuario(Context context, String idUsuario) {
		ConexionBD conexion = new ConexionBD(context);
		Cursor mCursor = null;
		Usuario usu = null;

		try{
			conexion.open();

			mCursor = conexion.getDatabase().query(DataBaseHelper.TABLA_USUARIO, null, Usuario.ID + "= ? ", new String[] {idUsuario}, null, null, null);

			if(mCursor.getCount()>0){
				mCursor.moveToFirst();
				usu = new Usuario(mCursor.getString(mCursor.getColumnIndex(Usuario.ID)), 
						         mCursor.getString(mCursor.getColumnIndex(Usuario.LOGIN)),
						         mCursor.getString(mCursor.getColumnIndex(Usuario.CORREO)),
						         mCursor.getString(mCursor.getColumnIndex(Usuario.PASSWORD)),
						         mCursor.getString(mCursor.getColumnIndex(Usuario.ID_ROL)),
								 mCursor.getString(mCursor.getColumnIndex(Usuario.TOKEN)));
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
	
	@Override
	public int eliminarUsuarios(Context context) {
		ConexionBD conexion = new ConexionBD(context);
		int numCol = 0;
		
		try{
			conexion.open();
			numCol = conexion.getDatabase().delete(DataBaseHelper.TABLA_USUARIO, "1", null);

		}finally{
			conexion.close();
		}

		return numCol;		

	}

	@Override
	public Cursor listarUsuarioNoSync(Context contexto) {
		ConexionBD conexion = new ConexionBD(contexto);
		Cursor mCursor = null;
		try{

			conexion.open();

			mCursor = conexion.getDatabase().query(DataBaseHelper.TABLA_USUARIO, null , Usuario.FECHA_SINCRONIZACION + "= NULL OR " + Usuario.FECHA_MODIFICACION + " > " +Usuario.FECHA_SINCRONIZACION ,null, null, null, null);


			if (mCursor != null) {
				mCursor.moveToFirst();
			}

		}finally{
			conexion.close();
		}

		return mCursor;		
	}

}
