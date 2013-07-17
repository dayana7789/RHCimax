package com.nahmens.rhcimax.database.sqliteDAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.nahmens.rhcimax.database.ConexionBD;
import com.nahmens.rhcimax.database.DataBaseHelper;
import com.nahmens.rhcimax.database.DAO.CheckinDAO;
import com.nahmens.rhcimax.database.modelo.Checkin;
import com.nahmens.rhcimax.utils.Formato;

public class CheckinSqliteDao implements CheckinDAO{

	@Override
	public String insertarCheckin(Context contexto, Checkin checkin) {
		ConexionBD conexion = new ConexionBD(contexto);
		String idFila = new Formato().getNumeroAleatorio();

		try{
			conexion.open();

			ContentValues values = new ContentValues();

			values.put(Checkin.ID, idFila);
			values.put(Checkin.LATITUD, checkin.getLatitud());
			values.put(Checkin.LONGITUD, checkin.getLongitud());
			values.put(Checkin.CHECKIN, checkin.getCheckin());
			values.put(Checkin.CHECKOUT, checkin.getCheckout());
			values.put(Checkin.ID_USUARIO, checkin.getIdUsuario());

			long value = conexion.getDatabase().insert(DataBaseHelper.TABLA_CHECKIN, null,values);
			
			if(value==-1){
				idFila = ""+value;
			}

		}finally{
			conexion.close();
		}

		return idFila;
	}

	@Override
	public Checkin buscarCheckin(Context contexto, String id) {
		ConexionBD conexion = new ConexionBD(contexto);
		Cursor mCursor = null;
		Checkin checkin = null;

		try{
			conexion.open();

			mCursor = conexion.getDatabase().query(DataBaseHelper.TABLA_CHECKIN , null , Checkin.ID + " = ?", new String [] {id}, null, null, null);

			if (mCursor.getCount() > 0) {
				mCursor.moveToFirst();

				checkin = new Checkin(  mCursor.getString(mCursor.getColumnIndex(Checkin.ID)), 
						mCursor.getDouble(mCursor.getColumnIndex(Checkin.LATITUD)), 
						mCursor.getDouble(mCursor.getColumnIndex(Checkin.LONGITUD)), 
						mCursor.getString(mCursor.getColumnIndex(Checkin.CHECKIN)), 
						mCursor.getString(mCursor.getColumnIndex(Checkin.CHECKOUT)),
						mCursor.getString(mCursor.getColumnIndex(Checkin.ID_USUARIO)));
			}

		}finally{
			conexion.close();
		}

		return checkin;
	}

	@Override
	public boolean modificarCheckin(Context contexto, Checkin checkin) {
		ConexionBD conexion = new ConexionBD(contexto);
		boolean modificado = false;
		
		try{
			conexion.open();

			ContentValues values = new ContentValues();
			
			values.put(Checkin.LATITUD, checkin.getLatitud());
			values.put(Checkin.LONGITUD, checkin.getLongitud());
			values.put(Checkin.CHECKIN, checkin.getCheckin());
			values.put(Checkin.CHECKOUT, checkin.getCheckout());
			values.put(Checkin.ID_USUARIO, checkin.getIdUsuario());

			int value = conexion.getDatabase().update(DataBaseHelper.TABLA_CHECKIN, values, "_id=?", new String []{checkin.getId()});

			if(value!=0){
				modificado = true;
			}
			
		}finally{
			conexion.close();
		}
		
		return modificado;
	}

}
