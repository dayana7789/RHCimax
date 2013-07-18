package com.nahmens.rhcimax.database.DAO;

import android.content.Context;
import android.database.Cursor;

import com.nahmens.rhcimax.database.modelo.Tarea;

public interface TareaDAO {
	
	public String insertarTarea(Context contexto, Tarea tarea);
	boolean modificarTarea(Context contexto, Tarea tarea);
	boolean eliminarTarea(Context contexto, String idTarea);
	Tarea buscarTarea(Context contexto, String idTarea);
	//Cursor listarTareas(Context contexto);
	/**
	 * Funcion utilizada por el buscador para filtrar la lista de tareas.
	 * Esta funcion compara el valor de args con todos los campos de la tarea.
	 * 
	 * @param args Argumentos pasados al query como los caracteres que se ingresan en el campo
	 *             de buscador
	 * @param fltrarPorUsuario Indica si queremos filtrar la lista por los items creados por el usuario o no.
	 * @return Cursor Lista filtrada
	 * 
	 */
	Cursor buscarTareaFilter(Context contexto, String args, boolean fltrarPorUsuario);
	Cursor listarTareasPorEmpresa(Context contexto, String idEmpresa);
	Cursor listarTareasPorEmpleado(Context contexto, String idEmpleado);
	boolean sincronizarTarea(Context contexto, String idTarea);
	
	/**
	 * Funcion que retorna lista de Tareas que no han sido sincronizados
	 */
	Cursor listarTareasNoSync(Context contexto);
	

}
