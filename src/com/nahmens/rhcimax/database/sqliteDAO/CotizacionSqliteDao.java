package com.nahmens.rhcimax.database.sqliteDAO;

import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.nahmens.rhcimax.database.ConexionBD;
import com.nahmens.rhcimax.database.DataBaseHelper;
import com.nahmens.rhcimax.database.DAO.CotizacionDAO;
import com.nahmens.rhcimax.database.modelo.Cotizacion;
import com.nahmens.rhcimax.utils.Formato;
import com.nahmens.rhcimax.utils.FormatoFecha;

public class CotizacionSqliteDao implements CotizacionDAO{

	public String insertarCotizacion(Context contexto, String idUsuario, String idEmpresa, String descripcion){

		ConexionBD conexion = new ConexionBD(contexto);
		long value = -1;
		String idFila = new Formato().getNumeroAleatorio();
		int numCotizacion = 1;
		
		try{
			conexion.open();

			String sqlQuery  = " SELECT MAX("+Cotizacion.NUM_COTIZACION+") + 1 FROM " + DataBaseHelper.TABLA_COTIZACION;

			Cursor mCursor = conexion.getDatabase().rawQuery(sqlQuery,null);

			if (mCursor != null) {
				mCursor.moveToFirst();
				numCotizacion = mCursor.getInt(0);
			}
			
			if(numCotizacion==0){
				numCotizacion = 1;
			}

			Log.e("numCotizacion", ""+numCotizacion);
			
			ContentValues values = new ContentValues();

			values.put(Cotizacion.ID, idFila);
			values.put(Cotizacion.ID_USUARIO, idUsuario);
			values.put(Cotizacion.ID_EMPRESA, idEmpresa);
			values.put(Cotizacion.NUM_COTIZACION,numCotizacion);
			values.put(Cotizacion.DESCRIPCION, descripcion);
			value = conexion.getDatabase().insert(DataBaseHelper.TABLA_COTIZACION, null,values);

			if(value==-1){
				idFila = ""+value;
			}

		}finally{
			conexion.close();
		}

		return idFila;
	}

	@Override
	public boolean eliminarCotizacion(Context contexto, String idCotizacion) {
		ConexionBD conexion = new ConexionBD(contexto);
		boolean eliminado = false;

		try{
			conexion.open();

			long value = conexion.getDatabase().delete(DataBaseHelper.TABLA_COTIZACION, Cotizacion.ID+"=?", new String[]{idCotizacion});

			if(value!=0){
				eliminado = true;
			}

		}finally{
			conexion.close();
		}

		return eliminado;
	}
	
	@Override
	public boolean sincronizarCotizacion(Context contexto, String idCotizacion) {
		ConexionBD conexion = new ConexionBD(contexto);
		boolean sincronizado = false;

		try{
			conexion.open();

			ContentValues contenido = new ContentValues();
			contenido.put(Cotizacion.FECHA_SINCRONIZACION, FormatoFecha.darFormatoDateTimeUS(new Date()));

			int value = conexion.getDatabase().update(DataBaseHelper.TABLA_COTIZACION, contenido, "_id=?", new String []{idCotizacion});

			if(value!=0){
				sincronizado = true;
			}

		}finally{
			conexion.close();
		}

		return sincronizado;
	}

}
