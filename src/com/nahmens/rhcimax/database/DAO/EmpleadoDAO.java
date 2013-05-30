package com.nahmens.rhcimax.database.DAO;

import android.content.Context;
import android.database.Cursor;

import com.nahmens.rhcimax.database.modelo.Empleado;

public interface EmpleadoDAO {
	boolean insertarEmpleado(Context contexto, Empleado empleado);
	boolean modificarEmpleado(Context contexto, Empleado empleado);
	boolean eliminarEmpleado(Context contexto, String idEmpleado);
	Empleado buscarEmpleado(Context contexto, String idEmpleado);
	Cursor listarEmpleados(Context contexto);
	Cursor listarEmpleadosPorEmpresa(Context contexto, String idEmpresa);
	
	/**
	 * Funcion utilizada por el buscador para filtrar la lista de empleados.
	 * Esta funcion compara el valor de args con el nombre del empleado, apellido 
	 * del empleado y nombre de la empresa donde trabaja el empleado.
	 * 
	 * @param args Argumentos pasados al query como los caracteres que se ingresan en el campo
	 *             de buscador
	 * @return Cursor Lista filtrada
	 * 
	 */
	Cursor buscarEmpleadoFilter(Context contexto, String args);

}
