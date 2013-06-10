package com.nahmens.rhcimax.adapters;

import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.nahmens.rhcimax.R;
import com.nahmens.rhcimax.database.modelo.Servicio;
import com.nahmens.rhcimax.mensaje.Mensaje;
import com.nahmens.rhcimax.utils.Par;



/*
 * Adaptador personalizado para iterar sobre los resultados de la BD
 * relacionados con la lista de servicios.
 */
public class ListaServiciosCursorAdapter extends SimpleCursorAdapter{

	private int layout;
	private String[] from;
	private int[] to;
	private static HashMap<Integer, Par> status; //Almacena los checkboxes que fueron seleccionados <idServicio, Par(booleano, medida)>
	
	@SuppressLint("UseSparseArrays")
	private final HashMap<Integer, EditText> hmEditText = new HashMap<Integer, EditText>(); //almacena las referencias a los EditText

	public static HashMap<Integer,Par> getServiciosSeleccionados(){
		return status;
	}


	public static void setServiciosSeleccionados( HashMap<Integer,Par> nuevo){
		status = nuevo;
	}

	public ListaServiciosCursorAdapter(Context context, int layout, Cursor c,
			String[] from, int[] to, int flags) {

		super(context, layout, c, from, to, flags);

		this.layout = layout;
		this.from = from;
		this.to = to;

		inicializarServiciosSeleccionados(c);
	}

	/**
	 * Inicializa la estructura status agregando todos los idServicio y asociandolos
	 * con valores false y "" para indicar que al inicio ningun servicio esta seleccionado
	 * por default.
	 * @param c Cursor
	 */
	@SuppressLint("UseSparseArrays")
	private void inicializarServiciosSeleccionados(Cursor c) {
		//se esta llamando a la lista de servicios por primera vez
		if(getServiciosSeleccionados() == null){
			setServiciosSeleccionados(new HashMap<Integer, Par>());

			//Inicializamos la tabla status con idServicio y false, pues no se ha 
			//seleccionado ningun checkbox
			while (!c.isAfterLast()) {
				int idServicio = c.getInt(c.getColumnIndex(Servicio.ID));
				getServiciosSeleccionados().put(idServicio, new Par(false, ""));
				hmEditText.put(idServicio,null);
				c.moveToNext();
			}
		}
	}

	//Clase que permite guardar referencia de los childs en el layout
	private class ViewHolder {
		CheckBox cbServicio;
		TextView tvUnidadMedicion;
		EditText etMedida;
		ImageButton ibVerServicio;
	}

	/*
	 * The newView() method should be responsible only for building the row view(inflating a layout, programatically constructing the row etc).
	 * The row will be built based on the **layout id** that you pass in the constructor
	 */
	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {

		final LayoutInflater inflater = LayoutInflater.from(context);
		View v = inflater.inflate(layout, parent, false);

		//Este holder se encarga de guardar las referencias a los elementos de las filas
		//de manera de que estas no se calculen cada vez que se entra en el bind view.
		//(operacion costosa en android)
		ViewHolder holder = null;
		holder = new ViewHolder();
		holder.cbServicio = (CheckBox) v.findViewById(R.id.checkBoxServicio);
		holder.tvUnidadMedicion = (TextView) v.findViewById(R.id.textViewUnidadMedicion);
		holder.etMedida = (EditText) v.findViewById(R.id.editTextMedida);
		holder.ibVerServicio = (ImageButton) v.findViewById(R.id.imageButtonVerServicio);
		v.setTag(holder);

		return v;
	}

	/*
	 * bindView() is where you'll actually bind the data from the Cursor to the views of the row that is passed in.
	 */
	@Override
	public void bindView(View v, Context context, Cursor cursor) { 


		//Obtenemos el holder de las referencias a los elementos.
		//de esta manera evitamos hacer v.findViewById 
		//(operacion costosa en android)
		ViewHolder holder = (ViewHolder) v.getTag();

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
			EditText etMedida = holder.etMedida;
			etMedida.setVisibility(View.INVISIBLE);

			TextView tvUnidadMedicion = holder.tvUnidadMedicion;
			tvUnidadMedicion.setVisibility(View.INVISIBLE);

		}else{
			EditText etMedida = holder.etMedida;
			etMedida.setVisibility(View.VISIBLE);

			TextView tvUnidadMedicion = holder.tvUnidadMedicion;
			tvUnidadMedicion.setVisibility(View.VISIBLE);
			tvUnidadMedicion.setText(unidadMedicion);
		}

		//guardamos la referencia del edit text de cada fila
		hmEditText.remove(idServicio);
		hmEditText.put(idServicio, holder.etMedida);

		ImageButton buttonVerServicio = holder.ibVerServicio;

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
		CheckBox cb = ( CheckBox ) holder.cbServicio;
		cb.setOnCheckedChangeListener(new OnCheckedChangeListener(){
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				Par par = getServiciosSeleccionados().get(mArgumentos.getInt("idServicio"));
				EditText et = (EditText) hmEditText.get(mArgumentos.getInt("idServicio"));
				String medida = par.getMedida();

				if(isChecked){
					et.setFocusable(true);
					et.setFocusableInTouchMode(true);
					et.setSelected(true);
					et.setEnabled(true);
					if(medida.equals("") || medida==null){
						et.requestFocus();
						et.setError(Mensaje.ERROR_CAMPO_OBLIGATORIO);
					}
				}else{
					et.setFocusable(false);
					et.setFocusableInTouchMode(false);
					et.setEnabled(false);
					et.setText("");
					medida = "";
					et.setError(null);
				}

				getServiciosSeleccionados().remove(mArgumentos.getInt("idServicio"));
				getServiciosSeleccionados().put(mArgumentos.getInt("idServicio"), new Par(isChecked, medida));
			}
		});

		//Creamos un listener para actualizar la lista de edit texts seleccionados
		EditText etMedida = holder.etMedida;
		/*etMedida.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable s) {

				if(!s.toString().equals("")){
				Log.e("aqi","idservicio: " + mArgumentos.getInt("idServicio"));
				Par par = status.get(mArgumentos.getInt("idServicio"));
            	boolean cheked = par.getBooleano();
            	//EditText et = (EditText) hmEditText.get(mArgumentos.getInt("idServicio"));
            	status.remove(mArgumentos.getInt("idServicio"));
				status.put(mArgumentos.getInt("idServicio"), new Par(cheked, s.toString()));
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}

		});*/
		etMedida.setOnFocusChangeListener(new OnFocusChangeListener() {
			public void onFocusChange(View v, boolean hasFocus) {

				if(!hasFocus && getServiciosSeleccionados()!=null){
					Log.e("setOnFocusChangeListener","idservicio: " + mArgumentos.getInt("idServicio"));
					Par par = getServiciosSeleccionados().get(mArgumentos.getInt("idServicio"));
					boolean cheked = par.getBooleano();
					EditText et = (EditText) hmEditText.get(mArgumentos.getInt("idServicio"));
					getServiciosSeleccionados().remove(mArgumentos.getInt("idServicio"));
					getServiciosSeleccionados().put(mArgumentos.getInt("idServicio"), new Par(cheked, et.getText().toString()));
				}
			}
		});

		//Esta linea de codigo es importante para evitar que se pierdan los checkboxes seleccionados
		//cuando hacemos scroll de la lista. De aqui la importancia del setOnCheckedChangeListener
		//y la lista status.
		Par par = getServiciosSeleccionados().get(idServicio);
		cb.setChecked(par.getBooleano());
		etMedida.setText(par.getMedida());
	}
}