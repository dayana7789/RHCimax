package com.nahmens.rhcimax.database.sqliteDAO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.nahmens.rhcimax.database.ConexionBD;
import com.nahmens.rhcimax.database.DataBaseHelper;
import com.nahmens.rhcimax.database.DAO.PermisoDAO;
import com.nahmens.rhcimax.database.modelo.Permiso;
import com.nahmens.rhcimax.database.modelo.Rol_Permiso;
import com.nahmens.rhcimax.utils.Formato;

public class PermisoSqliteDao implements PermisoDAO{

	@Override
	public String insertarPermiso(Context context, Permiso permiso) {
		
		long value = -1;
		ConexionBD conexion = new ConexionBD(context);
		String idFila = null;

		if(permiso.getId() == null){
			idFila= new Formato().getNumeroAleatorio();
		}else{
			idFila = permiso.getId();
		}
		
		try{

			conexion.open();

			ContentValues values = new ContentValues();

			values.put(Permiso.ID, idFila);
			values.put(Permiso.NOMBRE, permiso.getNombre());
			values.put(Permiso.DESCRIPCION, permiso.getDescripcion());

			value = conexion.getDatabase().insertOrThrow(DataBaseHelper.TABLA_PERMISO, null,values);
			
			if(value==-1){
				idFila = ""+value;
			}

		}finally{
			conexion.close();
		}

		return idFila;
	}
	
	@Override
	public JSONArray buscarPermisos(Context contexto, String idRol) {
		ConexionBD conexion = new ConexionBD(contexto);
		Cursor mCursor = null;
		JSONArray permisos = new JSONArray();
		try{

			conexion.open();
			
			String sqlQuery = "SELECT permiso."+Permiso.ID+", permiso."+Permiso.NOMBRE
				            + " FROM " + DataBaseHelper.TABLA_ROL_PERMISO
				            + " LEFT JOIN " + DataBaseHelper.TABLA_PERMISO + " ON ( rol_permiso." + Rol_Permiso.ID_PERMISO+ " = permiso."+Permiso.ID+" ) "
				            + " WHERE "
				            + " rol_permiso."+Rol_Permiso.ID_ROL+"="+idRol;
			

			mCursor = conexion.getDatabase().rawQuery(sqlQuery,null);
			
			if (mCursor != null) {
				mCursor.moveToFirst();
				
				while(!mCursor.isAfterLast()){
					
					JSONObject permiso = new JSONObject();
					permiso.put(Permiso.ID, mCursor.getString(mCursor.getColumnIndex(Permiso.ID)));
					permiso.put(Permiso.NOMBRE,  mCursor.getString(mCursor.getColumnIndex(Permiso.NOMBRE)));
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


	@Override
	public int eliminarPermisos(Context context) {
		ConexionBD conexion = new ConexionBD(context);
		int numCol = 0;
		
		try{
			conexion.open();
			numCol = conexion.getDatabase().delete(DataBaseHelper.TABLA_PERMISO, "1", null);

		}finally{
			conexion.close();
		}

		return numCol;	
	}

	@Override
	public boolean modificarPermiso(Context contexto, Permiso permiso) {
		ConexionBD conexion = new ConexionBD(contexto);
		boolean modificado = false;

		try{
			conexion.open();

			ContentValues values = new ContentValues();

			values.put(Permiso.NOMBRE, permiso.getNombre());
			values.put(Permiso.DESCRIPCION, permiso.getDescripcion());

			int value = conexion.getDatabase().update(DataBaseHelper.TABLA_PERMISO, values, "_id=?", new String []{permiso.getId()});

			if(value!=0){
				modificado = true;
			}

		}finally{
			conexion.close();
		}

		return modificado;
	}

}
