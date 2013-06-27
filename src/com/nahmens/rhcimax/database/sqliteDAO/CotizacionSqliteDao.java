package com.nahmens.rhcimax.database.sqliteDAO;

import android.content.ContentValues;
import android.content.Context;

import com.nahmens.rhcimax.database.ConexionBD;
import com.nahmens.rhcimax.database.DataBaseHelper;
import com.nahmens.rhcimax.database.DAO.CotizacionDAO;
import com.nahmens.rhcimax.database.modelo.Cotizacion;

public class CotizacionSqliteDao implements CotizacionDAO{

	public long insertarCotizacion(Context contexto, String idUsuario, String idEmpresa, String descripcion){

		ConexionBD conexion = new ConexionBD(contexto);
		long value = -1;

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
	

}
