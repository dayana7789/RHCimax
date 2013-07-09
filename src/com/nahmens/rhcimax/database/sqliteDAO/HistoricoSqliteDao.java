package com.nahmens.rhcimax.database.sqliteDAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.nahmens.rhcimax.database.ConexionBD;
import com.nahmens.rhcimax.database.DataBaseHelper;
import com.nahmens.rhcimax.database.DAO.HistoricoDAO;
import com.nahmens.rhcimax.database.modelo.Checkin;
import com.nahmens.rhcimax.database.modelo.Cotizacion;
import com.nahmens.rhcimax.database.modelo.Empleado;
import com.nahmens.rhcimax.database.modelo.Empleado_Cotizacion;
import com.nahmens.rhcimax.database.modelo.Empresa;
import com.nahmens.rhcimax.database.modelo.Historico;
import com.nahmens.rhcimax.database.modelo.Tarea;
import com.nahmens.rhcimax.database.modelo.Usuario;
import com.nahmens.rhcimax.utils.FormatoFecha;

public class HistoricoSqliteDao implements HistoricoDAO {

	String  consulta  = "historico." + Historico.ID + " as historicoId, historico."+Historico.TIPO_REGISTRO + ", historico." + Historico.FECHA_CREACION + " as historicoFechaCreacion"
			+ ", empresaVisita." + Empresa.NOMBRE + " as nombreEmpresaVisita"
			+ ", checkin."+Checkin.CHECKIN + ", checkin." + Checkin.CHECKOUT
			+ ", usuarioVisita."+Usuario.LOGIN + " as loginUsuarioVisita"
			+ ", cotizacion."+Cotizacion.ID+" as cotizacionId, cotizacion."+Cotizacion.FECHA_ENVIO+", cotizacion."+Cotizacion.FECHA_LEIDO + ", cotizacion."+Cotizacion.DESCRIPCION + " as cotizacionDescripcion, cotizacion." + Cotizacion.FECHA_CREACION + " as cotizacionFechaCreacion"
			+ ", usuario."+Usuario.LOGIN+" as loginUsuario"
			+ ", empresaCotizacion."+Empresa.ID+", empresaCotizacion."+Empresa.NOMBRE + " as nombreEmpresaCotizacion"
			+ ", empleadoCotizacion."+Empleado.ID+", empleadoCotizacion."+Empleado.NOMBRE + " as nombreEmpleadoCotizacion" + ", empleadoCotizacion."+Empleado.APELLIDO+" as apellidoEmpleadoCotizacion"+ ", empleadoCotizacion."+Empleado.EMAIL+" as emailEmpleadoCotizacion"
			+ ", tarea."+Tarea.ID+" as tareaId, tarea."+Tarea.NOMBRE+" as nombreTarea, tarea."+Tarea.FECHA+", tarea."+Tarea.HORA + ", tarea."+Tarea.FECHA_FINALIZACION+ ", tarea."+Tarea.DESCRIPCION + " as tareaDescripcion, tarea."+Tarea.FECHA_CREACION+" as tareaFechaCreacion, tarea."+Tarea.FECHA_MODIFICACION + " as tareaFechaModificacion, tarea."+Tarea.FECHA_SINCRONIZACION+" as tareaFechaSincronizacion "
			+ ", usuarioTarea."+Usuario.LOGIN + " as loginUsuarioTarea"
			+ ", empresaTarea."+Empresa.ID+", empresaTarea."+Empresa.NOMBRE + " as nombreEmpresaTarea"
			+ ", empleadoTarea."+Empleado.ID+", empleadoTarea."+Empleado.NOMBRE + " as nombreEmpleadoTarea"+", empleadoTarea."+Empleado.APELLIDO + " as apellidoEmpleadoTarea"			
			+ " FROM " + DataBaseHelper.TABLA_HISTORICO
			+ " LEFT JOIN " + DataBaseHelper.TABLA_EMPRESA + " as empresaVisita ON ( historico." + Historico.ID_EMPRESA + " = empresaVisita."+Empresa.ID+" ) "
			+ " LEFT JOIN " + DataBaseHelper.TABLA_CHECKIN + " ON ( empresaVisita." + Empresa.ID + " = checkin."+Checkin.ID_EMPRESA+" ) "
			+ " LEFT JOIN " + DataBaseHelper.TABLA_USUARIO + " as usuarioVisita ON ( checkin." + Checkin.ID_USUARIO + " = usuarioVisita."+Usuario.ID+" ) "
			+ " LEFT JOIN " + DataBaseHelper.TABLA_COTIZACION + " ON ( historico." + Historico.ID_COTIZACION + " = cotizacion."+Cotizacion.ID+" ) "
			+ " LEFT JOIN " + DataBaseHelper.TABLA_USUARIO + " ON ( cotizacion."+Cotizacion.ID_USUARIO + " = usuario."+Usuario.ID+" ) "
			+ " LEFT JOIN " + DataBaseHelper.TABLA_EMPRESA + " as empresaCotizacion ON ( cotizacion." + Cotizacion.ID_EMPRESA + " = empresaCotizacion."+Empresa.ID+" ) "
			+ " LEFT JOIN " + DataBaseHelper.TABLA_EMPLEADO_COTIZACION + " ON ( cotizacion." + Cotizacion.ID + " = empleado_cotizacion."+Empleado_Cotizacion.ID_COTIZACION+" ) "
			+ " LEFT JOIN " + DataBaseHelper.TABLA_EMPLEADO + " as empleadoCotizacion ON ( empleado_cotizacion." + Empleado_Cotizacion.ID_EMPLEADO + " = empleadoCotizacion."+Empleado.ID+" ) "
			+ " LEFT JOIN " + DataBaseHelper.TABLA_TAREA + " ON ( historico." + Historico.ID_TAREA + " = tarea." + Tarea.ID +" ) "
			+ " LEFT JOIN " + DataBaseHelper.TABLA_USUARIO + " as usuarioTarea ON ( tarea." + Tarea.ID_USUARIO_CREADOR + " = usuarioTarea."+Usuario.ID+" ) "
			+ " LEFT JOIN " + DataBaseHelper.TABLA_EMPLEADO + " as empleadoTarea ON ( tarea." + Tarea.ID_EMPLEADO + " = empleadoTarea."+Empleado.ID+" ) "
			+ " LEFT JOIN " + DataBaseHelper.TABLA_EMPRESA + " as empresaTarea ON ( tarea." + Tarea.ID_EMPRESA + " = empresaTarea."+Empresa.ID+" ) ";



