package com.nahmens.rhcimax.database.DAO;

import android.content.Context;
import android.database.Cursor;

import com.nahmens.rhcimax.database.modelo.Cotizacion_Servicio;

public interface Cotizacion_ServicioDAO {
	
	/**
	 * Funcion que inserta una nueva cotizacion_servicio
	 * @param contexto
	 * @param cot_serv
	 * @return id de la fila insertada o -1 si algun error ocurrio.
	 */
	long insertar(Context contexto, Cotizacion_Servicio cot_serv);
	Cursor listarCotizacion_Servicio(Context contexto, String idCotizacion);
}
