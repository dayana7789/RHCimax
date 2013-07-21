package com.nahmens.rhcimax.database.DAO;

import org.json.JSONObject;

import android.content.Context;
import android.database.Cursor;

public interface GenericoDAO {
	
	Cursor listarGenericoNoSync(Context contexto, String nombreTabla);
	String insertarGenerico(Context contexto, JSONObject json, String nombreTabla);
	boolean modificarGenerico(Context contexto, JSONObject json, String nombreTabla);

}
