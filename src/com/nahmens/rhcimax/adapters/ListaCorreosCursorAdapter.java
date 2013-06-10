package com.nahmens.rhcimax.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.nahmens.rhcimax.database.modelo.Empleado;


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


	public ListaCorreosCursorAdapter(Context context, int layout, Cursor c,
			String[] from, int[] to, int flags, String idEmpleado) {

		super(context, layout, c, from, to, flags);

		this.layout = layout;
		this.from = from;
		this.to = to;
		this.idEmpleado = idEmpleado;

	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {

		final LayoutInflater inflater = LayoutInflater.from(context);
		View v = inflater.inflate(layout, parent, false);

		return v;
	}

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

		//Nombre del checkbox en nuestro Layout.
		CheckBox checkbox = null;
		
		String correo=null;

		//Para cada valor de la BD solicitado, lo mostramos en el text view.
		for (int i=0; i<from.length; i++){
			columna = from[i];
			nombreCol = cursor.getColumnIndex(columna);
			nombre = cursor.getString(nombreCol);

			if(v.findViewById(to[i]) instanceof CheckBox){
				correo = nombre;
				nombreCol = cursor.getColumnIndex(Empleado.ID);
				nombre = cursor.getString(nombreCol);
				checkbox = (CheckBox) v.findViewById(to[i]);
				
				//Aqui sabemos que solo debemos marcar el correo de un solo empleado 
				//y no de todos los asociados a una empresa.
				if(idEmpleado!=null){

					if(checkbox != null){
						checkbox.setText(correo);

						if(nombre.equals(idEmpleado)){
							if (checkbox != null) {
								checkbox.setChecked(true);
							}

						}else{
							if (checkbox != null) {
								checkbox.setChecked(false);
							}
						}
					}
				}else{
					if(checkbox != null){
						checkbox.setText(correo);
					}
				}

			}else{
				nombre_text = (TextView) v.findViewById(to[i]);
				if (nombre_text != null) {
					nombre_text.setText(nombre);
				}
			}
		}
	}
}

