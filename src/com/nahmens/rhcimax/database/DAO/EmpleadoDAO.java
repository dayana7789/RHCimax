package com.nahmens.rhcimax.database.DAO;

import android.content.Context;
import android.database.Cursor;

import com.nahmens.rhcimax.database.modelo.Empleado;

public interface EmpleadoDAO {
	Boolean insertarEmpleado(Context contexto, Empleado empleado);
	void modificarEmpleado(Context contexto, Empleado empleado);
	void eliminarEmpleado(Context contexto, Empleado empleado);
	Cursor listarEmpleados(Context contexto);

}
