package com.nahmens.rhcimax.database.DAO;

import android.content.Context;
import android.database.Cursor;

import com.nahmens.rhcimax.database.modelo.Usuario;

public interface UsuarioDAO {
	
	Usuario buscarUsuario(Context context, String login, String password);
    Boolean insertarUsuario(Context context, Usuario usuario);
    void modificarUsuario(Context context, Usuario usuario);
    Cursor listarUsuarios(Context context);
}
