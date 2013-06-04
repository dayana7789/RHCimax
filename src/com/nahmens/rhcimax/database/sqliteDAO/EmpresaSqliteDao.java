package com.nahmens.rhcimax.database.sqliteDAO;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.nahmens.rhcimax.database.ConexionBD;
import com.nahmens.rhcimax.database.DataBaseHelper;
import com.nahmens.rhcimax.database.DAO.EmpresaDAO;
import com.nahmens.rhcimax.database.modelo.Empresa;

public class EmpresaSqliteDao implements EmpresaDAO{

	@Override
	public long insertarEmpresa(Context contexto, Empresa empresa, int idUsuario) {
		ConexionBD conexion = new ConexionBD(contexto);
		long idFila = 0;

		try{
			conexion.open();

			ContentValues values = new ContentValues();

			values.put("nombre",empresa.getNombre());
			values.put("telefono",empresa.getTelefono());
			values.put("web",empresa.getWeb());
			values.put("rif", empresa.getRif());
			values.put("dirFiscal",empresa.getDirFiscal());
			values.put("dirComercial",empresa.getDirComercial());
			values.put("idUsuario",idUsuario);

			idFila = conexion.getDatabase().insert(DataBaseHelper.TABLA_EMPRESA, null,values);

		}finally{
			conexion.close();
		}

		return idFila;
	}

	@Override
	public boolean modificarEmpresa(Context contexto, Empresa empresa) {
		ConexionBD conexion = new ConexionBD(contexto);
		boolean modificado = false;

		try{
			conexion.open();

			ContentValues contenido = new ContentValues();
			contenido.put("nombre", empresa.getNombre());
			contenido.put("telefono", empresa.getTelefono());
			contenido.put("rif", empresa.getRif());
			contenido.put("web", empresa.getWeb());
			contenido.put("dirFiscal", empresa.getDirFiscal());
			contenido.put("dirComercial", empresa.getDirComercial());

			int value = conexion.getDatabase().update(DataBaseHelper.TABLA_EMPRESA, contenido, "_id=?", new String []{Integer.toString(empresa.getId())});

			if(value!=0){
				modificado = true;
			}

		}finally{
			conexion.close();
		}

		return modificado;

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
	public Empresa buscarEmpresa(Context contexto, String id) {

		ConexionBD conexion = new ConexionBD(contexto);
		Cursor mCursor = null;
		Empresa empresa = null;

		try{
			conexion.open();

			mCursor = conexion.getDatabase().query(DataBaseHelper.TABLA_EMPRESA , null , Empresa.ID + " = ? ", new String [] {id}, null, null, null);

			if (mCursor.getCount() > 0) {
				mCursor.moveToFirst();

				empresa = new Empresa( mCursor.getString(mCursor.getColumnIndex("nombre")), 
						mCursor.getString(mCursor.getColumnIndex("telefono")), 
						mCursor.getString(mCursor.getColumnIndex("rif")), 
						mCursor.getString(mCursor.getColumnIndex("web")), 
						mCursor.getString(mCursor.getColumnIndex("dirFiscal")), 
						mCursor.getString(mCursor.getColumnIndex("dirComercial")),
						mCursor.getInt(mCursor.getColumnIndex("idUsuario")));
			}

		}finally{
			conexion.close();
		}

		return empresa;

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
	public Cursor listarNombresEmpresas(Context contexto, String args) {

		ConexionBD conexion = new ConexionBD(contexto);
		Cursor mCursor = null;
		String sqlQuery = "";
		try{
			conexion.open();

			sqlQuery  = " SELECT " + Empresa.ID + ", " + Empresa.NOMBRE;
			sqlQuery += " FROM " + DataBaseHelper.TABLA_EMPRESA;
			sqlQuery += " WHERE " + Empresa.NOMBRE + " LIKE '%" + args + "%' ";
			sqlQuery += " ORDER BY " + Empresa.NOMBRE;

			mCursor = conexion.getDatabase().rawQuery(sqlQuery,null);
			
			if (mCursor != null) {
				mCursor.moveToFirst();
			}
			
		}finally{
			conexion.close();
		}
		
		return mCursor;		
	}

	@Override
	public Cursor buscarEmpresaPorNombre(Context contexto, String args) {

		ConexionBD conexion = new ConexionBD(contexto);
		Cursor mCursor = null;
		String sqlQuery = "";
		try{
			conexion.open();

			sqlQuery  = " SELECT " + Empresa.ID + ", " + Empresa.NOMBRE;
			sqlQuery += " FROM " + DataBaseHelper.TABLA_EMPRESA;
			sqlQuery += " WHERE " + Empresa.NOMBRE + " = '" + args +"'";

			mCursor = conexion.getDatabase().rawQuery(sqlQuery,null);
			
			if (mCursor != null) {
				mCursor.moveToFirst();
			}
			
		}finally{
			conexion.close();
		}
		
		return mCursor;		
	}

	@Override
	public Cursor buscarEmpresaFilter(Context contexto, String args) {

		ConexionBD conexion = new ConexionBD(contexto);
		Cursor mCursor = null;
		String sqlQuery = "";
		try{
			conexion.open();

			sqlQuery  = " SELECT * ";
			sqlQuery += " FROM " + DataBaseHelper.TABLA_EMPRESA;
			sqlQuery += " WHERE " + Empresa.NOMBRE + " LIKE '%" + args + "%' ";

			mCursor = conexion.getDatabase().rawQuery(sqlQuery,null);

			if (mCursor != null) {
				mCursor.moveToFirst();
			}

		}finally{
			conexion.close();
		}

		return mCursor;	
	}

	@Override
	public boolean sincronizarEmpresa(Context contexto, String idEmpresa) {
		ConexionBD conexion = new ConexionBD(contexto);
		boolean sincronizado = false;
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss",Locale.getDefault());

		try{
			conexion.open();

			ContentValues contenido = new ContentValues();
			contenido.put(Empresa.FECHA_SINCRONIZACION,dateFormat.format(new Date()));

			int value = conexion.getDatabase().update(DataBaseHelper.TABLA_EMPRESA, contenido, "_id=?", new String []{idEmpresa});

			if(value!=0){
				sincronizado = true;
			}

		}finally{
			conexion.close();
		}

		return sincronizado;
	}
}
