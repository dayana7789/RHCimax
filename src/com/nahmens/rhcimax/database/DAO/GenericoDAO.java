package com.nahmens.rhcimax.database.DAO;

import org.json.JSONObject;

import android.content.Context;
import android.database.Cursor;

public interface GenericoDAO {
	
	Cursor listarGenericoNoSync(Context contexto, String nombreTabla);
	Cursor buscarGenerico(Context contexto, String nombreTabla, String idRegistro);
	String insertarGenerico(Context contexto, JSONObject json, String nombreTabla);
	boolean eliminarGenerico(Context contexto, String nombreTabla, String idRegistro);
	boolean modificarGenerico(Context contexto, JSONObject json, String nombreTabla);
	boolean sincronizarGenerico(Context contexto, JSONObject json, String nombreTabla);

}
