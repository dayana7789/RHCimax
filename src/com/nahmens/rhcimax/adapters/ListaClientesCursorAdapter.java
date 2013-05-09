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

public class ListaClientesCursorAdapter extends SimpleCursorAdapter{

	private Context context;
	private int layout;
	private String tipoCliente;

	public ListaClientesCursorAdapter(Context context, int layout, Cursor c,
			String[] from, int[] to, int flags, String tipoCliente) {

		super(context, layout, c, from, to, flags);

		this.context = context;
		this.layout = layout;
		this.tipoCliente = tipoCliente;
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {

		Cursor c = getCursor();

		final LayoutInflater inflater = LayoutInflater.from(context);
		View v = inflater.inflate(layout, parent, false);
		
		String columna1 = null;
		String columna2 = null;
		int imagen = 0;
		
		//utilizado para imprimir apellido en caso de empleado
		String nombreIzq11 = "";
		

		if(this.tipoCliente=="empresa"){
			columna1 = Empresa.NOMBRE;
			columna2 = Empresa.TELEFONO;
			imagen = R.drawable.ic_tab_empresa_unselected;
		}else{
			
			String columna11 = null;
			
			columna1 = Empleado.NOMBRE;
			columna2 = Empleado.EMPRESA;
			columna11 = Empleado.APELLIDO;
			imagen = R.drawable.ic_tab_empleado_unselected;
			
			int nombreCol11 = c.getColumnIndex(columna11);
			nombreIzq11 = c.getString(nombreCol11);
		}
		
		int nombreCol1 = c.getColumnIndex(columna1);
		String nombreIzq = c.getString(nombreCol1);

		int nombreCol2 = c.getColumnIndex(columna2);
		String nombreCent = c.getString(nombreCol2);
		
		

		/**
		 * Next set the name of the entry.
		 */    

		TextView nombre_text_izq = (TextView) v.findViewById(R.id.textViewNombreIzq);
		if (nombre_text_izq != null) {
			nombre_text_izq.setText(nombreIzq + " " + nombreIzq11);
		}

		TextView nombre_text_cent = (TextView) v.findViewById(R.id.textViewNombreCent);
		if (nombre_text_cent != null) {
			nombre_text_cent.setText(nombreCent);
		}

		ImageView imgView = (ImageView) v.findViewById(R.id.imageViewTipoCliente);
		if (imgView != null) {
			imgView.setBackgroundResource(imagen);
		}

		return v;
	}

	@Override
	public void bindView(View v, Context context, Cursor c) {

		String columna1 = null;
		String columna2 = null;
		int imagen = 0;
		
		//utilizado para imprimir apellido en caso de empleado
		String nombreIzq11 = "";
		

		if(this.tipoCliente=="empresa"){
			columna1 = Empresa.NOMBRE;
			columna2 = Empresa.TELEFONO;
			imagen = R.drawable.ic_tab_empresa_unselected;
		}else{
			
			String columna11 = null;
			
			columna1 = Empleado.NOMBRE;
			columna2 = Empleado.EMPRESA;
			columna11 = Empleado.APELLIDO;
			imagen = R.drawable.ic_tab_empleado_unselected;
			
			int nombreCol11 = c.getColumnIndex(columna11);
			nombreIzq11 = c.getString(nombreCol11);
		}
		
		int nombreCol1 = c.getColumnIndex(columna1);
		String nombreIzq = c.getString(nombreCol1);

		int nombreCol2 = c.getColumnIndex(columna2);
		String nombreCent = c.getString(nombreCol2);
		
		

		/**
		 * Next set the name of the entry.
		 */    

		TextView nombre_text_izq = (TextView) v.findViewById(R.id.textViewNombreIzq);
		if (nombre_text_izq != null) {
			nombre_text_izq.setText(nombreIzq + " " + nombreIzq11);
		}

		TextView nombre_text_cent = (TextView) v.findViewById(R.id.textViewNombreCent);
		if (nombre_text_cent != null) {
			nombre_text_cent.setText(nombreCent);
		}

		ImageView imgView = (ImageView) v.findViewById(R.id.imageViewTipoCliente);
		if (imgView != null) {
			imgView.setBackgroundResource(imagen);
		}
	}
}
