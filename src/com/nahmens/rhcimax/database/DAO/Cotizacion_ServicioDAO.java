package com.nahmens.rhcimax.database.DAO;

import android.content.Context;

public interface Cotizacion_ServicioDAO {
	
	/**
	 * Funcion que inserta una nueva cotizacion_servicio
	 * @param contexto
	 * @param idServicio
	 * @param idCotizacion
	 * @param medida
	 * @return id de la fila insertada o -1 si algun error ocurrio.
	 */
	long insertar(Context contexto, String idServicio, String idCotizacion, String medida);
}
