package com.nahmens.rhcimax.database.sqliteDAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.nahmens.rhcimax.database.ConexionBD;
import com.nahmens.rhcimax.database.DataBaseHelper;
import com.nahmens.rhcimax.database.DAO.EmpleadoDAO;
import com.nahmens.rhcimax.database.modelo.Empleado;

public class EmpleadoSqliteDao implements EmpleadoDAO{

	@Override
	public Boolean insertarEmpleado(Context contexto, Empleado empleado) {
		ConexionBD conexion = new ConexionBD(contexto);
		Boolean insertado = false;

		try{
			conexion.open();

			ContentValues values = new ContentValues();

			values.put("nombre",empleado.getNombre());
			values.put("apellido",empleado.getApellido());
			values.put("posicion",empleado.getPosicion());
			values.put("email", empleado.getEmail());
			values.put("telfOficina",empleado.getTelfOficina());
			values.put("celular",empleado.getCelular());
			values.put("pin",empleado.getPin());
			values.put("linkedin",empleado.getLinkedin());
			values.put("descripcion",empleado.getDescripcion());
			values.put("idEmpresa",empleado.getIdEmpresa());

			long value = conexion.getDatabase().insert(DataBaseHelper.TABLA_EMPLEADO, null,values);

			if(value!=-1){
				insertado = true;
			}

		}finally{
			conexion.close();
		}

		return insertado;
	}

	@Override
	public void modificarEmpleado(Context contexto, Empleado empleado) {
		// TODO Auto-generated method stub

	}

	@Override
	public void eliminarEmpleado(Context contexto, Empleado empleado) {
		// TODO Auto-generated method stub

	}

	@Override
	public Cursor listarEmpleados(Context contexto) {
		ConexionBD conexion = new ConexionBD(contexto);
		Cursor mCursor = null;
		try{

			conexion.open();

			mCursor = conexion.getDatabase().rawQuery("SELECT empresa._id, empleado.nombre as "+Empleado.NOMBRE+", empleado.apellido as "+Empleado.APELLIDO+", empresa.nombre as "+Empleado.EMPRESA+" FROM " + DataBaseHelper.TABLA_EMPLEADO 
												   + " INNER JOIN " + DataBaseHelper.TABLA_EMPRESA 
												   + " ON (empleado.idEmpresa=empresa._id) ORDER BY empleado.nombre", null);

			if (mCursor != null) {
				mCursor.moveToFirst();
			}
			
		}finally{
			conexion.close();
		}

		return mCursor;	
	}

}
