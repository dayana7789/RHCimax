package com.nahmens.rhcimax.database.sqliteDAO;

import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.nahmens.rhcimax.database.ConexionBD;
import com.nahmens.rhcimax.database.DataBaseHelper;
import com.nahmens.rhcimax.database.DAO.EmpresaDAO;
import com.nahmens.rhcimax.database.modelo.Empleado;
import com.nahmens.rhcimax.database.modelo.Empresa;
import com.nahmens.rhcimax.database.modelo.Tarea;
import com.nahmens.rhcimax.database.modelo.Usuario;
import com.nahmens.rhcimax.utils.FormatoFecha;

public class EmpresaSqliteDao implements EmpresaDAO{

	@Override
	public long insertarEmpresa(Context contexto, Empresa empresa) {
		ConexionBD conexion = new ConexionBD(contexto);
		long idFila = 0;

		try{
			conexion.open();

			ContentValues values = new ContentValues();

			values.put(Empresa.NOMBRE,empresa.getNombre());
			values.put(Empresa.TELEFONO,empresa.getTelefono());
			values.put(Empresa.WEB, empresa.getWeb());
			values.put(Empresa.RIF, empresa.getRif());
			values.put(Empresa.DIR_FISCAL, empresa.getDirFiscal());
			values.put(Empresa.DIR_COMERCIAL, empresa.getDirComercial());
			values.put(Empresa.ID_USUARIO_CREADOR,empresa.getIdUsuarioCreador());
			values.put(Empresa.ID_USUARIO_MODIFICADOR,empresa.getIdUsuarioModificador());

			idFila = conexion.getDatabase().insert(DataBaseHelper.TABLA_EMPRESA, null,values);

		}finally{
			conexion.close();
		}

		return idFila;
	}

	@Override
	public boolean modificarEmpresa(Context contexto, Empresa empresa) {
		ConexionBD conexion = new ConexionBD(contexto);
		boolean modificado = false;

		try{
			conexion.open();
			
			String fechaSync= null;

			ContentValues values = new ContentValues();
			
			values.put(Empresa.NOMBRE,empresa.getNombre());
			values.put(Empresa.TELEFONO,empresa.getTelefono());
			values.put(Empresa.WEB, empresa.getWeb());
			values.put(Empresa.RIF, empresa.getRif());
			values.put(Empresa.DIR_FISCAL, empresa.getDirFiscal());
			values.put(Empresa.DIR_COMERCIAL, empresa.getDirComercial());
			values.put(Empresa.LATITUD, empresa.getLatitud());
			values.put(Empresa.LONGITUD, empresa.getLongitud());
			values.put(Empresa.FECHA_MODIFICACION,empresa.getFechaModificacion());
			values.put(Empresa.ID_USUARIO_MODIFICADOR,empresa.getIdUsuarioModificador());
			values.put(Empresa.MODIFICADO, 1);


			int value = conexion.getDatabase().update(DataBaseHelper.TABLA_EMPRESA, values, "_id=?", new String []{Integer.toString(empresa.getId())});

			if(value!=0){
				modificado = true;
			}

		}finally{
			conexion.close();
		}

		return modificado;

	}

