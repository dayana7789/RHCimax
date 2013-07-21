package com.nahmens.rhcimax.database.sqliteDAO;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.nahmens.rhcimax.database.ConexionBD;
import com.nahmens.rhcimax.database.DataBaseHelper;
import com.nahmens.rhcimax.database.DAO.EmpleadoDAO;
import com.nahmens.rhcimax.database.modelo.Empleado;
import com.nahmens.rhcimax.database.modelo.Empresa;
import com.nahmens.rhcimax.database.modelo.Tarea;
import com.nahmens.rhcimax.database.modelo.Usuario;
import com.nahmens.rhcimax.utils.Formato;
import com.nahmens.rhcimax.utils.FormatoFecha;

public class EmpleadoSqliteDao implements EmpleadoDAO{

	@Override
	public String insertarEmpleado(Context contexto, Empleado empleado) {
		ConexionBD conexion = new ConexionBD(contexto);
		String idFila = null;

		if(empleado.getId() == null){
			idFila= new Formato().getNumeroAleatorio();
		}else{
			idFila = empleado.getId();
		}
		
		long value = -1;

		try{
			conexion.open();

			ContentValues values = new ContentValues();

			values.put(Empleado.ID,idFila);
			values.put(Empleado.NOMBRE,empleado.getNombre());
			values.put(Empleado.APELLIDO,empleado.getApellido());
			values.put(Empleado.POSICION,empleado.getPosicion());
			values.put(Empleado.EMAIL, empleado.getEmail());
			values.put(Empleado.TELF_OFICINA,empleado.getTelfOficina());
			values.put(Empleado.CELULAR,empleado.getCelular());
			values.put(Empleado.PIN,empleado.getPin());
			values.put(Empleado.LINKEDIN,empleado.getLinkedin());
			values.put(Empleado.DESCRIPCION,empleado.getDescripcion());
			values.put(Empleado.EMPRESA_ID,empleado.getIdEmpresa());
			values.put(Empleado.ID_USUARIO_CREADOR, empleado.getIdUsuarioCreador());
			values.put(Empleado.ID_USUARIO_MODIFICADOR,empleado.getIdUsuarioModificador());

			value = conexion.getDatabase().insertOrThrow(DataBaseHelper.TABLA_EMPLEADO, null,values);
			
			if(value==-1){
				idFila= ""+value;
			}


		}finally{
			conexion.close();
		}

		return idFila;
	}

	@Override
	public boolean modificarEmpleado(Context contexto, Empleado empleado) {
		ConexionBD conexion = new ConexionBD(contexto);
		boolean modificado = false;

		try{
			conexion.open();

			ContentValues values = new ContentValues();

			values.put(Empleado.NOMBRE,empleado.getNombre());
			values.put(Empleado.APELLIDO,empleado.getApellido());
			values.put(Empleado.POSICION,empleado.getPosicion());
			values.put(Empleado.EMAIL, empleado.getEmail());
			values.put(Empleado.TELF_OFICINA,empleado.getTelfOficina());
			values.put(Empleado.CELULAR,empleado.getCelular());
			values.put(Empleado.PIN,empleado.getPin());
			values.put(Empleado.LINKEDIN,empleado.getLinkedin());
			values.put(Empleado.DESCRIPCION,empleado.getDescripcion());
			values.put(Empleado.EMPRESA_ID,empleado.getIdEmpresa());
			values.put(Empleado.FECHA_MODIFICACION,empleado.getFechaModificacion());
			values.put(Empleado.ID_USUARIO_MODIFICADOR,empleado.getIdUsuarioModificador());

			int value = conexion.getDatabase().update(DataBaseHelper.TABLA_EMPLEADO, values, "_id=?", new String []{empleado.getId()});

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

			//long value = conexion.getDatabase().delete(DataBaseHelper.TABLA_EMPLEADO, "_id=?", new String[]{idEmpleado});

			ContentValues values = new ContentValues();
			values.put("status", "inactivo");
			values.put(Empleado.FECHA_MODIFICACION, FormatoFecha.obtenerFechaTiempoActualEN());

			//Actualizamos el status del empleado
			int value = conexion.getDatabase().update(DataBaseHelper.TABLA_EMPLEADO, values, "_id=?", new String []{idEmpleado});

			//Actualizamos el status de las tareas
			@SuppressWarnings("unused")
			int value2 = conexion.getDatabase().update(DataBaseHelper.TABLA_TAREA, values, Tarea.ID_EMPLEADO + "=?", new String []{idEmpleado});

			if(value!=0){
				eliminado = true;
			}

		}finally{
			conexion.close();
		}

		return eliminado;
	}

