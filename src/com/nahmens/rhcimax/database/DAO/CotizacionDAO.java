package com.nahmens.rhcimax.database.DAO;

import android.content.Context;
import android.database.Cursor;

import com.nahmens.rhcimax.database.modelo.Cotizacion;

public interface CotizacionDAO {
	String insertarCotizacion(Context contexto, Cotizacion cotizacion);
	boolean eliminarCotizacion(Context contexto, String idCotizacion);
	boolean sincronizarCotizacion(Context contexto, String idCotizacion);
	/**
	 * Funcion que retorna lista de Cotizaciones que no han sido sincronizados
	 */
	Cursor listarCotizacionNoSync(Context contexto);
	
}
