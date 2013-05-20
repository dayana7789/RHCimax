package com.nahmens.rhcimax.database.sqliteDAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

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
			values.put("telefono",empresa.getTelefono());
			values.put("web",empresa.getWeb());
			values.put("rif", empresa.getRif());
			values.put("dirFiscal",empresa.getDirFiscal());
			values.put("dirComercial",empresa.getDirComercial());

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
	public boolean eliminarEmpresa(Context contexto, String idEmpresa) {
		ConexionBD conexion = new ConexionBD(contexto);
		boolean eliminado = false;

		try{
			conexion.open();

			long value = conexion.getDatabase().delete(DataBaseHelper.TABLA_EMPRESA, "_id=?", new String[]{idEmpresa});

			if(value!=0){
				eliminado = true;
			}

		}finally{
			conexion.close();
		}

		return eliminado;
	}

	@Override
	public Empresa buscarEmpresa(Context contexto, String nombre) {
		ConexionBD conexion = new ConexionBD(contexto);
		Cursor mCursor = null;
		Empresa empresa = null;

		try{
			conexion.open();

			mCursor = conexion.getDatabase().query(DataBaseHelper.TABLA_EMPRESA, new String[] {"nombre","telefono","rif","web","dirFiscal","dirComercial"}, "nombre=?", new String[] {nombre}, null, null, null);

			if(mCursor.getCount()>0){
				mCursor.moveToFirst();
				empresa = new Empresa(mCursor.getString(0), mCursor.getString(1), mCursor.getString(2), mCursor.getString(3), mCursor.getString(4), mCursor.getString(5));
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

	@Override
	public Cursor listarEmpresas(Context contexto) {
		ConexionBD conexion = new ConexionBD(contexto);
		Cursor mCursor = null;
		try{

			conexion.open();

			mCursor = conexion.getDatabase().rawQuery("SELECT * FROM " + DataBaseHelper.TABLA_EMPRESA  + " order by nombre", null);

			if (mCursor != null) {
				mCursor.moveToFirst();
			}

		}finally{
			conexion.close();
		}

		return mCursor;		
	}

	@Override
	public Cursor listarNombresEmpresas(Context contexto, String args, ConexionBD conexion) {

		Cursor mCursor = null;
		String sqlQuery = "";

		sqlQuery  = " SELECT " + Empresa.ID + ", " + Empresa.NOMBRE;
		sqlQuery += " FROM " + DataBaseHelper.TABLA_EMPRESA;
		sqlQuery += " WHERE " + Empresa.NOMBRE + " LIKE '%" + args + "%' ";
		sqlQuery += " ORDER BY " + Empresa.NOMBRE;

		mCursor = conexion.getDatabase().rawQuery(sqlQuery,null);

		return mCursor;		
	}
}
