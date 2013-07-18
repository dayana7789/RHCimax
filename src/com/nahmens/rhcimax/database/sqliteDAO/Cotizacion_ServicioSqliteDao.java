package com.nahmens.rhcimax.database.sqliteDAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.nahmens.rhcimax.database.ConexionBD;
import com.nahmens.rhcimax.database.DataBaseHelper;
import com.nahmens.rhcimax.database.DAO.Cotizacion_ServicioDAO;
import com.nahmens.rhcimax.database.modelo.Cotizacion_Servicio;
import com.nahmens.rhcimax.database.modelo.Servicio;

public class Cotizacion_ServicioSqliteDao implements Cotizacion_ServicioDAO{

	@Override
	public String insertar(Context contexto, Cotizacion_Servicio cot_serv){

		ConexionBD conexion = new ConexionBD(contexto);
		long value = -1;

		try{
			conexion.open();

			ContentValues values = new ContentValues();

			values.put(Cotizacion_Servicio.ID_COTIZACION, cot_serv.getIdCotizacion());
			values.put(Cotizacion_Servicio.ID_SERVICIO, cot_serv.getIdServicio());
			values.put(Cotizacion_Servicio.MEDIDA, cot_serv.getMedida());
			values.put(Cotizacion_Servicio.PRECIO, cot_serv.getPrecio());
			values.put(Cotizacion_Servicio.INICIAL, cot_serv.getInicial());
			values.put(Cotizacion_Servicio.DESCRIPCION, cot_serv.getDescripcion());

			value = conexion.getDatabase().insertOrThrow(DataBaseHelper.TABLA_COTIZACION_SERVICIO, null,values);


		}finally{
			conexion.close();
		}

		return value+"";
	}
	
	@Override
	public Cursor listarCotizacion_Servicio(Context contexto, String idCotizacion) {
		ConexionBD conexion = new ConexionBD(contexto);
		Cursor mCursor = null;
		String sqlQuery = null;
		try{

			conexion.open();

			sqlQuery  = "SELECT cotizacion_servicio."+ Cotizacion_Servicio.DESCRIPCION + ", cotizacion_servicio." + Cotizacion_Servicio.INICIAL;
			sqlQuery  +=", cotizacion_servicio."+Cotizacion_Servicio.MEDIDA+", cotizacion_servicio."+Cotizacion_Servicio.PRECIO;
			sqlQuery  +=", "+Servicio.NOMBRE+", "+Servicio.UNIDAD_MEDICION;
			sqlQuery  += " FROM " + DataBaseHelper.TABLA_COTIZACION_SERVICIO;
			sqlQuery  += " LEFT JOIN " + DataBaseHelper.TABLA_COTIZACION + " ON (cotizacion._id = cotizacion_servicio.idCotizacion)";
			sqlQuery  += " LEFT JOIN " + DataBaseHelper.TABLA_SERVICIO + " ON (servicio._id = cotizacion_servicio.idServicio)";
			sqlQuery  += " WHERE " +  Cotizacion_Servicio.ID_COTIZACION+ "="+idCotizacion;

			mCursor = conexion.getDatabase().rawQuery(sqlQuery , null);

			if (mCursor != null) {
				mCursor.moveToFirst();
			}
			
		}finally{
			conexion.close();
		}

		return mCursor;	
	}

	@Override
	public Cursor listarCotizacion_ServicioNoSync(Context contexto) {
		ConexionBD conexion = new ConexionBD(contexto);
		Cursor mCursor = null;
		try{

			conexion.open();

			mCursor = conexion.getDatabase().query(DataBaseHelper.TABLA_COTIZACION_SERVICIO, null , Cotizacion_Servicio.FECHA_SINCRONIZACION + "= NULL OR " + Cotizacion_Servicio.FECHA_MODIFICACION + " > " +Cotizacion_Servicio.FECHA_SINCRONIZACION ,null, null, null, null);


			if (mCursor != null) {
				mCursor.moveToFirst();
			}

		}finally{
			conexion.close();
		}

		return mCursor;		
	}

}