	@Override
	public boolean eliminarEmpresa(Context contexto, String idEmpresa) {
		ConexionBD conexion = new ConexionBD(contexto);
		boolean eliminado = true;

		try{
			conexion.open();

			//long value = conexion.getDatabase().delete(DataBaseHelper.TABLA_EMPRESA, "_id=?", new String[]{idEmpresa});

			ContentValues values = new ContentValues();
			values.put("status", "inactivo");
			
			//Actualizamos el status de la empresa
			//long value = conexion.getDatabase().delete(DataBaseHelper.TABLA_TAREA, "_id=?", new String[]{idTarea});
			int value = conexion.getDatabase().update(DataBaseHelper.TABLA_EMPRESA, values, "_id=?", new String []{idEmpresa});
			

			//Actualizamos el status de los empleados y tareas
			EmpleadoSqliteDao empleadoDao = new EmpleadoSqliteDao();
			
			Cursor mEmpleados = empleadoDao.listarEmpleadosPorEmpresa(contexto, idEmpresa);
			
			if(mEmpleados!=null){
				mEmpleados.moveToFirst();
				boolean elim = false;
				while(!mEmpleados.isAfterLast()){
					int idEmpleado = mEmpleados.getInt(mEmpleados.getColumnIndex(Empleado.ID));
					elim = empleadoDao.eliminarEmpleado(contexto, ""+idEmpleado);
					eliminado = eliminado || elim;

					mEmpleados.moveToNext();
				}
			}
			
			TareaSqliteDao tareaDao = new TareaSqliteDao();
			
			Cursor mTareas = tareaDao.listarTareasPorEmpresa(contexto, idEmpresa);
			
			if(mTareas!=null){
				mTareas.moveToFirst();
				boolean elim = false;
				while(!mTareas.isAfterLast()){
					int idTarea = mTareas.getInt(mTareas.getColumnIndex(Tarea.ID));
					elim = tareaDao.eliminarTarea(contexto, ""+idTarea);
					eliminado = eliminado || elim;
					mTareas.moveToNext();
				}
			}
			
			
			if(value!=0 && eliminado==true){
				eliminado = true;
			}

		}finally{
			conexion.close();
		}

		return eliminado;
	}

	@Override
	public Empresa buscarEmpresa(Context contexto, String id) {

		ConexionBD conexion = new ConexionBD(contexto);
		Cursor mCursor = null;
		Empresa empresa = null;

		try{
			conexion.open();

			mCursor = conexion.getDatabase().query(DataBaseHelper.TABLA_EMPRESA , null , Empresa.ID + " = ? AND status='activo'", new String [] {id}, null, null, null);

			if (mCursor.getCount() > 0) {
				mCursor.moveToFirst();

				empresa = new Empresa(  mCursor.getInt(mCursor.getColumnIndex(Empresa.ID)),
						mCursor.getString(mCursor.getColumnIndex(Empresa.NOMBRE)), 
						mCursor.getString(mCursor.getColumnIndex(Empresa.TELEFONO)), 
						mCursor.getString(mCursor.getColumnIndex(Empresa.RIF)), 
						mCursor.getString(mCursor.getColumnIndex(Empresa.WEB)), 
						mCursor.getString(mCursor.getColumnIndex(Empresa.DIR_FISCAL)), 
						mCursor.getString(mCursor.getColumnIndex(Empresa.DIR_COMERCIAL)),
						mCursor.getDouble(mCursor.getColumnIndex(Empresa.LATITUD)),
						mCursor.getDouble(mCursor.getColumnIndex(Empresa.LONGITUD)),
						mCursor.getString(mCursor.getColumnIndex(Empresa.FECHA_CREACION)),
						mCursor.getInt(mCursor.getColumnIndex(Empresa.ID_USUARIO_CREADOR)),
						mCursor.getString(mCursor.getColumnIndex(Empresa.FECHA_MODIFICACION)),
						mCursor.getInt(mCursor.getColumnIndex(Empresa.ID_USUARIO_MODIFICADOR)),
						mCursor.getString(mCursor.getColumnIndex(Empresa.FECHA_SINCRONIZACION)),
						mCursor.getInt(mCursor.getColumnIndex(Empresa.MODIFICADO)));
			}

		}finally{
			conexion.close();
		}

		return empresa;

	}

	@Override
	public Cursor listarEmpresasSync(Context contexto) {
		ConexionBD conexion = new ConexionBD(contexto);
		Cursor mCursor = null;
		try{

			conexion.open();

			mCursor = conexion.getDatabase().query(DataBaseHelper.TABLA_EMPRESA , null , Empresa.FECHA_MODIFICACION + ">" + Empresa.FECHA_SINCRONIZACION,null, null, null, null);


			if (mCursor != null) {
				mCursor.moveToFirst();
			}

		}finally{
			conexion.close();
		}

		return mCursor;		
	}

