package com.nahmens.rhcimax.controlador;

import com.nahmens.rhcimax.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;



public class ClientesActivity extends Fragment  {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		View view = inflater.inflate(R.layout.activity_clientes, container, false);


		// Register for the Button.OnClick event
		Button b = (Button)view.findViewById(R.id.buttonNuevo);
		b.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// Toast.makeText(Tab1Fragment.this.getActivity(), "OnClickMe button clicked", Toast.LENGTH_LONG).show();
				DatosClienteActivity fragmentDatosClientes = new DatosClienteActivity();
				final FragmentTransaction ft = getFragmentManager().beginTransaction();
				//Cambiamos el layout de clientes por datos_cliente
				ft.replace(android.R.id.tabcontent,fragmentDatosClientes, "fragmentDatosClientes"); 
				//preservamos el estado anterior al hacer click en back button
				ft.addToBackStack(null);
				ft.commit(); 
			}
		});
		return view;
	}

}
