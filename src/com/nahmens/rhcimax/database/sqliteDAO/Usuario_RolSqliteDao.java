package com.nahmens.rhcimax.database.sqliteDAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.nahmens.rhcimax.database.ConexionBD;
import com.nahmens.rhcimax.database.DataBaseHelper;
import com.nahmens.rhcimax.database.DAO.Usuario_RolDAO;
import com.nahmens.rhcimax.database.modelo.Usuario_Rol;

public class Usuario_RolSqliteDao implements Usuario_RolDAO{

	@Override
	public String insertarUsuario_Rol(Context context, Usuario_Rol usuario_rol) {
		
		long value = -1;
		ConexionBD conexion = new ConexionBD(context);
		try{

			conexion.open();

			ContentValues values = new ContentValues();

			values.put(Usuario_Rol.ID_ROL, usuario_rol.getIdRol());
			values.put(Usuario_Rol.ID_USUARIO, usuario_rol.getIdUsuario());

			value = conexion.getDatabase().insertOrThrow(DataBaseHelper.TABLA_USUARIO_ROL, null,values);

		}finally{
			conexion.close();
		}

		return value+"";
	}

	@Override
	public int eliminarUsuarios_Roles(Context context) {
		ConexionBD conexion = new ConexionBD(context);
		int numCol = 0;
		
		try{
			conexion.open();
			numCol = conexion.getDatabase().delete(DataBaseHelper.TABLA_USUARIO_ROL, "1", null);

		}finally{
			conexion.close();
		}

		return numCol;	
	}

	@Override
	public boolean existeUsuario_Rol(Context context, Usuario_Rol usuario_rol) {
		ConexionBD conexion = new ConexionBD(context);
		Cursor mCursor = null;
		boolean existe = false;

		try{
			conexion.open();

			mCursor = conexion.getDatabase().query(DataBaseHelper.TABLA_USUARIO_ROL , null , Usuario_Rol.ID_USUARIO+ " = ? AND " + Usuario_Rol.ID_ROL + " = ?", new String [] {usuario_rol.getIdUsuario(), usuario_rol.getIdRol()}, null, null, null);

			if (mCursor.getCount() > 0) {
				existe = true;
			}

		}finally{
			conexion.close();
		}

		return existe;
	}
}
