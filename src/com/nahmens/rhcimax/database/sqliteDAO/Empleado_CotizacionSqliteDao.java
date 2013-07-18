package com.nahmens.rhcimax.database.sqliteDAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.nahmens.rhcimax.database.ConexionBD;
import com.nahmens.rhcimax.database.DataBaseHelper;
import com.nahmens.rhcimax.database.DAO.Empleado_CotizacionDAO;
import com.nahmens.rhcimax.database.modelo.Empleado_Cotizacion;

public class Empleado_CotizacionSqliteDao implements Empleado_CotizacionDAO{

	@Override
	public String insertar(Context contexto, String idEmpleado,
			String idCotizacion) {

		ConexionBD conexion = new ConexionBD(contexto);
		long value = -1;

		try{
			conexion.open();

			ContentValues values = new ContentValues();

			values.put(Empleado_Cotizacion.ID_COTIZACION, idCotizacion);
			values.put(Empleado_Cotizacion.ID_EMPLEADO, idEmpleado);

			value = conexion.getDatabase().insertOrThrow(DataBaseHelper.TABLA_EMPLEADO_COTIZACION, null,values);


		}finally{
			conexion.close();
		}

		return value+"";
	}

	@Override
	public Cursor listarEmpleado_CotizacionNoSync(Context contexto) {
		ConexionBD conexion = new ConexionBD(contexto);
		Cursor mCursor = null;
		try{

			conexion.open();

			mCursor = conexion.getDatabase().query(DataBaseHelper.TABLA_EMPLEADO_COTIZACION, null , Empleado_Cotizacion.FECHA_SINCRONIZACION + "= NULL OR " + Empleado_Cotizacion.FECHA_MODIFICACION + " > " +Empleado_Cotizacion.FECHA_SINCRONIZACION ,null, null, null, null);


			if (mCursor != null) {
				mCursor.moveToFirst();
			}

		}finally{
			conexion.close();
		}

		return mCursor;		
	}

}
