package com.nahmens.rhcimax.controlador;

import com.nahmens.rhcimax.R;
import com.nahmens.rhcimax.adapters.ListaEmpleadosCursorAdapter;
import com.nahmens.rhcimax.adapters.ListaServiciosCursorAdapter;
import com.nahmens.rhcimax.database.modelo.Empleado;
import com.nahmens.rhcimax.database.modelo.Empresa;
import com.nahmens.rhcimax.database.modelo.Servicio;
import com.nahmens.rhcimax.database.sqliteDAO.EmpleadoSqliteDao;
import com.nahmens.rhcimax.database.sqliteDAO.EmpresaSqliteDao;
import com.nahmens.rhcimax.database.sqliteDAO.ServicioSqliteDao;

import android.os.Bundle;
import android.app.Activity;
import android.database.Cursor;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class ServiciosActivity extends Fragment {

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.activity_servicios, container, false);

		final Bundle mArgumentos = this.getArguments();

		//Nos aseguramos que nos hayan pasado argumentos
		if(mArgumentos!= null){
			String idEmpresa = mArgumentos.getString("idEmpresa");
			
			if(idEmpresa != null){
				llenarCampos(view, idEmpresa);
			}else{
				Log.e("ServiciosActivity", "Me llamaron sin idEmpresa");
			}
				
		}
		
		return view;
	}
	
	
	private void llenarCampos(View v, String idEmpresa){
		EmpresaSqliteDao empresaDao = new EmpresaSqliteDao();
		Empresa empresa  = empresaDao.buscarEmpresa(getActivity(),idEmpresa);
		
		EmpleadoSqliteDao empleadoDao = new EmpleadoSqliteDao();
		Cursor mCursorEmpleados = empleadoDao.listarEmpleadosPorEmpresa(getActivity(), idEmpresa);
		
		ServicioSqliteDao servicioDao = new ServicioSqliteDao();
		Cursor mCursorServicios = servicioDao.listarServicios(getActivity());
		
		TextView tvEmpresa = (TextView) v.findViewById(R.id.textViewEmpresa);
		tvEmpresa.setText(empresa.getNombre());
		
		//Lista de correos de empleados
		if(mCursorEmpleados.getCount()>0){

			//indicamos los campos que queremos mostrar (from) y en donde (to)
			String[] from = new String[] { Empleado.NOMBRE, Empleado.APELLIDO, Empleado.EMAIL};
			int[] to = new int[] { R.id.textViewServNombreEmp,R.id.textViewServApellidoEmp, R.id.checkBoxServEmail};
			ListView lvEmpleados = (ListView) v.findViewById (R.id.listViewCorreos);

			//Creamos un array adapter para desplegar cada una de las filas
			SimpleCursorAdapter notes = new SimpleCursorAdapter(getActivity(), R.layout.activity_fila_correo, mCursorEmpleados, from, to, 0);
			lvEmpleados.setAdapter(notes);

		}
		
		//Lista de servicios
		if(mCursorServicios.getCount()>0){
			//indicamos los campos que queremos mostrar (from) y en donde (to)
			String[] from = new String[] { Servicio.NOMBRE, Servicio.PRECIO, Servicio.DESCRIPCION};
			int[] to = new int[] { R.id.checkBoxServicio,0, 1};
			ListView lvServicios = (ListView) v.findViewById (R.id.listViewServicios);

			//Creamos un array adapter para desplegar cada una de las filas
			ListaServiciosCursorAdapter notes = new ListaServiciosCursorAdapter(getActivity(), R.layout.activity_fila_servicio, mCursorServicios, from, to, 0);
			lvServicios.setAdapter(notes);

		}else{
			Log.e("no deberia entrar aqio","no entre");
		}
		
	}
	
}
