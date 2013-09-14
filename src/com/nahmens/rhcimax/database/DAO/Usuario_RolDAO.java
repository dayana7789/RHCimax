package com.nahmens.rhcimax.database.DAO;

import android.content.Context;

import com.nahmens.rhcimax.database.modelo.Usuario_Rol;

public interface Usuario_RolDAO {

	String insertarUsuario_Rol(Context context, Usuario_Rol usuario_rol);
	
	boolean existeUsuario_Rol(Context context, Usuario_Rol usuario_rol);
	/**
     * Funcion que elimina todos los registros de una tabla
     * @param context
     * @return numero de registros eliminados
     */
    int eliminarUsuarios_Roles(Context context);
}