	String orderBy  = " ORDER BY historico." + Historico.FECHA_CREACION + " DESC";

	@Override
	public long insertarHistorico(Context contexto, Historico historico) {
		ConexionBD conexion = new ConexionBD(contexto);
		long idFila = 0;
		String idCot = null;
		String idTar = null;
		String idEmp = null;

		if(historico.getIdCotizacion()!=0){
			idCot = ""+historico.getIdCotizacion();
		}

		if(historico.getIdTarea()!=0){
			idTar = ""+historico.getIdTarea();
		}

		if(historico.getIdEmpresa()!=0){
			idEmp = ""+historico.getIdEmpresa();
		}

		try{
			conexion.open();

			ContentValues values = new ContentValues();

			values.put(Historico.TIPO_REGISTRO,historico.getTipoRegistro());
			values.put(Historico.ID_COTIZACION,idCot);
			values.put(Historico.ID_TAREA, idTar);
			values.put(Historico.ID_EMPRESA, idEmp);

			idFila = conexion.getDatabase().insert(DataBaseHelper.TABLA_HISTORICO, null,values);

		}finally{
			conexion.close();
		}

		return idFila;
	}

	/*@Override
	public Cursor listarHistoricos(Context contexto) {
		ConexionBD conexion = new ConexionBD(contexto);
		Cursor mCursor = null;
		String query = null;
		try{

			conexion.open();

			query  = "SELECT ";
			query += consulta;
			query += orderBy;

			mCursor = conexion.getDatabase().rawQuery(query , null);

			if (mCursor != null) {
				mCursor.moveToFirst();
			}

		}finally{
			conexion.close();
		}

		return mCursor;	
	}*/

