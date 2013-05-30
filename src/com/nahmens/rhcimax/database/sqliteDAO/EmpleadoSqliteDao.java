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
	public boolean insertarEmpleado(Context contexto, Empleado empleado) {
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
	public boolean modificarEmpleado(Context contexto, Empleado empleado) {
		ConexionBD conexion = new ConexionBD(contexto);
		boolean modificado = false;
		
		try{
			conexion.open();
			
			ContentValues contenido = new ContentValues();
			contenido.put("idEmpresa", empleado.getIdEmpresa());
			contenido.put("nombre", empleado.getNombre());
			contenido.put("apellido", empleado.getApellido());
			contenido.put("posicion", empleado.getPosicion());
			contenido.put("email", empleado.getEmail());
			contenido.put("telfOficina", empleado.getTelfOficina());
			contenido.put("celular", empleado.getCelular());
			contenido.put("pin", empleado.getPin());
			contenido.put("linkedin", empleado.getLinkedin());
			contenido.put("descripcion", empleado.getDescripcion());

			int value = conexion.getDatabase().update(DataBaseHelper.TABLA_EMPLEADO, contenido, "_id=?", new String []{Integer.toString(empleado.getId())});

			if(value!=0){
				modificado = true;
			}
			
		}finally{
			conexion.close();
		}
		
		return modificado;
	}

	@Override
	public boolean eliminarEmpleado(Context contexto, String idEmpleado) {
		ConexionBD conexion = new ConexionBD(contexto);
		boolean eliminado = false;
		
		try{
			conexion.open();

			long value = conexion.getDatabase().delete(DataBaseHelper.TABLA_EMPLEADO, "_id=?", new String[]{idEmpleado});

			if(value!=0){
				eliminado = true;
			}
			
		}finally{
			conexion.close();
		}
		
		return eliminado;
	}

	@Override
	public Cursor listarEmpleados(Context contexto) {
		ConexionBD conexion = new ConexionBD(contexto);
		Cursor mCursor = null;
		try{

			conexion.open();

			mCursor = conexion.getDatabase().rawQuery("SELECT empresa._id, empresa._id as " + Empleado.EMPRESA_ID + ", empleado._id as " + Empleado.ID + ", empleado.nombre as "+Empleado.NOMBRE+", empleado.apellido as "+Empleado.APELLIDO+", empresa.nombre as "+Empleado.EMPRESA
					                               + " FROM " + DataBaseHelper.TABLA_EMPLEADO 
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
	
	@Override
	public Cursor listarEmpleadosPorEmpresa(Context contexto, String idEmpresa) {
		ConexionBD conexion = new ConexionBD(contexto);
		Cursor mCursor = null;
		try{

			conexion.open();

			mCursor = conexion.getDatabase().query(DataBaseHelper.TABLA_EMPLEADO , new String [] {Empleado.ID, Empleado.NOMBRE, Empleado.APELLIDO, Empleado.POSICION, Empleado.EMAIL} , Empleado.EMPRESA_ID + " = ? ", new String [] {idEmpresa}, null, null, Empleado.NOMBRE);
			
			if (mCursor != null) {
				mCursor.moveToFirst();
			}
			
		}finally{
			conexion.close();
		}

		return mCursor;	
	}

	@Override
	public Empleado buscarEmpleado(Context contexto, String idEmpleado) {

		ConexionBD conexion = new ConexionBD(contexto);
		Cursor mCursor = null;
		Empleado empleado = null;
		
		try{
			conexion.open();

			mCursor = conexion.getDatabase().query(DataBaseHelper.TABLA_EMPLEADO , null , Empleado.ID + " = ? ", new String [] {idEmpleado}, null, null, null);

			if (mCursor.getCount() > 0) {
				mCursor.moveToFirst();

				empleado = new Empleado( mCursor.getString(mCursor.getColumnIndex("nombre")), 
						mCursor.getString(mCursor.getColumnIndex("apellido")), 
						mCursor.getString(mCursor.getColumnIndex("posicion")), 
						mCursor.getString(mCursor.getColumnIndex("email")), 
						mCursor.getString(mCursor.getColumnIndex("telfOficina")), 
						mCursor.getString(mCursor.getColumnIndex("celular")), 
						mCursor.getString(mCursor.getColumnIndex("pin")), 
						mCursor.getString(mCursor.getColumnIndex("linkedin")), 
						mCursor.getString(mCursor.getColumnIndex("descripcion")), 
						mCursor.getInt(mCursor.getColumnIndex("idEmpresa")));
			}
			
		}finally{
			conexion.close();
		}

		return empleado;	
	}

	@Override
	public Cursor buscarEmpleadoFilter(Context contexto, String args) {
		ConexionBD conexion = new ConexionBD(contexto);
		Cursor mCursor = null;
		String sqlQuery = "";
		
		try{
			conexion.open();
			sqlQuery  = " SELECT  empresa._id, empresa._id as " + Empleado.EMPRESA_ID + ", empleado._id as " + Empleado.ID + ", empleado.nombre as "+Empleado.NOMBRE+", empleado.apellido as "+Empleado.APELLIDO+", empresa.nombre as "+Empleado.EMPRESA;
			sqlQuery += " FROM " + DataBaseHelper.TABLA_EMPLEADO;
			sqlQuery += " INNER JOIN " + DataBaseHelper.TABLA_EMPRESA;
			sqlQuery += " ON (empleado.idEmpresa=empresa._id)";
			sqlQuery += " WHERE empleado.nombre LIKE '%" + args + "%' ";
			sqlQuery += " OR empleado.apellido LIKE '%" + args + "%' ";
			sqlQuery += " OR empresa.nombre LIKE '%" + args + "%' ";

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
