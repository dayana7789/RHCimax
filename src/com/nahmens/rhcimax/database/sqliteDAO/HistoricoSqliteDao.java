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

public class HistoricoSqliteDao implements HistoricoDAO {
	
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

	@Override
	public Cursor listarHistoricos(Context contexto) {
		ConexionBD conexion = new ConexionBD(contexto);
		Cursor mCursor = null;
		String sqlQuery = null;
		try{

			conexion.open();
			
			sqlQuery  = "SELECT ";
			sqlQuery  += "historico." + Historico.ID + " as historicoId, historico."+Historico.TIPO_REGISTRO + ", historico." + Historico.FECHA_CREACION + " as historicoFechaCreacion"; 
			sqlQuery  += ", empresaVisita." + Empresa.NOMBRE + " as nombreEmpresaVisita";
			sqlQuery  += ", checkin."+Checkin.CHECKIN + ", checkin." + Checkin.CHECKOUT; 
			sqlQuery  += ", usuarioVisita."+Usuario.LOGIN + " as loginUsuarioVisita";
			sqlQuery  += ", cotizacion."+Cotizacion.ID+" as cotizacionId, cotizacion."+Cotizacion.FECHA_ENVIO+", cotizacion."+Cotizacion.FECHA_LEIDO + ", cotizacion."+Cotizacion.DESCRIPCION + " as cotizacionDescripcion, cotizacion." + Cotizacion.FECHA_CREACION + " as cotizacionFechaCreacion";
			sqlQuery  += ", usuario."+Usuario.LOGIN+" as loginUsuario";
			sqlQuery  += ", empresaCotizacion."+Empresa.ID+", empresaCotizacion."+Empresa.NOMBRE + " as nombreEmpresaCotizacion";
			sqlQuery  += ", empleadoCotizacion."+Empleado.ID+", empleadoCotizacion."+Empleado.NOMBRE + " as nombreEmpleadoCotizacion" + ", empleadoCotizacion."+Empleado.APELLIDO+" as apellidoEmpleadoCotizacion"+ ", empleadoCotizacion."+Empleado.EMAIL+" as emailEmpleadoCotizacion";
			sqlQuery  += ", tarea."+Tarea.ID+" as tareaId, tarea."+Tarea.NOMBRE+" as nombreTarea, tarea."+Tarea.FECHA+", tarea."+Tarea.HORA + ", tarea."+Tarea.FECHA_FINALIZACION+ ", tarea."+Tarea.DESCRIPCION + " as tareaDescripcion, tarea."+Tarea.FECHA_CREACION+" as tareaFechaCreacion";
			sqlQuery  += ", usuarioTarea."+Usuario.LOGIN + " as loginUsuarioTarea";
			sqlQuery  += ", empresaTarea."+Empresa.ID+", empresaTarea."+Empresa.NOMBRE + " as nombreEmpresaTarea";
			sqlQuery  += ", empleadoTarea."+Empleado.ID+", empleadoTarea."+Empleado.NOMBRE + " as nombreEmpleadoTarea"+", empleadoTarea."+Empleado.APELLIDO + " as apellidoEmpleadoTarea";			
			sqlQuery  += " FROM " + DataBaseHelper.TABLA_HISTORICO;
			sqlQuery  += " LEFT JOIN " + DataBaseHelper.TABLA_EMPRESA + " as empresaVisita ON ( historico." + Historico.ID_EMPRESA + " = empresaVisita."+Empresa.ID+" ) ";
			sqlQuery  += " LEFT JOIN " + DataBaseHelper.TABLA_CHECKIN + " ON ( empresaVisita." + Empresa.ID + " = checkin."+Checkin.ID_EMPRESA+" ) ";
			sqlQuery  += " LEFT JOIN " + DataBaseHelper.TABLA_USUARIO + " as usuarioVisita ON ( checkin." + Checkin.ID_USUARIO + " = usuarioVisita."+Usuario.ID+" ) ";
			sqlQuery  += " LEFT JOIN " + DataBaseHelper.TABLA_COTIZACION + " ON ( historico." + Historico.ID_COTIZACION + " = cotizacion."+Cotizacion.ID+" ) ";
			sqlQuery  += " LEFT JOIN " + DataBaseHelper.TABLA_USUARIO + " ON ( cotizacion."+Cotizacion.ID_USUARIO + " = usuario."+Usuario.ID+" ) ";
			sqlQuery  += " LEFT JOIN " + DataBaseHelper.TABLA_EMPRESA + " as empresaCotizacion ON ( cotizacion." + Cotizacion.ID_EMPRESA + " = empresaCotizacion."+Empresa.ID+" ) ";
			sqlQuery  += " LEFT JOIN " + DataBaseHelper.TABLA_EMPLEADO_COTIZACION + " ON ( cotizacion." + Cotizacion.ID + " = empleado_cotizacion."+Empleado_Cotizacion.ID_COTIZACION+" ) ";
			sqlQuery  += " LEFT JOIN " + DataBaseHelper.TABLA_EMPLEADO + " as empleadoCotizacion ON ( empleado_cotizacion." + Empleado_Cotizacion.ID_EMPLEADO + " = empleadoCotizacion."+Empleado.ID+" ) ";
			sqlQuery  += " LEFT JOIN  " + DataBaseHelper.TABLA_TAREA + " ON ( historico." + Historico.ID_TAREA + " = tarea." + Tarea.ID +" ) ";
			sqlQuery  += " LEFT JOIN " + DataBaseHelper.TABLA_USUARIO + " as usuarioTarea ON ( tarea." + Tarea.ID_USUARIO_CREADOR + " = usuarioTarea."+Usuario.ID+" ) ";
			sqlQuery  += " LEFT JOIN " + DataBaseHelper.TABLA_EMPLEADO + " as empleadoTarea ON ( tarea." + Tarea.ID_EMPLEADO + " = empleadoTarea."+Empleado.ID+" ) ";
			sqlQuery  += " LEFT JOIN " + DataBaseHelper.TABLA_EMPRESA + " as empresaTarea ON ( tarea." + Tarea.ID_EMPRESA + " = empresaTarea."+Empresa.ID+" ) ";
			sqlQuery  += " ORDER BY historico." + Historico.FECHA_CREACION;

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
	public Cursor buscarHistoricoFilter(Context contexto, String args) {
		// TODO Auto-generated method stub
		return null;
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
		String sqlQuery = null;
		try{

			conexion.open();
			
			sqlQuery  = "SELECT ";
			sqlQuery  += "historico." + Historico.ID + " as historicoId, historico."+Historico.TIPO_REGISTRO + ", historico." + Historico.FECHA_CREACION + " as historicoFechaCreacion"; 
			sqlQuery  += ", empresaVisita." + Empresa.NOMBRE + " as nombreEmpresaVisita";
			sqlQuery  += ", checkin."+Checkin.CHECKIN + ", checkin." + Checkin.CHECKOUT; 
			sqlQuery  += ", usuarioVisita."+Usuario.LOGIN + " as loginUsuarioVisita";
			sqlQuery  += ", cotizacion."+Cotizacion.ID+" as cotizacionId, cotizacion."+Cotizacion.FECHA_ENVIO+", cotizacion."+Cotizacion.FECHA_LEIDO + ", cotizacion."+Cotizacion.DESCRIPCION + " as cotizacionDescripcion, cotizacion." + Cotizacion.FECHA_CREACION + " as cotizacionFechaCreacion";
			sqlQuery  += ", usuario."+Usuario.LOGIN+" as loginUsuario";
			sqlQuery  += ", empresaCotizacion."+Empresa.ID+", empresaCotizacion."+Empresa.NOMBRE + " as nombreEmpresaCotizacion";
			sqlQuery  += ", empleadoCotizacion."+Empleado.ID+", empleadoCotizacion."+Empleado.NOMBRE + " as nombreEmpleadoCotizacion" + ", empleadoCotizacion."+Empleado.APELLIDO+" as apellidoEmpleadoCotizacion"+ ", empleadoCotizacion."+Empleado.EMAIL+" as emailEmpleadoCotizacion";
			sqlQuery  += ", tarea."+Tarea.ID+" as tareaId, tarea."+Tarea.NOMBRE+" as nombreTarea, tarea."+Tarea.FECHA+", tarea."+Tarea.HORA + ", tarea."+Tarea.FECHA_FINALIZACION+ ", tarea."+Tarea.DESCRIPCION + " as tareaDescripcion, tarea."+Tarea.FECHA_CREACION+" as tareaFechaCreacion";
			sqlQuery  += ", usuarioTarea."+Usuario.LOGIN + " as loginUsuarioTarea";
			sqlQuery  += ", empresaTarea."+Empresa.ID+", empresaTarea."+Empresa.NOMBRE + " as nombreEmpresaTarea";
			sqlQuery  += ", empleadoTarea."+Empleado.ID+", empleadoTarea."+Empleado.NOMBRE + " as nombreEmpleadoTarea"+", empleadoTarea."+Empleado.APELLIDO + " as apellidoEmpleadoTarea";			
			sqlQuery  += " FROM " + DataBaseHelper.TABLA_HISTORICO;
			sqlQuery  += " LEFT JOIN " + DataBaseHelper.TABLA_EMPRESA + " as empresaVisita ON ( historico." + Historico.ID_EMPRESA + " = empresaVisita."+Empresa.ID+" ) ";
			sqlQuery  += " LEFT JOIN " + DataBaseHelper.TABLA_CHECKIN + " ON ( empresaVisita." + Empresa.ID + " = checkin."+Checkin.ID_EMPRESA+" ) ";
			sqlQuery  += " LEFT JOIN " + DataBaseHelper.TABLA_USUARIO + " as usuarioVisita ON ( checkin." + Checkin.ID_USUARIO + " = usuarioVisita."+Usuario.ID+" ) ";
			sqlQuery  += " LEFT JOIN " + DataBaseHelper.TABLA_COTIZACION + " ON ( historico." + Historico.ID_COTIZACION + " = cotizacion."+Cotizacion.ID+" ) ";
			sqlQuery  += " LEFT JOIN " + DataBaseHelper.TABLA_USUARIO + " ON ( cotizacion."+Cotizacion.ID_USUARIO + " = usuario."+Usuario.ID+" ) ";
			sqlQuery  += " LEFT JOIN " + DataBaseHelper.TABLA_EMPRESA + " as empresaCotizacion ON ( cotizacion." + Cotizacion.ID_EMPRESA + " = empresaCotizacion."+Empresa.ID+" ) ";
			sqlQuery  += " LEFT JOIN " + DataBaseHelper.TABLA_EMPLEADO_COTIZACION + " ON ( cotizacion." + Cotizacion.ID + " = empleado_cotizacion."+Empleado_Cotizacion.ID_COTIZACION+" ) ";
			sqlQuery  += " LEFT JOIN " + DataBaseHelper.TABLA_EMPLEADO + " as empleadoCotizacion ON ( empleado_cotizacion." + Empleado_Cotizacion.ID_EMPLEADO + " = empleadoCotizacion."+Empleado.ID+" ) ";
			sqlQuery  += " LEFT JOIN  " + DataBaseHelper.TABLA_TAREA + " ON ( historico." + Historico.ID_TAREA + " = tarea." + Tarea.ID +" ) ";
			sqlQuery  += " LEFT JOIN " + DataBaseHelper.TABLA_USUARIO + " as usuarioTarea ON ( tarea." + Tarea.ID_USUARIO_CREADOR + " = usuarioTarea."+Usuario.ID+" ) ";
			sqlQuery  += " LEFT JOIN " + DataBaseHelper.TABLA_EMPLEADO + " as empleadoTarea ON ( tarea." + Tarea.ID_EMPLEADO + " = empleadoTarea."+Empleado.ID+" ) ";
			sqlQuery  += " LEFT JOIN " + DataBaseHelper.TABLA_EMPRESA + " as empresaTarea ON ( tarea." + Tarea.ID_EMPRESA + " = empresaTarea."+Empresa.ID+" ) ";
			sqlQuery  += " WHERE historico." + Historico.ID_EMPRESA + " = " + idEmpresa;
			sqlQuery  += " ORDER BY historico." + Historico.FECHA_CREACION;

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
	public Cursor listarHistoricosPorEmpleado(Context contexto, String idEmpleado) {
		ConexionBD conexion = new ConexionBD(contexto);
		Cursor mCursor = null;
		String sqlQuery = null;
		try{

			conexion.open();
			
			sqlQuery  = "SELECT ";
			sqlQuery  += "historico." + Historico.ID + " as historicoId, historico."+Historico.TIPO_REGISTRO + ", historico." + Historico.FECHA_CREACION + " as historicoFechaCreacion"; 
			sqlQuery  += ", empresaVisita." + Empresa.NOMBRE + " as nombreEmpresaVisita";
			sqlQuery  += ", checkin."+Checkin.CHECKIN + ", checkin." + Checkin.CHECKOUT; 
			sqlQuery  += ", usuarioVisita."+Usuario.LOGIN + " as loginUsuarioVisita";
			sqlQuery  += ", cotizacion."+Cotizacion.ID+" as cotizacionId, cotizacion."+Cotizacion.FECHA_ENVIO+", cotizacion."+Cotizacion.FECHA_LEIDO + ", cotizacion."+Cotizacion.DESCRIPCION + " as cotizacionDescripcion, cotizacion." + Cotizacion.FECHA_CREACION + " as cotizacionFechaCreacion";
			sqlQuery  += ", usuario."+Usuario.LOGIN+" as loginUsuario";
			sqlQuery  += ", empresaCotizacion."+Empresa.ID+", empresaCotizacion."+Empresa.NOMBRE + " as nombreEmpresaCotizacion";
			sqlQuery  += ", empleadoCotizacion."+Empleado.ID+", empleadoCotizacion."+Empleado.NOMBRE + " as nombreEmpleadoCotizacion" + ", empleadoCotizacion."+Empleado.APELLIDO+" as apellidoEmpleadoCotizacion"+ ", empleadoCotizacion."+Empleado.EMAIL+" as emailEmpleadoCotizacion";
			sqlQuery  += ", tarea."+Tarea.ID+" as tareaId, tarea."+Tarea.NOMBRE+" as nombreTarea, tarea."+Tarea.FECHA+", tarea."+Tarea.HORA + ", tarea."+Tarea.FECHA_FINALIZACION+ ", tarea."+Tarea.DESCRIPCION + " as tareaDescripcion, tarea."+Tarea.FECHA_CREACION+" as tareaFechaCreacion";
			sqlQuery  += ", usuarioTarea."+Usuario.LOGIN + " as loginUsuarioTarea";
			sqlQuery  += ", empresaTarea."+Empresa.ID+", empresaTarea."+Empresa.NOMBRE + " as nombreEmpresaTarea";
			sqlQuery  += ", empleadoTarea."+Empleado.ID+", empleadoTarea."+Empleado.NOMBRE + " as nombreEmpleadoTarea"+", empleadoTarea."+Empleado.APELLIDO + " as apellidoEmpleadoTarea";			
			sqlQuery  += " FROM " + DataBaseHelper.TABLA_HISTORICO;
			sqlQuery  += " LEFT JOIN " + DataBaseHelper.TABLA_EMPRESA + " as empresaVisita ON ( historico." + Historico.ID_EMPRESA + " = empresaVisita."+Empresa.ID+" ) ";
			sqlQuery  += " LEFT JOIN " + DataBaseHelper.TABLA_CHECKIN + " ON ( empresaVisita." + Empresa.ID + " = checkin."+Checkin.ID_EMPRESA+" ) ";
			sqlQuery  += " LEFT JOIN " + DataBaseHelper.TABLA_USUARIO + " as usuarioVisita ON ( checkin." + Checkin.ID_USUARIO + " = usuarioVisita."+Usuario.ID+" ) ";
			sqlQuery  += " LEFT JOIN " + DataBaseHelper.TABLA_COTIZACION + " ON ( historico." + Historico.ID_COTIZACION + " = cotizacion."+Cotizacion.ID+" ) ";
			sqlQuery  += " LEFT JOIN " + DataBaseHelper.TABLA_USUARIO + " ON ( cotizacion."+Cotizacion.ID_USUARIO + " = usuario."+Usuario.ID+" ) ";
			sqlQuery  += " LEFT JOIN " + DataBaseHelper.TABLA_EMPRESA + " as empresaCotizacion ON ( cotizacion." + Cotizacion.ID_EMPRESA + " = empresaCotizacion."+Empresa.ID+" ) ";
			sqlQuery  += " LEFT JOIN " + DataBaseHelper.TABLA_EMPLEADO_COTIZACION + " ON ( cotizacion." + Cotizacion.ID + " = empleado_cotizacion."+Empleado_Cotizacion.ID_COTIZACION+" ) ";
			sqlQuery  += " LEFT JOIN " + DataBaseHelper.TABLA_EMPLEADO + " as empleadoCotizacion ON ( empleado_cotizacion." + Empleado_Cotizacion.ID_EMPLEADO + " = empleadoCotizacion."+Empleado.ID+" ) ";
			sqlQuery  += " LEFT JOIN  " + DataBaseHelper.TABLA_TAREA + " ON ( historico." + Historico.ID_TAREA + " = tarea." + Tarea.ID +" ) ";
			sqlQuery  += " LEFT JOIN " + DataBaseHelper.TABLA_USUARIO + " as usuarioTarea ON ( tarea." + Tarea.ID_USUARIO_CREADOR + " = usuarioTarea."+Usuario.ID+" ) ";
			sqlQuery  += " LEFT JOIN " + DataBaseHelper.TABLA_EMPLEADO + " as empleadoTarea ON ( tarea." + Tarea.ID_EMPLEADO + " = empleadoTarea."+Empleado.ID+" ) ";
			sqlQuery  += " LEFT JOIN " + DataBaseHelper.TABLA_EMPRESA + " as empresaTarea ON ( tarea." + Tarea.ID_EMPRESA + " = empresaTarea."+Empresa.ID+" ) ";
			sqlQuery  += " WHERE";
			sqlQuery  += " empleado_cotizacion." + Empleado_Cotizacion.ID_EMPLEADO + " = " + idEmpleado;
			sqlQuery  += " OR tarea." + Tarea.ID_EMPLEADO + " = " + idEmpleado;
			sqlQuery  += " ORDER BY historico." + Historico.FECHA_CREACION;

			mCursor = conexion.getDatabase().rawQuery(sqlQuery , null);

			if (mCursor != null) {
				mCursor.moveToFirst();
			}

		}finally{
			conexion.close();
		}

		return mCursor;	
	}


}
