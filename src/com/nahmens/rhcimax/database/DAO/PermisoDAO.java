package com.nahmens.rhcimax.database.DAO;

import org.json.JSONArray;

import android.content.Context;

public interface PermisoDAO {
	
	JSONArray buscarPermisos(Context contexto, String idRol);

}
