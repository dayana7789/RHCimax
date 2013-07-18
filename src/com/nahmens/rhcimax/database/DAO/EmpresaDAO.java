package com.nahmens.rhcimax.database.DAO;

import android.content.Context;
import android.database.Cursor;

import com.nahmens.rhcimax.database.modelo.Empresa;

public interface EmpresaDAO {
	
	/** 
	 * Funcion que inserta una nueva empresa.
	 * 
	 * @param empresa Datos de la empresa a ingresar.
	 * @return Id de la empresa ingresada o -1 en caso de error.
	 */
	String insertarEmpresa(Context contexto, Empresa empresa);
	boolean modificarEmpresa(Context contexto, Empresa empresa);
	boolean eliminarEmpresa(Context contexto, String idEmpresa);
	
	/**
	 * Funcion que retorna lista de empresas que no han sido sincronizados
	 */
	Cursor listarEmpresasNoSync(Context contexto);
	
	/**
	 * Funcion utilizada para autocomplete.
	 * 
	 * @param contexto
	 * @param args Argumentos pasados al sql como los caracteres que se ingresan en el 
	 *             campo autocomplete.
	 * @return Nombres de empresas
	 */
	Cursor listarNombresEmpresas(Context contexto, String args);
	Empresa buscarEmpresa(Context contexto, String id);
	Cursor buscarEmpresaPorNombre(Context contexto, String args);
	
	/**
	 * Funcion utilizada por el buscador para filtrar la lista de empresas.
	 * Esta funcion compara el valor de args con el nombre de la empresa.
	 * 
	 * @param args Argumentos pasados al query como los caracteres que se ingresan en el campo
	 *             de buscador
	 * @return Cursor Lista filtrada
	 * 
	 */
	Cursor buscarEmpresaFilter(Context contexto, String args);
	
	/**
	 * Funcion que sincroniza a una empresa.
	 * @param contexto
	 * @param idEmpresa Empresa a ser sincronizada
	 * @return si la sincronizacion fue llevada exitosamente.
	 */
	boolean sincronizarEmpresa(Context contexto, String idEmpresa);

	/**
	 * Funcion que indica si dado un idEmpresa es o no cliente del 
	 * idUsuario. Se entiende que una empresa es cliente del
	 * usuario, si el usuario creo el registro de esta empresa.
	 * @param contexto
	 * @param idEmpresa
	 * @param idUsuario
	 * @return si es o no cliente del idUsuario
	 */
	boolean esClienteDelUsuario(Context contexto, String idEmpresa, String idUsuario);

}
