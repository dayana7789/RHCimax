package com.nahmens.rhcimax.database.DAO;

import android.content.Context;
import android.database.Cursor;

import com.nahmens.rhcimax.database.modelo.Servicio;

public interface ServicioDAO {

	Cursor listarServicios(Context contexto);
	Servicio buscarServicio (Context contexto, String idServicio);
}
