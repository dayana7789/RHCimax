package com.nahmens.rhcimax.controlador;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.nahmens.rhcimax.R;

public class TareasActivity extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		View view = inflater.inflate(R.layout.activity_tareas, container, false);

		//Asociamos los valores al combo box o spinner
		inicializarSpinner(view);

		// Registro del evento OnClick del buttonEmpresa
		Button bTarea = (Button)view.findViewById(R.id.buttonTarea);
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
}
