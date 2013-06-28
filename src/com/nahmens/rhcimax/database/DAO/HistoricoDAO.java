package com.nahmens.rhcimax.database.DAO;

import android.content.Context;
import android.database.Cursor;

import com.nahmens.rhcimax.database.modelo.Historico;

public interface HistoricoDAO {
	
	long insertarHistorico(Context contexto, Historico historico);
	Cursor listarHistoricos(Context contexto);
	

	/**
	 * Funcion utilizada por el buscador para filtrar la lista de historicos.
	 * Esta funcion compara el valor de args con ..
	 * 
	 * @param args Argumentos pasados al query como los caracteres que se ingresan en el campo
	 *             de buscador
	 * @return Cursor Lista filtrada
	 * 
	 */
	Cursor buscarHistoricoFilter(Context contexto, String args);
	
	boolean sincronizarHistorico(Context contexto, String idEmpleado);

}