	@Override
	public Cursor buscarHistoricoFilter(Context contexto, String args) {

		ConexionBD conexion = new ConexionBD(contexto);
		Cursor mCursor = null;
		String sqlQuery = "";
		String [] palabras = {};
		String condicion = "1";

		if(args !=null){
			//utilizamos substr para obtener solo la fecha 'yyyy-mm-dd'
			if(args.equals("Todos")){
				condicion = "1";
			}else if(args.equals("Hoy")){
				condicion = "(substr(historicoFechaCreacion, 1, 4) || '-' || substr(historicoFechaCreacion, 6, 2) || '-' || substr(historicoFechaCreacion, 9, 2))='"+FormatoFecha.obtenerFecha(0)+"'";
			}else if(args.equals("Ayer")){
				condicion = "(substr(historicoFechaCreacion, 1, 4) || '-' || substr(historicoFechaCreacion, 6, 2) || '-' || substr(historicoFechaCreacion, 9, 2))='"+FormatoFecha.obtenerFecha(-1)+"'";
			}else if(args.equals("Esta semana")){
				condicion = "(substr(historicoFechaCreacion, 1, 4) || '-' || substr(historicoFechaCreacion, 6, 2) || '-' || substr(historicoFechaCreacion, 9, 2)) BETWEEN '"+FormatoFecha.obtenerFecha(-7)+"' AND '"+FormatoFecha.obtenerFecha(0)+"'";
			}else{
				palabras = args.split(" ");
			}
		}

		try{
			conexion.open();

			sqlQuery  = "SELECT ";
			sqlQuery  += consulta;
			sqlQuery  += " WHERE ";
			sqlQuery  += condicion;
			
			for(int i =0; i< palabras.length; i++){

				sqlQuery += " AND (";

				sqlQuery +=	" nombreTarea LIKE '%" + palabras[i] + "%' ";
				sqlQuery += " OR (substr(tarea.fecha, 9, 2) || '/' || substr(tarea.fecha, 6, 2) || '/' || substr(tarea.fecha, 1, 4)) LIKE '%" + palabras[i] + "%' ";
				sqlQuery += " OR tarea.hora LIKE '%" + palabras[i] + "%' ";
				sqlQuery += " OR tarea.descripcion LIKE '%" + palabras[i] + "%' ";
				sqlQuery += " OR (substr(tarea.fechaFinalizacion, 9, 2) || '/' || substr(tarea.fechaFinalizacion, 6, 2) || '/' || substr(tarea.fechaFinalizacion, 1, 4)) LIKE '%" + palabras[i] + "%' ";
				sqlQuery += " OR nombreEmpleadoTarea LIKE '%" + palabras[i] + "%' ";
				sqlQuery += " OR apellidoEmpleadoTarea LIKE '%" + palabras[i] + "%' ";
				sqlQuery += " OR nombreEmpresaTarea LIKE '%" + palabras[i] + "%' ";
				sqlQuery += " OR loginUsuarioTarea LIKE '%" + palabras[i] + "%' ";
				
				sqlQuery += " OR historico.tipoRegistro LIKE '%" + palabras[i] + "%' ";
				
				sqlQuery += " OR nombreEmpresaVisita LIKE '%" + palabras[i] + "%' ";
				sqlQuery += " OR (substr(checkin.checkin, 9, 2) || '/' || substr(checkin.checkin, 6, 2) || '/' || substr(checkin.checkin, 1, 4)) LIKE '%" + palabras[i] + "%' ";
				sqlQuery += " OR (substr(checkin.checkout, 9, 2) || '/' || substr(checkin.checkout, 6, 2) || '/' || substr(checkin.checkout, 1, 4)) LIKE '%" + palabras[i] + "%' ";
				sqlQuery += " OR loginUsuarioVisita LIKE '%" + palabras[i] + "%' ";
				
				sqlQuery += " OR (substr(cotizacion.fechaEnvio, 9, 2) || '/' || substr(cotizacion.fechaEnvio, 6, 2) || '/' || substr(cotizacion.fechaEnvio, 1, 4)) LIKE '%" + palabras[i] + "%' ";
				sqlQuery += " OR (substr(cotizacion.fechaLeido, 9, 2) || '/' || substr(cotizacion.fechaLeido, 6, 2) || '/' || substr(cotizacion.fechaLeido, 1, 4)) LIKE '%" + palabras[i] + "%' ";
				sqlQuery += " OR loginUsuario LIKE '%" + palabras[i] + "%' ";
				sqlQuery += " OR nombreEmpresaCotizacion LIKE '%" + palabras[i] + "%' ";
				sqlQuery += " OR nombreEmpleadoCotizacion LIKE '%" + palabras[i] + "%' ";
				sqlQuery += " OR apellidoEmpleadoCotizacion LIKE '%" + palabras[i] + "%' ";
				sqlQuery += " OR emailEmpleadoCotizacion LIKE '%" + palabras[i] + "%' ";
				sqlQuery += " OR cotizacionId LIKE '%" + palabras[i] + "%' ";
				
				sqlQuery += ") ";
			}
			
			
			sqlQuery  += orderBy;

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
	public boolean sincronizarHistorico(Context contexto, String idEmpleado) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Cursor listarHistoricosPorEmpresa(Context contexto, String idEmpresa) {

		ConexionBD conexion = new ConexionBD(contexto);
		Cursor mCursor = null;
		String query = null;
		try{

			conexion.open();

			query  = "SELECT ";
			query  += consulta;
			query  += " WHERE historico." + Historico.ID_EMPRESA + " = " + idEmpresa;
			query  += orderBy;

			mCursor = conexion.getDatabase().rawQuery(query , null);

			if (mCursor != null) {
				mCursor.moveToFirst();
			}

		}finally{
			conexion.close();
		}

		return mCursor;	
	}


	@Override
	public Cursor listarHistoricosPorEmpleado(Context contexto, String idEmpleado) {
		ConexionBD conexion = new ConexionBD(contexto);
		Cursor mCursor = null;
		String query = null;
		try{

			conexion.open();

			query  = "SELECT ";
			query  += consulta;
			query  += " WHERE";
			query  += " empleado_cotizacion." + Empleado_Cotizacion.ID_EMPLEADO + " = " + idEmpleado;
			query  += " OR tarea." + Tarea.ID_EMPLEADO + " = " + idEmpleado;
			query  += orderBy;

			mCursor = conexion.getDatabase().rawQuery(query , null);

			if (mCursor != null) {
				mCursor.moveToFirst();
			}

		}finally{
			conexion.close();
		}

		return mCursor;	
	}


}
