package com.nahmens.rhcimax.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SimpleCursorAdapter;
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

				if(nombre==null){
					nombre = "--";
				}

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
				//sincronizarCliente(id);
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
	 * Funcion que sincroniza a un cliente.
	 * @param Id del empleado o empresa
	 */
	/*private void sincronizarCliente(String id) {
		final LayoutInflater inflater = LayoutInflater.from(context);
		Boolean sincronizado =  false;
		Mensaje mToast = null;
		String mensajeError = null;
		String mensajeOk = null;

		if(tipoCliente.equals("empresa")){
			EmpresaSqliteDao empresaDao = new EmpresaSqliteDao();

			EmpleadoSqliteDao empleadosDao = new EmpleadoSqliteDao();

			Cursor cursorlistEmpl = empleadosDao.listarEmpleadosPorEmpresa(context, ""+id);
			ArrayList<Boolean> arr = new ArrayList<Boolean>();

			if (cursorlistEmpl != null) {
				cursorlistEmpl.moveToFirst();
			}

			while(!cursorlistEmpl.isAfterLast()){

				if(cursorlistEmpl.getString(cursorlistEmpl.getColumnIndex(Empleado.FECHA_SINCRONIZACION)) == null){
					arr.add(false);
				}else{
					arr.add(true);
				}

				cursorlistEmpl.moveToNext();
			}

			if(arr.contains(true) && arr.contains(false)){
				sincronizado = empresaDao.sincronizarEmpresa(context, ""+id, true);

			}else if(arr.contains(true)){
				sincronizado = empresaDao.sincronizarEmpresa(context, ""+id, false);


			}else if(arr.contains(false)){
				sincronizado = empresaDao.sincronizarEmpresa(context, ""+id, null);
			}

			empresaDao.setModificado(context, id, false);

			//sincronizamos los empleados asociados a la empresa
			//EmpleadoSqliteDao empleadoDao = new EmpleadoSqliteDao();
			//empleadoDao.sincronizarEmpleados(this.context, id);

			mensajeOk = "ok_sincronizado_empresa";
			mensajeError = "error_sincronizado_empresa";

		}else if(tipoCliente.equals("empleado")){
			EmpleadoSqliteDao empleadoDao = new EmpleadoSqliteDao();
			sincronizado = empleadoDao.sincronizarEmpleado(this.context, id);
			Empleado emp = empleadoDao.buscarEmpleado(context, id);




			EmpresaSqliteDao empresaDao = new EmpresaSqliteDao();
			EmpleadoSqliteDao empleadosDao = new EmpleadoSqliteDao();

			Cursor cursorlistEmpl = empleadosDao.listarEmpleadosPorEmpresa(context, ""+emp.getIdEmpresa());
			ArrayList<Boolean> arr = new ArrayList<Boolean>();

			if (cursorlistEmpl != null) {
				cursorlistEmpl.moveToFirst();
			}

			while(!cursorlistEmpl.isAfterLast()){

				if(cursorlistEmpl.getString(cursorlistEmpl.getColumnIndex(Empleado.FECHA_SINCRONIZACION)) == null){
					arr.add(false);
				}else{
					arr.add(true);
				}

				cursorlistEmpl.moveToNext();
			}

			if(arr.contains(true) && arr.contains(false)){
				Log.e("####", "amarillo");
				empresaDao.sincronizarEmpresa(context, ""+id, true);

			}else if(arr.contains(true)){

				Log.e("####", "verde");
				empresaDao.sincronizarEmpresa(context, ""+id, false);


			}else if(arr.contains(false)){
				Log.e("####", "rojo");
				empresaDao.sincronizarEmpresa(context, ""+id, null);
			}





			mensajeOk = "ok_sincronizado_empleado";
			mensajeError = "error_sincronizado_empleado";

		}else{
			Log.e("ListaClientesCursorAdapter","tipoCliente no soportado en funcion sincronizarCliente: " + tipoCliente);
		}

		if(sincronizado){
			mToast = new Mensaje(inflater, (AplicacionActivity)this.context, mensajeOk);

			if(tipoCliente.equals("empresa")){
				//Actualizamos los valores del cursor de la lista de empresas
				EmpresaSqliteDao empresaDao = new EmpresaSqliteDao();
				this.changeCursor(empresaDao.listarEmpresas(context));

				//Cuando actualizamos a una empresa, actualizamos tambien a sus empleados
				//Actualizamos los valores del cursor de la lista de empleados
				if(ClientesActivity.listCursorAdapterEmpleados!=null){
					EmpleadoSqliteDao empleadoDao = new EmpleadoSqliteDao();
					ClientesActivity.listCursorAdapterEmpleados.changeCursor(empleadoDao.listarEmpleados(context));
				}

			}else if(tipoCliente.equals("empleado")){
				//Actualizamos los valores del cursor de la lista de empleados
				EmpleadoSqliteDao empleadoDao = new EmpleadoSqliteDao();
				this.changeCursor(empleadoDao.listarEmpleados(context));

				//Cuando actualizamos a una empleado, actualizamos tambien el status d la empresa
				//Actualizamos los valores del cursor de la lista de empresas
				if(ClientesActivity.listCursorAdapterEmpresas!=null){
					EmpresaSqliteDao empresaDao = new EmpresaSqliteDao();
					ClientesActivity.listCursorAdapterEmpresas.changeCursor(empresaDao.listarEmpresas(context));
					//	this.clienteAdapter.getCursor()
				}else{
					Log.e("no entre","no entre");
				}
			}

			Log.e("primero?","primero?");

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
	 * Actualiza los colores de los cuadros de notificación según la sincronización del empleado.
	 * @param v
	 * @param cursor
	 */
	/*private void actualizarCuadrosNotificacionEmpleado(View v, Cursor cursor) {
		String strFechaSincronizacion = cursor.getString(cursor.getColumnIndex(Empleado.FECHA_SINCRONIZACION));

		TextView tvAvisoRojoFila = (TextView) v.findViewById(R.id.avisoRojoFila);
		TextView tvAvisoVerdeFila = (TextView) v.findViewById(R.id.avisoVerdeFila);

		Date fechaSincronizacion = null;
		SimpleDateFormat formatoDelTexto = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

		try {

			if(strFechaSincronizacion != null){
				fechaSincronizacion = formatoDelTexto.parse(strFechaSincronizacion);
			}

			if(fechaSincronizacion==null){
				tvAvisoRojoFila.setBackgroundResource(R.drawable.borde_rojo);
				tvAvisoVerdeFila.setBackgroundResource(R.drawable.borde_blanco);
			}else{
				tvAvisoRojoFila.setBackgroundResource(R.drawable.borde_blanco);
				tvAvisoVerdeFila.setBackgroundResource(R.drawable.borde_verde);
			}

		} catch (ParseException ex) {
			ex.printStackTrace();
		}


		//solo para imprimir
		String strFechaCreacion = cursor.getString(cursor.getColumnIndex(Empleado.FECHA_CREACION));
		Date now = new Date();


		Log.d("Lista clientes", "tipoCliente: "+ tipoCliente+ ", Nombre: " +  cursor.getString(cursor.getColumnIndex("nombre")) 
				+ ", idUsuario: " + cursor.getInt(cursor.getColumnIndex("idUsuario")) 
				+ ", fechaCreacion: " + strFechaCreacion
				+ ", fechaSincronizacion: " + strFechaSincronizacion
				+ ", now: " + formatoDelTexto.format(now));

	}

	/**
	 * Actualiza los colores de los cuadros de notificación según la sincronización de la empresa.
	 * @param v
	 * @param cursor
	 */
	/*private void actualizarCuadrosNotificacionEmpresa(View v, Cursor cursor) {

		String strFechaSincronizacion = cursor.getString(cursor.getColumnIndex(Empresa.FECHA_SINCRONIZACION));
		int modificado =  cursor.getInt(cursor.getColumnIndex(Empresa.MODIFICADO));

		TextView tvAvisoRojoFila = (TextView) v.findViewById(R.id.avisoRojoFila);
		TextView tvAvisoVerdeFila = (TextView) v.findViewById(R.id.avisoVerdeFila);
		TextView tvAvisoAmarilloFila = (TextView) v.findViewById(R.id.avisoAmarilloFila);

		int idEmpresa =  cursor.getInt(cursor.getColumnIndex(Empresa.ID));
		EmpleadoSqliteDao empleadosDao = new EmpleadoSqliteDao();
		Cursor cursorlistEmpl = empleadosDao.listarEmpleadosPorEmpresa(context, ""+idEmpresa);
		ArrayList<Boolean> arr = new ArrayList<Boolean>();

		if (cursorlistEmpl != null) {
			cursorlistEmpl.moveToFirst();
		}

		while(!cursorlistEmpl.isAfterLast()){

			if(cursorlistEmpl.getString(cursorlistEmpl.getColumnIndex(Empleado.FECHA_SINCRONIZACION)) == null){
				arr.add(false);
			}else{
				arr.add(true);
			}

			cursorlistEmpl.moveToNext();
		}

		if(arr.contains(true) && arr.contains(false)){
			tvAvisoAmarilloFila.setBackgroundResource(R.drawable.borde_amarillo);
			tvAvisoRojoFila.setBackgroundResource(R.drawable.borde_blanco);
			tvAvisoVerdeFila.setBackgroundResource(R.drawable.borde_blanco);

			EmpresaSqliteDao empresaDao = new EmpresaSqliteDao();
			empresaDao.sincronizarEmpresa(context, ""+idEmpresa, true);

		}else if(arr.contains(true)){

			if(modificado == 1){
				//Si la empresa SI fue modificada y mis empleados SI estan sincronizados..
				tvAvisoAmarilloFila.setBackgroundResource(R.drawable.borde_amarillo);
				tvAvisoRojoFila.setBackgroundResource(R.drawable.borde_blanco);
				tvAvisoVerdeFila.setBackgroundResource(R.drawable.borde_blanco);

				EmpresaSqliteDao empresaDao = new EmpresaSqliteDao();
				empresaDao.sincronizarEmpresa(context, ""+idEmpresa, true);
			}else{

				//Si la empresa NO fue modificada y mis empleados SI estan sincronizados..
				tvAvisoAmarilloFila.setBackgroundResource(R.drawable.borde_blanco);
				tvAvisoRojoFila.setBackgroundResource(R.drawable.borde_blanco);
				tvAvisoVerdeFila.setBackgroundResource(R.drawable.borde_verde);

				EmpresaSqliteDao empresaDao = new EmpresaSqliteDao();
				empresaDao.sincronizarEmpresa(context, ""+idEmpresa, false);
			}

		}else if(arr.contains(false)){

			if(modificado == 0){
				//Si la empresa NO fue modificada y mis empleados NO estan sincronizados..
				tvAvisoAmarilloFila.setBackgroundResource(R.drawable.borde_amarillo);
				tvAvisoRojoFila.setBackgroundResource(R.drawable.borde_blanco);
				tvAvisoVerdeFila.setBackgroundResource(R.drawable.borde_blanco);

				EmpresaSqliteDao empresaDao = new EmpresaSqliteDao();
				empresaDao.sincronizarEmpresa(context, ""+idEmpresa, true);
			}else{

				//Si la empresa SI fue modificada y mis empleados NO estan sincronizados..
				tvAvisoAmarilloFila.setBackgroundResource(R.drawable.borde_blanco);
				tvAvisoRojoFila.setBackgroundResource(R.drawable.borde_rojo);
				tvAvisoVerdeFila.setBackgroundResource(R.drawable.borde_blanco);

				EmpresaSqliteDao empresaDao = new EmpresaSqliteDao();
				empresaDao.sincronizarEmpresa(context, ""+idEmpresa, null);
			}

		}else{
			//caso empresa sin empleados
			if(strFechaSincronizacion==null){
				tvAvisoAmarilloFila.setBackgroundResource(R.drawable.borde_blanco);
				tvAvisoRojoFila.setBackgroundResource(R.drawable.borde_rojo);
				tvAvisoVerdeFila.setBackgroundResource(R.drawable.borde_blanco);
			}else{
				tvAvisoAmarilloFila.setBackgroundResource(R.drawable.borde_blanco);
				tvAvisoRojoFila.setBackgroundResource(R.drawable.borde_blanco);
				tvAvisoVerdeFila.setBackgroundResource(R.drawable.borde_verde);
			}
		}



		//solo para imprimir
		Date now = new Date();
		SimpleDateFormat formatoDelTexto = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date fechaSincronizacion = null;
		String strFechaCreacion = cursor.getString(cursor.getColumnIndex(Empresa.FECHA_CREACION));
		try {


			if(strFechaSincronizacion==null || strFechaSincronizacion.equals("")){

			}else{
				if(strFechaSincronizacion != null){
					fechaSincronizacion = formatoDelTexto.parse(strFechaSincronizacion);
				}
			}

		} catch (ParseException ex) {
			ex.printStackTrace();
		}

		Log.d("Lista clientes", "tipoCliente: "+ tipoCliente+ ", Nombre: " +  cursor.getString(cursor.getColumnIndex("nombre")) 
				+ ", idUsuario: " + cursor.getInt(cursor.getColumnIndex("idUsuario")) 
				+ ", fechaCreacion: " + strFechaCreacion
				+ ", fechaSincronizacion: " + strFechaSincronizacion
				+ ", now: " + formatoDelTexto.format(now));

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
