package com.nahmens.rhcimax.database.sqliteDAO;

import android.content.ContentValues;
import android.content.Context;

import com.nahmens.rhcimax.database.ConexionBD;
import com.nahmens.rhcimax.database.DataBaseHelper;
import com.nahmens.rhcimax.database.DAO.Rol_PermisoDAO;
import com.nahmens.rhcimax.database.modelo.Rol_Permiso;

public class Rol_PermisoSqliteDao implements Rol_PermisoDAO{

	@Override
	public String insertaRol_Permiso(Context context, Rol_Permiso rol_permiso) {
		
		long value = -1;
		ConexionBD conexion = new ConexionBD(context);
		try{

			conexion.open();

			ContentValues values = new ContentValues();

			values.put(Rol_Permiso.ID_ROL, rol_permiso.getIdRol());
			values.put(Rol_Permiso.ID_PERMISO, rol_permiso.getIdRol());

			value = conexion.getDatabase().insertOrThrow(DataBaseHelper.TABLA_ROL_PERMISO, null,values);

		}finally{
			conexion.close();
		}

		return value+"";
	}

	@Override
	public int eliminarRoles_Permisos(Context context) {
		ConexionBD conexion = new ConexionBD(context);
		int numCol = 0;
		
		try{
			conexion.open();
			numCol = conexion.getDatabase().delete(DataBaseHelper.TABLA_ROL_PERMISO, "1", null);

		}finally{
			conexion.close();
		}

		return numCol;	
	}

}
