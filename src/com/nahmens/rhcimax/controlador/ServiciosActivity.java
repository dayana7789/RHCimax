package com.nahmens.rhcimax.controlador;

import java.util.HashMap;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.nahmens.rhcimax.R;
import com.nahmens.rhcimax.adapters.ListaCorreosCursorAdapter;
import com.nahmens.rhcimax.adapters.ListaServiciosCursorAdapter;
import com.nahmens.rhcimax.database.modelo.Empleado;
import com.nahmens.rhcimax.database.modelo.Empresa;
import com.nahmens.rhcimax.database.modelo.Servicio;
import com.nahmens.rhcimax.database.sqliteDAO.EmpleadoSqliteDao;
import com.nahmens.rhcimax.database.sqliteDAO.EmpresaSqliteDao;
import com.nahmens.rhcimax.database.sqliteDAO.ServicioSqliteDao;

public class ServiciosActivity extends Fragment {

	private FragmentManager fragmentManager; 

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.activity_servicios, container, false);
		this.fragmentManager = this.getFragmentManager();
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

	/**
	 * Funcion que llena la informacion de servicios y clientes.
	 * @param id Se refiere al idEmpresa o al idEmpleado segun el tipoCliente.
	 * @param tipoCliente Posibles valores: empleado o empresa.
	 */

	private void llenarCampos(View v, String id, String tipoCliente){

		if(tipoCliente.equals("empresa")){
			llenarCamposCliente(v,id, null);

		}else if(tipoCliente.equals("empleado")){
			EmpleadoSqliteDao empleadoDao = new EmpleadoSqliteDao();
			Empleado empleado = empleadoDao.buscarEmpleado(getActivity(), id);
			
			llenarCamposCliente(v, ""+empleado.getIdEmpresa(), id);

		}else{
			Log.e("ServiciosActivity", "Tipo de cliente no valido: " + tipoCliente);
		}

		llenarCamposServicios(v,id,tipoCliente);
	}

	/**
	 * Funcion que llena en pantalla los datos relacionados a los servicios:
	 * lista de servicios.
	 * 
	 * @param v  View del fragmento
	 * @param id Identificador de la empresa
	 * @param tipoCliente Posibles valores: empresa o empleado
	 */
	private void llenarCamposServicios(View v, String id, String tipoCliente) {
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
			
			Button buttonFinalizar= (Button)  v.findViewById(R.id.buttonFinalizar);
			buttonFinalizar.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v){
					HashMap<Integer, Boolean> servSeleccionados = ListaServiciosCursorAdapter.getServiciosSeleccionados();
					
					ClientesActivity fragment = new ClientesActivity();

					Bundle mArgumentos =  new Bundle();
					mArgumentos.putString(AplicacionActivity.tagCuadroColor, AplicacionActivity.tagRojo);
					
					//pasamos al fragment la notificacion de cambio de color
					//del cuadro de color
					fragment.setArguments(mArgumentos); 
					
					fragmentManager.beginTransaction()
					.replace(android.R.id.tabcontent,fragment, AplicacionActivity.tagFragmentClientes)
					.addToBackStack(null)
					.commit();
					
					//seteamos la lista de servicios seleccionados a null de la vista servicios
					ListaServiciosCursorAdapter.setServiciosSeleccionados(null);

				}});
		}
	}


	/**
	 * Funcion que llena en pantalla los datos relacionados al cliente:
	 * lista de correos de todos los empleados.
	 * 
	 * @param v  View del fragmento
	 * @param idEmpresa Identificador de la empresa
	 * @param idEmpleado Identificador del empleado. 
	 *                   Si idEmpleado==null, se marcan todos los correos (checkbox).
	 *                   Si no, se marca solo el correo de idEmpleado.
	 */
	private void llenarCamposCliente(View v, String idEmpresa, String idEmpleado) {
		EmpresaSqliteDao empresaDao = new EmpresaSqliteDao();
		Empresa empresa  = empresaDao.buscarEmpresa(getActivity(),idEmpresa);

		TextView tvEmpresa = (TextView) v.findViewById(R.id.textViewEmpresa);
		tvEmpresa.setText(empresa.getNombre());

		EmpleadoSqliteDao empleadoDao = new EmpleadoSqliteDao();
		Cursor mCursorEmpleados = empleadoDao.listarEmpleadosPorEmpresa(getActivity(), idEmpresa);

		//Lista de correos de empleados
		if(mCursorEmpleados.getCount()>0){

			//indicamos los campos que queremos mostrar (from) y en donde (to)
			String[] from = new String[] { Empleado.NOMBRE, Empleado.APELLIDO, Empleado.EMAIL};
			int[] to = new int[] { R.id.textViewServNombreEmp,R.id.textViewServApellidoEmp, R.id.checkBoxServEmail};
			ListView lvEmpleados = (ListView) v.findViewById (R.id.listViewCorreos);

			//Creamos un array adapter para desplegar cada una de las filas
			ListaCorreosCursorAdapter notes = null;
			
			if(idEmpleado==null){
				notes = new ListaCorreosCursorAdapter(getActivity(), R.layout.activity_fila_correo, mCursorEmpleados, from, to, 0, null);
			}else{
				notes = new ListaCorreosCursorAdapter(getActivity(), R.layout.activity_fila_correo, mCursorEmpleados, from, to, 0, idEmpleado);
			}
			lvEmpleados.setAdapter(notes);
		}
	}
}
