package com.nahmens.rhcimax.database.sqliteDAO;

import java.util.Date;

import android.content.ContentValues;
import android.content.Context;

import com.nahmens.rhcimax.database.ConexionBD;
import com.nahmens.rhcimax.database.DataBaseHelper;
import com.nahmens.rhcimax.database.DAO.CotizacionDAO;
import com.nahmens.rhcimax.database.modelo.Cotizacion;
import com.nahmens.rhcimax.database.modelo.Tarea;
import com.nahmens.rhcimax.utils.FormatoFecha;

public class CotizacionSqliteDao implements CotizacionDAO{

	public long insertarCotizacion(Context contexto, String idUsuario, String idEmpresa, String descripcion){

		ConexionBD conexion = new ConexionBD(contexto);
		long value = -1;
		
		
		if(idEmpresa.equals("0")){
			idEmpresa = null;
		}
		
		try{
			conexion.open();

			ContentValues values = new ContentValues();

			values.put(Cotizacion.ID_USUARIO, idUsuario);
			values.put(Cotizacion.ID_EMPRESA, idEmpresa);
			values.put(Cotizacion.DESCRIPCION, descripcion);
			value = conexion.getDatabase().insert(DataBaseHelper.TABLA_COTIZACION, null,values);


		}finally{
			conexion.close();
		}

		return value;
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
