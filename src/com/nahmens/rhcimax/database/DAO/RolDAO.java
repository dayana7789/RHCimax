package com.nahmens.rhcimax.database.DAO;

import android.content.Context;

import com.nahmens.rhcimax.database.modelo.Rol;

public interface RolDAO {
	
	Rol buscarRolUsuario(Context contexto, String idUsuario);
	long insertarRol(Context context, Rol rol, boolean autoincrement);
	/**
     * Funcion que elimina todos los registros de una tabla
     * @param context
     * @return numero de registros eliminados
     */
    int eliminarRoles(Context context);

}
