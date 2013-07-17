package com.nahmens.rhcimax.adapters;

import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TableLayout;
import android.widget.TableLayout.LayoutParams;
import android.widget.TableRow;
import android.widget.TextView;

import com.nahmens.rhcimax.R;
import com.nahmens.rhcimax.database.modelo.Empleado;
import com.nahmens.rhcimax.utils.Tripleta;

/**
 * Adaptador personalizado para iterar sobre los resultados de la BD,
 * relacionados con la lista de correos de empleados asociados a una empresa.
 * Se utiliza en la pagina de servicios y se implementa para saber cuales
 * checkboxes de la lista de correos se marcan por default.
 * 
 * Alternativa para evitar usar ListView dentro de ScrollView.
 */
public class ListaCorreosCursorAdapter{

	private String[] from;
	private int[] to;
	private String idEmpleado;
	private static HashMap<String, Boolean> correosSeleccionados; //Almacena los checkboxes que fueron seleccionados <idEmpleado, booleano>
	private Button bFinalizar; //referencia al boton finalizar
	private TableLayout tlListCorreos;

	public static HashMap<String,Boolean> getCorreosSeleccionados(){
		return correosSeleccionados;
	}

	public static void setCorreosSeleccionados( HashMap<String,Boolean> nuevo){
		correosSeleccionados = nuevo;
	}

	public ListaCorreosCursorAdapter(TableLayout tlListCorreos, Context context, int layout, Cursor c,
			String[] from, int[] to, int flags, String idEmpleado, Button bFinalizar) {

		this.from = from;
		this.to = to;
		this.idEmpleado = idEmpleado;
		this.bFinalizar = bFinalizar;
		this.tlListCorreos = tlListCorreos;

		inicializarCorreosSeleccionados(c);
		mbindView(context, c);
	}


	/**
	 * Inicializa la estructura correosSeleccionados agregando todos los idEmpleados y asociandolos
	 * con valores true o false para indicar a priori cuales correos se muestran como checkeados 
	 * por default.
	 * @param c Cursor
	 */
	@SuppressLint("UseSparseArrays")
	private void inicializarCorreosSeleccionados(Cursor c) {
		//se esta llamando a la lista de servicios por primera vez
		if(getCorreosSeleccionados() == null){
			setCorreosSeleccionados(new HashMap<String, Boolean>());

			while (!c.isAfterLast()) {
				String idEmpleadoNuevo = c.getString(c.getColumnIndex(Empleado.ID));

				//Si se nos indica el idEmpleado, quiere decir que solo el correo
				//de esta persona sera checkeado por default
				if(idEmpleado!=null){

					if(idEmpleado.equals(idEmpleadoNuevo)){
						getCorreosSeleccionados().put(idEmpleadoNuevo, true);

					}else{
						getCorreosSeleccionados().put(idEmpleadoNuevo, false);
					}

					//De resto marcamos todos los correos como seleccionados por default
				}else{
					getCorreosSeleccionados().put(idEmpleadoNuevo, true);
				}

				c.moveToNext();
			}
		}
		c.moveToFirst();
	}


	/**
	 * Funcion que se encarga de llenar las filas de la tabla: tlListCorreos dinamicamente.
	 * @param context
	 * @param cursor Contiene los datos de los empleados.
	 */
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
		TextView tv = null;
		CheckBox cb = null;

		String idEmpleadoNuevo = null;
		boolean chequeado = false;

		View mTableRow = null;

		//Para cada valor de la BD solicitado, lo mostramos en el text view.
		while(!cursor.isAfterLast()){

			//obtenemos la referencia a la fila descrita en activity_fila_correo.xml
			mTableRow = (TableRow) View.inflate(context, R.layout.activity_fila_correo, null);

			for (int i=0; i<from.length; i++){

				columna = from[i];
				nombreCol = cursor.getColumnIndex(columna);
				nombre = cursor.getString(nombreCol);

				idEmpleadoNuevo = cursor.getString(cursor.getColumnIndex(Empleado.ID));

				mView = (View) mTableRow.findViewById(to[i]);
//Log.e("correos",getCorreosSeleccionados().toString());
				if(mView instanceof CheckBox){
					cb = (CheckBox) mView;
					cb.setText(nombre);
					chequeado = getCorreosSeleccionados().get(idEmpleadoNuevo);
					cb.setChecked(chequeado);

					final Bundle mArgumentos = new Bundle();
					mArgumentos.putString("idEmpleado", idEmpleadoNuevo);

					cb.setOnCheckedChangeListener(new OnCheckedChangeListener(){
						public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
							mOnCheckedChanged(mArgumentos, isChecked);
						}
					});

				}else if(mView instanceof TextView){
					tv = (TextView) mView;
					tv.setText(nombre);
				}
			}

			//agregamos las filas o TableRows a TableLayout
			tlListCorreos.addView(mTableRow);
			
			//Creamos una linea divisora entre las filas
			View line = new View(context);
			line.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 1));
			line.setBackgroundColor(Color.LTGRAY);
			tlListCorreos.addView(line);

			cursor.moveToNext();
		}
	}


	/**
	 * Funcion que se ejecuta cuando se presiona un check.
	 * @param mArgumentos
	 * @param isChecked
	 */
	protected void mOnCheckedChanged(Bundle mArgumentos, boolean isChecked) {
		getCorreosSeleccionados().remove(mArgumentos.getString("idEmpleado"));
		getCorreosSeleccionados().put(mArgumentos.getString("idEmpleado"), isChecked);

		boolean flagServicio = false;
		boolean flagCorreo = false;
		Tripleta par = null;

		for (Map.Entry<String, Tripleta> entry : ListaServiciosCursorAdapter.getServiciosSeleccionados().entrySet()) {
			par = entry.getValue();
			//si tengo algun servicio seleccionado..
			if( par.getBooleano() == true){
				flagServicio = true;
			}
		}

		for (Map.Entry<String, Boolean> entry : getCorreosSeleccionados().entrySet()) {
			//si tengo algun correo seleccionado..
			if( entry.getValue() == true){
				flagCorreo = true;
			}
		}

		//si flag es falso es porque ningun servicio o correo fue seleccionado.
		if(flagServicio==false || flagCorreo == false){
			bFinalizar.setEnabled(false);
		}else{
			bFinalizar.setEnabled(true);
		}
	}
}

