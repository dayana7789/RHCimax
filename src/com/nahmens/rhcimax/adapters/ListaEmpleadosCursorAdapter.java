package com.nahmens.rhcimax.adapters;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TableLayout.LayoutParams;
import android.support.v4.app.FragmentManager;

import com.nahmens.rhcimax.R;
import com.nahmens.rhcimax.controlador.AplicacionActivity;
import com.nahmens.rhcimax.controlador.DatosClienteActivity;
import com.nahmens.rhcimax.database.modelo.Empleado;


/**
 * Adaptador personalizado para iterar sobre los resultados de la BD,
 * relacionados con la lista de empleados asociados a una empresa.
 * Se utiliza en DatosEmpresaActivity.java
 * 
 * Alternativa para evitar usar ListView dentro de ScrollView.
 */
public class ListaEmpleadosCursorAdapter{

	private int layout;
	private String[] from;
	private int[] to;
	private FragmentManager fragmentManager;
	private TableLayout tlListCorreos;


	public ListaEmpleadosCursorAdapter(TableLayout tlListCorreos, Context context, int layout, Cursor c,
			String[] from, int[] to, int flags, FragmentManager fragmentManager) {

		this.fragmentManager=fragmentManager;
		this.layout = layout;
		this.from = from;
		this.to = to;
		this.tlListCorreos = tlListCorreos;
		
		mbindView(context, c);
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

		int idEmpleado = 0;

		View mTableRow = null;

		//Para cada valor de la BD solicitado, lo mostramos en el text view.
		while(!cursor.isAfterLast()){

			//obtenemos la referencia a la fila descrita en activity_fila_empleado.xml
			mTableRow = (TableRow) View.inflate(context, layout, null);

			for (int i=0; i<from.length; i++){

				columna = from[i];
				nombreCol = cursor.getColumnIndex(columna);
				nombre = cursor.getString(nombreCol);

				idEmpleado = cursor.getInt(cursor.getColumnIndex(Empleado.ID));

				mView = (View) mTableRow.findViewById(to[i]);

				if(mView instanceof TextView){
					tv = (TextView) mView;
					tv.setText(nombre);
				}
			}
			
			ImageButton buttonVerEmpleado = (ImageButton)  mTableRow.findViewById(R.id.imageButtonVerEmpleado);

			//almacenamos en un bundle, id empleado.
			final Bundle mArgumentos = new Bundle();
			mArgumentos.putString("id", ""+idEmpleado);

			buttonVerEmpleado.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v){

					DatosClienteActivity fragment = new DatosClienteActivity();

					//pasamos al fragment el id del empleado
					fragment.setArguments(mArgumentos); 

					fragmentManager.beginTransaction()
					.replace(android.R.id.tabcontent,fragment, AplicacionActivity.tagFragmentDatosCliente)
					.addToBackStack(null)
					.commit();

				}});


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
}

