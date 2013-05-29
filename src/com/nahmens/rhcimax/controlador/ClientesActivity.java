package com.nahmens.rhcimax.controlador;

import android.R.drawable;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.nahmens.rhcimax.R;
import com.nahmens.rhcimax.adapters.ListaClientesCursorAdapter;
import com.nahmens.rhcimax.database.modelo.Empleado;
import com.nahmens.rhcimax.database.modelo.Empresa;
import com.nahmens.rhcimax.database.sqliteDAO.EmpleadoSqliteDao;
import com.nahmens.rhcimax.database.sqliteDAO.EmpresaSqliteDao;
import android.widget.AdapterView.OnItemClickListener;

public class ClientesActivity extends ListFragment {
	private Cursor mCursorEmpleados;
	private Cursor mCursorEmpresas;

	/*INICIO DE CODIGO PARA PERMITIR QUE UNA FILA PUEDA SER SELECCIONADA 
	 * NOTA: Es importante que en el layout de la fila (activity_fila_cliente.xml)
	 * en la layout raiz tenga: android:descendantFocusability="blocksDescendants"
	 * De lo contrario, el onListItemClick no va a funcionar!.
	 * 
	 * NOTA2: En AplicacionActivity, se implementa la interfaz OnEmpleadoSelectedListener
	 * con su metodo onEmpleadoSelected.
	 */

	OnClienteSelectedListener mClienteListener;

