package com.nahmens.rhcimax.database.sqliteDAO;

import android.content.Context;
import android.database.Cursor;

import com.nahmens.rhcimax.database.ConexionBD;
import com.nahmens.rhcimax.database.DataBaseHelper;
import com.nahmens.rhcimax.database.DAO.ServicioDAO;
import com.nahmens.rhcimax.database.modelo.Servicio;

public class ServicioSqliteDao implements ServicioDAO{

	public Cursor listarServicios(Context contexto){
		ConexionBD conexion = new ConexionBD(contexto);
		Cursor mCursor = null;
		try{

			conexion.open();

			String sqlQuery = "";

			sqlQuery  = " SELECT *";
			sqlQuery += " FROM " + DataBaseHelper.TABLA_SERVICIO;
			sqlQuery += " WHERE " + Servicio.STATUS+ " LIKE '%activo%' ";
			sqlQuery += " ORDER BY " + Servicio.NOMBRE;

			mCursor = conexion.getDatabase().rawQuery(sqlQuery,null);
			
			if (mCursor != null) {
				mCursor.moveToFirst();
			}

		}finally{
			conexion.close();
		}

		return mCursor;		
	}

	@Override
	public Servicio buscarServicio(Context contexto, String idServicio) {
		ConexionBD conexion = new ConexionBD(contexto);
		Cursor mCursor = null;
		Servicio servicio = null;
		
		try{
			conexion.open();

			mCursor = conexion.getDatabase().query(DataBaseHelper.TABLA_SERVICIO , null , Servicio.ID + " = ? ", new String [] {idServicio}, null, null, null);

			if (mCursor.getCount() > 0) {
				mCursor.moveToFirst();

				servicio = new Servicio( mCursor.getString(mCursor.getColumnIndex(Servicio.ID)), 
						mCursor.getString(mCursor.getColumnIndex(Servicio.NOMBRE)), 
						mCursor.getDouble(mCursor.getColumnIndex(Servicio.PRECIO)), 
						mCursor.getString(mCursor.getColumnIndex(Servicio.DESCRIPCION)), 
						mCursor.getString(mCursor.getColumnIndex(Servicio.STATUS)), 
						mCursor.getString(mCursor.getColumnIndex(Servicio.UNIDAD_MEDICION)), 
						mCursor.getDouble(mCursor.getColumnIndex(Servicio.INICIAL)));
			}
			
		}finally{
			conexion.close();
		}

		return servicio;	
	}
}
