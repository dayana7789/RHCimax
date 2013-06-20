package com.nahmens.rhcimax.controlador;


import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.nahmens.rhcimax.R;
import com.nahmens.rhcimax.adapters.ListaTareasCursorAdapter;
import com.nahmens.rhcimax.database.modelo.Tarea;
import com.nahmens.rhcimax.database.sqliteDAO.TareaSqliteDao;

public class TareasActivity extends ListFragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		View view = inflater.inflate(R.layout.activity_tareas, container, false);

		//Asociamos los valores al combo box o spinner
		inicializarSpinner(view);

		listarTareas(view);

		// Registro del evento OnClick del buttonTarea
		Button bTarea = (Button)view.findViewById(R.id.buttonAgregarTarea);
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


		return view;
	}


	private void listarTareas(View view) {
		//Cargamos la lista de tareas
		TareaSqliteDao tareaDao = new TareaSqliteDao();
		Context context = getActivity();
		Cursor mCursorTareas = tareaDao.listarTareas(getActivity());

		if(mCursorTareas.getCount()>0){
			//indicamos los campos que queremos mostrar (from) y en donde (to)
			String[] from = new String[] { Tarea.NOMBRE, Tarea.FECHA, Tarea.HORA, Tarea.NOMBRE_EMPLEADO, Tarea.APELLIDO_EMPLEADO, Tarea.NOMBRE_EMPRESA, Tarea.FECHA_FINALIZACION};
			int[] to = new int[] {R.id.textViewTitulo,  R.id.textViewFil1Col2, R.id.textViewFil1Col2, R.id.textViewFil2Col1, R.id.textViewFil2Col1, R.id.textViewFil1Col1, R.id.textViewFil2Col2 };
			ListView lvTareas = (ListView) view.findViewById (android.R.id.list);

			//Creamos un array adapter para desplegar cada una de las filas
			final ListaTareasCursorAdapter listCursorAdapterTareas = new ListaTareasCursorAdapter(context, R.layout.activity_fila_tarea, mCursorTareas, from, to, 0,this.getFragmentManager());
			lvTareas.setAdapter(listCursorAdapterTareas);
			
			//Registro del evento addTextChangedListener cuando utilizamos el buscador
			EditText etBuscar = (EditText) view.findViewById(R.id.editTextBuscar);
			etBuscar.addTextChangedListener(new TextWatcher() {

				@Override
				public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
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
	 * Funcion que inicializa los valores del spinner o combo box
	 * @param view
	 */
	private void inicializarSpinner(View view) {
		Spinner spinner = (Spinner) view.findViewById(R.id.spinnerTareas);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getActivity(),
				R.array.historicos_filtro_array, android.R.layout.simple_spinner_item);

		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
	}
	
	
	/**
	 * INICIO DE CODIGO PARA PERMITIR QUE UNA FILA PUEDA SER SELECCIONADA 
	 * NOTA: Es importante que en el layout de la fila (activity_fila_tarea.xml)
	 * en la layout raiz tenga: android:descendantFocusability="blocksDescendants"
	 * De lo contrario, el onListItemClick no va a funcionar!.
	 * 
	 * NOTA2: En AplicacionActivity, se implementa la interfaz OnTareaSelectedListener
	 * con su metodo onTareaSelected.
	 */

/*	OnTareaSelectedListener mTareaListener;

	/**
	 * interface utilizada para comunicar la lista con el detalle
	 * de cada fila.
	 */
/*	public interface OnTareaSelectedListener {
		public void onTareaSelected(Bundle mArgumentos, String id);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mTareaListener = (OnTareaSelectedListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString() + " se debe implementar OnTareaSelectedListener ");
		}
	}

	@Override
	/**
	 * @param position numero de posicion en la lista
	 * @param id Id del cliente sobre el cual se hizo click. Puede pertenecer a un empleado o a una empresa.
	 *           Este Id se asigna automaticamente por la definicion de _id en la BD.
	 */
/*	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);

		String idString = String.valueOf(id); 

		if (l.getId() == android.R.id.list) {
		//	mTareaListener.onTareaSelected(mArgumentos, id);
		}

	}

	/*********** FIN ******************/
}