	/*
	 * interface utilizada para comunicar la lista con el detalle
	 * de cada fila.
	 */
	public interface OnClienteSelectedListener {
		public void onEmpleadoSelected(String id);
		public void onEmpresaSelected(String id);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mClienteListener = (OnClienteSelectedListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString() + " se debe implementar OnClientSelectedListener ");
		}
	}

	@Override
	/*
	 * @param position numero de posicion en la lista
	 * @param id Id del cliente sobre el cual se hizo click. Puede pertenecer a un empleado o a una empresa.
	 *           Este Id se asigna automaticamente por la definicion de _id en la BD.
	 */
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);

		String idString = String.valueOf(id); 
		
		if (l.getId() == android.R.id.list) {
			mClienteListener.onEmpleadoSelected(idString);
		} else if (l.getId() == R.id.listEmpresas) {
			mClienteListener.onEmpresaSelected(idString);
		}

	}

	/*********** FIN ******************/

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.activity_clientes, container, false);

		listarEmpleados(view);
		listarEmpresas(view);
		
		Bundle mArgumentos = this.getArguments();
		
		//Reviso si me pasaron argumentos: notificacion de cambio de color en cuadro de notificacion
		if(mArgumentos!= null){
			String color = mArgumentos.getString(AplicacionActivity.tagCuadroColor);
			cambiarColorCuadroNotificacion(view, inflater, color);
		}

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


	/*
	 * Funcion encargada de modificar los colores de los cuadros de notificacion.
	 * @param view  Vista del fragmento
	 * @param color Color a ser modificado
	 */
	private void cambiarColorCuadroNotificacion(View view, LayoutInflater inflater, String color) {
		View viewFila = getLayoutInflater(getArguments()).inflate(R.layout.activity_fila_cliente, null, false);
		
		TextView tvAmarilloFila = (TextView) viewFila.findViewById(R.id.avisoAmarilloFila);
		TextView tvVerdeFila = (TextView) viewFila.findViewById(R.id.avisoVerdeFila);
		TextView tvRojoFila = (TextView) viewFila.findViewById(R.id.avisoRojoFila);
		
		TextView tvAmarillo = (TextView) view.findViewById(R.id.avisoAmarillo);
		
		
		if(color.equals(AplicacionActivity.tagRojo)){
			Log.e("entre","entre "+ tvRojoFila + " " + tvRojoFila.getTag());
			tvRojoFila.setBackgroundResource(R.drawable.borde_rojo);
			tvAmarillo.setBackgroundResource(R.drawable.borde_amarillo);
			
			tvVerdeFila.setBackgroundResource(R.drawable.borde_blanco);
			
			
			tvAmarilloFila.setBackgroundResource(R.drawable.borde_blanco);
			
		}else if(color.equals(AplicacionActivity.tagVerde)){
			
		}else if(color.equals(AplicacionActivity.tagAmarillo)){
			
		}else{
			Log.e("ClientesActivity: linea 138","Color invalido: " + color);
		}
		
	}

	private void listarEmpresas(View view){
		//Cargamos la lista de empresas
		EmpresaSqliteDao empresaDao = new EmpresaSqliteDao();
		Context contexto = getActivity();
		mCursorEmpresas = empresaDao.listarEmpresas(getActivity());

		if(mCursorEmpresas.getCount()>0){
			//indicamos los campos que queremos mostrar (from) y en donde (to)
			//OJO: Aqui pasamos Empresa.ID para no invocarlo directamente en el ListaClientesCursorAdapter
			// y lo relacionamos en el arreglo 'to' con el valor 0.
			String[] from = new String[] { Empresa.ID, Empresa.NOMBRE, Empresa.TELEFONO};
			int[] to = new int[] { 0, R.id.textViewNombreIzq,  R.id.textViewNombreCent };
			final ListView lvEmpresas = (ListView) view.findViewById (R.id.listEmpresas);

			//Creamos un array adapter para desplegar cada una de las filas
			//SimpleCursorAdapter notes = new SimpleCursorAdapter(context, R.layout.activity_fila_cliente, mCursor, from, to);
			ListaClientesCursorAdapter notes = new ListaClientesCursorAdapter(contexto, R.layout.activity_fila_cliente, mCursorEmpresas, from, to, 0, "empresa");
			lvEmpresas.setAdapter(notes);
			
			//OJO: como en el layout la lista que contiene a las empresas es android:id="@+id/listEmpresas" 
			// y  no android:id="@id/android:list", se debe hacer el setOnItemClickListener para que se llame
			// el onListItemClick sobreescrito arriba.
		    //OJO: ListView es subclase de AdapterView
			lvEmpresas.setOnItemClickListener(new OnItemClickListener() {
				@Override
			    public void onItemClick(AdapterView<?> adapter, View view, int position, long id)   {
					onListItemClick(lvEmpresas,view,position,id);
			    }
			});

		}
	}

	private void listarEmpleados(View view){
		//Cargamos la lista de empleados
		EmpleadoSqliteDao empleadoDao = new EmpleadoSqliteDao();
		Context context = getActivity();
		mCursorEmpleados = empleadoDao.listarEmpleados(getActivity());

		if(mCursorEmpleados.getCount()>0){
			//indicamos los campos que queremos mostrar (from) y en donde (to)
			//OJO: Aqui pasamos  Empleado.ID para no invocarlo directamente en el ListaClientesCursorAdapter
			// y lo relacionamos en el arreglo 'to' con el valor 0.
			String[] from = new String[] { Empleado.ID, Empleado.NOMBRE, Empleado.APELLIDO, Empleado.EMPRESA};
			int[] to = new int[] { 0, R.id.textViewNombreIzq,  R.id.textViewApellidoIzq, R.id.textViewNombreCent };
			ListView lvEmpleados = (ListView) view.findViewById (android.R.id.list);

			//Creamos un array adapter para desplegar cada una de las filas
			//SimpleCursorAdapter notes = new SimpleCursorAdapter(context, R.layout.activity_fila_cliente, mCursor, from, to);
			ListaClientesCursorAdapter notes = new ListaClientesCursorAdapter(context, R.layout.activity_fila_cliente, mCursorEmpleados, from, to, 0, "empleado");
			lvEmpleados.setAdapter(notes);
			
			//OJO: como en el layout la lista que contiene a las empleados es android:id="@id/android:list", 
			// no hay necesidad de hacer el setOnItemClickListener como se hizo en listarEmpresas. Esto
			//android lo hace automaticamente.

			//enables filtering for the contents of the given ListView
			//lvEmpleados.setTextFilterEnabled(true);


		}
	}

}
