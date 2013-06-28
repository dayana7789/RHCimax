package com.nahmens.rhcimax.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nahmens.rhcimax.R;
import com.nahmens.rhcimax.database.modelo.Cotizacion;
import com.nahmens.rhcimax.database.modelo.Historico;
import com.nahmens.rhcimax.database.modelo.Tarea;
import com.nahmens.rhcimax.database.sqliteDAO.TareaSqliteDao;

public class ListaHistoricosCursorAdapter extends SimpleCursorAdapter implements Filterable{

	private Context context;
	private int layout;
	private String[] fromCotizacion;
	private int[] toCotizacion;
	private String[] fromTarea;
	private int[] toTarea;


	/**
	 * @param tipoCliente Puede ser empleado o empresa. Se utiliza para saber sobre
	 * 					  que tipo de lista estoy iterando.
	 */
	public ListaHistoricosCursorAdapter(Context context, int layout, Cursor c,
			String[] fromCotizacion, int[] toCotizacion, int flags, String[] fromTarea, int[] toTarea) {

		super(context, layout, c, fromCotizacion, toCotizacion, flags);

		this.context = context;
		this.layout = layout;
		this.fromCotizacion = fromCotizacion;
		this.toCotizacion = toCotizacion;
		this.fromTarea = fromTarea;
		this.toTarea = toTarea;

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

		String tipoRegistro = cursor.getString(cursor.getColumnIndex(Historico.TIPO_REGISTRO));

		TextView campoLetra = (TextView) v.findViewById(R.id.textViewLetra);
		TextView campoExtra = (TextView) v.findViewById(R.id.textViewExtra);
		LinearLayout lnCuadrosNotif = (LinearLayout) v.findViewById(R.id.cuadrosNotificacion);
		ImageView ivCorreo = (ImageView) v.findViewById(R.id.imageViewCorreo);
		
		
		//Para cada valor de la BD solicitado, lo mostramos en el text view.
		for (int i=0; i<fromCotizacion.length; i++){
			
			if (tipoRegistro.equals("cotizacion")){
				
				columna = fromCotizacion[i];
				campoLetra.setText("C");
				campoExtra.setVisibility(View.VISIBLE);
				lnCuadrosNotif.setVisibility(View.GONE);
				ivCorreo.setVisibility(View.VISIBLE);
				
			}else if(tipoRegistro.equals("tarea")){
				
				columna = fromTarea[i];
				campoLetra.setText("T");
				campoExtra.setVisibility(View.GONE);
				lnCuadrosNotif.setVisibility(View.VISIBLE);
				ivCorreo.setVisibility(View.GONE);
			}
			
			
			nombreCol = cursor.getColumnIndex(columna);
			nombre = cursor.getString(nombreCol);

			if(nombre==null){
				nombre = "--";
			}

			if(columna.equals("loginUsuario")){
				nombre = "Enviado por: " + nombre;
			}else if(columna.equals("nombreTarea")){
				nombre = "Tarea: " + nombre;
			}else if(columna.equals("idCotizacion")){
				nombre = "Cotización número: " + nombre;
			}else if(columna.equals(Cotizacion.FECHA_ENVIO)){
				nombre = "Enviado: " + nombre;
			}else if(columna.equals(Cotizacion.FECHA_LEIDO)){
				nombre = "Leído: " + nombre;
			}else if(columna.equals("nombreEmpresaCotizacion") || columna.equals("nombreEmpresaTarea")){
				nombre = "Empresa: " + nombre;
			}else if(columna.equals("nombreEmpleadoCotizacion") || columna.equals("nombreEmpleadoTarea")){
				nombreCompleto = nombre;
			}else if(columna.equals("apellidoEmpleadoCotizacion")|| columna.equals("apellidoEmpleadoTarea")){
				nombreCompleto = nombreCompleto + " " + nombre;
				nombre = "Contacto: "+nombreCompleto;
			}else if(columna.equals(Tarea.FECHA)){
				fechaCompleta = "Pautado: " +nombre;
			}else if(columna.equals(Tarea.HORA)){
				fechaCompleta = fechaCompleta + " " + nombre;
				nombre = fechaCompleta;
			}else if(columna.equals(Tarea.FECHA_FINALIZACION)){
				nombre = "Finalizado: " + nombre;
			}

			if (tipoRegistro.equals("cotizacion")){
				nombre_text = (TextView) v.findViewById(toCotizacion[i]);
			}else if(tipoRegistro.equals("tarea")){
				nombre_text = (TextView) v.findViewById(toTarea[i]);
			}
			
			if (nombre_text != null) {
				nombre_text.setText(nombre);
			}
		}

		//Si la pantalla esta horizontal, mostramos los botones. 
		//De lo contrario, no mostramos los botones
		int display_mode = context.getResources().getConfiguration().orientation;

		if (display_mode != 1) {

			//int id = cursor.getInt(cursor.getColumnIndex(Tarea.ID));

			//almacenamos en un bundle, el id de la tarea y nombre de la tarea.
			//final Bundle mArgumentos = new Bundle();
			//mArgumentos.putInt("id", id);
			//mArgumentos.putString("nombre", nombreTarea);


			ImageButton buttonSincronizar = (ImageButton)  v.findViewById(R.id.imageButtonSync);
			buttonSincronizar.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v){
					//int id = mArgumentos.getInt("id");
				}

			});

			
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
