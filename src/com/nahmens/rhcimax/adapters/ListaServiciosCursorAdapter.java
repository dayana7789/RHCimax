package com.nahmens.rhcimax.adapters;

import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.text.InputType;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableLayout.LayoutParams;
import android.widget.TableRow;
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
	private TableLayout tlListServicios;

	public static HashMap<Integer,Tripleta> getServiciosSeleccionados(){
		return status;
	}


	public static void setServiciosSeleccionados( HashMap<Integer,Tripleta> nuevo){
		status = nuevo;
	}

	public ListaServiciosCursorAdapter(TableLayout tlListServicios, Context context, int layout, Cursor c,
			String[] from, int[] to, int flags, Button bFinalizar) {

		super(context, layout, c, from, to, flags);

		this.layout = layout;
		this.from = from;
		this.to = to;
		this.bFinalizar = bFinalizar;
		this.tlListServicios = tlListServicios;

		inicializarServiciosSeleccionados(c);
		mbindView(context, c);
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
			c.moveToFirst();
		}
	}

	

	public void mbindView(Context context, Cursor cursor) { 

		//Columna de la BD que queremos recuperar
		String columna = null;

		//Indice de la columna de BD 
		int nombreCol = 0;

		//Resultado de obtener el indice de la columna de BD 
		String nombre = null;

		String nombreServicio = null;


		//Nombre del view en nuestro Layout donde queremos
		//que aparezca el resultado.
		View mView = null;
		CheckBox cb = null;
		boolean chequeado = false;

		View mTableRow = null;

		//Para cada valor de la BD solicitado, lo mostramos en el text view.
		while(!cursor.isAfterLast()){

			int idServicio = cursor.getInt(cursor.getColumnIndex(Servicio.ID));

			//obtenemos la referencia a la fila descrita en activity_fila_correo.xml
			mTableRow = (TableRow) View.inflate(context, R.layout.activity_fila_servicio, null);

			Tripleta tripleta = getServiciosSeleccionados().get(idServicio);
			
			for (int i=0; i<from.length; i++){

				columna = from[i];
				nombreCol = cursor.getColumnIndex(columna);
				nombre = cursor.getString(nombreCol);
				
				mView = (View) mTableRow.findViewById(to[i]);

				if(mView instanceof CheckBox){
					cb = (CheckBox) mView;
					cb.setText(nombre);

					chequeado = tripleta.getBooleano();
					cb.setChecked(chequeado);
					
				}
			}

			//agregamos las filas o TableRows a TableLayout
			tlListServicios.addView(mTableRow);

			//Creamos una linea divisora entre las filas
			View line = new View(context);
			line.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 1));
			line.setBackgroundColor(Color.LTGRAY);
			tlListServicios.addView(line);
			
			
			//Obtenemos resto de valores de la BD
			double precio = cursor.getDouble(cursor.getColumnIndex(Servicio.PRECIO));
			String descripcion = cursor.getString(cursor.getColumnIndex(Servicio.DESCRIPCION));
			String unidadMedicion = cursor.getString(cursor.getColumnIndex(Servicio.UNIDAD_MEDICION));
			double inicial =  cursor.getDouble(cursor.getColumnIndex(Servicio.INICIAL));

			
			//almacenamos en un bundle, precio y descripcion del servicio.
			final Bundle mArgumentos = new Bundle();
			mArgumentos.putString("nombre", nombreServicio);
			mArgumentos.putDouble("precio", precio);
			mArgumentos.putString("descripcion", descripcion);
			mArgumentos.putString("unidadMedicion", unidadMedicion);
			mArgumentos.putDouble("inicial", inicial);
			mArgumentos.putInt("idServicio", idServicio);
			
			final EditText etMedida = (EditText) mTableRow.findViewById(R.id.editTextMedida);
			etMedida.setFocusable(false);
			etMedida.setFocusableInTouchMode(false);
			etMedida.setEnabled(false);
			etMedida.setText("");
			etMedida.setError(null);
			TextView tvUnidadMedicion = (TextView) mTableRow.findViewById(R.id.textViewUnidadMedicion);

			if(unidadMedicion.equals("ninguno")){
				etMedida.setVisibility(View.INVISIBLE);
				tvUnidadMedicion.setVisibility(View.INVISIBLE);

			}else{
				etMedida.setVisibility(View.VISIBLE);
				tvUnidadMedicion.setVisibility(View.VISIBLE);
				tvUnidadMedicion.setText(unidadMedicion);
			}
			
			ImageButton buttonVerServicio = (ImageButton) mTableRow.findViewById(R.id.imageButtonVerServicio);
			
			
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

			
			
			final ImageButton ibDescripcionServ = (ImageButton) mTableRow.findViewById(R.id.imageButtonDescripcionServ);

			ibDescripcionServ.setEnabled(false);
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

			//Creamos un listener para actualizar la lista de edit texts seleccionados

			
			etMedida.setOnFocusChangeListener(new OnFocusChangeListener() {
				public void onFocusChange(View v, boolean hasFocus) {

					if(!hasFocus){
						//	Log.e("setOnFocusChangeListener","idservicio: " + mArgumentos.getInt("idServicio"));
						Tripleta tripleta = getServiciosSeleccionados().get(mArgumentos.getInt("idServicio"));
						boolean cheked = tripleta.getBooleano();
						String descripcion = tripleta.getDescripcion(); 
						getServiciosSeleccionados().remove(mArgumentos.getInt("idServicio"));
						getServiciosSeleccionados().put(mArgumentos.getInt("idServicio"), new Tripleta(cheked, etMedida.getText().toString(), descripcion));
					}
				}
			});

			//Creamos un listener para actualizar la lista de checkboxes seleccionados
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
			
			
			etMedida.setText(tripleta.getMedida());
			
			cursor.moveToNext();

		}
	
	}
}