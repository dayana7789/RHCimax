package com.nahmens.rhcimax.database.sqliteDAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

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
			values.put(Rol_Permiso.ID_PERMISO, rol_permiso.getIdPermiso());

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

	@Override
	public boolean existeRol_Permiso(Context contexto, Rol_Permiso rol_permiso) {
		ConexionBD conexion = new ConexionBD(contexto);
		Cursor mCursor = null;
		boolean existe = false;

		try{
			conexion.open();

			mCursor = conexion.getDatabase().query(DataBaseHelper.TABLA_ROL_PERMISO , null , Rol_Permiso.ID_PERMISO + " = ? AND " + Rol_Permiso.ID_ROL + " = ?", new String [] {rol_permiso.getIdPermiso(), rol_permiso.getIdRol()}, null, null, null);

			if (mCursor.getCount() > 0) {
				existe = true;
			}

		}finally{
			conexion.close();
		}

		return existe;
	}
}
