package com.nahmens.rhcimax.database.DAO;

import android.content.Context;
import android.database.Cursor;

import com.nahmens.rhcimax.database.modelo.Checkin;

public interface CheckinDAO {
	
	String insertarCheckin(Context contexto, Checkin checkin);
	boolean modificarCheckin(Context contexto, Checkin checkin);
	Checkin buscarCheckin(Context contexto, String id);
	/**
	 * Funcion que retorna lista de checkins que no han sido sincronizados
	 */
	Cursor listarCheckinNoSync(Context contexto);

}
