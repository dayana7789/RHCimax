package com.nahmens.rhcimax.adapters;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.nahmens.rhcimax.R;
import com.nahmens.rhcimax.controlador.AplicacionActivity;
import com.nahmens.rhcimax.controlador.DatosClienteActivity;
import com.nahmens.rhcimax.mensaje.Mensaje;


/*
 * Adaptador personalizado para iterar sobre los resultados de la BD,
 * relacionados con la lista de empleados asociados a una empresa.
 */
public class ListaEmpleadosCursorAdapter extends SimpleCursorAdapter{

	private Context context;
	private int layout;
	private String[] from;
	private int[] to;
	private FragmentManager fragmentManager;


	public ListaEmpleadosCursorAdapter(Context context, int layout, Cursor c,
			String[] from, int[] to, int flags, FragmentManager fragmentManager) {

		super(context, layout, c, from, to, flags);

		this.fragmentManager=fragmentManager;
		this.context = context;
		this.layout = layout;
		this.from = from;
		this.to = to;

	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {

		final LayoutInflater inflater = LayoutInflater.from(context);
		View v = inflater.inflate(layout, parent, false);

		//Columna de la BD que queremos recuperar
		String columna = null;

		//Indice de la columna de BD 
		int nombreCol = 0;

		//Resultado de obtener el indice de la columna de BD 
		String nombre = null;

		//Nombre del textView en nuestro Layout donde queremos
		//que aparezca el resultado.
		TextView nombre_text = null;

		String id = null;

		//Para cada valor de la BD solicitado, lo mostramos en el text view.
		for (int i=0; i<from.length; i++){
			columna = from[i];
			nombreCol = cursor.getColumnIndex(columna);
			nombre = cursor.getString(nombreCol);

			//los valores to[i] iguales a 0 indican que son ID's
			//lo que estamos recuperando: IDEmpleado.
			if(to[i] == 0){
				id = nombre;
			}

			nombre_text = (TextView) v.findViewById(to[i]);
			if (nombre_text != null) {
				nombre_text.setText(nombre);
			}
		}


		ImageButton buttonVerEmpleado = (ImageButton)  v.findViewById(R.id.imageButtonVerEmpleado);

		//almacenamos en un bundle, id empleado.
		final Bundle mArgumentos = new Bundle();
		mArgumentos.putString("id", id);

		buttonVerEmpleado.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v){

				DatosClienteActivity fragment = new DatosClienteActivity();

				//pasamos al fragment el id del empleado
				fragment.setArguments(mArgumentos); 

				fragmentManager.beginTransaction()
				.replace(android.R.id.tabcontent,fragment, AplicacionActivity.tagFragmentDatosCliente)
				.addToBackStack(null)
				.commit();

			}});

		return v;
	}
}

