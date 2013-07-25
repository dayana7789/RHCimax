package com.nahmens.rhcimax.controlador;


import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
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
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.nahmens.rhcimax.R;
import com.nahmens.rhcimax.adapters.ListaTareasCursorAdapter;
import com.nahmens.rhcimax.database.DataBaseHelper;
import com.nahmens.rhcimax.database.modelo.Permiso;
import com.nahmens.rhcimax.database.modelo.Tarea;
import com.nahmens.rhcimax.database.sqliteDAO.TareaSqliteDao;
import com.nahmens.rhcimax.mensaje.Mensaje;
import com.nahmens.rhcimax.utils.SesionUsuario;
import com.nahmens.rhcimax.utils.SincronizacionAsyncTask;

public class TareasActivity extends ListFragment{

	public static ListaTareasCursorAdapter listCursorAdapterTareas;
	@SuppressLint("UseSparseArrays")
	HashMap<String,Boolean> arrSincronizados = new HashMap<String, Boolean>(); //Contiene idTarea y si esta sincronizado o no
	private ArrayList<String> permisos;

	//Las siguientes variables son utilizadas para evitar llamadas a
	//la BD innecesarias porq los listeners se disparan aunque el 
	//usuario no lo haya solicitado.

	//variable que se utiliza para evitar llamar al listener
	//del spinner cuando el usuario no ha seleccionado 
	//explicitamente el spinner. Su valor se inicializa en el 
	//metodo onResume.
	private boolean mSpinnerBool;

	//Se utiliza para evitar llamar al metodo onChanged del
	//DataSetObserver dos veces. Su valor se inicializa en el 
	//metodo onResume.
	private boolean mObserverBool;

	//Se utiliza para evitar llamar al metodo OnTextChanged del
	//dos veces. Su valor se inicializa en el 
	//metodo onResume.
	private boolean mOnTextChangedBool;

	//Creamos un DataSetObserver para saber cuando el listView de tarea
	//ha sido modificado y lo registramos al adaptor con la funcion 
	//registerDataSetObserver().
	private DataSetObserver observer = new DataSetObserver() {
		public void onChanged(){

			if(mObserverBool){
				setArrSincronizados(listCursorAdapterTareas.getCursor());
				cambiarColorCuadroNotificacion(getView());
				mObserverBool = false;

			}else{
				mObserverBool = true;
			}
		}
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.activity_tareas, container, false);
		permisos = SesionUsuario.getPermisos(getActivity());

