package com.nahmens.rhcimax.adapters;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nahmens.rhcimax.R;
import com.nahmens.rhcimax.controlador.AplicacionActivity;
import com.nahmens.rhcimax.database.modelo.Permiso;
import com.nahmens.rhcimax.database.modelo.Tarea;
import com.nahmens.rhcimax.database.sqliteDAO.TareaSqliteDao;
import com.nahmens.rhcimax.mensaje.Mensaje;
import com.nahmens.rhcimax.utils.FormatoFecha;
import com.nahmens.rhcimax.utils.SesionUsuario;

public class ListaTareasCursorAdapter extends SimpleCursorAdapter implements Filterable{

	private Context context;
	private int layout;
	private String[] from;
	private int[] to;
	private FragmentManager fragmentManager;
	private HashMap<String,Boolean> arrSincronizados;
	private ArrayList<String> permisos;

	/**
	 * @param tipoCliente Puede ser empleado o empresa. Se utiliza para saber sobre
	 * 					  que tipo de lista estoy iterando.
	 * @param arrSincronizados contiene <idTarea, si esta sincronizado o no> Se utiliza para pintar
	 *                         los cuadros de notificacion principal.
	 */
	public ListaTareasCursorAdapter(Context context, int layout, Cursor c,
			String[] from, int[] to, int flags, FragmentManager fragmentManager, HashMap<String,Boolean> arrSincronizados) {

		super(context, layout, c, from, to, flags);

		this.context = context;
		this.layout = layout;
		this.from = from;
		this.to = to;
		this.fragmentManager=fragmentManager;
		this.arrSincronizados = arrSincronizados;
		this.permisos = SesionUsuario.getPermisos(context);
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

			if(nombre==null){
				nombre = "--";
			}

			if(columna.equals(Tarea.NOMBRE)){
				nombreTarea = nombre;
			}

			if(columna.equals("loginUsuario")){
				nombre = "Creado por: " + nombre;

			}

			if(columna.equals(Tarea.NOMBRE_EMPRESA)){
				nombre = "Empresa: " + nombre;

			}

			if(columna.equals(Tarea.NOMBRE_EMPLEADO)){
				nombreCompleto = nombre;
			}

			if(columna.equals(Tarea.APELLIDO_EMPLEADO)){
				nombreCompleto = nombreCompleto + " " + nombre;
				nombre = "Contacto: "+nombreCompleto;
			}

			if(columna.equals(Tarea.FECHA)){
				fechaCompleta = "Pautado: " +FormatoFecha.darFormatoDateES(nombre);
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

		actualizarCuadrosNotificacionTarea(v, cursor);

		actualizarColorFondo(v, cursor);

		//Si la pantalla esta horizontal, mostramos los botones. 
		//De lo contrario, no mostramos los botones
		int display_mode = context.getResources().getConfiguration().orientation;

		if (display_mode != 1) {

			String id = cursor.getString(cursor.getColumnIndex(Tarea.ID));

			//almacenamos en un bundle, el id de la tarea y nombre de la tarea.
			final Bundle mArgumentos = new Bundle();
			mArgumentos.putString("id", id);
			mArgumentos.putString("nombre", nombreTarea);


			ImageButton buttonSincronizar = (ImageButton)  v.findViewById(R.id.imageButtonSync);
			buttonSincronizar.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v){
					String id = mArgumentos.getString("id");
					sincronizarTarea(id);
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
							String id = mArgumentos.getString("id");
							borrarTarea(id);
						}
					});

