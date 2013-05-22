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

}
