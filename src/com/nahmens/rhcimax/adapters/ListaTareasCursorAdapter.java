package com.nahmens.rhcimax.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.TextView;

import com.nahmens.rhcimax.R;
import com.nahmens.rhcimax.controlador.AplicacionActivity;
import com.nahmens.rhcimax.controlador.DatosTareaActivity;
import com.nahmens.rhcimax.database.modelo.Tarea;
import com.nahmens.rhcimax.database.sqliteDAO.TareaSqliteDao;
import com.nahmens.rhcimax.mensaje.Mensaje;

public class ListaTareasCursorAdapter extends SimpleCursorAdapter implements Filterable{

	private Context context;
	private int layout;
	private String[] from;
	private int[] to;
	private FragmentManager fragmentManager;

	/**
	 * @param tipoCliente Puede ser empleado o empresa. Se utiliza para saber sobre
	 * 					  que tipo de lista estoy iterando.
	 */
	public ListaTareasCursorAdapter(Context context, int layout, Cursor c,
			String[] from, int[] to, int flags, FragmentManager fragmentManager) {

		super(context, layout, c, from, to, flags);

		this.context = context;
		this.layout = layout;
		this.from = from;
		this.to = to;
		this.fragmentManager=fragmentManager;
	}


	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {

		LayoutInflater inflater = LayoutInflater.from(context);
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


		String nombreCompleto = ""; //almacena nombre completo del empleado
		String fechaCompleta = ""; //almacena fecha y hora
		String nombreTarea = ""; //almacena nombre de la tarea

		//Para cada valor de la BD solicitado, lo mostramos en el text view.
		for (int i=0; i<from.length; i++){
			columna = from[i];
			nombreCol = cursor.getColumnIndex(columna);
			nombre = cursor.getString(nombreCol);
			
			Log.e("columna " + columna, "nombre " + nombre);
			if(nombre==null){
				nombre = "--";
			}

			if(columna.equals(Tarea.NOMBRE)){
				nombreTarea = nombre;
			}

			if(columna.equals(Tarea.NOMBRE_EMPLEADO)){
				nombreCompleto = nombre;
			}

			if(columna.equals(Tarea.APELLIDO_EMPLEADO)){
				nombreCompleto = nombreCompleto + " " + nombre;
				nombre = nombreCompleto;
			}

			if(columna.equals(Tarea.FECHA)){
				fechaCompleta = "Pautado: " +nombre;
			}

			if(columna.equals(Tarea.HORA)){
				fechaCompleta = fechaCompleta + " " + nombre;
				nombre = fechaCompleta;
			}


			if(columna.equals(Tarea.FECHA_FINALIZACION)){
				nombre = "Finalizado: " + nombre;
			}

			nombre_text = (TextView) v.findViewById(to[i]);
			if (nombre_text != null) {
				nombre_text.setText(nombre);
			}
		}

		int id = cursor.getInt(cursor.getColumnIndex(Tarea.ID));

		//almacenamos en un bundle, el id de la tarea y nombre de la tarea.
		final Bundle mArgumentos = new Bundle();
		mArgumentos.putInt("id", id);
		mArgumentos.putString("nombre", nombreTarea);


		ImageButton buttonSincronizar = (ImageButton)  v.findViewById(R.id.imageButtonSync);
		buttonSincronizar.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v){
				int id = mArgumentos.getInt("id");
			}

		});

		ImageButton buttonModificar = (ImageButton)  v.findViewById(R.id.imageButtonModificar);
		buttonModificar.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v){
				int id = mArgumentos.getInt("id");
				modificarTarea(id);
			}

		});

		ImageButton buttonBorrar = (ImageButton)  v.findViewById(R.id.imageButtonBorrar);
		buttonBorrar.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v){

				AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
				String[] mensArray = null;
				Mensaje mensajeDialog = null;
				String nombre = mArgumentos.getString("nombre");
				mensajeDialog = new Mensaje("eliminar_tarea");


				try {
					mensArray = mensajeDialog.controlMensajesDialog(nombre);
				} catch (Exception e) {
					e.printStackTrace();
				}

				alert.setMessage(mensArray[0]); 
				alert.setTitle(mensArray[1]); 

				alert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						dialog.cancel();
					}});

				alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						int id = mArgumentos.getInt("id");
						borrarTarea(id);
					}
				});

				AlertDialog alertDialog = alert.create();
				alertDialog.show();

			}});
	}

	protected void modificarTarea(int id) {
		//creamos un bundle para poder enviar al fragment, el id de la tarea
		Bundle arguments = new Bundle();
		arguments.putString("idTarea", ""+id);

		DatosTareaActivity fragment = new DatosTareaActivity();

		//pasamos al fragment el id del empleado
		fragment.setArguments(arguments); 

		fragmentManager.beginTransaction()
		.replace(android.R.id.tabcontent,fragment, AplicacionActivity.tagFragmentDatosTareas)
		.addToBackStack(null)
		.commit();
	}



	/** 
	 * Funcion que elimina de la BD y del list view tareas.
	 * @param id Id de la tarea
	 *
	 */
	private void borrarTarea(int id) {
		final LayoutInflater inflater = LayoutInflater.from(context);
		Boolean eliminado =  false;
		Mensaje mToast = null;
		String mensajeError = null;
		String mensajeOk = null;

		TareaSqliteDao tareaDao = new TareaSqliteDao();
		eliminado = tareaDao.eliminarTarea(this.context, ""+id);
		mensajeOk = "ok_eliminado_tarea";
		mensajeError = "error_eliminado_empresa";

		if(eliminado){
			mToast = new Mensaje(inflater, (AplicacionActivity)this.context, mensajeOk);

			//Actualizamos los valores del cursor de la lista de empleados
			this.changeCursor(tareaDao.listarTareas(context));

			//Notificamos que la lista cambio
			this.notifyDataSetChanged();

		}else{
			mToast = new Mensaje(inflater, (AplicacionActivity)this.context, mensajeError);
		}

		try {
			mToast.controlMensajesToast();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Esta funcion realiza la magia del filtrado!!
	 * Es la unica funcion que se implementa aqui la que se encarga del filtrado.
	 * Nota1: Es propia de la clase Filterable la cual esta clase implementa (implements Filterable).
	 * Nota2: Desde TareasActivity.java es importante el Registro del evento 
	 *        addTextChangedListener cuando utilizamos el buscador y llamar a 
	 *        adaptador.getFilter().filter(cs); dentro del metodo onTextChanged()
	 * Nota3: El ListView que se va a filtrar debe poseer el atributo android:textFilterEnabled="true"
	 */
	public Cursor runQueryOnBackgroundThread(CharSequence constraint) {

		if (getFilterQueryProvider() != null){ 
			return getFilterQueryProvider().runQuery(constraint); 
		}

		Cursor filterResultsData = null;

		TareaSqliteDao tareaDao = new TareaSqliteDao();
		filterResultsData = tareaDao.buscarTareaFilter(context, constraint.toString());


		return filterResultsData;
	}
}