					AlertDialog alertDialog = alert.create();
					alertDialog.show();

				}});
			
			
			//si tengo permisos, muestro o no el boton de eliminar
			if(permisos.contains(Permiso.ELIMINAR_TODO)){
				buttonBorrar.setVisibility(View.VISIBLE);
				
			}else if(permisos.contains(Permiso.ELIMINAR_PROPIOS)){
				
				String idUsuarioCreador = cursor.getString(cursor.getColumnIndex("idUsuario"));
				String idUsuarioSesion = SesionUsuario.getIdUsuario(context);			
				
				if(idUsuarioCreador==idUsuarioSesion){
					buttonBorrar.setVisibility(View.VISIBLE);
				}else{
					buttonBorrar.setVisibility(View.GONE);
				}

			}else{
				buttonBorrar.setVisibility(View.GONE);
			}
		}
	}


	/**
	 * Funcion que sincroniza una tarea.
	 * @param id Id de la tarea
	 */
	private void sincronizarTarea(String id) {
		final LayoutInflater inflater = LayoutInflater.from(context);
		Boolean sincronizado =  false;
		Mensaje mToast = null;
		String mensajeError = null;
		String mensajeOk = null;

		TareaSqliteDao tareaDao = new TareaSqliteDao();
		sincronizado = tareaDao.sincronizarTarea(context, id);

		mensajeOk = "ok_sincronizado_tarea";
		mensajeError = "error_sincronizado_tarea";

		if(sincronizado){
			mToast = new Mensaje(inflater, (AplicacionActivity)this.context, mensajeOk);
			
			arrSincronizados.put(id,true);

			//Actualizamos los valores del cursor de la lista de tareas
			if(permisos.contains(Permiso.LISTAR_TODO)){
				this.changeCursor(tareaDao.buscarTareaFilter(context,null, false));
			}else if(permisos.contains(Permiso.LISTAR_PROPIOS)){
				this.changeCursor(tareaDao.buscarTareaFilter(context,null, true));
			}else{
				this.changeCursor(tareaDao.buscarTareaFilter(context,null, false));
			}

			//Notificamos que la lista cambio
			this.notifyDataSetChanged();

		}else{
			arrSincronizados.put(id,false);
			mToast = new Mensaje(inflater, (AplicacionActivity)this.context, mensajeError);
		}

		try {
			mToast.controlMensajesToast();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}


	private void actualizarCuadrosNotificacionTarea(View v, Cursor cursor) {
		String strFechaSincronizacion = cursor.getString(cursor.getColumnIndex(Tarea.FECHA_SINCRONIZACION));
		String strFechaModificacion = cursor.getString(cursor.getColumnIndex(Tarea.FECHA_MODIFICACION));

		TextView tvAvisoRojoFila = (TextView) v.findViewById(R.id.avisoRojoFila);
		TextView tvAvisoVerdeFila = (TextView) v.findViewById(R.id.avisoVerdeFila);

		if(strFechaSincronizacion==null || FormatoFecha.compararDateTimes(strFechaSincronizacion, strFechaModificacion)==1){
			tvAvisoRojoFila.setBackgroundResource(R.drawable.borde_rojo);
			tvAvisoVerdeFila.setBackgroundResource(R.drawable.borde_blanco);
		}else{
			tvAvisoRojoFila.setBackgroundResource(R.drawable.borde_blanco);
			tvAvisoVerdeFila.setBackgroundResource(R.drawable.borde_verde);
		}

	}


	private void actualizarColorFondo(View v, Cursor cursor) {
		LinearLayout llFila = (LinearLayout) v.findViewById(R.id.linearLayoutTarea);
		String fechaTarea = cursor.getString(cursor.getColumnIndex(Tarea.FECHA));

		String fechaActual = FormatoFecha.darFormatoDateUS(new Date());

		int resultado = FormatoFecha.compararDates(fechaTarea, fechaActual);

		if(resultado==1){
			llFila.setBackgroundResource(R.drawable.fondo_gradiente_rojo);

		}else if(resultado==0 || resultado==2){
			llFila.setBackgroundResource(R.drawable.fondo_gradiente_verde);

		}else{
			Log.e("Error","Ha ocurrido un error al cambiar de color el fondo de la fila de tarea");
		}

	}


	/** 
	 * Funcion que elimina de la BD y del list view tareas.
	 * @param id Id de la tarea
	 *
	 */
	private void borrarTarea(String id) {
		final LayoutInflater inflater = LayoutInflater.from(context);
		Boolean eliminado =  false;
		Mensaje mToast = null;
		String mensajeError = null;
		String mensajeOk = null;

		TareaSqliteDao tareaDao = new TareaSqliteDao();
		eliminado = tareaDao.eliminarTarea(this.context, id);
		mensajeOk = "ok_eliminado_tarea";
		mensajeError = "error_eliminado_empresa";

		if(eliminado){
			mToast = new Mensaje(inflater, (AplicacionActivity)this.context, mensajeOk);

			//Actualizamos los valores del cursor de la lista de empleados
			if(permisos.contains(Permiso.LISTAR_TODO)){
				this.changeCursor(tareaDao.buscarTareaFilter(context,null, false));
			}else if(permisos.contains(Permiso.LISTAR_PROPIOS)){
				this.changeCursor(tareaDao.buscarTareaFilter(context,null, true));
			}else{
				this.changeCursor(tareaDao.buscarTareaFilter(context,null, false));
			}

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
		
		if(permisos.contains(Permiso.LISTAR_TODO)){
			filterResultsData = tareaDao.buscarTareaFilter(context, constraint.toString(), false);
		}else if(permisos.contains(Permiso.LISTAR_PROPIOS)){
			filterResultsData = tareaDao.buscarTareaFilter(context, constraint.toString(),true);
		}else{
			filterResultsData = tareaDao.buscarTareaFilter(context, constraint.toString(), false);
		}
		


		return filterResultsData;
	}
}
