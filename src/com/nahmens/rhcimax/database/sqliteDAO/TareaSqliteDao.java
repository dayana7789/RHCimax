package com.nahmens.rhcimax.database.sqliteDAO;

import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.nahmens.rhcimax.database.ConexionBD;
import com.nahmens.rhcimax.database.DataBaseHelper;
import com.nahmens.rhcimax.database.DAO.TareaDAO;
import com.nahmens.rhcimax.database.modelo.Empleado;
import com.nahmens.rhcimax.database.modelo.Empresa;
import com.nahmens.rhcimax.database.modelo.Tarea;
import com.nahmens.rhcimax.database.modelo.Usuario;
import com.nahmens.rhcimax.utils.Formato;
import com.nahmens.rhcimax.utils.FormatoFecha;
import com.nahmens.rhcimax.utils.SesionUsuario;

public class TareaSqliteDao implements TareaDAO{
	
	String consulta = "DISTINCT tarea."+Tarea.ID+", tarea."+Tarea.NOMBRE+", "+Tarea.FECHA+", "+Tarea.HORA
		              +", tarea."+Tarea.DESCRIPCION+", "+Tarea.FECHA_FINALIZACION+", tarea."+Tarea.ID_EMPLEADO
		              +", tarea."+Tarea.ID_EMPRESA+", empresa."+Empresa.NOMBRE+" as nombreEmpresa"
		              +", tarea."+Tarea.FECHA_MODIFICACION+", tarea."+Tarea.FECHA_SINCRONIZACION
		              +", empleado."+Empleado.NOMBRE+" as nombreEmpleado, empleado."+Empleado.APELLIDO+" as apellidoEmpleado"
		              +", usuario."+Usuario.LOGIN + " as loginUsuario "
		              +", usuario." + Usuario.ID + " as idUsuario"
		              + " FROM tarea "
		              + " LEFT JOIN empleado ON ( tarea." + Tarea.ID_EMPLEADO + " = empleado."+Empleado.ID+" ) "
		              + " LEFT JOIN empresa ON ( tarea." + Tarea.ID_EMPRESA + " = empresa."+Empresa.ID+" ) "
		              + " LEFT JOIN usuario ON ( tarea." + Tarea.ID_USUARIO_CREADOR + " = usuario."+Usuario.ID+" ) ";
	
	String orderBy  = " ORDER BY " + Tarea.FECHA + " ASC";
	
	@Override
	public String insertarTarea(Context contexto, Tarea tarea) {
		ConexionBD conexion = new ConexionBD(contexto);
		long value = -1;
		String idFila = null;

		if(tarea.getId() == null){
			idFila= new Formato().getNumeroAleatorio();
		}else{
			idFila = tarea.getId();
		}

		try{
			conexion.open();

			ContentValues values = new ContentValues();

			values.put(Tarea.ID, idFila);
			values.put(Tarea.NOMBRE, tarea.getNombre());
			values.put(Tarea.FECHA, tarea.getFecha());
			values.put(Tarea.HORA, tarea.getHora());
			values.put(Tarea.DESCRIPCION, tarea.getDescripcion());
			values.put(Tarea.FECHA_FINALIZACION, tarea.getFechaFinalizacion());
			values.put(Tarea.ID_USUARIO_CREADOR, tarea.getIdUsuarioCreador());
			values.put(Tarea.ID_USUARIO_MODIFICADOR, tarea.getIdUsuarioModificador());
			values.put(Tarea.ID_EMPLEADO,tarea.getIdEmpleado());
			values.put(Tarea.ID_EMPRESA,tarea.getIdEmpresa());

			value = conexion.getDatabase().insertOrThrow(DataBaseHelper.TABLA_TAREA, null,values);
			
			if(value==-1){
				idFila = ""+value;
			}

		}finally{
			conexion.close();
		}

		return idFila;
	}

	@Override
	public boolean modificarTarea(Context contexto, Tarea tarea) {
		ConexionBD conexion = new ConexionBD(contexto);
		boolean modificado = false;

		try{
			conexion.open();

			ContentValues values = new ContentValues();
			values.put(Tarea.NOMBRE, tarea.getNombre());
			values.put(Tarea.FECHA, tarea.getFecha());
			values.put(Tarea.HORA, tarea.getHora());
			values.put(Tarea.DESCRIPCION, tarea.getDescripcion());
			values.put(Tarea.FECHA_FINALIZACION, tarea.getFechaFinalizacion());
			values.put(Tarea.FECHA_MODIFICACION, tarea.getFechaModificacion());
			values.put(Tarea.ID_USUARIO_MODIFICADOR, tarea.getIdUsuarioModificador());
			values.put(Tarea.ID_EMPLEADO,tarea.getIdEmpleado());
			values.put(Tarea.ID_EMPRESA,tarea.getIdEmpresa());

			int value = conexion.getDatabase().update(DataBaseHelper.TABLA_TAREA, values, "_id=?", new String []{tarea.getId()});

			if(value!=0){
				modificado = true;
			}

		}finally{
			conexion.close();
		}

		return modificado;
	}

