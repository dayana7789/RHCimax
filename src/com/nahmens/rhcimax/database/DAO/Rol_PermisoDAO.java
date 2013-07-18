package com.nahmens.rhcimax.database.DAO;

import android.content.Context;
import android.database.Cursor;

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
    
    /**
   	 * Funcion que retorna lista de Rol_Permiso que no han sido sincronizados
   	 */
   	Cursor listarRol_PermisoNoSync(Context contexto);
}
