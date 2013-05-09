package com.nahmens.rhcimax.controlador;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.nahmens.rhcimax.R;
import com.nahmens.rhcimax.adapters.ListaClientesCursorAdapter;
import com.nahmens.rhcimax.database.modelo.Empleado;
import com.nahmens.rhcimax.database.modelo.Empresa;
import com.nahmens.rhcimax.database.sqliteDAO.EmpleadoSqliteDao;
import com.nahmens.rhcimax.database.sqliteDAO.EmpresaSqliteDao;



public class ClientesActivity extends ListFragment{
	private Cursor mCursor;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.activity_clientes, container, false);

		listarEmpleados(view);
		listarEmpresas(view);

		// Registro del evento OnClick del buttonEmpresa
		Button bEmp = (Button)view.findViewById(R.id.buttonEmpresa);
		bEmp.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				DatosEmpresaActivity fragmentDatosEmpresa = new DatosEmpresaActivity();
				final FragmentTransaction ft = getFragmentManager().beginTransaction();
				//Cambiamos el layout de clientes por datos_empresa e indicamos el tag del frame.
				ft.replace(android.R.id.tabcontent,fragmentDatosEmpresa, AplicacionActivity.tagFragmentDatosEmpresa); 
				//preservamos el estado anterior al hacer click en back button
				ft.addToBackStack(null);
				ft.commit(); 
			}
		});

		// Registro del evento OnClick del buttonCliente
		Button bClient = (Button)view.findViewById(R.id.buttonCliente);
		bClient.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				DatosClienteActivity fragmentDatosClientes = new DatosClienteActivity();
				final FragmentTransaction ft = getFragmentManager().beginTransaction();
				//Cambiamos el layout de clientes por datos_cliente e indicamos el tag del frame.
				ft.replace(android.R.id.tabcontent,fragmentDatosClientes, AplicacionActivity.tagFragmentDatosCliente); 
				//preservamos el estado anterior al hacer click en back button
				ft.addToBackStack(null);
				ft.commit(); 
			}
		});


		return view;
	}

	private void listarEmpresas(View view){
		
		//Cargamos la lista de empresas
		EmpresaSqliteDao empresaDao = new EmpresaSqliteDao();

		Context context = getActivity();
		mCursor = empresaDao.listarEmpresas(getActivity());

		if(mCursor.getCount()>0){

			//indicamos los campos que queremos mostrar (from) y en donde (to)
			String[] from = new String[] { Empresa.NOMBRE, Empresa.TELEFONO};
			int[] to = new int[] { R.id.textViewNombreIzq,  R.id.textViewNombreCent };


			ListView lvEmpresas = (ListView) view.findViewById (R.id.listEmpresas);

			//Creamos un array adapter para desplegar cada una de las filas
			//SimpleCursorAdapter notes = new SimpleCursorAdapter(context, R.layout.activity_fila_cliente, mCursor, from, to);
			ListaClientesCursorAdapter notes = new ListaClientesCursorAdapter(context, R.layout.activity_fila_cliente, mCursor, from, to, 0, "empresa");
			lvEmpresas.setAdapter(notes);
		}
	}
	
	private void listarEmpleados(View view){
		//Cargamos la lista de empleados
		EmpleadoSqliteDao empleadoDao = new EmpleadoSqliteDao();

		Context context = getActivity();
		mCursor = empleadoDao.listarEmpleados(getActivity());

		if(mCursor.getCount()>0){

			//indicamos los campos que queremos mostrar (from) y en donde (to)
			String[] from = new String[] { Empleado.NOMBRE, Empleado.EMPRESA};
			int[] to = new int[] { R.id.textViewNombreIzq,  R.id.textViewNombreCent };


			ListView lvEmpleados = (ListView) view.findViewById (android.R.id.list);
			
			//Creamos un array adapter para desplegar cada una de las filas
			//SimpleCursorAdapter notes = new SimpleCursorAdapter(context, R.layout.activity_fila_cliente, mCursor, from, to);
			ListaClientesCursorAdapter notes = new ListaClientesCursorAdapter(context, R.layout.activity_fila_cliente, mCursor, from, to, 0, "empleado");
			setListAdapter(notes);
			lvEmpleados.setAdapter(notes);
		}
	}

}
