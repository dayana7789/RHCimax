package com.nahmens.rhcimax.adapters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.nahmens.rhcimax.R;
import com.nahmens.rhcimax.database.modelo.Servicio;



/*
 * Adaptador personalizado para iterar sobre los resultados de la BD
 * relacionados con la lista de servicios.
 */
public class ListaServiciosCursorAdapter extends SimpleCursorAdapter{

	private Context context;
	private int layout;
	private String[] from;
	private int[] to;
	private HashMap<Integer,Boolean> status; //Almacena los checkboxes que fueron seleccionados <idServicio, seleccionado?>


	public ListaServiciosCursorAdapter(Context context, int layout, Cursor c,
			String[] from, int[] to, int flags) {

		super(context, layout, c, from, to, flags);

		this.context = context;
		this.layout = layout;
		this.from = from;
		this.to = to;
		this.status = new HashMap<Integer, Boolean>();

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

		String nombreServicio = null;

		//Para cada valor de la BD solicitado, lo mostramos en el text view.
		for (int i=0; i<from.length; i++){
			columna = from[i];
			nombreCol = cursor.getColumnIndex(columna);
			nombre = cursor.getString(nombreCol);
			nombreServicio = nombre; 
			nombre_text = (TextView) v.findViewById(to[i]);

			if (nombre_text != null) {
				nombre_text.setText(nombre);
			}
		}

		//Obtenemos resto de valores de la BD
		double precio = cursor.getDouble(cursor.getColumnIndex(Servicio.PRECIO));
		String descripcion = cursor.getString(cursor.getColumnIndex(Servicio.DESCRIPCION));
		String unidadMedicion = cursor.getString(cursor.getColumnIndex(Servicio.UNIDAD_MEDICION));
		double inicial =  cursor.getDouble(cursor.getColumnIndex(Servicio.INICIAL));
		int idServicio = cursor.getInt(cursor.getColumnIndex(Servicio.ID));

		if(unidadMedicion.equals("ninguno")){
			EditText etMedida = (EditText) v.findViewById(R.id.editTextMedida);
			etMedida.setVisibility(View.INVISIBLE);

			TextView tvUnidadMedicion = (TextView) v.findViewById(R.id.textViewUnidadMedicion);
			tvUnidadMedicion.setVisibility(View.INVISIBLE);


		}else{
			EditText etMedida = (EditText) v.findViewById(R.id.editTextMedida);
			etMedida.setVisibility(View.VISIBLE);

			TextView tvUnidadMedicion = (TextView) v.findViewById(R.id.textViewUnidadMedicion);
			tvUnidadMedicion.setVisibility(View.VISIBLE);
			tvUnidadMedicion.setText(unidadMedicion);

		}



		ImageButton buttonVerServicio = (ImageButton)  v.findViewById(R.id.imageButtonVerServicio);

		//almacenamos en un bundle, precio y descripcion del servicio.
		final Bundle mArgumentos = new Bundle();
		mArgumentos.putString("nombre", nombreServicio);
		mArgumentos.putDouble("precio", precio);
		mArgumentos.putString("descripcion", descripcion);
		mArgumentos.putString("unidadMedicion", unidadMedicion);
		mArgumentos.putDouble("inicial", inicial);
		mArgumentos.putInt("idServicio", idServicio);


		buttonVerServicio.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v){

				AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());

				builder.setMessage(mArgumentos.getInt("idServicio") +","+mArgumentos.getString("unidadMedicion") + ", " + mArgumentos.getString("descripcion"))
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


		//Creamos un listener para actualizar la lista de checkboxes seleccionados
		CheckBox cb = ( CheckBox ) v.findViewById( R.id.checkBoxServicio );
		cb.setOnCheckedChangeListener(new OnCheckedChangeListener(){
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

				if(status.containsKey(mArgumentos.getInt("idServicio"))){
					status.remove(mArgumentos.getInt("idServicio"));
					status.put(mArgumentos.getInt("idServicio"), isChecked);
				}else{
					status.put(mArgumentos.getInt("idServicio"), isChecked);
				}
			}
		});

		
		try{
			//Esta linea de codigo es importante para evitar que se pierdan los checkboxes seleccionados
			//cuando hacemos scroll de la lista. De aqui la importancia del setOnCheckedChangeListener
			//y la lista status.
			cb.setChecked(status.get(mArgumentos.getInt("idServicio")));
		}catch (Exception e) {
			// Ingnorar. Esta se excepcion se llama cuando status.get(mArgumentos.getInt("idServicio")) devuelve null.
			//Esto es porque no inicializamos la lista a priori con los ids de los servicios, sino que la vamos cargando 
			//a medida que setOnCheckedChangeListener es llamado.
		}
	}
}

