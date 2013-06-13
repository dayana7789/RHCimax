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

}
