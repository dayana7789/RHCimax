package com.nahmens.rhcimax.controlador;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.nahmens.rhcimax.R;
import com.nahmens.rhcimax.database.sqliteDAO.UsuarioSqliteDao;



public class ClientesActivity extends ListFragment{
	private Cursor mCursor;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		View view = inflater.inflate(R.layout.activity_clientes, container, false);

		//Cargamos la lista de clientes
		UsuarioSqliteDao usuDao = new UsuarioSqliteDao();
		
		Context context = getActivity();
		mCursor = usuDao.listarUsuarios(context);

		//indicamos los campos que queremos mostrar (from) y en donde (to)
 		String[] from = new String[] { "login", "password" };
 		int[] to = new int[] { R.id.textViewClienteFila,  R.id.textViewClienteFila2};
 
 		//Creamos un array adapter para desplegar cada una de las filas
 		SimpleCursorAdapter notes = new SimpleCursorAdapter(context, R.layout.activity_cliente_fila, mCursor, from, to);
 		setListAdapter(notes);

		// Registro del evento OnClick del buttonNuevo
		Button b = (Button)view.findViewById(R.id.buttonNuevo);
		b.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
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
