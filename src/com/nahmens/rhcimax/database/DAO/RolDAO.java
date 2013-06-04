package com.nahmens.rhcimax.database.DAO;

import android.content.Context;

import com.nahmens.rhcimax.database.modelo.Rol;

public interface RolDAO {
	
	Rol buscarRolUsuario(Context contexto, String idUsuario);

}
