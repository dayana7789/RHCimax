package com.nahmens.rhcimax.controlador;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
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

	/*
	 * Necesitamos que los siguientes adaptadores sean estaticos para
	 * poder llamarlos directamente dentro su clase: ListaClientesCursorAdapter
	 */
	public static ListaClientesCursorAdapter listCursorAdapterEmpleados;
	public static ListaClientesCursorAdapter listCursorAdapterEmpresas;
	
	//Creamos un DataSetObserver para saber cuando los listView de empleados
	//y empresas han sido modificados
	//y lo registramos al adaptor con la funcion registerDataSetObserver().
	private DataSetObserver observer = new DataSetObserver() {
		public void onChanged(){
			//cambiarColorCuadroNotificacion(null);
		}
	};

	/**
	 * INICIO DE CODIGO PARA PERMITIR QUE UNA FILA PUEDA SER SELECCIONADA 
	 * NOTA: Es importante que en el layout de la fila (activity_fila_cliente.xml)
	 * en la layout raiz tenga: android:descendantFocusability="blocksDescendants"
	 * De lo contrario, el onListItemClick no va a funcionar!.
	 * 
	 * NOTA2: En AplicacionActivity, se implementa la interfaz OnEmpleadoSelectedListener
	 * con su metodo onEmpleadoSelected.
	 */

	OnClienteSelectedListener mClienteListener;

	/**
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
	/**
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

		//primero listamos a los empleados donde vamos a inicializar el valor de listCursorAdapterEmpleados
		listarEmpleados(view);

		//por ultimo listamos a las empresas, la cual utiliza la referencia de listCursorAdapterEmpleados dentro
		//del adaptador. OJO con esto.
		listarEmpresas(view);

		//Bundle mArgumentos = this.getArguments();

		//Reviso si me pasaron argumentos: notificacion de cambio de color en cuadro de notificacion
		//if(mArgumentos!= null){
			//String color = mArgumentos.getString(AplicacionActivity.tagCuadroColor);
			//cambiarColorCuadroNotificacion(view);
		//}

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

		//Registro del evento addTextChangedListener cuando utilizamos el buscador
		EditText etBuscar = (EditText) view.findViewById(R.id.editTextBuscar);
		etBuscar.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
				// When user changed the Text

				if(listCursorAdapterEmpleados!=null){
					listCursorAdapterEmpleados.getFilter().filter(cs);   
				}
				
				if(listCursorAdapterEmpresas!=null){
					listCursorAdapterEmpresas.getFilter().filter(cs); 
				}
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub                          
			}
		});


		return view;
	}


	/**
	 * Funcion encargada de modificar los colores de los cuadros de notificacion principal.
	 * @param color Color a ser modificado
	 */
	private void cambiarColorCuadroNotificacion(View v) {
		
		if(v==null){
			v = getView();
		}
		TextView tvAmarillo = (TextView) v.findViewById(R.id.avisoAmarillo);
		TextView tvVerde = (TextView) v.findViewById(R.id.avisoVerde);
		TextView tvRojo = (TextView) v.findViewById(R.id.avisoRojo);

		EmpresaSqliteDao empresasDao = new EmpresaSqliteDao();
		Cursor cursorlistEmp = empresasDao.listarEmpresas(getActivity());
		ArrayList<Boolean> arr = new ArrayList<Boolean>();

		//Log.e("empresas: ", " id: "+cursorlistEmp.getString(cursorlistEmp.getColumnIndex(Empresa.ID)) + " fechSync: ," + cursorlistEmp.getString(cursorlistEmp.getColumnIndex(Empresa.FECHA_SINCRONIZACION))+",");
		if (cursorlistEmp != null) {
			cursorlistEmp.moveToFirst();
		}
		
		while(!cursorlistEmp.isAfterLast()){

			if(cursorlistEmp.getString(cursorlistEmp.getColumnIndex(Empresa.FECHA_SINCRONIZACION)) == null){
				arr.add(false);
			}else if(cursorlistEmp.getString(cursorlistEmp.getColumnIndex(Empresa.FECHA_SINCRONIZACION)).equals("")){
				arr.add(null);
			}else{
				arr.add(true);
			}

			cursorlistEmp.moveToNext();
		}

		if(arr.contains(true) && arr.contains(false)){
			tvAmarillo.setBackgroundResource(R.drawable.borde_amarillo);
			tvRojo.setBackgroundResource(R.drawable.borde_blanco);
			tvVerde.setBackgroundResource(R.drawable.borde_blanco);

		}else if(arr.contains(true)){
			tvAmarillo.setBackgroundResource(R.drawable.borde_blanco);
			tvRojo.setBackgroundResource(R.drawable.borde_blanco);
			tvVerde.setBackgroundResource(R.drawable.borde_verde);

		}else if(arr.contains(false)){
			tvAmarillo.setBackgroundResource(R.drawable.borde_blanco);
			tvRojo.setBackgroundResource(R.drawable.borde_rojo);
			tvVerde.setBackgroundResource(R.drawable.borde_blanco);
		}else if(arr.contains(null)){
			tvAmarillo.setBackgroundResource(R.drawable.borde_amarillo);
			tvRojo.setBackgroundResource(R.drawable.borde_blanco);
			tvVerde.setBackgroundResource(R.drawable.borde_blanco);
		}/*else{
			//caso empresa sin empleados
			if(strFechaSincronizacion==null){
				tvAmarillo.setBackgroundResource(R.drawable.borde_blanco);
				tvRojo.setBackgroundResource(R.drawable.borde_rojo);
				tvVerde.setBackgroundResource(R.drawable.borde_blanco);
			}else{
				tvAmarillo.setBackgroundResource(R.drawable.borde_blanco);
				tvRojo.setBackgroundResource(R.drawable.borde_blanco);
				tvVerde.setBackgroundResource(R.drawable.borde_verde);
			}
			
		}*/

		/*if(color.equals(AplicacionActivity.tagRojo)){
			tvRojo.setBackgroundResource(R.drawable.borde_rojo);
			tvVerde.setBackgroundResource(R.drawable.borde_blanco);
			tvAmarillo.setBackgroundResource(R.drawable.borde_blanco);

		}else if(color.equals(AplicacionActivity.tagVerde)){
			tvRojo.setBackgroundResource(R.drawable.borde_blanco);
			tvVerde.setBackgroundResource(R.drawable.borde_verde);
			tvAmarillo.setBackgroundResource(R.drawable.borde_blanco);

		}else if(color.equals(AplicacionActivity.tagAmarillo)){
			tvRojo.setBackgroundResource(R.drawable.borde_blanco);
			tvVerde.setBackgroundResource(R.drawable.borde_blanco);
			tvAmarillo.setBackgroundResource(R.drawable.borde_amarillo);

		}else{
			Log.e("ClientesActivity: linea 138","Color invalido: " + color);
		}*/
	}

	/**
	 * Funcion que muestra lista de empresas y crea adaptador para iterar sobre la misma.
	 * @param view
	 */
	private void listarEmpresas(View view){

		//Cargamos la lista de empresas
		EmpresaSqliteDao empresaDao = new EmpresaSqliteDao();
		Context contexto = getActivity();
		Cursor mCursorEmpresas = empresaDao.listarEmpresas(getActivity());

		if(mCursorEmpresas.getCount()>0){
			//indicamos los campos que queremos mostrar (from) y en donde (to)
			//OJO: Aqui pasamos Empresa.ID para no invocarlo directamente en el ListaClientesCursorAdapter
			// y lo relacionamos en el arreglo 'to' con el valor 0.
			String[] from = new String[] { Empresa.ID, Empresa.NOMBRE, Empresa.TELEFONO};
			int[] to = new int[] { 0, R.id.textViewNombreIzq,  R.id.textViewNombreCent };
			final ListView lvEmpresas = (ListView) view.findViewById (R.id.listEmpresas);

			//Creamos un array adapter para desplegar cada una de las filas
			listCursorAdapterEmpresas = new ListaClientesCursorAdapter(contexto, R.layout.activity_fila_cliente, mCursorEmpresas, from, to, 0, "empresa");
			lvEmpresas.setAdapter(listCursorAdapterEmpresas);
			
			
			//registramos el DataSetObserver al adaptador
			listCursorAdapterEmpresas.registerDataSetObserver(observer);

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

	/**
	 * Funcion que muestra lista de empleados y crea adaptador para iterar sobre la misma.
	 * @param view
	 *
	 */
	private void listarEmpleados(View view){
		//Cargamos la lista de empleados
		EmpleadoSqliteDao empleadoDao = new EmpleadoSqliteDao();
		Context context = getActivity();
		Cursor mCursorEmpleados = empleadoDao.listarEmpleados(getActivity());

		if(mCursorEmpleados.getCount()>0){
			//indicamos los campos que queremos mostrar (from) y en donde (to)
			//OJO: Aqui pasamos  Empleado.ID para no invocarlo directamente en el ListaClientesCursorAdapter
			// y lo relacionamos en el arreglo 'to' con el valor 0.
			String[] from = new String[] { Empleado.ID, Empleado.NOMBRE, Empleado.APELLIDO, Empleado.EMPRESA};
			int[] to = new int[] { 0, R.id.textViewNombreIzq,  R.id.textViewApellidoIzq, R.id.textViewNombreCent };
			ListView lvEmpleados = (ListView) view.findViewById (android.R.id.list);

			//Creamos un array adapter para desplegar cada una de las filas
			listCursorAdapterEmpleados = new ListaClientesCursorAdapter(context, R.layout.activity_fila_cliente, mCursorEmpleados, from, to, 0, "empleado");
			lvEmpleados.setAdapter(listCursorAdapterEmpleados);

			
			//registramos el DataSetObserver al adaptador
			listCursorAdapterEmpleados.registerDataSetObserver(observer);
			
			//OJO: como en el layout la lista que contiene a las empleados es android:id="@id/android:list", 
			// no hay necesidad de hacer el setOnItemClickListener como se hizo en listarEmpresas. Esto
			//android lo hace automaticamente.

			//enables filtering for the contents of the given ListView
			//lvEmpleados.setTextFilterEnabled(true);
		}
	}
}