	@Override
	public Cursor listarNombresEmpresas(Context contexto, String args) {

		ConexionBD conexion = new ConexionBD(contexto);
		Cursor mCursor = null;
		String sqlQuery = "";
		try{
			conexion.open();

			sqlQuery  = " SELECT " + Empresa.ID + ", " + Empresa.NOMBRE;
			sqlQuery += " FROM " + DataBaseHelper.TABLA_EMPRESA;
			sqlQuery += " WHERE " + Empresa.NOMBRE + " LIKE '%" + args + "%' ";
			sqlQuery += " AND status='activo' ";
			sqlQuery += " ORDER BY " + Empresa.NOMBRE;

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
	public Cursor buscarEmpresaPorNombre(Context contexto, String args) {

		ConexionBD conexion = new ConexionBD(contexto);
		Cursor mCursor = null;
		String sqlQuery = "";
		try{
			conexion.open();

			sqlQuery  = " SELECT " + Empresa.ID + ", " + Empresa.NOMBRE;
			sqlQuery += " FROM " + DataBaseHelper.TABLA_EMPRESA;
			sqlQuery += " WHERE " + Empresa.NOMBRE + " = '" + args +"'";
			sqlQuery += " AND status='activo' ";

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
	public Cursor buscarEmpresaFilter(Context contexto, String args) {
		ConexionBD conexion = new ConexionBD(contexto);
		String [] palabras = {};

		if(args!=null){
			palabras = args.split(" ");
		}
		
		Cursor mCursor = null;
		String sqlQuery = "";
		try{
			conexion.open();

			sqlQuery  = " SELECT empresa.*";
			sqlQuery += ", usuario." + Usuario.LOGIN + " as usuarioCreador";
			sqlQuery += ", usuario." + Usuario.ID + " as idUsuario";
			sqlQuery += " FROM " + DataBaseHelper.TABLA_EMPRESA;
			sqlQuery += " LEFT JOIN " + DataBaseHelper.TABLA_USUARIO;
			sqlQuery += " ON (empresa.idUsuarioCreador=usuario._id)";
			sqlQuery += " WHERE status='activo' ";
			
			
			for(int i =0; i< palabras.length; i++){

				sqlQuery += " AND (";

				sqlQuery +=	" empresa.nombre LIKE '%" + palabras[i] + "%' ";
				sqlQuery += " OR empresa.telefono LIKE '%" + palabras[i] + "%' ";
				sqlQuery += " OR empresa.web LIKE '%" + palabras[i] + "%' ";
				sqlQuery += " OR empresa.rif LIKE '%" + palabras[i] + "%' ";
				sqlQuery += " OR empresa.dirFiscal LIKE '%" + palabras[i] + "%' ";
				sqlQuery += " OR empresa.dirComercial LIKE '%" + palabras[i] + "%' ";
				sqlQuery += " OR usuario.login LIKE '%" + palabras[i] + "%' ";
			
				sqlQuery += ") ";
			}
			
			
			sqlQuery += " ORDER BY " + Empresa.NOMBRE;

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
	public boolean sincronizarEmpresa(Context contexto, String idEmpresa) {
		
		ConexionBD conexion = new ConexionBD(contexto);
		boolean sincronizado = false;

		try{
			conexion.open();

			ContentValues contenido = new ContentValues();
			contenido.put(Empresa.FECHA_SINCRONIZACION, FormatoFecha.darFormatoDateTimeUS(new Date()));

			int value = conexion.getDatabase().update(DataBaseHelper.TABLA_EMPRESA, contenido, "_id=?", new String []{idEmpresa});

			if(value!=0){
				sincronizado = true;
			}

		}finally{
			conexion.close();
		}

		return sincronizado;
	}
	
	@Override
	public boolean esClienteDelUsuario(Context contexto, String idEmpresa, String idUsuario) {
		ConexionBD conexion = new ConexionBD(contexto);
		Cursor mCursor = null;
		boolean esCliente = false;

		try{
			conexion.open();

			mCursor = conexion.getDatabase().query(DataBaseHelper.TABLA_EMPRESA , null , Empleado.ID + " = ? AND "+Empresa.ID_USUARIO_CREADOR+" = ? AND status='activo'", new String [] {idEmpresa, idUsuario}, null, null, null);

			if (mCursor.getCount() > 0) {
				esCliente = true;
			}

		}finally{
			conexion.close();
		}

		return esCliente;
	}

}
