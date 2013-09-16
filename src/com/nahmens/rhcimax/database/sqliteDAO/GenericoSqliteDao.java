package com.nahmens.rhcimax.database.sqliteDAO;

import java.util.Date;
import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.nahmens.rhcimax.database.ConexionBD;
import com.nahmens.rhcimax.database.DataBaseHelper;
import com.nahmens.rhcimax.database.DAO.GenericoDAO;
import com.nahmens.rhcimax.database.modelo.Cotizacion_Servicio;
import com.nahmens.rhcimax.database.modelo.Empleado_Cotizacion;
import com.nahmens.rhcimax.database.modelo.Rol_Permiso;
import com.nahmens.rhcimax.database.modelo.Usuario_Rol;
import com.nahmens.rhcimax.utils.FormatoFecha;

public class GenericoSqliteDao implements GenericoDAO{

	private final String FECHA_SINCRONIZACION = "fechaSincronizacion";
	private final String SINCRONIZADO = "sincronizado";
	private final String ID = "_id";

	@Override
	public Cursor listarGenericoNoSync(Context contexto, String nombreTabla) {
		ConexionBD conexion = new ConexionBD(contexto);
		Cursor mCursor = null;
		try{

			conexion.open();

			mCursor = conexion.getDatabase().query(nombreTabla, null , SINCRONIZADO + " = 0",null, null, null, null);

			if (mCursor != null) {
				mCursor.moveToFirst();
			}

		}finally{
			conexion.close();
		}

		return mCursor;		
	}

	@Override
	public String insertarGenerico(Context contexto, JSONObject json,
			String nombreTabla) {

		ConexionBD conexion = new ConexionBD(contexto);
		long value = -1;
		ContentValues values = new ContentValues();
		Iterator<?> keys = json.keys();

		try{
			conexion.open();

			while(keys.hasNext()){
				String key = (String)keys.next();
				values.put(key,json.getString(key));
			}

			value = conexion.getDatabase().insertOrThrow(nombreTabla, null,values);

		} catch (JSONException e) {
			e.printStackTrace();

		}finally{
			conexion.close();
		}

		return value+"";
	}

	@Override
	public boolean modificarGenerico(Context contexto, JSONObject json,
			String nombreTabla) {

		ConexionBD conexion = new ConexionBD(contexto);
		long value = -1;
		boolean modificado = false;
		ContentValues values = new ContentValues();
		Iterator<?> keys = json.keys();

		try{
			conexion.open();

			if(nombreTabla.equals(DataBaseHelper.TABLA_COTIZACION_SERVICIO)){
				String idCotizacion = null;
				String idServicio = null;

				while(keys.hasNext()){
					String key = (String)keys.next();
					String valor = json.getString(key);

					if(valor.equals("null") || valor==null){
						values.putNull(key);
					}else{
						values.put(key,valor);
					}


					if(key.equals(Cotizacion_Servicio.ID_COTIZACION)){
						idCotizacion = json.getString(key);
					}

					if(key.equals(Cotizacion_Servicio.ID_SERVICIO)){
						idServicio = json.getString(key);
					}
				}

				value = conexion.getDatabase().update(nombreTabla, values, Cotizacion_Servicio.ID_COTIZACION+ "=? AND " + Cotizacion_Servicio.ID_SERVICIO + "=?", new String []{idCotizacion, idServicio});

			}else if(nombreTabla.equals(DataBaseHelper.TABLA_EMPLEADO_COTIZACION)){
				String idCotizacion = null;
				String idEmpleado = null;

				while(keys.hasNext()){
					String key = (String)keys.next();
					String valor = json.getString(key);

					if(valor.equals("null") || valor==null){
						values.putNull(key);
					}else{
						values.put(key,valor);
					}

					if(key.equals(Empleado_Cotizacion.ID_EMPLEADO)){
						idEmpleado = json.getString(key);
					}

					if(key.equals(Empleado_Cotizacion.ID_COTIZACION)){
						idCotizacion = json.getString(key);
					}
				}

				value = conexion.getDatabase().update(nombreTabla, values,  Empleado_Cotizacion.ID_EMPLEADO+ "=? AND " + Empleado_Cotizacion.ID_COTIZACION + "=?", new String []{idEmpleado, idCotizacion});

			}else if(nombreTabla.equals(DataBaseHelper.TABLA_ROL_PERMISO)){
				String idRol = null;
				String idPermiso = null;

				while(keys.hasNext()){
					String key = (String)keys.next();
					String valor = json.getString(key);

					if(valor.equals("null") || valor==null){
						values.putNull(key);
					}else{
						values.put(key,valor);
					}

					if(key.equals(Rol_Permiso.ID_ROL)){
						idRol = json.getString(key);
					}

					if(key.equals(Rol_Permiso.ID_PERMISO)){
						idPermiso = json.getString(key);
					}
				}

				value = conexion.getDatabase().update(nombreTabla, values, Rol_Permiso.ID_ROL+ "=? AND " + Rol_Permiso.ID_PERMISO + "=?", new String []{idRol, idPermiso});

			}else if(nombreTabla.equals(DataBaseHelper.TABLA_USUARIO_ROL)){
				String idRol = null;
				String idUsuario = null;

				while(keys.hasNext()){
					String key = (String)keys.next();
					String valor = json.getString(key);

					if(valor.equals("null") || valor==null){
						values.putNull(key);
					}else{
						values.put(key,valor);
					}

					if(key.equals(Usuario_Rol.ID_ROL)){
						idRol = json.getString(key);
					}

					if(key.equals(Usuario_Rol.ID_USUARIO)){
						idUsuario = json.getString(key);
					}
				}

				value = conexion.getDatabase().update(nombreTabla, values, Usuario_Rol.ID_ROL+ "=? AND " + Usuario_Rol.ID_USUARIO + "=?", new String []{idRol, idUsuario});

			}else{
				String id = null;

				while(keys.hasNext()){
					String key = (String)keys.next();
					String valor = json.getString(key);

					if(valor.equals("null") || valor==null){
						values.putNull(key);
					}else{
						values.put(key,valor);
					}

					if(key.equals(ID)){
						id = json.getString(key);
					}
				}

				value = conexion.getDatabase().update(nombreTabla, values, "_id=?", new String []{id});
			}


			if(value!=0){
				modificado = true;
			}

		} catch (JSONException e) {
			e.printStackTrace();

		}finally{
			conexion.close();
		}

		return modificado;
	}

