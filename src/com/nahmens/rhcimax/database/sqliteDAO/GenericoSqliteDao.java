package com.nahmens.rhcimax.database.sqliteDAO;

import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.nahmens.rhcimax.database.ConexionBD;
import com.nahmens.rhcimax.database.DAO.GenericoDAO;

public class GenericoSqliteDao implements GenericoDAO{

	private final String FECHA_SINCRONIZACION = "fechaSincronizacion";
	private final String FECHA_MODIFICACION = "fechaModificacion";
	private final String ID = "_id";

	@Override
	public Cursor listarGenericoNoSync(Context contexto, String nombreTabla) {
		ConexionBD conexion = new ConexionBD(contexto);
		Cursor mCursor = null;
		try{

			conexion.open();

			mCursor = conexion.getDatabase().query(nombreTabla, null , FECHA_SINCRONIZACION + " IS NULL OR " + FECHA_MODIFICACION + " > " + FECHA_SINCRONIZACION ,null, null, null, null);


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
		String id = null;

		try{
			conexion.open();

			while(keys.hasNext()){
				String key = (String)keys.next();
				values.put(key,json.getString(key));
				
				if(key.equals(ID)){
					id = json.getString(key);
				}
			}

			Log.e("DEBUG","id a modificar: " + id);
			value = conexion.getDatabase().update(nombreTabla, values, "_id=?", new String []{id});

			
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



}
