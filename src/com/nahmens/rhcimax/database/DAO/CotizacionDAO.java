package com.nahmens.rhcimax.database.DAO;

import android.content.Context;

import com.nahmens.rhcimax.database.modelo.Cotizacion;

public interface CotizacionDAO {
	String insertarCotizacion(Context contexto, Cotizacion cotizacion);
	boolean eliminarCotizacion(Context contexto, String idCotizacion);
	boolean sincronizarCotizacion(Context contexto, String idCotizacion);
	
}
