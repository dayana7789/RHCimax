package com.nahmens.rhcimax.adapters;

import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.nahmens.rhcimax.R;
import com.nahmens.rhcimax.database.modelo.Servicio;
import com.nahmens.rhcimax.mensaje.Mensaje;
import com.nahmens.rhcimax.utils.Tripleta;



/*
 * Adaptador personalizado para iterar sobre los resultados de la BD
 * relacionados con la lista de servicios.
 */
public class ListaServiciosCursorAdapter extends SimpleCursorAdapter{

	private int layout;
	private String[] from;
	private int[] to;
	private static HashMap<Integer, Tripleta> status; //Almacena los checkboxes que fueron seleccionados <idServicio, Tripleta(booleano, medida, descripcion)>
	private Button bFinalizar; //referencia al boton finalizar

	public static HashMap<Integer,Tripleta> getServiciosSeleccionados(){
		return status;
	}


	public static void setServiciosSeleccionados( HashMap<Integer,Tripleta> nuevo){
		status = nuevo;
	}

	public ListaServiciosCursorAdapter(Context context, int layout, Cursor c,
			String[] from, int[] to, int flags, Button bFinalizar) {

		super(context, layout, c, from, to, flags);

		this.layout = layout;
		this.from = from;
		this.to = to;
		this.bFinalizar = bFinalizar;

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
			setServiciosSeleccionados(new HashMap<Integer, Tripleta>());

			//Inicializamos la tabla status con idServicio y false, pues no se ha 
			//seleccionado ningun checkbox
			while (!c.isAfterLast()) {
				int idServicio = c.getInt(c.getColumnIndex(Servicio.ID));
				getServiciosSeleccionados().put(idServicio, new Tripleta(false, "", ""));
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
		ImageButton ibDescripcionServ;
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
		holder.etMedida.setFocusable(false);
		holder.etMedida.setFocusableInTouchMode(false);
		holder.etMedida.setEnabled(false);
		holder.etMedida.setText("");
		holder.etMedida.setError(null);
		
		holder.ibVerServicio = (ImageButton) v.findViewById(R.id.imageButtonVerServicio);
		
		holder.ibDescripcionServ = (ImageButton) v.findViewById(R.id.imageButtonDescripcionServ);
		holder.ibDescripcionServ.setEnabled(false);
		
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

		final EditText etMedida = holder.etMedida;
		final ImageButton ibDescripcionServ = holder.ibDescripcionServ;

		ibDescripcionServ.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v){

				if (!ibDescripcionServ.isEnabled()) { 
					return; 
				} else {  
					final Tripleta tripleta = getServiciosSeleccionados().get(mArgumentos.getInt("idServicio"));

					AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
					builder.setTitle("Descripción servicio");

					// Specify the type of input expected;
					final EditText input = new EditText(v.getContext());
					input.setText(tripleta.getDescripcion());

					input.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES|InputType.TYPE_TEXT_FLAG_MULTI_LINE|InputType.TYPE_CLASS_TEXT);
					builder.setView(input);

					// Set up the buttons
					builder.setPositiveButton("Guardar", new DialogInterface.OnClickListener() { 
						@Override
						public void onClick(DialogInterface dialog, int which) {

							boolean checked = tripleta.getBooleano();
							String medida = tripleta.getMedida();
							String descripcion = input.getText().toString();

							getServiciosSeleccionados().remove(mArgumentos.getInt("idServicio"));
							getServiciosSeleccionados().put(mArgumentos.getInt("idServicio"), new Tripleta(checked, medida, descripcion));

						}
					});
					builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.cancel();
						}
					});

					builder.show();
				}

			}});



		//Creamos un listener para actualizar la lista de checkboxes seleccionados
		CheckBox cb = ( CheckBox ) holder.cbServicio;
		cb.setOnCheckedChangeListener(new OnCheckedChangeListener(){
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				Tripleta tripleta = getServiciosSeleccionados().get(mArgumentos.getInt("idServicio"));
				String medida = tripleta.getMedida();
				String descripcion = tripleta.getDescripcion();

				if(isChecked){
					etMedida.setFocusable(true);
					etMedida.setFocusableInTouchMode(true);
					etMedida.setSelected(true);
					etMedida.setEnabled(true);
					ibDescripcionServ.setClickable(true);
					ibDescripcionServ.setEnabled(true);
					if(medida.equals("") || medida==null){
						etMedida.requestFocus();
						etMedida.setError(Mensaje.ERROR_CAMPO_OBLIGATORIO);
					}
				}else{
					etMedida.setFocusable(false);
					etMedida.setFocusableInTouchMode(false);
					etMedida.setEnabled(false);
					ibDescripcionServ.setClickable(false);
					ibDescripcionServ.setEnabled(false);
					etMedida.setText("");
					medida = "";
					etMedida.setError(null);
				}

				getServiciosSeleccionados().remove(mArgumentos.getInt("idServicio"));
				getServiciosSeleccionados().put(mArgumentos.getInt("idServicio"), new Tripleta(isChecked, medida, descripcion));

				boolean flagServicio = false;
				boolean flagCorreo = false;
				tripleta = null;

				for (Map.Entry<Integer, Tripleta> entry : getServiciosSeleccionados().entrySet()) {
					tripleta = entry.getValue();
					//si tengo algun servicio seleccionado..
					if( tripleta.getBooleano() == true){
						flagServicio = true;
					}
				}

				for (Map.Entry<Integer, Boolean> entry : ListaCorreosCursorAdapter.getCorreosSeleccionados().entrySet()) {
					//si tengo algun correo seleccionado..
					if( entry.getValue() == true){
						flagCorreo = true;
					}
				}

				//si flag es falso es porque ningun servicio fue seleccionado.
				if(flagServicio==false || flagCorreo == false){
					bFinalizar.setEnabled(false);
				}else{
					bFinalizar.setEnabled(true);
				}
			}
		});

		//Creamos un listener para actualizar la lista de edit texts seleccionados
		
		/*etMedida.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable s) {

				if(!s.toString().equals("")){
				Log.e("aqi","idservicio: " + mArgumentos.getInt("idServicio"));
				Par par = status.get(mArgumentos.getInt("idServicio"));
            	boolean cheked = par.getBooleano();
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

				/*if(!hasFocus){
					//	Log.e("setOnFocusChangeListener","idservicio: " + mArgumentos.getInt("idServicio"));
					Tripleta tripleta = getServiciosSeleccionados().get(mArgumentos.getInt("idServicio"));
					boolean cheked = tripleta.getBooleano();
					String descripcion = tripleta.getDescripcion(); 
					getServiciosSeleccionados().remove(mArgumentos.getInt("idServicio"));
					getServiciosSeleccionados().put(mArgumentos.getInt("idServicio"), new Tripleta(cheked, etMedida.getText().toString(), descripcion));
				}*/
			}
		});

		//Esta linea de codigo es importante para evitar que se pierdan los checkboxes seleccionados
		//cuando hacemos scroll de la lista. De aqui la importancia del setOnCheckedChangeListener
		//y la lista status.
		Tripleta tripleta = getServiciosSeleccionados().get(idServicio);
		cb.setChecked(tripleta.getBooleano());
		etMedida.setText(tripleta.getMedida());
	}
}