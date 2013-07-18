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
	String insertar(Context contexto, Cotizacion_Servicio cot_serv);
	Cursor listarCotizacion_Servicio(Context contexto, String idCotizacion);
	/**
	 * Funcion que retorna lista de Cotizacion_Servicio que no han sido sincronizados
	 */
	Cursor listarCotizacion_ServicioNoSync(Context contexto);
}
