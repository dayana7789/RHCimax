package com.nahmens.rhcimax.database.DAO;

import android.content.Context;
import android.database.Cursor;

public interface Empleado_CotizacionDAO {

	/**
	 * Funcion que inserta una nueva empleado_cotizacion
	 * @param contexto
	 * @param idEmpleado    Empleado al cual se le envio la cotizacion
	 * @param idCotizacion  Cotizacion enviada al empleado
	 * @return id de la fila insertada o -1 si algun error ocurrio.
	 */
	String insertar(Context contexto, String idEmpleado, String idCotizacion);
	/**
	 * Funcion que retorna lista de Empleado_Cotizacion que no han sido sincronizados
	 */
	Cursor listarEmpleado_CotizacionNoSync(Context contexto);
}
