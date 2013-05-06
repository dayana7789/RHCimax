package com.nahmens.rhcimax.database.DAO;

import android.content.Context;

import com.nahmens.rhcimax.database.modelo.Usuario;

public interface UsuarioDAO {
	
	Usuario buscarUsuario(Context context, String nombre);
    Boolean insertarUsuario(Context context, Usuario usuario);
    void modificarUsuario(Context context, Usuario usuario);
    void listarUsuarios(Context context);
}
