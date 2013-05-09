package com.nahmens.rhcimax.database.DAO;

import android.content.Context;
import android.database.Cursor;

import com.nahmens.rhcimax.database.modelo.Empresa;

public interface EmpresaDAO {
	
	Boolean insertarEmpresa(Context contexto, Empresa empresa);
	void modificarEmpresa(Context contexto, Empresa empresa);
	void eliminarEmpresa(Context contexto, Empresa empresa);
	Cursor listarEmpresas(Context contexto);
	Empresa buscarEmpresa(Context contexto, String nombre);
	Empresa buscarEmpresa(Context contexto, int id);
}
