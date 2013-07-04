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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableLayout.LayoutParams;
import android.widget.TableRow;
import android.widget.TextView;

import com.nahmens.rhcimax.R;
import com.nahmens.rhcimax.database.modelo.Servicio;
import com.nahmens.rhcimax.utils.Tripleta;



/*
 * Adaptador personalizado para iterar sobre los resultados de la BD
 * relacionados con la lista de servicios.
 */
public class ListaServiciosCursorAdapter extends SimpleCursorAdapter{

	private int layout;
	private String[] from;
	private int[] to;
	private static HashMap<Integer, Tripleta> status; //Almacena los checkboxes que fueron seleccionados <idServicio, Tripleta(booleano, medida, descripcion, precio, inicial)>
	private Button bFinalizar; //referencia al boton finalizar
	private TableLayout tlListServicios;
	private Context contexto;

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
		this.contexto = context;

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
				getServiciosSeleccionados().put(idServicio, new Tripleta(false, "", "",0,0));
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

		//Nombre del view en nuestro Layout donde queremos
		//que aparezca el resultado.
		View mView = null;
		CheckBox cb = null;
		boolean chequeado = false;

		//Para cada valor de la BD solicitado, lo mostramos en el text view.
		while(!cursor.isAfterLast()){

			int idServicio = cursor.getInt(cursor.getColumnIndex(Servicio.ID));
			String nombreServicio = cursor.getString(cursor.getColumnIndex(Servicio.NOMBRE));

			//obtenemos la referencia a la fila descrita en activity_fila_servicio.xml
			final View mTableRow = (TableRow) View.inflate(context, R.layout.activity_fila_servicio, null);
			

			Tripleta tripleta = getServiciosSeleccionados().get(idServicio);
			
			//Obtenemos resto de valores de la BD
			double precio = cursor.getDouble(cursor.getColumnIndex(Servicio.PRECIO));
			String descripcion = cursor.getString(cursor.getColumnIndex(Servicio.DESCRIPCION));
			String unidadMedicion = cursor.getString(cursor.getColumnIndex(Servicio.UNIDAD_MEDICION));
			double inicial =  cursor.getDouble(cursor.getColumnIndex(Servicio.INICIAL));
			
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
					
					if(chequeado){
						visibilidadDatosMedida(true,mTableRow, tripleta.getMedida(), unidadMedicion);
					}else{
						visibilidadDatosMedida(false,mTableRow, null, null);
					}
					
				}
			}

			//agregamos las filas o TableRows a TableLayout
			tlListServicios.addView(mTableRow);

			//Creamos una linea divisora entre las filas
			View line = new View(context);
			line.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 1));
			line.setBackgroundColor(Color.LTGRAY);
			tlListServicios.addView(line);

			//almacenamos en un bundle, precio y descripcion del servicio.
			final Bundle mArgumentos = new Bundle();
			mArgumentos.putString("nombre", nombreServicio);
			mArgumentos.putDouble("precio", precio);
			mArgumentos.putString("descripcion", descripcion);
			mArgumentos.putString("unidadMedicion", unidadMedicion);
			mArgumentos.putDouble("inicial", inicial);
			mArgumentos.putInt("idServicio", idServicio);
	
			final CompoundButton cbutton = cb;
			
			ImageButton buttonOpciones = (ImageButton) mTableRow.findViewById(R.id.imageButtonOpciones);
			buttonOpciones.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v){
					mostrarListaOpciones(cbutton, mArgumentos, mTableRow);
				}});

			
		
			//Creamos un listener para actualizar la lista de checkboxes seleccionados
			cb.setOnCheckedChangeListener(new OnCheckedChangeListener(){
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					Tripleta tripleta = getServiciosSeleccionados().get(mArgumentos.getInt("idServicio"));
					
					
					if(isChecked){
						String unidadMedicion = mArgumentos.getString("unidadMedicion");
						if(!unidadMedicion.equals("ninguno")){
							asignarMedida(buttonView,mArgumentos, mTableRow);
						}
					}else{
						borrarMedida(buttonView, mArgumentos, mTableRow);
					}
					
					String medida = tripleta.getMedida();
					String descripcion = tripleta.getDescripcion();
					
					getServiciosSeleccionados().remove(mArgumentos.getInt("idServicio"));
					getServiciosSeleccionados().put(mArgumentos.getInt("idServicio"), new Tripleta(isChecked, medida, descripcion,mArgumentos.getDouble("precio"),mArgumentos.getDouble("inicial")));

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
			
			cursor.moveToNext();

		}
	
	}


	private void visibilidadDatosMedida(boolean esVisible, View mView, String medida, String unidadMedicion) {
		LinearLayout lnMedida = (LinearLayout)  mView.findViewById(R.id.linearLayoutMedida);
		TextView tvMedida = (TextView) mView.findViewById(R.id.textViewMedida);
		TextView tvUnidadMedida = (TextView) mView.findViewById(R.id.textViewUnidadMedicion);
		
		if(esVisible){
			lnMedida.setVisibility(View.VISIBLE);
			tvMedida.setText(medida);
			tvUnidadMedida.setText(unidadMedicion);
		}else{
			lnMedida.setVisibility(View.GONE);
		}
	}


	private void borrarMedida(CompoundButton buttonView, Bundle mArgumentos, View mView) {
		Tripleta tripleta = getServiciosSeleccionados().get(mArgumentos.getInt("idServicio"));
		
		boolean checked = false;
		String medida = "";
		String descripcion = "";
		double precio = mArgumentos.getDouble("precio");
		double inicial = mArgumentos.getDouble("inicial");

		getServiciosSeleccionados().remove(mArgumentos.getInt("idServicio"));
		getServiciosSeleccionados().put(mArgumentos.getInt("idServicio"), new Tripleta(checked, medida, descripcion, precio, inicial));

		buttonView.setChecked(checked);
		visibilidadDatosMedida(false,mView,null,null);
	}


	/**
	 * Funcion que muestra opciones al presionar boton opciones
	 * @param mArgumentos 
	 */
	protected void mostrarListaOpciones(final CompoundButton buttonView, final Bundle mArgumentos, final View mView) {

		String unidadMedicion = mArgumentos.getString("unidadMedicion");
		AlertDialog.Builder builder = new AlertDialog.Builder(contexto);
		builder.setTitle("Opciones para " + mArgumentos.getString("nombre"));
		
		if(unidadMedicion.equals("ninguno")){
			String[] arr = {"Ver detalle","Asignar descripción"};
			
			builder.setItems(arr, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					switch (which) {
					case 0:
						verDetalle(mArgumentos);
						break;
						
					case 1:
						modificarDescripcion(mArgumentos);
						break;
					}
				}
			});
			
		}else{
			String[] arr = {"Ver detalle","Asignar cantidad","Asignar descripción"};
			builder.setItems(arr, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					switch (which) {
					case 0:
						verDetalle(mArgumentos);
						break;

					case 1:
						asignarMedida(buttonView,mArgumentos, mView);
						break;
						
					case 2:
						modificarDescripcion(mArgumentos);
						break;
					}
				}
			});
		}

		AlertDialog alert = builder.create();
		alert.show();
	}


	/**
	 * FUncion que muestra ventana para asignar medida o cantidad a los servicios
	 * @param buttonView
	 * @param mArgumentos
	 * @param mView
	 */
	private void asignarMedida(final CompoundButton buttonView, final Bundle mArgumentos, final View mView) {

		final Tripleta tripleta = getServiciosSeleccionados().get(mArgumentos.getInt("idServicio"));

		AlertDialog.Builder builder = new AlertDialog.Builder(contexto);
		builder.setTitle("Asignar cantidad: " + mArgumentos.getString("nombre"));

		final EditText input = new EditText(contexto);
		
		input.setHint(mArgumentos.getString("unidadMedicion"));
		input.setText(""+tripleta.getMedida());


		input.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES|InputType.TYPE_TEXT_FLAG_MULTI_LINE|InputType.TYPE_CLASS_NUMBER);
		builder.setView(input);
		//builder.setView(unidadMed);

		// Set up the buttons
		builder.setPositiveButton("Guardar", new DialogInterface.OnClickListener() { 
			@Override
			public void onClick(DialogInterface dialog, int which) {

				boolean checked = true;
				String medida = input.getText().toString();
				
				if(medida.equals("")){
					checked = false;
				}
				String descripcion = tripleta.getDescripcion();
				double precio = mArgumentos.getDouble("precio");
				double inicial = mArgumentos.getDouble("inicial");

				getServiciosSeleccionados().remove(mArgumentos.getInt("idServicio"));
				getServiciosSeleccionados().put(mArgumentos.getInt("idServicio"), new Tripleta(checked, medida, descripcion, precio, inicial));
				
				buttonView.setChecked(checked);
				visibilidadDatosMedida(checked,mView, medida, mArgumentos.getString("unidadMedicion"));
			}
		});
		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
				
				boolean checked = true;
				String medida = tripleta.getMedida();
				String descripcion = tripleta.getDescripcion();
				
				if(medida.equals("")){
					checked = false;
					descripcion = "";
				}
				
				double precio = mArgumentos.getDouble("precio");
				double inicial = mArgumentos.getDouble("inicial");

				getServiciosSeleccionados().remove(mArgumentos.getInt("idServicio"));
				getServiciosSeleccionados().put(mArgumentos.getInt("idServicio"), new Tripleta(checked, medida, descripcion, precio, inicial));
				
				buttonView.setChecked(checked);
				visibilidadDatosMedida(checked,mView, medida, mArgumentos.getString("unidadMedicion"));
				
			}
		});

		builder.show();
		
	}


	/**
	 * Funcion que modifica la descripcion de cada servicio
	 * @param mArgumentos
	 */
	private void modificarDescripcion(final Bundle mArgumentos) {
		
		final Tripleta tripleta = getServiciosSeleccionados().get(mArgumentos.getInt("idServicio"));

		AlertDialog.Builder builder = new AlertDialog.Builder(contexto);
		builder.setTitle("Descripción servicio: " + mArgumentos.getString("nombre"));

		// Specify the type of input expected;
		final EditText input = new EditText(contexto);
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
				double precio =  mArgumentos.getDouble("precio");
				double inicial =  mArgumentos.getDouble("inicial");

				getServiciosSeleccionados().remove(mArgumentos.getInt("idServicio"));
				getServiciosSeleccionados().put(mArgumentos.getInt("idServicio"), new Tripleta(checked, medida, descripcion, precio, inicial));

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


	/**
	 * Funcion que muestra el detalle de cada servicio
	 * @param mArgumentos
	 */
	private void verDetalle(Bundle mArgumentos) {
		AlertDialog.Builder builder = new AlertDialog.Builder(contexto);
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
		
	}
}