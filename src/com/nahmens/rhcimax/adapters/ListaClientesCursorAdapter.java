package com.nahmens.rhcimax.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nahmens.rhcimax.R;
import com.nahmens.rhcimax.database.modelo.Empleado;
import com.nahmens.rhcimax.database.modelo.Empresa;

/*
 * Adaptador personalizado para iterar sobre los resultados de la BD,
 * relacionados con la lista de clientes.
 */
public class ListaClientesCursorAdapter extends SimpleCursorAdapter{

	private Context context;
	private int layout;
	private String tipoCliente;
	private String[] from;
	private int[] to;

	/*
	 * @param tipoCliente Puede ser empleado o empresa. Se utiliza para saber sobre
	 * 					  que tipo de lista estoy iterando.
	 */
	public ListaClientesCursorAdapter(Context context, int layout, Cursor c,
			String[] from, int[] to, int flags, String tipoCliente) {

		super(context, layout, c, from, to, flags);

		this.context = context;
		this.layout = layout;
		this.tipoCliente = tipoCliente;
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

		int imagen = 0 ;

		//Especificamos la imagen segun corresponda: empleado o empresa.
		if(this.tipoCliente=="empresa"){
			imagen = R.drawable.ic_tab_empresa_unselected;
		}else{
			imagen = R.drawable.ic_tab_empleado_unselected;
		}

		ImageView imgView = (ImageView) v.findViewById(R.id.imageViewTipoCliente);

		if (imgView != null) {
			imgView.setBackgroundResource(imagen);
		}

		return v;
	}

}
