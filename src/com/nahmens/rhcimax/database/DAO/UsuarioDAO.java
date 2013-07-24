package com.nahmens.rhcimax.database.DAO;

import android.content.Context;
import android.database.Cursor;

import com.nahmens.rhcimax.database.modelo.Usuario;

public interface UsuarioDAO {
	
	Usuario buscarUsuario(Context context, String login, String password);
	Usuario buscarUsuario(Context context, String idUsuario);
	
	/**
	 * Funcion que inserta un nuevo usuario
	 * @param context
	 * @param usuario
	 * @param autoincrement Indica si queremos insertar un usuario con un determinado id o 
	 *                      dejar que el sisteme cree el id.
	 * @return Id del usuario ingresado o -1 en caso de error.
	 */
	String insertarUsuario(Context context, Usuario usuario);
    boolean modificarUsuario(Context context, Usuario usuario);
    Cursor listarUsuarios(Context context);
    
    /**
     * Funcion que elimina todos los registros de una tabla
     * @param context
     * @return numero de regsitros eliminados
     */
    int eliminarUsuarios(Context context);
 
}
