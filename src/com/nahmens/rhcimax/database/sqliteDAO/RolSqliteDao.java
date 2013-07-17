package com.nahmens.rhcimax.database.sqliteDAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.nahmens.rhcimax.database.ConexionBD;
import com.nahmens.rhcimax.database.DataBaseHelper;
import com.nahmens.rhcimax.database.DAO.RolDAO;
import com.nahmens.rhcimax.database.modelo.Rol;
import com.nahmens.rhcimax.utils.Formato;

public class RolSqliteDao implements RolDAO {
	
	@Override
	public String insertarRol(Context context, Rol rol) {
		long value = -1;
		ConexionBD conexion = new ConexionBD(context);
		String idFila = null;
		
		if(rol.getId() == null){
			idFila= new Formato().getNumeroAleatorio();
		}else{
			idFila = rol.getId();
		}
		
		try{

			conexion.open();

			ContentValues values = new ContentValues();

			values.put(Rol.ID, idFila);
			values.put(Rol.NOMBRE, rol.getNombre());
			values.put(Rol.DESCRIPCION, rol.getDescripcion());

			value = conexion.getDatabase().insert(DataBaseHelper.TABLA_ROL, null,values);
			
			if(value==-1){
				idFila = ""+value;
			}

		}finally{
			conexion.close();
		}

		return idFila;
	}
	
	
	@Override
	public Rol buscarRolUsuario(Context contexto, String idUsuario){
		ConexionBD conexion = new ConexionBD(contexto);
		Cursor mCursor = null;
		Rol rol = null;

		try{
			conexion.open();

			mCursor = conexion.getDatabase().query(DataBaseHelper.TABLA_ROL , null , Rol.ID + " = ? ", new String [] {idUsuario}, null, null, null);

			if (mCursor.getCount() > 0) {
				mCursor.moveToFirst();

				rol = new Rol( mCursor.getString(mCursor.getColumnIndex(Rol.ID)), 
						mCursor.getString(mCursor.getColumnIndex(Rol.NOMBRE)), 
						mCursor.getString(mCursor.getColumnIndex(Rol.DESCRIPCION)));
			}

		}finally{
			conexion.close();
		}

		return rol;
	}


	@Override
	public int eliminarRoles(Context context) {
		ConexionBD conexion = new ConexionBD(context);
		int numCol = 0;
		
		try{
			conexion.open();
			numCol = conexion.getDatabase().delete(DataBaseHelper.TABLA_ROL, "1", null);

		}finally{
			conexion.close();
		}

		return numCol;	
	}

}