	/*@Override
	public Cursor listarEmpleados(Context contexto) {
		ConexionBD conexion = new ConexionBD(contexto);
		Cursor mCursor = null;
		String sqlQuery = null;
		try{

			conexion.open();

			sqlQuery  = "SELECT empresa._id, empresa._id as " + Empleado.EMPRESA_ID + ", empleado._id as " + Empleado.ID;
			sqlQuery  +=", empleado.nombre as "+Empleado.NOMBRE+", empleado.apellido as "+Empleado.APELLIDO;
			sqlQuery  +=", empresa.nombre as "+Empleado.EMPRESA;
			sqlQuery  += ", empleado." + Empleado.FECHA_SINCRONIZACION;
			sqlQuery  += " FROM " + DataBaseHelper.TABLA_EMPLEADO;
			sqlQuery  += " LEFT JOIN " + DataBaseHelper.TABLA_EMPRESA;
			sqlQuery  += " ON (empleado.idEmpresa=empresa._id)";
			sqlQuery  += " WHERE empleado.status = 'activo'";
			sqlQuery  += " ORDER BY empleado.nombre";

			mCursor = conexion.getDatabase().rawQuery(sqlQuery , null);

			if (mCursor != null) {
				mCursor.moveToFirst();
			}

		}finally{
			conexion.close();
		}

		return mCursor;	
	}*/

	@Override
	public Cursor listarEmpleadosPorEmpresa(Context contexto, String idEmpresa) {
		ConexionBD conexion = new ConexionBD(contexto);
		Cursor mCursor = null;
		try{

			conexion.open();

			mCursor = conexion.getDatabase().query(DataBaseHelper.TABLA_EMPLEADO , null , Empleado.EMPRESA_ID + " = ? AND empleado.status='activo'", new String [] {idEmpresa}, null, null, Empleado.NOMBRE);

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

			mCursor = conexion.getDatabase().query(DataBaseHelper.TABLA_EMPLEADO , null , Empleado.ID + " = ? AND empleado.status='activo'", new String [] {idEmpleado}, null, null, null);

			if (mCursor.getCount() > 0) {
				mCursor.moveToFirst();

				empleado = new Empleado( 
						mCursor.getString(mCursor.getColumnIndex(Empleado.ID)), 
						mCursor.getString(mCursor.getColumnIndex(Empleado.NOMBRE)), 
						mCursor.getString(mCursor.getColumnIndex(Empleado.APELLIDO)), 
						mCursor.getString(mCursor.getColumnIndex(Empleado.POSICION)), 
						mCursor.getString(mCursor.getColumnIndex(Empleado.EMAIL)), 
						mCursor.getString(mCursor.getColumnIndex(Empleado.TELF_OFICINA)), 
						mCursor.getString(mCursor.getColumnIndex(Empleado.CELULAR)), 
						mCursor.getString(mCursor.getColumnIndex(Empleado.PIN)), 
						mCursor.getString(mCursor.getColumnIndex(Empleado.LINKEDIN)), 
						mCursor.getString(mCursor.getColumnIndex(Empleado.DESCRIPCION)), 
						mCursor.getString(mCursor.getColumnIndex(Empleado.EMPRESA_ID)), //NOTA: getColumnIndexOrThrow hace que devuelva 0, si este campo es null en la BD
						mCursor.getString(mCursor.getColumnIndex(Empleado.FECHA_CREACION)),
						mCursor.getString(mCursor.getColumnIndex(Empleado.FECHA_MODIFICACION)),
						mCursor.getString(mCursor.getColumnIndex(Empleado.FECHA_SINCRONIZACION)),
						mCursor.getString(mCursor.getColumnIndex(Empleado.ID_USUARIO_CREADOR)),
						mCursor.getString(mCursor.getColumnIndex(Empleado.ID_USUARIO_MODIFICADOR)));
			}

		}finally{
			conexion.close();
		}

		return empleado;	
	}

