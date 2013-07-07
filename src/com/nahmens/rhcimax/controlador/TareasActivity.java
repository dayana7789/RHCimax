package com.nahmens.rhcimax.controlador;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
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
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;

import com.nahmens.rhcimax.R;
import com.nahmens.rhcimax.adapters.ListaTareasCursorAdapter;
import com.nahmens.rhcimax.database.modelo.Tarea;
import com.nahmens.rhcimax.database.sqliteDAO.TareaSqliteDao;
import com.nahmens.rhcimax.mensaje.Mensaje;

public class TareasActivity extends ListFragment{

	ListaTareasCursorAdapter listCursorAdapterTareas;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.activity_tareas, container, false);

		if (savedInstanceState==null){
			//Nos aseguramos que no importa desde donde nos llamen, el indicador del 
			//tab es el correspondiente.
			AplicacionActivity.mTabsWidget.setCurrentTab(AplicacionActivity.posicionTagFragmentTareas);	

			//Asociamos los valores al combo box o spinner
			inicializarSpinner(view);

			TareaSqliteDao tareaDao = new TareaSqliteDao();
			Cursor mCursorTareas = null;

			Bundle mArgumentos = this.getArguments();

			//Si me pasaron argumentos, filtro la lista. 
			//De lo contrario, listo todo.
			if(mArgumentos!= null){

				String idEmpresa = mArgumentos.getString("idEmpresa");
				String idEmpleado = mArgumentos.getString("idEmpleado");


				if(idEmpresa!=null){
					mCursorTareas = tareaDao.listarTareasPorEmpresa(getActivity(), idEmpresa);

				}else if(idEmpleado !=null){
					mCursorTareas = tareaDao.listarTareasPorEmpleado(getActivity(), idEmpleado);
				}

			}else{
				mCursorTareas = tareaDao.buscarTareaFilter(getActivity(),null);
			}

			listarTareas(view, mCursorTareas);

			// Registro del evento OnClick del buttonTarea
			ImageButton bTarea = (ImageButton)view.findViewById(R.id.ImageButtonAgregarTarea);
			bTarea.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					DatosTareaActivity fragmentDatosTarea = new DatosTareaActivity();
					final FragmentTransaction ft = getFragmentManager().beginTransaction();
					//Cambiamos el layout de clientes por datos_empresa e indicamos el tag del frame.
					ft.replace(android.R.id.tabcontent,fragmentDatosTarea, AplicacionActivity.tagFragmentDatosTareas); 
					//preservamos el estado anterior al hacer click en back button
					ft.addToBackStack(null);
					ft.commit(); 
				}
			});

		}
		return view;
	}


	private void listarTareas(View view, Cursor mCursorTareas) {

		if(mCursorTareas.getCount()>0){
			//indicamos los campos que queremos mostrar (from) y en donde (to)
			String[] from = new String[] { Tarea.NOMBRE, "loginUsuario", Tarea.FECHA, Tarea.HORA, Tarea.NOMBRE_EMPLEADO, Tarea.APELLIDO_EMPLEADO, Tarea.NOMBRE_EMPRESA, Tarea.FECHA_FINALIZACION};
			int[] to = new int[] {R.id.textViewTitulo, R.id.textViewUsuario, R.id.textViewFil1Col2, R.id.textViewFil1Col2, R.id.textViewFil2Col1, R.id.textViewFil2Col1, R.id.textViewFil1Col1, R.id.textViewFil2Col2 };
			ListView lvTareas = (ListView) view.findViewById (android.R.id.list);


			//Creamos un array adapter para desplegar cada una de las filas
			listCursorAdapterTareas = new ListaTareasCursorAdapter(getActivity(), R.layout.activity_fila_tarea, mCursorTareas, from, to, 0, getFragmentManager());
			lvTareas.setAdapter(listCursorAdapterTareas);

			lvTareas.setOnItemLongClickListener(new OnItemLongClickListener() {

				public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
						int position, long id) {
					mostrarListaOpciones(position);

					return true;
				}
			}); 


			//Registro del evento addTextChangedListener cuando utilizamos el buscador
			EditText etBuscar = (EditText) view.findViewById(R.id.editTextBuscar);
			etBuscar.addTextChangedListener(new TextWatcher() {

				@Override
				public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
					Log.e("filter", " " +  cs);
					if(listCursorAdapterTareas!=null){
						listCursorAdapterTareas.getFilter().filter(cs);   
					}
				}

				@Override
				public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
						int arg3) {}

				@Override
				public void afterTextChanged(Editable arg0) {}
			});



		}
	}

	/**
	 * Funcion que muestra opciones al dejar una fila preionada
	 * @param position Numero de positicion que ocupa la fila que se longClikeo
	 */
	protected void mostrarListaOpciones(int position) {
		Cursor cursor = (Cursor) getListView().getItemAtPosition(position);
		final int idTarea = cursor.getInt(cursor.getColumnIndex(Tarea.ID));
		final String nombreTarea = cursor.getString(cursor.getColumnIndex(Tarea.NOMBRE));
		Log.v("long clicked","pos"+" "+idTarea);

		String[] arr = {"Actualizar","Eliminar"};

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(""+nombreTarea)
		.setItems(arr, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case 0:

					break;

				case 1:
					mensajeAlertaEliminar(nombreTarea, idTarea);
					break;
				}
			}
		});
		AlertDialog alert = builder.create();
		alert.show();
	}


	@Override
	/**
	 * Funcion que se llama cuando una fila es seleccionada.
	 */
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);

		//OJO: aqui estamos usando  getListView() porq tenemos un solo ListView
		//pero de tener mas de uno, no debemos usar esto (Ver ClientesActivity.java)
		Cursor cursor = (Cursor) getListView().getItemAtPosition(position);

		int idTarea = cursor.getInt(cursor.getColumnIndex(Tarea.ID));

		Bundle arguments = new Bundle();
		arguments.putString("idTarea", ""+idTarea);

		DatosTareaActivity fragment = new DatosTareaActivity();

		//pasamos al fragment el id de la tarea
		fragment.setArguments(arguments); 

		getFragmentManager().beginTransaction()
		.replace(android.R.id.tabcontent,fragment, AplicacionActivity.tagFragmentDatosTareas)
		.addToBackStack(null)
		.commit();

	}

	/**
	 * Funcion que inicializa los valores del spinner o combo box
	 * @param view
	 */
	private void inicializarSpinner(View view) {
		Spinner spinner = (Spinner) view.findViewById(R.id.spinnerTareas);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getActivity(),
				R.array.historicos_filtro_array, android.R.layout.simple_spinner_item);

		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);

		//add the listener:
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long arg3) {

				String valor = (String)parent.getItemAtPosition(position);
				Log.e("valor", " " +valor);

				if(listCursorAdapterTareas!=null){
					listCursorAdapterTareas.getFilter().filter(valor);   
				}
			}

			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});
	}


	/**
	 * Funcion que muestra mensaje de alerta ante el intento de eliminacion
	 * @param v
	 * @param nombre Nombre de la tarea
	 * @param idTarea Id de la tarea
	 */
	public void mensajeAlertaEliminar(String nombre, final int idTarea){

		AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
		String[] mensArray = null;
		Mensaje mensajeDialog = null;
		mensajeDialog = new Mensaje("eliminar_tarea");


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

				borrarTarea(idTarea);
			}
		});

		AlertDialog alertDialog = alert.create();
		alertDialog.show();

	}

	/** 
	 * Funcion que elimina de la BD y del list view tareas.
	 * @param id Id de la tarea
	 *
	 */
	private void borrarTarea(int id) {
		final LayoutInflater inflater = LayoutInflater.from(getActivity());
		Boolean eliminado =  false;
		Mensaje mToast = null;
		String mensajeError = null;
		String mensajeOk = null;

		TareaSqliteDao tareaDao = new TareaSqliteDao();
		eliminado = tareaDao.eliminarTarea(getActivity(), ""+id);
		mensajeOk = "ok_eliminado_tarea";
		mensajeError = "error_eliminado_empresa";

		if(eliminado){
			mToast = new Mensaje(inflater, (AplicacionActivity)getActivity(), mensajeOk);

			//Actualizamos los valores del cursor de la lista de tareas
			listCursorAdapterTareas.changeCursor(tareaDao.buscarTareaFilter(getActivity(),null));

			//Notificamos que la lista cambio
			listCursorAdapterTareas.notifyDataSetChanged();

		}else{
			mToast = new Mensaje(inflater, (AplicacionActivity)getActivity(), mensajeError);
		}

		try {
			mToast.controlMensajesToast();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
