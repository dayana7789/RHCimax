package com.nahmens.rhcimax.database.DAO;

import android.content.Context;

public interface Empleado_CotizacionDAO {

	/**
	 * Funcion que inserta una nueva empleado_cotizacion
	 * @param contexto
	 * @param idEmpleado    Empleado al cual se le envio la cotizacion
	 * @param idCotizacion  Cotizacion enviada al empleado
	 * @return id de la fila insertada o -1 si algun error ocurrio.
	 */
	long insertar(Context contexto, String idEmpleado, String idCotizacion);
}
