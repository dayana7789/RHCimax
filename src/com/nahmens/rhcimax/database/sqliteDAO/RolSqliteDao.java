package com.nahmens.rhcimax.database.sqliteDAO;

import android.content.Context;
import android.database.Cursor;

import com.nahmens.rhcimax.database.ConexionBD;
import com.nahmens.rhcimax.database.DataBaseHelper;
import com.nahmens.rhcimax.database.DAO.RolDAO;
import com.nahmens.rhcimax.database.modelo.Rol;

public class RolSqliteDao implements RolDAO {
	
	public Rol buscarRolUsuario(Context contexto, String idUsuario){
		ConexionBD conexion = new ConexionBD(contexto);
		Cursor mCursor = null;
		Rol rol = null;

		try{
			conexion.open();

			mCursor = conexion.getDatabase().query(DataBaseHelper.TABLA_ROL , null , Rol.ID + " = ? ", new String [] {idUsuario}, null, null, null);

			if (mCursor.getCount() > 0) {
				mCursor.moveToFirst();

				rol = new Rol( mCursor.getInt(mCursor.getColumnIndex(Rol.ID)), 
						mCursor.getString(mCursor.getColumnIndex(Rol.NOMBRE)), 
						mCursor.getString(mCursor.getColumnIndex(Rol.DESCRIPCION)));
			}

		}finally{
			conexion.close();
		}

		return rol;
	}

}
