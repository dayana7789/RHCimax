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
 * Adaptador personalizado para iterar sobre los resultados de la BD
 * relacionados con la lista de servicios.
 */
public class ListaServiciosCursorAdapter extends SimpleCursorAdapter{

	private Context context;
	private int layout;
	private String[] from;
	private int[] to;


	public ListaServiciosCursorAdapter(Context context, int layout, Cursor c,
			String[] from, int[] to, int flags) {

		super(context, layout, c, from, to, flags);

		this.context = context;
		this.layout = layout;
		this.from = from;
		this.to = to;

	}

	/*
	 * The newView() method should be responsible only for building the row view(inflating a layout, programatically constructing the row etc).
	 * The row will be built based on the **layout id** that you pass in the constructor
	 */
	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {

		final LayoutInflater inflater = LayoutInflater.from(context);
		View v = inflater.inflate(layout, parent, false);

		return v;
	}
	
	/*
	 * bindView() is where you'll actually bind the data from the Cursor to the views of the row that is passed in.
	 */
	@Override
    public void bindView(View v, Context context, Cursor cursor) { 
		
		//Columna de la BD que queremos recuperar
		String columna = null;

		//Indice de la columna de BD 
		int nombreCol = 0;

		//Resultado de obtener el indice de la columna de BD 
		String nombre = null;

		//Nombre del textView en nuestro Layout donde queremos
		//que aparezca el resultado.
		TextView nombre_text = null;

		String precio = null;
		String descripcion = null;
		String nombreServicio = null;

		//Para cada valor de la BD solicitado, lo mostramos en el text view.
		for (int i=0; i<from.length; i++){
			columna = from[i];
			nombreCol = cursor.getColumnIndex(columna);
			nombre = cursor.getString(nombreCol);

			//los valores to[i] iguales a 0 y 1 indican valores que necesitamos
			//pero que no se van a mostrar en la lista directamente.
			//1 -> descripcion
			//0 -> precio
			if(to[i] == 0){
				precio = nombre;
			}else if(to[i] == 1){
				descripcion = nombre;
			}else{
				nombreServicio = nombre; 
				nombre_text = (TextView) v.findViewById(to[i]);
				if (nombre_text != null) {
					nombre_text.setText(nombre);
				}
			}
		}


		ImageButton buttonVerServicio = (ImageButton)  v.findViewById(R.id.imageButtonVerServicio);

		//almacenamos en un bundle, precio y descripcion del servicio.
		final Bundle mArgumentos = new Bundle();
		mArgumentos.putString("nombre", nombreServicio);
		mArgumentos.putString("precio", precio);
		mArgumentos.putString("descripcion", descripcion);
		
		buttonVerServicio.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v){

				AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
				
				builder.setMessage(mArgumentos.getString("descripcion"))
				.setTitle(mArgumentos.getString("nombre"))
				.setCancelable(false)
				.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
				
				AlertDialog alert = builder.create();
				alert.show();

			}});
	}
}

