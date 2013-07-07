package com.nahmens.rhcimax.controlador;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.nahmens.rhcimax.mensaje.Mensaje;

import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

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


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.activity_clientes, container, false);
		
		//Nos aseguramos que no importa desde donde nos llamen, el indicador del 
		//tab es el correspondiente.
		AplicacionActivity.mTabsWidget.setCurrentTab(AplicacionActivity.posicionTagFragmentClientes);		
		
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

				if(listCursorAdapterEmpleados!=null){
					listCursorAdapterEmpleados.getFilter().filter(cs);   
				}

				if(listCursorAdapterEmpresas!=null){
					listCursorAdapterEmpresas.getFilter().filter(cs); 
				}
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {}

			@Override
			public void afterTextChanged(Editable arg0) {}
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
			tvRojo.setBackgroundResource(R.drawable.borde_blanco);
			tvVerde.setBackgroundResource(R.drawable.borde_blanco);

		}else if(arr.contains(true)){
			tvRojo.setBackgroundResource(R.drawable.borde_blanco);
			tvVerde.setBackgroundResource(R.drawable.borde_verde);

		}else if(arr.contains(false)){
			tvRojo.setBackgroundResource(R.drawable.borde_rojo);
			tvVerde.setBackgroundResource(R.drawable.borde_blanco);
		}else if(arr.contains(null)){
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

			lvEmpresas.setOnItemLongClickListener(new OnItemLongClickListener() {

				public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
						int position, long id) {

					ListView lv = (ListView) arg0;
					mostrarListaOpciones(lv,position);

					return true;
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
		Cursor mCursorEmpleados = empleadoDao.buscarEmpleadoFilter(getActivity(),null);

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

			lvEmpleados.setOnItemLongClickListener(new OnItemLongClickListener() {

				public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
						int position, long id) {

					ListView lv = (ListView) arg0;

					mostrarListaOpciones(lv, position);

					return true;
				}
			}); 

			//enables filtering for the contents of the given ListView
			//lvEmpleados.setTextFilterEnabled(true);
		}
	}

	/**
	 * Funcion que muestra opciones al dejar una fila preionada
	 * @param lv ListView que llamo al evento onLongClick
	 * @param position Numero de positicion que ocupa la fila que se longClikeo
	 */
	protected void mostrarListaOpciones(ListView l, int position) {

		Cursor cursor = (Cursor) l.getItemAtPosition(position);
		final int id = cursor.getInt(cursor.getColumnIndex("_id"));
		final String nombre = cursor.getString(cursor.getColumnIndex("nombre"));
		String tipoCliente = null;

		if (l.getId() == android.R.id.list) {
			tipoCliente = "empleado";
		} else if (l.getId() == R.id.listEmpresas) {
			tipoCliente = "empresa";
		}

		final Bundle mArgumentos = new Bundle();
		mArgumentos.putString("tipoCliente", tipoCliente);

		String[] arr = {"Actualizar","Eliminar"};

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(""+nombre)
		.setItems(arr, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case 0:
					break;

				case 1:
					mensajeAlertaEliminar(nombre, id, mArgumentos.getString("tipoCliente"));
					break;
				}
			}
		});
		AlertDialog alert = builder.create();
		alert.show();
	}

	/**
	 * Funcion que muestra mensaje de alerta ante el intento de eliminacion
	 * @param v
	 * @param nombre Nombre de la tarea
	 * @param idTarea Id de la tarea
	 */
	public void mensajeAlertaEliminar(String nombre, final int id, final String tipoCliente){

		AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
		String[] mensArray = null;
		Mensaje mensajeDialog = null;


		if(tipoCliente.equals("empresa")){
			mensajeDialog = new Mensaje("eliminar_empresa");

		}else if(tipoCliente.equals("empleado")){
			mensajeDialog = new Mensaje("eliminar_empleado");

		}else{
			Log.e("ListaClientesCursorAdapter","tipoCliente no soportado en funcion onClick de boton eliminar: " + tipoCliente);
		}

		try {
			mensArray = mensajeDialog.controlMensajesDialog(nombre);
		} catch (Exception e) {
			e.printStackTrace();
		}

		alert.setMessage(mensArray[0]); 
		alert.setTitle(mensArray[1]); 

		alert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				dialog.cancel();
			}});

		alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				borrarCliente(id+"", tipoCliente);
			}
		});

		AlertDialog alertDialog = alert.create();
		alertDialog.show();



	}

	/** Funcion que elimina de la BD y del list view empleados o empresas.
	 * 
	 * @param id Id del empleado o empresa
	 * @param tipoCliente Posibles valores: empresa o empleado
	 *
	 */
	private void borrarCliente(String id, String tipoCliente) {
		final LayoutInflater inflater = LayoutInflater.from(getActivity());
		Boolean eliminado =  false;
		Mensaje mToast = null;
		String mensajeError = null;
		String mensajeOk = null;

		if(tipoCliente.equals("empresa")){
			EmpresaSqliteDao empresaDao = new EmpresaSqliteDao();
			eliminado = empresaDao.eliminarEmpresa(getActivity(), id);
			mensajeOk = "ok_eliminado_empresa";
			mensajeError = "error_eliminado_empresa";
		}else if(tipoCliente.equals("empleado")){
			EmpleadoSqliteDao empleadoDao = new EmpleadoSqliteDao();
			eliminado = empleadoDao.eliminarEmpleado(getActivity(), id);
			mensajeOk = "ok_eliminado_empleado";
			mensajeError = "error_eliminado_empleado";
		}else{
			Log.e("ListaClientesCursorAdapter","tipoCliente no soportado en funcion borrarCliente: " + tipoCliente);
		}

		if(eliminado){
			mToast = new Mensaje(inflater, (AplicacionActivity)getActivity(), mensajeOk);

			if(tipoCliente.equals("empresa")){
				//Actualizamos los valores del cursor de la lista de empresas
				EmpresaSqliteDao empresaDao = new EmpresaSqliteDao();
				listCursorAdapterEmpresas.changeCursor(empresaDao.listarEmpresas(getActivity()));
				//Notificamos que la lista cambio
				listCursorAdapterEmpresas.notifyDataSetChanged();

				//Cuando eliminamos a una empresa, eliminamos tambien a sus empleados
				//Actualizamos los valores del cursor de la lista de empleados
				if(listCursorAdapterEmpleados!=null){
					EmpleadoSqliteDao empleadoDao = new EmpleadoSqliteDao();
					listCursorAdapterEmpleados.changeCursor(empleadoDao.buscarEmpleadoFilter(getActivity(),null));
					//Notificamos que la lista cambio
					listCursorAdapterEmpleados.notifyDataSetChanged();
				}

			}else if(tipoCliente.equals("empleado")){
				//Actualizamos los valores del cursor de la lista de empleados
				EmpleadoSqliteDao empleadoDao = new EmpleadoSqliteDao();
				listCursorAdapterEmpleados.changeCursor(empleadoDao.buscarEmpleadoFilter(getActivity(),null));
				//Notificamos que la lista cambio
				listCursorAdapterEmpleados.notifyDataSetChanged();
			}


		}else{
			mToast = new Mensaje(inflater, getActivity(), mensajeError);
		}

		try {
			mToast.controlMensajesToast();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Metodo que se llama cuando se selecciona a un empleado o empresa
	 * de la lista.
	 */
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);

		String idString = String.valueOf(id); 

		if (l.getId() == android.R.id.list) {
			onEmpleadoSelected(idString);
		} else if (l.getId() == R.id.listEmpresas) {
			onEmpresaSelected(idString);
		}

	}


	/**
	 * Metodo que se llama al seleccionar un empleado del listView
	 * @param idEmpleado
	 */
	public void onEmpleadoSelected(String idEmpleado) {
		Bundle arguments = new Bundle();
		arguments.putString("id", idEmpleado);

		DatosClienteActivity fragment = new DatosClienteActivity();

		//pasamos al fragment el id de la tarea
		fragment.setArguments(arguments); 

		getFragmentManager().beginTransaction()
		.replace(android.R.id.tabcontent,fragment, AplicacionActivity.tagFragmentDatosCliente)
		.addToBackStack(null)
		.commit();
	}

	/**
	 * Metodo que se llama al seleccionar una empresa del ListView
	 * @param idEmpresa
	 */
	public void onEmpresaSelected(String idEmpresa) {

		Bundle arguments = new Bundle();
		arguments.putString("idEmpresa", idEmpresa);

		DatosEmpresaActivity fragment = new DatosEmpresaActivity();

		//pasamos al fragment el id de la tarea
		fragment.setArguments(arguments); 

		getFragmentManager().beginTransaction()
		.replace(android.R.id.tabcontent,fragment, AplicacionActivity.tagFragmentDatosEmpresa)
		.addToBackStack(null)
		.commit();
	}
}
