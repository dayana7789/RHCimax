package com.nahmens.rhcimax.controlador;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.nahmens.rhcimax.R;
import com.nahmens.rhcimax.adapters.ListaServiciosCursorAdapter;
import com.nahmens.rhcimax.database.modelo.Empleado;
import com.nahmens.rhcimax.database.modelo.Empresa;
import com.nahmens.rhcimax.database.modelo.Servicio;
import com.nahmens.rhcimax.database.sqliteDAO.EmpleadoSqliteDao;
import com.nahmens.rhcimax.database.sqliteDAO.EmpresaSqliteDao;
import com.nahmens.rhcimax.database.sqliteDAO.ServicioSqliteDao;

import android.os.Bundle;
import android.database.Cursor;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
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
				llenarCampos(view, idEmpresa, "empresa");
			}else{
				String idEmpleado = mArgumentos.getString("id");
				if(idEmpleado != null){
					llenarCampos(view, idEmpleado, "empleado");
				}
			}

		}

		return view;
	}

	/*
	 * @param id Se refiere al idEmpresa o al idEmpleado segun el tipoCliente.
	 * @param tipoCliente Posibles valores: empleado o empresa.
	 */

	private void llenarCampos(View v, String id, String tipoCliente){

		if(tipoCliente.equals("empresa")){
			
			EmpresaSqliteDao empresaDao = new EmpresaSqliteDao();
			Empresa empresa  = empresaDao.buscarEmpresa(getActivity(),id);

			TextView tvEmpresa = (TextView) v.findViewById(R.id.textViewEmpresa);
			tvEmpresa.setText(empresa.getNombre());

			EmpleadoSqliteDao empleadoDao = new EmpleadoSqliteDao();
			Cursor mCursorEmpleados = empleadoDao.listarEmpleadosPorEmpresa(getActivity(), id);

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
		}else if(tipoCliente.equals("empleado")){

			EmpleadoSqliteDao empleadoDao = new EmpleadoSqliteDao();
			Empleado empleado = empleadoDao.buscarEmpleado(getActivity(), id);
			
			EmpresaSqliteDao empresaDao = new EmpresaSqliteDao();
			Empresa empresa  = empresaDao.buscarEmpresa(getActivity(),""+empleado.getIdEmpresa());

			TextView tvEmpresa = (TextView) v.findViewById(R.id.textViewEmpresa);
			tvEmpresa.setText(empresa.getNombre());

			//Correo del empleado
			if(empleado!=null){
				List<HashMap<String, String>> mList = new ArrayList<HashMap<String, String>>();
				
				HashMap<String, String> map = new HashMap<String, String>();
	        	map.put(Empleado.NOMBRE, empleado.getNombre());
	        	map.put(Empleado.APELLIDO, empleado.getApellido());
	        	map.put(Empleado.EMAIL,empleado.getEmail());

	        	
				mList.add(map);
				//indicamos los campos que queremos mostrar (from) y en donde (to)
				String[] from = new String[] { Empleado.NOMBRE, Empleado.APELLIDO, Empleado.EMAIL};
				int[] to = new int[] { R.id.textViewServNombreEmp,R.id.textViewServApellidoEmp, R.id.checkBoxServEmail};
				ListView lvEmpleados = (ListView) v.findViewById (R.id.listViewCorreos);

				//Creamos un array adapter para desplegar cada una de las filas
				SimpleAdapter notes = new SimpleAdapter(getActivity(),mList, R.layout.activity_fila_correo, from, to);
				lvEmpleados.setAdapter(notes);

			}
			
		}else{
			Log.e("ServiciosActivity", "Tipo de cliente no valido: " + tipoCliente);
		}

		ServicioSqliteDao servicioDao = new ServicioSqliteDao();
		Cursor mCursorServicios = servicioDao.listarServicios(getActivity());

		//Lista de servicios
		if(mCursorServicios.getCount()>0){
			//indicamos los campos que queremos mostrar (from) y en donde (to)
			String[] from = new String[] { Servicio.NOMBRE};
			int[] to = new int[] { R.id.checkBoxServicio};
			ListView lvServicios = (ListView) v.findViewById (R.id.listViewServicios);

			//Creamos un array adapter para desplegar cada una de las filas
			ListaServiciosCursorAdapter notes = new ListaServiciosCursorAdapter(getActivity(), R.layout.activity_fila_servicio, mCursorServicios, from, to, 0);
			lvServicios.setAdapter(notes);
			lvServicios.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

		}

	}

}
