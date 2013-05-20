package com.nahmens.rhcimax.database.DAO;

import android.content.Context;
import android.database.Cursor;

import com.nahmens.rhcimax.database.ConexionBD;
import com.nahmens.rhcimax.database.modelo.Empresa;

public interface EmpresaDAO {
	
	Boolean insertarEmpresa(Context contexto, Empresa empresa);
	void modificarEmpresa(Context contexto, Empresa empresa);
	boolean eliminarEmpresa(Context contexto, String idEmpresa);
	/**
	 * 
	 * @param contexto
	 * @return Toda la informacion almacenada acerca de cada una de las empresas
	 */
	Cursor listarEmpresas(Context contexto);
	
	/**
	 * Funcion utilizada para autocomplete.
	 * 
	 * @param contexto
	 * @param args Argumentos pasados al sql como los caracteres que se ingresan en el 
	 *             campo autocomplete.
	 * @param conexion Conexion abierta de la BD desde la actividad. Es importante que se haga
	 *                 asi, para poder cerrar la conexion a la BD en el momento en que la actividad
	 *                 se destruya. De lo contrario el cierre a la conexion nunca se hace bien y 
	 *                 se muestran excepciones en el log. 
	 * @return Nombres de empresas
	 */
	Cursor listarNombresEmpresas(Context contexto, String args, ConexionBD conexion);
	Empresa buscarEmpresa(Context contexto, String nombre);
	Empresa buscarEmpresa(Context contexto, int id);
}
