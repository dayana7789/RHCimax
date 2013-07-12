package com.nahmens.rhcimax.database.DAO;

import android.content.Context;

public interface CotizacionDAO {
	long insertarCotizacion(Context contexto, String idUsuario, String idEmpleado, String descripcion);
	boolean eliminarCotizacion(Context contexto, String idCotizacion);
	boolean sincronizarCotizacion(Context contexto, String idCotizacion);
	
}
