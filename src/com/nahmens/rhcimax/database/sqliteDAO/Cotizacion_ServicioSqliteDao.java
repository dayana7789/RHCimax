package com.nahmens.rhcimax.database.sqliteDAO;

import android.content.ContentValues;
import android.content.Context;

import com.nahmens.rhcimax.database.ConexionBD;
import com.nahmens.rhcimax.database.DataBaseHelper;
import com.nahmens.rhcimax.database.DAO.Cotizacion_ServicioDAO;
import com.nahmens.rhcimax.database.modelo.Cotizacion_Servicio;

public class Cotizacion_ServicioSqliteDao implements Cotizacion_ServicioDAO{

	@Override
	public long insertar(Context contexto, String idServicio, String idCotizacion, String medida){

		ConexionBD conexion = new ConexionBD(contexto);
		long value = -1;

		try{
			conexion.open();

			ContentValues values = new ContentValues();

			values.put(Cotizacion_Servicio.ID_COTIZACION, idCotizacion);
			values.put(Cotizacion_Servicio.ID_SERVICIO, idServicio);
			values.put(Cotizacion_Servicio.MEDIDA, medida);

			value = conexion.getDatabase().insert(DataBaseHelper.TABLA_COTIZACION_SERVICIO, null,values);


		}finally{
			conexion.close();
		}

		return value;
	}
}

