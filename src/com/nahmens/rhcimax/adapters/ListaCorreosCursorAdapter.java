package com.nahmens.rhcimax.adapters;

import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.nahmens.rhcimax.R;
import com.nahmens.rhcimax.database.modelo.Empleado;
import com.nahmens.rhcimax.utils.Tripleta;



/**
 * Adaptador personalizado para iterar sobre los resultados de la BD,
 * relacionados con la lista de correos de empleados asociados a una empresa.
 * Se utiliza en la pagina de servicios y se implementa para saber cuales
 * checkboxes de la lista de correos se marcan por default.
 */
public class ListaCorreosCursorAdapter extends SimpleCursorAdapter{

	private int layout;
	private String[] from;
	private int[] to;
	private String idEmpleado;
	private static HashMap<Integer, Boolean> correosSeleccionados; //Almacena los checkboxes que fueron seleccionados <idEmpleado, booleano>
	private Button bFinalizar; //referencia al boton finalizar
	 
	
	public static HashMap<Integer,Boolean> getCorreosSeleccionados(){
		return correosSeleccionados;
	}


	public static void setCorreosSeleccionados( HashMap<Integer,Boolean> nuevo){
		correosSeleccionados = nuevo;
	}

	public ListaCorreosCursorAdapter(Context context, int layout, Cursor c,
			String[] from, int[] to, int flags, String idEmpleado, Button bFinalizar) {

		super(context, layout, c, from, to, flags);

		this.layout = layout;
		this.from = from;
		this.to = to;
		this.idEmpleado = idEmpleado;
		this.bFinalizar = bFinalizar;

		inicializarCorreosSeleccionados(c);

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
			setCorreosSeleccionados(new HashMap<Integer, Boolean>());

			while (!c.isAfterLast()) {
				int idEmpleadoNuevo = c.getInt(c.getColumnIndex(Empleado.ID));

				//Si se nos indica el idEmpleado, quiere decir que solo el correo
				//de esta persona sera checkeado por default
				if(idEmpleado!=null){

					if(idEmpleado.equals(""+idEmpleadoNuevo)){
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
	}


	//Clase que permite guardar referencia de los childs en el layout
	private class ViewHolder {
		CheckBox cbCorreo;
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {

		final LayoutInflater inflater = LayoutInflater.from(context);
		View v = inflater.inflate(layout, parent, false);

		//Este holder se encarga de guardar las referencias a los elementos de las filas
		//de manera de que estas no se calculen cada vez que se entra en el bind view.
		//(operacion costosa en android)
		ViewHolder holder = null;
		holder = new ViewHolder();
		holder.cbCorreo = (CheckBox) v.findViewById(R.id.checkBoxServEmail);
		v.setTag(holder);

		return v;
	}

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

		//Para cada valor de la BD solicitado, lo mostramos en el text view.
		for (int i=0; i<from.length; i++){
			columna = from[i];
			nombreCol = cursor.getColumnIndex(columna);
			nombre = cursor.getString(nombreCol);
			nombre_text = (TextView) v.findViewById(to[i]);
			
			if (nombre_text != null) {
				nombre_text.setText(nombre);
			}
		}

		int idEmpleado = cursor.getInt(cursor.getColumnIndex(Empleado.ID));

		//almacenamos en un bundle id del usuario.
		final Bundle mArgumentos = new Bundle();
		mArgumentos.putInt("idEmpleado", idEmpleado);

		CheckBox cb = ( CheckBox ) holder.cbCorreo;
		cb.setOnCheckedChangeListener(new OnCheckedChangeListener(){
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				getCorreosSeleccionados().remove(mArgumentos.getInt("idEmpleado"));
				getCorreosSeleccionados().put(mArgumentos.getInt("idEmpleado"), isChecked);
				
				boolean flagServicio = false;
				boolean flagCorreo = false;
				Tripleta par = null;
				
				for (Map.Entry<Integer, Tripleta> entry : ListaServiciosCursorAdapter.getServiciosSeleccionados().entrySet()) {
					par = entry.getValue();
					//si tengo algun servicio seleccionado..
					if( par.getBooleano() == true){
						flagServicio = true;
					}
				}
				
				for (Map.Entry<Integer, Boolean> entry : getCorreosSeleccionados().entrySet()) {
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
		});

		//Esta linea de codigo es importante para evitar que se pierdan los checkboxes seleccionados
		//cuando hacemos scroll de la lista. De aqui la importancia del setOnCheckedChangeListener
		//y la lista correosSeleccionados.
		cb.setChecked(getCorreosSeleccionados().get(idEmpleado));
	}
}