	@Override
	public boolean eliminarTarea(Context contexto, String idTarea) {
		ConexionBD conexion = new ConexionBD(contexto);
		boolean eliminado = false;

		try{
			conexion.open();

			ContentValues values = new ContentValues();
			values.put("status", "inactivo");
			
			//long value = conexion.getDatabase().delete(DataBaseHelper.TABLA_TAREA, "_id=?", new String[]{idTarea});
			int value = conexion.getDatabase().update(DataBaseHelper.TABLA_TAREA, values, "_id=?", new String []{idTarea});

			if(value!=0){
				eliminado = true;
			}

		}finally{
			conexion.close();
		}

		return eliminado;
	}

	@Override
	public Tarea buscarTarea(Context contexto, String idTarea) {
		ConexionBD conexion = new ConexionBD(contexto);
		Cursor mCursor = null;
		Tarea tarea = null;
		

		try{
			conexion.open();
			
			mCursor = conexion.getDatabase().query(DataBaseHelper.TABLA_TAREA , null , Tarea.ID + " = ? AND status='activo'", new String [] {idTarea}, null, null, null);

			if (mCursor.getCount() > 0) {
				mCursor.moveToFirst();
				
				tarea = new Tarea( mCursor.getString(mCursor.getColumnIndex(Tarea.ID)), 
						mCursor.getString(mCursor.getColumnIndex(Tarea.NOMBRE)), 
						mCursor.getString(mCursor.getColumnIndex(Tarea.FECHA)), 
						mCursor.getString(mCursor.getColumnIndex(Tarea.HORA)), 
						mCursor.getString(mCursor.getColumnIndex(Tarea.DESCRIPCION)), 
						mCursor.getString(mCursor.getColumnIndex(Tarea.FECHA_FINALIZACION)), 
						mCursor.getString(mCursor.getColumnIndex(Tarea.FECHA_CREACION)), 
						mCursor.getString(mCursor.getColumnIndex(Tarea.FECHA_MODIFICACION)), 
						mCursor.getString(mCursor.getColumnIndex(Tarea.FECHA_SINCRONIZACION)), 
						mCursor.getString(mCursor.getColumnIndex(Tarea.ID_USUARIO_CREADOR)),
						mCursor.getString(mCursor.getColumnIndex(Tarea.ID_USUARIO_MODIFICADOR)),
						mCursor.getString(mCursor.getColumnIndex(Tarea.ID_EMPRESA)),
						mCursor.getString(mCursor.getColumnIndex(Tarea.ID_EMPLEADO)));
			}

		}finally{
			conexion.close();
		}

		return tarea;
	}

/*	@Override
	public Cursor listarTareas(Context contexto) {
		ConexionBD conexion = new ConexionBD(contexto);
		Cursor mCursor = null;
		String sqlQuery = null;
		try{

			conexion.open();

			
			sqlQuery = "SELECT DISTINCT tarea."+Tarea.ID+", tarea."+Tarea.NOMBRE+", "+Tarea.FECHA+", "+Tarea.HORA
					+", tarea."+Tarea.DESCRIPCION+", "+Tarea.FECHA_FINALIZACION+", tarea."+Tarea.ID_EMPLEADO
					+", tarea."+Tarea.ID_EMPRESA+", empresa."+Empresa.NOMBRE+" as nombreEmpresa, "
					+"empleado."+Empleado.NOMBRE+" as nombreEmpleado, empleado."+Empleado.APELLIDO+" as apellidoEmpleado "
					+ "FROM tarea "
					+ "LEFT JOIN empleado ON ( tarea." + Tarea.ID_EMPLEADO + " = empleado."+Empleado.ID+" ) "
					+ "LEFT JOIN empresa ON ( tarea." + Tarea.ID_EMPRESA + " = empresa."+Empresa.ID+" ) "
					+ "WHERE tarea.status='activo' AND " + Tarea.FECHA_FINALIZACION + " IS NULL "
					 + "ORDER BY " + Tarea.FECHA + " DESC";


			//Log.e("query", " "+ sqlQuery);
			mCursor = conexion.getDatabase().rawQuery(sqlQuery, null);


			if (mCursor != null) {
				mCursor.moveToFirst();
			}

		}finally{
			conexion.close();
		}

		return mCursor;
	}
*/
	@Override
	public Cursor buscarTareaFilter(Context contexto, String args, boolean fltrarPorUsuario) {

		ConexionBD conexion = new ConexionBD(contexto);
		Cursor mCursor = null;
		String sqlQuery = "";
		String [] palabras = {};


		String condicion = "";
		
		if(args !=null){
			if(args.equals("Todos")){
				condicion = "";
			}else if(args.equals("Hoy")){
				condicion = " AND "+Tarea.FECHA+"='"+FormatoFecha.obtenerFecha(0)+"'";
			}else if(args.equals("Ayer")){
				condicion = " AND "+Tarea.FECHA+"='"+FormatoFecha.obtenerFecha(-1)+"'";
			}else if(args.equals("Esta semana")){
				
				//condicion = " AND "+Tarea.FECHA+" BETWEEN '"+FormatoFecha.obtenerFecha(-7)+"' AND '"+FormatoFecha.obtenerFecha(0)+"'";
				condicion = " AND "+Tarea.FECHA+" BETWEEN '"+FormatoFecha.obtenerFecha(0)+"' AND '"+FormatoFecha.darFormatoDateUS(FormatoFecha.getFechaUltimoDiaDeLaSemana())+"'";

			}else{
				palabras = args.split(" ");
			}
		}
		
		if(fltrarPorUsuario){
			condicion += " AND tarea."+Tarea.ID_USUARIO_CREADOR + " = " + SesionUsuario.getIdUsuario(contexto);
		}

		try{
			conexion.open();


			sqlQuery = "SELECT ";
			sqlQuery += consulta;
			sqlQuery += " WHERE tarea.status='activo' AND "+ Tarea.FECHA_FINALIZACION + " IS NULL ";
			sqlQuery += condicion;

			
			for(int i =0; i< palabras.length; i++){

				sqlQuery += " AND (";

				sqlQuery +=	" tarea.nombre LIKE '%" + palabras[i] + "%' ";
				sqlQuery += " OR (substr(tarea.fecha, 9, 2) || '/' || substr(tarea.fecha, 6, 2) || '/' || substr(tarea.fecha, 1, 4)) LIKE '%" + palabras[i] + "%' ";
				sqlQuery += " OR tarea.hora LIKE '%" + palabras[i] + "%' ";
				sqlQuery += " OR tarea.descripcion LIKE '%" + palabras[i] + "%' ";
				
				sqlQuery += " OR empleado.nombre LIKE '%" + palabras[i] + "%' ";
				sqlQuery += " OR empleado.apellido LIKE '%" + palabras[i] + "%' ";
				sqlQuery += " OR empresa.nombre LIKE '%" + palabras[i] + "%' ";
				sqlQuery += " OR loginUsuario LIKE '%" + palabras[i] + "%' ";

				sqlQuery += ") ";
			}
			
			sqlQuery += orderBy;

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
	public Cursor listarTareasPorEmpresa(Context contexto, String idEmpresa) {
		ConexionBD conexion = new ConexionBD(contexto);
		Cursor mCursor = null;
		String sqlQuery = null;
		try{
			conexion.open();

			sqlQuery = "SELECT "
				        + consulta
						+ "WHERE tarea.status='activo' AND " + Tarea.FECHA_FINALIZACION + " IS NULL "
						+ "AND tarea." + Tarea.ID_EMPRESA + " = " + idEmpresa 
						+ orderBy;

			mCursor = conexion.getDatabase().rawQuery(sqlQuery, null);


			if (mCursor != null) {
				mCursor.moveToFirst();
			}

		}finally{
			conexion.close();
		}

		return mCursor;
	}
	
	@Override
	public Cursor listarTareasPorEmpleado(Context contexto, String idEmpleado) {
		ConexionBD conexion = new ConexionBD(contexto);
		Cursor mCursor = null;
		String sqlQuery = null;
		try{
			conexion.open();

			sqlQuery = "SELECT "
			        + consulta
					+ "WHERE tarea.status='activo' AND " + Tarea.FECHA_FINALIZACION + " IS NULL "
					+ "AND tarea." + Tarea.ID_EMPLEADO + " = " + idEmpleado
					+ orderBy;

			mCursor = conexion.getDatabase().rawQuery(sqlQuery, null);


			if (mCursor != null) {
				mCursor.moveToFirst();
			}

		}finally{
			conexion.close();
		}

		return mCursor;
	}
	
	@Override
	public boolean sincronizarTarea(Context contexto, String idTarea) {
		ConexionBD conexion = new ConexionBD(contexto);
		boolean sincronizado = false;

		try{
			conexion.open();

			ContentValues contenido = new ContentValues();
			contenido.put(Tarea.FECHA_SINCRONIZACION, FormatoFecha.darFormatoDateTimeUS(new Date()));

			int value = conexion.getDatabase().update(DataBaseHelper.TABLA_TAREA, contenido, "_id=?", new String []{idTarea});

			if(value!=0){
				sincronizado = true;
			}

		}finally{
			conexion.close();
		}

		return sincronizado;
	}

}
