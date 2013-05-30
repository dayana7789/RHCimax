package com.nahmens.rhcimax.database.DAO;

import com.nahmens.rhcimax.database.modelo.Configuracion;

import android.content.Context;


public interface ConfiguracionDAO {
	
	boolean modificarKeyValue(Context contexto, Configuracion configuracion);
	Configuracion buscarPorKey(Context contexto, String key);

}
