package com.nahmens.rhcimax.database.sqliteDAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.nahmens.rhcimax.database.ConexionBD;
import com.nahmens.rhcimax.database.DataBaseHelper;
import com.nahmens.rhcimax.database.DAO.ConfiguracionDAO;
import com.nahmens.rhcimax.database.modelo.Configuracion;
import com.nahmens.rhcimax.database.modelo.Empresa;

public class ConfiguracionSqliteDao implements ConfiguracionDAO {

	@Override
	public boolean modificarKeyValue(Context contexto, Configuracion configuracion) {
		
		ConexionBD conexion = new ConexionBD(contexto);
		boolean modificado = false;

		try{
			conexion.open();
		
			ContentValues contenido = new ContentValues();
			contenido.put( configuracion.VALUE, configuracion.getValue());

			int value = conexion.getDatabase().update(DataBaseHelper.TABLA_CONFIGURACION, contenido,  Configuracion.KEY +"=?", new String []{configuracion.getKey()});

			if(value!=0){
				modificado = true;
			}
			
		}finally{
			conexion.close();
		}
		
		return modificado;
	}

	@Override
	public Configuracion buscarPorKey(Context contexto, String key) {
		ConexionBD conexion = new ConexionBD(contexto);
		Cursor mCursor = null;
		Configuracion configuracion= null;
		
		try{
			conexion.open();

			mCursor = conexion.getDatabase().query(DataBaseHelper.TABLA_CONFIGURACION , null , Configuracion.KEY + " = ? ", new String [] {key}, null, null, null);

			if (mCursor.getCount() > 0) {
				mCursor.moveToFirst();

				configuracion = new Configuracion( mCursor.getInt(mCursor.getColumnIndex(Configuracion.ID)), 
						mCursor.getString(mCursor.getColumnIndex(Configuracion.KEY)), 
						mCursor.getString(mCursor.getColumnIndex(Configuracion.VALUE)));
			}
			
		}finally{
			conexion.close();
		}

		return configuracion;
	}

}
