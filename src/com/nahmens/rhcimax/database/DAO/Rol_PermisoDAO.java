package com.nahmens.rhcimax.database.DAO;

import android.content.Context;

import com.nahmens.rhcimax.database.modelo.Rol_Permiso;

public interface Rol_PermisoDAO {

	String insertaRol_Permiso(Context context, Rol_Permiso rol_permiso);
	
	boolean existeRol_Permiso(Context context, Rol_Permiso rol_permiso);
	/**
     * Funcion que elimina todos los registros de una tabla
     * @param context
     * @return numero de registros eliminados
     */
    int eliminarRoles_Permisos(Context context);
}