	@Override
	public Cursor buscarEmpleadoFilter(Context contexto, String args) {
		ConexionBD conexion = new ConexionBD(contexto);
		String [] palabras = {};

		if(args!=null){
			palabras = args.split(" ");
		}
		Cursor mCursor = null;
		String sqlQuery = "";



		try{
			conexion.open();

			sqlQuery  = "SELECT empresa._id, empresa._id as " + Empleado.EMPRESA_ID + ", empleado._id as " + Empleado.ID;
			sqlQuery += ", empleado.nombre as "+Empleado.NOMBRE+", empleado.apellido as "+Empleado.APELLIDO;
			sqlQuery += ", empresa.nombre as "+Empleado.EMPRESA;
			sqlQuery += ", empleado.posicion as "+Empleado.POSICION;
			sqlQuery += ", empleado.email as "+Empleado.EMAIL;
			sqlQuery += ", empleado.telfOficina as "+Empleado.TELF_OFICINA;
			sqlQuery += ", empleado.celular as "+Empleado.CELULAR;
			sqlQuery += ", empleado.pin as "+Empleado.PIN;
			sqlQuery += ", empleado.linkedin as "+Empleado.LINKEDIN;
			sqlQuery += ", empleado." + Empleado.FECHA_SINCRONIZACION;
			sqlQuery += ", empleado." + Empleado.FECHA_MODIFICACION;
			sqlQuery += ", usuario." + Usuario.LOGIN + " as usuarioCreador";
			sqlQuery += ", usuario." + Usuario.ID + " as idUsuario";
			sqlQuery += " FROM " + DataBaseHelper.TABLA_EMPLEADO;
			sqlQuery += " LEFT JOIN " + DataBaseHelper.TABLA_EMPRESA;
			sqlQuery += " ON (empleado.idEmpresa=empresa._id)";
			sqlQuery += " LEFT JOIN " + DataBaseHelper.TABLA_USUARIO;
			sqlQuery += " ON (empleado.idUsuarioCreador=usuario._id)";
			sqlQuery += " WHERE empleado.status = 'activo' ";


			for(int i =0; i< palabras.length; i++){

				sqlQuery += " AND (";

				sqlQuery +=	" empleado.nombre LIKE '%" + palabras[i] + "%' ";
				sqlQuery += " OR empleado.apellido LIKE '%" + palabras[i] + "%' ";
				sqlQuery += " OR empleado.posicion LIKE '%" + palabras[i] + "%' ";
				sqlQuery += " OR empleado.email LIKE '%" + palabras[i] + "%' ";
				sqlQuery += " OR empleado.telfOficina LIKE '%" + palabras[i] + "%' ";
				sqlQuery += " OR empleado.celular LIKE '%" + palabras[i] + "%' ";
				sqlQuery += " OR empleado.pin LIKE '%" + palabras[i] + "%' ";
				sqlQuery += " OR empleado.linkedin LIKE '%" + palabras[i] + "%' ";
				sqlQuery += " OR empresa.nombre LIKE '%" + palabras[i] + "%' ";
				sqlQuery += " OR usuario.login LIKE '%" + palabras[i] + "%' ";

				sqlQuery += ") ";
			}

			sqlQuery += " ORDER BY empleado.nombre";

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
	public boolean sincronizarEmpleado(Context contexto, String idEmpleado) {
		ConexionBD conexion = new ConexionBD(contexto);
		boolean sincronizado = false;

		try{
			conexion.open();

			ContentValues contenido = new ContentValues();
			contenido.put(Empleado.FECHA_SINCRONIZACION, FormatoFecha.darFormatoDateTimeUS(new Date()));

			int value = conexion.getDatabase().update(DataBaseHelper.TABLA_EMPLEADO, contenido, "_id=?", new String []{idEmpleado});

			if(value!=0){
				sincronizado = true;
			}

		}finally{
			conexion.close();
		}

		return sincronizado;
	}


	@Override
	public boolean sincronizarEmpleados(Context contexto, String idEmpresa) {
		ConexionBD conexion = new ConexionBD(contexto);
		boolean sincronizado = false;
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss",Locale.getDefault());

		try{
			conexion.open();

			ContentValues contenido = new ContentValues();
			contenido.put(Empresa.FECHA_SINCRONIZACION,dateFormat.format(new Date()));


			int value = conexion.getDatabase().update(DataBaseHelper.TABLA_EMPLEADO, contenido, Empleado.EMPRESA_ID+"=?", new String []{idEmpresa});

			if(value>0){
				sincronizado = true;
			}


		}finally{
			conexion.close();
		}

		return sincronizado;
	}

	@Override
	public Cursor listarNombresEmpleados(Context contexto, String args) {
		ConexionBD conexion = new ConexionBD(contexto);
		Cursor mCursor = null;
		String sqlQuery = "";
		try{
			conexion.open();

			sqlQuery  = " SELECT " + Empleado.ID + ", " + Empleado.NOMBRE + ", " + Empleado.APELLIDO;
			sqlQuery += " FROM " + DataBaseHelper.TABLA_EMPLEADO;
			sqlQuery += " WHERE empleado.status='activo'"; 
			sqlQuery += " AND (" + Empleado.NOMBRE + " LIKE '%" + args + "%' ";
			sqlQuery += " OR " + Empleado.APELLIDO + " LIKE '%" + args + "%') ";
			sqlQuery += " ORDER BY " + Empleado.NOMBRE;

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
	public Cursor listarEmpleadosPorEmpresaPorArgs(Context contexto,
			String idEmpresa, String args) {

		ConexionBD conexion = new ConexionBD(contexto);
		Cursor mCursor = null;
		String sqlQuery = "";
		try{

			conexion.open();

			sqlQuery  = " SELECT " + Empleado.ID + ", " + Empleado.NOMBRE + ", " + Empleado.APELLIDO;
			sqlQuery += " FROM " + DataBaseHelper.TABLA_EMPLEADO;
			sqlQuery += " WHERE empleado.status='activo'" ;
			sqlQuery += " AND " + Empleado.EMPRESA_ID + " = " + idEmpresa;
			sqlQuery += " AND (" + Empleado.NOMBRE + " LIKE '%" + args + "%' ";
			sqlQuery += " OR " + Empleado.APELLIDO + " LIKE '%" + args + "%') ";
			sqlQuery += " ORDER BY " + Empleado.NOMBRE;

			mCursor = conexion.getDatabase().rawQuery(sqlQuery,null);
			//mCursor = conexion.getDatabase().query(DataBaseHelper.TABLA_EMPLEADO , null , Empleado.EMPRESA_ID + " = ? ", new String [] {idEmpresa}, null, null, Empleado.NOMBRE);

			if (mCursor != null) {
				mCursor.moveToFirst();
			}

		}finally{
			conexion.close();
		}

		return mCursor;	
	}


	@Override
	public Cursor buscarEmpleadoCursor(Context contexto, String idEmpleado) {
		ConexionBD conexion = new ConexionBD(contexto);
		Cursor mCursor = null;

		try{
			conexion.open();

			mCursor = conexion.getDatabase().query(DataBaseHelper.TABLA_EMPLEADO , null , Empleado.ID + " = ? AND empleado.status='activo'", new String [] {idEmpleado}, null, null, null);

			if (mCursor.getCount() > 0) {
				mCursor.moveToFirst();
			}

		}finally{
			conexion.close();
		}

		return mCursor;
	}

	@Override
	public boolean esClienteDelUsuario(Context contexto, String idEmpleado, String idUsuario) {
		ConexionBD conexion = new ConexionBD(contexto);
		Cursor mCursor = null;
		boolean esCliente = false;

		try{
			conexion.open();

			mCursor = conexion.getDatabase().query(DataBaseHelper.TABLA_EMPLEADO , null , Empleado.ID + " = ? AND "+Empleado.ID_USUARIO_CREADOR+" = ? AND empleado.status='activo'", new String [] {idEmpleado, idUsuario}, null, null, null);

			if (mCursor.getCount() > 0) {
				esCliente = true;
			}

		}finally{
			conexion.close();
		}

		return esCliente;
	}

	@Override
	public Cursor listarEmpleadosNoSync(Context contexto) {
		ConexionBD conexion = new ConexionBD(contexto);
		Cursor mCursor = null;
		try{

			conexion.open();

			mCursor = conexion.getDatabase().query(DataBaseHelper.TABLA_EMPLEADO , null ,  Empleado.FECHA_SINCRONIZACION + " IS NULL OR " + Empleado.FECHA_MODIFICACION + " > " +Empleado.FECHA_SINCRONIZACION ,null, null, null, null);


			if (mCursor != null) {
				mCursor.moveToFirst();
			}

		}finally{
			conexion.close();
		}

		return mCursor;		
	}

}
