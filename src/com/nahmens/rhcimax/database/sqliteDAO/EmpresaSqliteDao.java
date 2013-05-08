package com.nahmens.rhcimax.database.sqliteDAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.nahmens.rhcimax.database.ConexionBD;
import com.nahmens.rhcimax.database.DataBaseHelper;
import com.nahmens.rhcimax.database.DAO.EmpresaDAO;
import com.nahmens.rhcimax.database.modelo.Empresa;

public class EmpresaSqliteDao implements EmpresaDAO{

	@Override
	public Boolean insertarEmpresa(Context contexto, Empresa empresa) {
		ConexionBD conexion = new ConexionBD(contexto);
		Boolean insertado = false;
		
		try{
			conexion.open();

			ContentValues values = new ContentValues();

			values.put("nombre",empresa.getNombre());
			values.put("web",empresa.getWeb());
			values.put("telefono",empresa.getTelefono());
			values.put("direccion",empresa.getDireccion());

			long value = conexion.getDatabase().insert(DataBaseHelper.TABLA_EMPRESA, null,values);

			if(value!=-1){
				insertado = true;
			}

		}finally{
			conexion.close();
		}
		
		return insertado;
	}

	@Override
	public void modificarEmpresa(Context contexto, Empresa empresa) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void eliminarEmpresa(Context contexto, Empresa empresa) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Empresa buscarEmpresa(Context contexto, String nombre) {
		ConexionBD conexion = new ConexionBD(contexto);
		Cursor mCursor = null;
		Empresa empresa = null;

		try{
			conexion.open();

			mCursor = conexion.getDatabase().query(DataBaseHelper.TABLA_EMPRESA, new String[] {"nombre","web","telefono","direccion"}, "nombre=?", new String[] {nombre}, null, null, null);

			if(mCursor.getCount()>0){
				mCursor.moveToFirst();
				empresa = new Empresa(mCursor.getString(0), mCursor.getString(1), mCursor.getString(2), mCursor.getString(3));
			}

		}finally{
			conexion.close();
		}

		return empresa;

	}

	@Override
	public Empresa buscarEmpresa(Context contexto, int id) {
		// TODO Auto-generated method stub
		return null;
	}


}
