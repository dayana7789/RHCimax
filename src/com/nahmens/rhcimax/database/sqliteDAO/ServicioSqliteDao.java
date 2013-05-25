package com.nahmens.rhcimax.database.sqliteDAO;

import android.content.Context;
import android.database.Cursor;

import com.nahmens.rhcimax.database.ConexionBD;
import com.nahmens.rhcimax.database.DataBaseHelper;
import com.nahmens.rhcimax.database.DAO.ServicioDAO;
import com.nahmens.rhcimax.database.modelo.Empleado;
import com.nahmens.rhcimax.database.modelo.Empresa;
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
}
