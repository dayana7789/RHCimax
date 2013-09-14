package com.nahmens.rhcimax.database.sqliteDAO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.nahmens.rhcimax.database.ConexionBD;
import com.nahmens.rhcimax.database.DataBaseHelper;
import com.nahmens.rhcimax.database.DAO.UsuarioDAO;
import com.nahmens.rhcimax.database.modelo.Permiso;
import com.nahmens.rhcimax.database.modelo.Rol;
import com.nahmens.rhcimax.database.modelo.Rol_Permiso;
import com.nahmens.rhcimax.database.modelo.Usuario;
import com.nahmens.rhcimax.database.modelo.Usuario_Rol;
import com.nahmens.rhcimax.utils.Utils;

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
			idFila= new Utils().getNumeroAleatorio();
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
			values.put(Usuario.TOKEN,usuario.getToken());
			values.put(Usuario.SINCRONIZADO,0);

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
	public Usuario buscarUsuarioByLogin(Context context, String login) {
		ConexionBD conexion = new ConexionBD(context);
		Cursor mCursor = null;
		Usuario usu = null;

		try{
			conexion.open();

			mCursor = conexion.getDatabase().query(DataBaseHelper.TABLA_USUARIO, null, "login=?", new String[] {login}, null, null, null);

			if(mCursor.getCount()>0){
				mCursor.moveToFirst();
				usu = new Usuario(mCursor.getString(mCursor.getColumnIndex(Usuario.ID)), 
				         mCursor.getString(mCursor.getColumnIndex(Usuario.LOGIN)),
				         mCursor.getString(mCursor.getColumnIndex(Usuario.PASSWORD)),
				         mCursor.getString(mCursor.getColumnIndex(Usuario.SALT)),
				         mCursor.getString(mCursor.getColumnIndex(Usuario.CORREO)),
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
				         mCursor.getString(mCursor.getColumnIndex(Usuario.PASSWORD)),
				         mCursor.getString(mCursor.getColumnIndex(Usuario.SALT)),
				         mCursor.getString(mCursor.getColumnIndex(Usuario.CORREO)),
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
	
	public JSONArray buscarPermisos(Context context, String idUsuario) {
		ConexionBD conexion = new ConexionBD(context);
		Cursor mCursor = null;
		JSONArray permisos = new JSONArray();
		try{
			conexion.open();
			
			String sqlQuery = "SELECT permiso." + Permiso.NOMBRE + ", permiso." + Permiso.ID 
							 + " FROM " + DataBaseHelper.TABLA_USUARIO + ", " + DataBaseHelper.TABLA_ROL
							 + ", " + DataBaseHelper.TABLA_USUARIO_ROL + ", " + DataBaseHelper.TABLA_PERMISO
							 + ", " + DataBaseHelper.TABLA_ROL_PERMISO + ""
							 + " WHERE"
							 + " usuario." + Usuario.ID + "= "+ idUsuario +" AND "
							 + "usuario." + Usuario.ID + " = usuario_rol."+Usuario_Rol.ID_USUARIO+" AND "
							 + "rol." + Rol.ID + " = usuario_rol."+Usuario_Rol.ID_ROL+" AND "
							 + "rol." + Rol.ID + " = rol_permiso."+Usuario_Rol.ID_ROL+" AND "
							 + "permiso." + Permiso.ID + " = rol_permiso."+Rol_Permiso.ID_PERMISO+";";
Log.e("QUERY: ", sqlQuery);

			mCursor = conexion.getDatabase().rawQuery(sqlQuery,null);
			
			if (mCursor != null) {
				mCursor.moveToFirst();
				
				while(!mCursor.isAfterLast()){
					
					JSONObject permiso = new JSONObject();
					permiso.put(Rol.ID, mCursor.getString(mCursor.getColumnIndex(Rol.ID)));
					permiso.put(Rol.NOMBRE,  mCursor.getString(mCursor.getColumnIndex(Rol.NOMBRE)));
					permisos.put(permiso);

					mCursor.moveToNext();
		        }
			}

		} catch (JSONException e) {
			e.printStackTrace();
		
		}finally{
			conexion.close();
		}

		return permisos;	
	}

}