		if (savedInstanceState==null){
			//Nos aseguramos que no importa desde donde nos llamen, el indicador del 
			//tab es el correspondiente.
			AplicacionActivity.mTabsWidget.setCurrentTab(AplicacionActivity.posicionTagFragmentTareas);	

			//Asociamos los valores al combo box o spinner
			inicializarSpinner(view);

			TareaSqliteDao tareaDao = new TareaSqliteDao();

			Cursor mCursorTareas = null;

			if(permisos.contains(Permiso.LISTAR_TODO)){
				mCursorTareas = tareaDao.buscarTareaFilter(getActivity(),null, false);
			}else if(permisos.contains(Permiso.LISTAR_PROPIOS)){
				mCursorTareas = tareaDao.buscarTareaFilter(getActivity(),null, true);
			}else{
				mCursorTareas = tareaDao.buscarTareaFilter(getActivity(),null, false);
			}

			listarTareas(view, mCursorTareas);


			//Registro del evento addTextChangedListener cuando utilizamos el buscador
			EditText etBuscar = (EditText) view.findViewById(R.id.editTextBuscar);
			etBuscar.addTextChangedListener(new TextWatcher() {

				@Override
				public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
					//Este if es para que este metodo no se llame automaticamente
					//al iniciar la actividad
					if(mOnTextChangedBool){
						if(listCursorAdapterTareas!=null){
							listCursorAdapterTareas.getFilter().filter(cs);   
						}
					}else{
						mOnTextChangedBool=true;
					}

				}

				@Override
				public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
						int arg3) {}

				@Override
				public void afterTextChanged(Editable arg0) {}
			});


			Bundle mArgumentos = this.getArguments();

			//Si me pasaron argumentos, filtro la lista. 
			//De lo contrario, listo todo.
			if(mArgumentos!= null){

				String nombreEmpresa = mArgumentos.getString("nombreEmpresa");
				String nombreEmpleado = mArgumentos.getString("nombreEmpleado");

				if(nombreEmpresa!=null){
					//OJO: es importante crear el listener antes de hacer el setText
					//de lo contrario no se llama al metodo onTextChanged automaticamnt

					//esta linea es para que se llame onTextChanged dos veces. 
					//recordar q el filtro empieza cuando al menos dos letras son ingresadas
					//en el edit text del buscador.
					etBuscar.setText(""); 
					etBuscar.setText(nombreEmpresa);

				}else if(nombreEmpleado !=null){
					etBuscar.setText(""); 
					etBuscar.setText(nombreEmpleado);

				}

			}

			setArrSincronizados(mCursorTareas);
			cambiarColorCuadroNotificacion(view);
			

			// Registro del evento OnClick del buttonActualizar
			Button bAct = (Button)view.findViewById(R.id.buttonActualizar);
			bAct.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					new SincronizacionAsyncTask(getActivity()).execute(DataBaseHelper.TABLA_ROL, 
																	   DataBaseHelper.TABLA_USUARIO,
																	   DataBaseHelper.TABLA_PERMISO,
																	   DataBaseHelper.TABLA_PERMISO,
											                           DataBaseHelper.TABLA_ROL_PERMISO, 
											                           DataBaseHelper.TABLA_EMPRESA,
											                           DataBaseHelper.TABLA_EMPLEADO,
											                           DataBaseHelper.TABLA_COTIZACION,
											                           DataBaseHelper.TABLA_EMPLEADO_COTIZACION,
											                           DataBaseHelper.TABLA_SERVICIO,
											                           DataBaseHelper.TABLA_COTIZACION_SERVICIO,
											                           DataBaseHelper.TABLA_TAREA,
											                           DataBaseHelper.TABLA_CHECKIN,
											                           DataBaseHelper.TABLA_HISTORICO);

				}
			});

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
					ft.addToBackStack(AplicacionActivity.tagFragmentDatosTareas);
					ft.commit(); 
				}
			});

		}
		return view;
	}


	/**
	 * Funcion que inicializa el arreglo de sincronizados
	 * a ser utilizado para determinar el color de los
	 * cuadro de notificacion principal 
	 * @param mCursorTareas
	 */
	public void setArrSincronizados(Cursor mCursorTareas) {

		int strSincronizado = 0;
		String id = null;

		if (mCursorTareas != null) {
			mCursorTareas.moveToFirst();
		}

		while(!mCursorTareas.isAfterLast()){
			id =  mCursorTareas.getString(mCursorTareas.getColumnIndex(Tarea.ID));

			strSincronizado = mCursorTareas.getInt(mCursorTareas.getColumnIndex(Tarea.SINCRONIZADO));

			if(strSincronizado==0){
				arrSincronizados.put(id,false);
			}else{
				arrSincronizados.put(id, true);
			}

			mCursorTareas.moveToNext();
		}

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

			//registramos el DataSetObserver al adaptador
			listCursorAdapterTareas.registerDataSetObserver(observer);

			lvTareas.setOnItemLongClickListener(new OnItemLongClickListener() {

				public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
						int position, long id) {
					mostrarListaOpciones(position);

					return true;
				}
			}); 

		}
	}

	/**
	 * Funcion que muestra opciones al dejar una fila preionada
	 * @param position Numero de positicion que ocupa la fila que se longClikeo
	 */
	protected void mostrarListaOpciones(int position) {
		Cursor cursor = (Cursor) getListView().getItemAtPosition(position);
		final String idTarea = cursor.getString(cursor.getColumnIndex(Tarea.ID));
		final String nombreTarea = cursor.getString(cursor.getColumnIndex(Tarea.NOMBRE));

		if(permisos.contains(Permiso.ELIMINAR_TODO)){
			mostrarOpcionActualizarEliminar(idTarea, nombreTarea);

		}else if(permisos.contains(Permiso.ELIMINAR_PROPIOS)){

			String idUsuarioCreador = cursor.getString(cursor.getColumnIndex("idUsuario"));
			String idUsuarioSesion = SesionUsuario.getIdUsuario(getActivity());

			if(idUsuarioCreador.equals(idUsuarioSesion)){
				mostrarOpcionActualizarEliminar(idTarea, nombreTarea);
			}else{
				mostrarOpcionActualizar(idTarea, nombreTarea);
			}

		}else{
			mostrarOpcionActualizar(idTarea, nombreTarea);
		}
	}


	private void mostrarOpcionActualizar(final String idTarea, final String nombreTarea){
		String[] arr = {"Actualizar"};

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(nombreTarea)
		.setItems(arr, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case 0:
					sincronizarTarea(idTarea);
					break;
				}
			}
		});
		AlertDialog alert = builder.create();
		alert.show();
	}

	private void mostrarOpcionActualizarEliminar(final String idTarea, final String nombreTarea){
		String[] arr = {"Actualizar","Eliminar"};

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(nombreTarea)
		.setItems(arr, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case 0:
					sincronizarTarea(idTarea);
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

		String idTarea = cursor.getString(cursor.getColumnIndex(Tarea.ID));

		Bundle arguments = new Bundle();
		arguments.putString("idTarea", idTarea);

		DatosTareaActivity fragment = new DatosTareaActivity();

		//pasamos al fragment el id de la tarea
		fragment.setArguments(arguments); 

		getFragmentManager().beginTransaction()
		.replace(android.R.id.tabcontent,fragment, AplicacionActivity.tagFragmentDatosTareas)
		.addToBackStack(AplicacionActivity.tagFragmentDatosTareas)
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

				if(mSpinnerBool){
					String valor = (String)parent.getItemAtPosition(position);

					if(listCursorAdapterTareas!=null){
						Log.e("spinner","spinner");
						listCursorAdapterTareas.getFilter().filter(valor);   
					}
				}else{
					mSpinnerBool=true;
				}
			}

			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});
	}


	//Se utiliza este metodo para que cuando le de 
	//al back button estas variables se inicialicen
	//y puedan funcionar correctamente.
	@Override
	public void onResume() {
		super.onResume();
		mSpinnerBool=false;
		mObserverBool=false;
		mOnTextChangedBool = false;
	}
	/**
	 * Funcion que muestra mensaje de alerta ante el intento de eliminacion
	 * @param v
	 * @param nombre Nombre de la tarea
	 * @param idTarea Id de la tarea
	 */
	public void mensajeAlertaEliminar(String nombre, final String idTarea){

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
	private void borrarTarea(String id) {
		final LayoutInflater inflater = LayoutInflater.from(getActivity());
		Boolean eliminado =  false;
		Mensaje mToast = null;
		String mensajeError = null;
		String mensajeOk = null;

		TareaSqliteDao tareaDao = new TareaSqliteDao();
		eliminado = tareaDao.eliminarTarea(getActivity(), id);
		mensajeOk = "ok_eliminado_tarea";
		mensajeError = "error_eliminado_empresa";

		if(eliminado){
			mToast = new Mensaje(inflater, (AplicacionActivity)getActivity(), mensajeOk);

			//Actualizamos los valores del cursor de la lista de tareas
			if(permisos.contains(Permiso.LISTAR_TODO)){
				listCursorAdapterTareas.changeCursor(tareaDao.buscarTareaFilter(getActivity(),null,false));
			}else if(permisos.contains(Permiso.LISTAR_PROPIOS)){
				listCursorAdapterTareas.changeCursor(tareaDao.buscarTareaFilter(getActivity(),null,true));
			}else{
				listCursorAdapterTareas.changeCursor(tareaDao.buscarTareaFilter(getActivity(),null, false));
			}


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

	/**
	 * Funcion encargada de modificar los colores de los cuadros de notificacion principal.
	 * @param v
	 */
	private void cambiarColorCuadroNotificacion(View v) {

		TextView tvVerde = (TextView) v.findViewById(R.id.avisoVerde);
		TextView tvRojo = (TextView) v.findViewById(R.id.avisoRojo);


		//pintamos..
		if(arrSincronizados.containsValue(true) && arrSincronizados.containsValue(false)){
			tvRojo.setBackgroundResource(R.drawable.borde_rojo);
			tvVerde.setBackgroundResource(R.drawable.borde_blanco);

		}else if(arrSincronizados.containsValue(true)){
			tvRojo.setBackgroundResource(R.drawable.borde_blanco);
			tvVerde.setBackgroundResource(R.drawable.borde_verde);

		}else if(arrSincronizados.containsValue(false)){
			tvRojo.setBackgroundResource(R.drawable.borde_rojo);
			tvVerde.setBackgroundResource(R.drawable.borde_blanco);
		}
	}

	/**
	 * Funcion que sincroniza una tarea.
	 * @param id Id de la tarea
	 */
	private void sincronizarTarea(String id) {
		final LayoutInflater inflater = LayoutInflater.from(getActivity());
		Boolean sincronizado =  false;
		Mensaje mToast = null;
		String mensajeError = null;
		String mensajeOk = null;

		TareaSqliteDao tareaDao = new TareaSqliteDao();
		sincronizado = tareaDao.sincronizarTarea(getActivity(), id);

		mensajeOk = "ok_sincronizado_tarea";
		mensajeError = "error_sincronizado_tarea";

		if(sincronizado){
			mToast = new Mensaje(inflater, (AplicacionActivity)getActivity(), mensajeOk);

			//Actualizamos los valores del cursor de la lista de tareas
			if(permisos.contains(Permiso.LISTAR_TODO)){
				listCursorAdapterTareas.changeCursor(tareaDao.buscarTareaFilter(getActivity(),null,false));
			}else if(permisos.contains(Permiso.LISTAR_PROPIOS)){
				listCursorAdapterTareas.changeCursor(tareaDao.buscarTareaFilter(getActivity(),null,true));
			}else{
				listCursorAdapterTareas.changeCursor(tareaDao.buscarTareaFilter(getActivity(),null, false));
			}

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
