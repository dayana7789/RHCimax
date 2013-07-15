package com.nahmens.rhcimax.database.sqliteDAO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.database.Cursor;

import com.nahmens.rhcimax.database.ConexionBD;
import com.nahmens.rhcimax.database.DataBaseHelper;
import com.nahmens.rhcimax.database.DAO.PermisoDAO;
import com.nahmens.rhcimax.database.modelo.Permiso;
import com.nahmens.rhcimax.database.modelo.Rol_Permiso;

public class PermisoSqliteDao implements PermisoDAO{

	
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

}
