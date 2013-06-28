package com.nahmens.rhcimax.database.DAO;

import android.content.Context;

import com.nahmens.rhcimax.database.modelo.Checkin;

public interface CheckinDAO {
	
	long insertarCheckin(Context contexto, Checkin checkin);
	boolean modificarCheckin(Context contexto, Checkin checkin);
	Checkin buscarCheckin(Context contexto, String id);

}
