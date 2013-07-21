package com.nahmens.rhcimax.database.sqliteDAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.nahmens.rhcimax.database.ConexionBD;
import com.nahmens.rhcimax.database.DataBaseHelper;
import com.nahmens.rhcimax.database.DAO.ServicioDAO;
import com.nahmens.rhcimax.database.modelo.Servicio;
import com.nahmens.rhcimax.utils.Formato;

public class ServicioSqliteDao implements ServicioDAO{

	@Override
	public String insertarServicio(Context context, Servicio servicio) {
		long value = -1;
		String idFila = null;

		if(servicio.getId() == null){
			idFila= new Formato().getNumeroAleatorio();
		}else{
			idFila = servicio.getId();
		}
		
		ConexionBD conexion = new ConexionBD(context);
		try{

			conexion.open();

			ContentValues values = new ContentValues();

			values.put(Servicio.ID, idFila);
			values.put(Servicio.NOMBRE,servicio.getNombre());
			values.put(Servicio.PRECIO,servicio.getPrecio());
			values.put(Servicio.DESCRIPCION,servicio.getDescripcion());
			values.put(Servicio.STATUS,servicio.getStatus());
			values.put(Servicio.UNIDAD_MEDICION,servicio.getUnidadMedicion());
			values.put(Servicio.INICIAL,servicio.getInicial());

			value = conexion.getDatabase().insertOrThrow(DataBaseHelper.TABLA_SERVICIO, null,values);
			
			if(value==-1){
				idFila = ""+value;
			}

		}finally{
			conexion.close();
		}

		return idFila;
	}
	
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

	@Override
	public Cursor listarServicioNoSync(Context contexto) {
		ConexionBD conexion = new ConexionBD(contexto);
		Cursor mCursor = null;
		try{

			conexion.open();

			mCursor = conexion.getDatabase().query(DataBaseHelper.TABLA_SERVICIO, null , Servicio.FECHA_SINCRONIZACION + " IS NULL OR " + Servicio.FECHA_MODIFICACION + " > " +Servicio.FECHA_SINCRONIZACION ,null, null, null, null);


			if (mCursor != null) {
				mCursor.moveToFirst();
			}

		}finally{
			conexion.close();
		}

		return mCursor;		
	}
}
