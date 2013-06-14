package com.nahmens.rhcimax.database.DAO;

import android.content.Context;
import android.database.Cursor;

import com.nahmens.rhcimax.database.modelo.Empleado;

public interface EmpleadoDAO {
	boolean insertarEmpleado(Context contexto, Empleado empleado, int idUsuario);
	boolean modificarEmpleado(Context contexto, Empleado empleado);
	boolean eliminarEmpleado(Context contexto, String idEmpleado);
	Empleado buscarEmpleado(Context contexto, String idEmpleado);
	Cursor listarEmpleados(Context contexto);
	
	/**
	 * Funcion que lista a todos los empleados asociados a una empresa.
	 * @param contexto
	 * @param idEmpresa
	 * @return
	 */
	Cursor listarEmpleadosPorEmpresa(Context contexto, String idEmpresa);
	
	/**
	 * Funcion para autocomplete. 
	 * Lista a todos los empleados asociados a una empresa.
	 * @param contexto
	 * @param idEmpresa
	 * @param args Argumentos pasados al sql como los caracteres que se ingresan en el 
	 *             campo autocomplete.
	 * @return
	 */
	Cursor listarEmpleadosPorEmpresaPorArgs(Context contexto, String idEmpresa, String args);
	
	/**
	 * Funcion utilizada para autocomplete.
	 * @param contexto
	 * @param args Argumentos pasados al sql como los caracteres que se ingresan en el 
	 *             campo autocomplete.
	 * @return Nombres de empleados
	 */
	Cursor listarNombresEmpleados(Context contexto, String args);
	
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
	boolean sincronizarEmpleado(Context contexto, String idEmpleado);
	
	/**
	 * Funcion que sincroniza a todos los empleados asociados a una empresa.
	 * @param contexto
	 * @param idEmpresa
	 * @return
	 */
	boolean sincronizarEmpleados(Context contexto, String idEmpresa); 

}
