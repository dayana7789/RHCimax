package com.nahmens.rhcimax.database.DAO;

import android.content.Context;
import android.database.Cursor;

import com.nahmens.rhcimax.database.modelo.Tarea;

public interface TareaDAO {
	
	public long insertarTarea(Context contexto, Tarea tarea);
	boolean modificarTarea(Context contexto, Tarea tarea);
	boolean eliminarTarea(Context contexto, String idTarea);
	Tarea buscarTarea(Context contexto, String idTarea);
	Cursor listarTareas(Context contexto);
	/**
	 * Funcion utilizada por el buscador para filtrar la lista de tareas.
	 * Esta funcion compara el valor de args con todos los campos de la tarea.
	 * 
	 * @param args Argumentos pasados al query como los caracteres que se ingresan en el campo
	 *             de buscador
	 * @return Cursor Lista filtrada
	 * 
	 */
	Cursor buscarTareaFilter(Context contexto, String args);
	
	

}
