package com.nahmens.rhcimax.database.DAO;

import android.content.Context;
import android.database.Cursor;

import com.nahmens.rhcimax.database.modelo.Rol;

public interface RolDAO {
	
	Rol buscarRolUsuario(Context contexto, String idUsuario);
	String insertarRol(Context context, Rol rol);
	boolean modificarRol(Context context, Rol rol);
	/**
     * Funcion que elimina todos los registros de una tabla
     * @param context
     * @return numero de registros eliminados
     */
    int eliminarRoles(Context context);
    
    /**
   	 * Funcion que retorna lista de Rol que no han sido sincronizados
   	 */
   	Cursor listarRolNoSync(Context contexto);

}