	@Override
	public boolean sincronizarGenerico(Context contexto, JSONObject json,
			String nombreTabla) {

		ConexionBD conexion = new ConexionBD(contexto);
		boolean sincronizado = false;
		int value = 0;

		try{
			conexion.open();

			ContentValues values = new ContentValues();
			values.put(FECHA_SINCRONIZACION, FormatoFecha.darFormatoDateTimeUS(new Date()));
			values.put(SINCRONIZADO, 1);

			if(nombreTabla.equals(DataBaseHelper.TABLA_COTIZACION_SERVICIO)){
				String idCotizacion = json.getString(Cotizacion_Servicio.ID_COTIZACION);
				String idServicio = json.getString(Cotizacion_Servicio.ID_SERVICIO);

				value = conexion.getDatabase().update(nombreTabla, values, Cotizacion_Servicio.ID_COTIZACION+ "=? AND " + Cotizacion_Servicio.ID_SERVICIO + "=?", new String []{idCotizacion, idServicio});

			}else if(nombreTabla.equals(DataBaseHelper.TABLA_EMPLEADO_COTIZACION)){
				String idEmpleado  = json.getString(Empleado_Cotizacion.ID_EMPLEADO);
				String idCotizacion = json.getString(Empleado_Cotizacion.ID_COTIZACION);

				value = conexion.getDatabase().update(nombreTabla, values,  Empleado_Cotizacion.ID_EMPLEADO+ "=? AND " + Empleado_Cotizacion.ID_COTIZACION + "=?", new String []{idEmpleado, idCotizacion});

			}else if(nombreTabla.equals(DataBaseHelper.TABLA_ROL_PERMISO)){
				String idRol = json.getString(Rol_Permiso.ID_ROL);
				String idPermiso = json.getString(Rol_Permiso.ID_PERMISO);

				value = conexion.getDatabase().update(nombreTabla, values, Rol_Permiso.ID_ROL+ "=? AND " + Rol_Permiso.ID_PERMISO + "=?", new String []{idRol, idPermiso});

			}else if(nombreTabla.equals(DataBaseHelper.TABLA_USUARIO_ROL)){
				String idRol = json.getString(Usuario_Rol.ID_ROL);
				String idUsuario = json.getString(Usuario_Rol.ID_USUARIO);

				value = conexion.getDatabase().update(nombreTabla, values, Usuario_Rol.ID_ROL+ "=? AND " + Usuario_Rol.ID_USUARIO + "=?", new String []{idRol, idUsuario});

			}else{
				String id = json.getString(ID);

				value = conexion.getDatabase().update(nombreTabla, values, "_id=?", new String []{id});

			}

			if(value!=0){
				sincronizado = true;
			}

		}catch (JSONException e) {
			e.printStackTrace();

		}finally{
			conexion.close();
		}

		return sincronizado;

	}

	@Override
	public Cursor buscarGenerico(Context contexto, String nombreTabla,
			String idRegistro) {
		
		ConexionBD conexion = new ConexionBD(contexto);
		Cursor mCursor = null;
		try{

			conexion.open();

			mCursor = conexion.getDatabase().query(nombreTabla, null , ID + " = ?", new String []{idRegistro}, null, null, null);

			if (mCursor != null) {
				mCursor.moveToFirst();
			}

		}finally{
			conexion.close();
		}

		return mCursor;		
	}

	@Override
	public boolean eliminarGenerico(Context contexto, String nombreTabla,
			String idRegistro) {
		
		ConexionBD conexion = new ConexionBD(contexto);
		int numCol = 0;
		boolean eliminado = false;
		
		try{
			conexion.open();
			numCol = conexion.getDatabase().delete(nombreTabla, ID + " = ? ", new String []{idRegistro});

			if(numCol!=0){
				eliminado = true;
			}

		}finally{
			conexion.close();
		}

		return eliminado;		
	}

}
