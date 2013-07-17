package com.nahmens.rhcimax.database.DAO;

import org.json.JSONArray;

import android.content.Context;

import com.nahmens.rhcimax.database.modelo.Permiso;

public interface PermisoDAO {
	
	JSONArray buscarPermisos(Context contexto, String idRol);
	long insertarPermiso(Context context, Permiso permiso, boolean autoincrement);
	/**
     * Funcion que elimina todos los registros de una tabla
     * @param context
     * @return numero de registros eliminados
     */
    int eliminarPermisos(Context context);

}
