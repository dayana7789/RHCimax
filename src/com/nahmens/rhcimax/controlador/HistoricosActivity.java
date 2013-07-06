package com.nahmens.rhcimax.controlador;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TableRow.LayoutParams;

import com.nahmens.rhcimax.R;
import com.nahmens.rhcimax.adapters.ListaHistoricosCursorAdapter;
import com.nahmens.rhcimax.database.modelo.Checkin;
import com.nahmens.rhcimax.database.modelo.Cotizacion;
import com.nahmens.rhcimax.database.modelo.Cotizacion_Servicio;
import com.nahmens.rhcimax.database.modelo.Historico;
import com.nahmens.rhcimax.database.modelo.Servicio;
import com.nahmens.rhcimax.database.modelo.Tarea;
import com.nahmens.rhcimax.database.sqliteDAO.Cotizacion_ServicioSqliteDao;
import com.nahmens.rhcimax.database.sqliteDAO.HistoricoSqliteDao;


public class HistoricosActivity extends ListFragment{

	ListaHistoricosCursorAdapter listCursorAdapterHistoricos;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.activity_historicos, container, false);

		//Nos aseguramos que no importa desde donde nos llamen, el indicador del 
		//tab es el correspondiente.
		AplicacionActivity.mTabsWidget.setCurrentTab(AplicacionActivity.posicionTagFragmentHistoricos);	

		//Asociamos los valores al combo box o spinner
		inicializarSpinner(view);

		HistoricoSqliteDao historicoDao = new HistoricoSqliteDao();
		Cursor mCursorHistoricos = null;

		Bundle mArgumentos = this.getArguments();

		//Si me pasaron argumentos, filtro la lista. 
		//De lo contrario, listo todo.
		if(mArgumentos!= null){

			String idEmpresa = mArgumentos.getString("idEmpresa");
			String idEmpleado = mArgumentos.getString("idEmpleado");

			if(idEmpresa!=null){
				mCursorHistoricos = historicoDao.listarHistoricosPorEmpresa(getActivity(), idEmpresa);

			}else if(idEmpleado !=null){
				mCursorHistoricos = historicoDao.listarHistoricosPorEmpleado(getActivity(), idEmpleado);
			}


		}else{
			mCursorHistoricos = historicoDao.listarHistoricos(getActivity());
		}


		listarHistoricos(view, mCursorHistoricos);


		return view;
	}

	/**
	 * Funcion que inicializa los valores del spinner o combo box
	 * @param view
	 */
	private void inicializarSpinner(View view) {
		//Asociamos los valores al combo box o spinner
		Spinner spinner = (Spinner) view.findViewById(R.id.spinnerHistoricos);
		// Create an ArrayAdapter using the string array and a default spinner layout
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getActivity(),
				R.array.historicos_filtro_array, android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		spinner.setAdapter(adapter);

	}

	private void listarHistoricos(View view, Cursor mCursorHistoricos) {

		if(mCursorHistoricos.getCount()>0){

			final ListView lvHistoricos = (ListView) view.findViewById (android.R.id.list);
			//indicamos los campos que queremos mostrar (from) y en donde (to)
			String[] fromCotizacion = new String[] {"cotizacionId", "loginUsuario", "nombreEmpresaCotizacion", "nombreEmpleadoCotizacion", "apellidoEmpleadoCotizacion", Cotizacion.FECHA_ENVIO, Cotizacion.FECHA_LEIDO};
			int[] toCotizacion = new int[] { R.id.textViewTitulo,  R.id.textViewUsuario,  R.id.textViewFil1Col1, R.id.textViewFil2Col1, R.id.textViewFil2Col1, R.id.textViewFil1Col2, R.id.textViewFil2Col2};

			String[] fromTarea = new String[] { "nombreTarea", "loginUsuarioTarea", Tarea.FECHA, Tarea.HORA, "nombreEmpleadoTarea", "apellidoEmpleadoTarea", "nombreEmpresaTarea", Tarea.FECHA_FINALIZACION};
			int[] toTarea = new int[] {R.id.textViewTitulo,  R.id.textViewUsuario, R.id.textViewFil1Col2, R.id.textViewFil1Col2, R.id.textViewFil2Col1, R.id.textViewFil2Col1, R.id.textViewFil1Col1, R.id.textViewFil2Col2 };

			String[] fromVisita = new String[] { "nombreEmpresaVisita",  "loginUsuarioVisita", Checkin.CHECKIN, Checkin.CHECKOUT};
			int[] toVisita = new int[] {R.id.textViewTitulo,  R.id.textViewUsuario, R.id.textViewFil1Col1, R.id.textViewFil2Col1};


			//Creamos un array adapter para desplegar cada una de las filas
			listCursorAdapterHistoricos = new ListaHistoricosCursorAdapter(getActivity(), R.layout.activity_fila_historico, mCursorHistoricos, fromCotizacion, toCotizacion, 0, fromTarea, toTarea, fromVisita, toVisita);
			lvHistoricos.setAdapter(listCursorAdapterHistoricos);


		}

	}

	@Override
	/**
	 * Funcion que se llama cuando una fila es seleccionada.
	 */
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();
		View mLayout = inflater.inflate(R.layout.dialog_historico, null);

		//Filas solo visibles para cotizaciones
		TableRow tbServiciosDest = (TableRow) mLayout.findViewById(R.id.tableRowServiciosDestinatarios);
		HorizontalScrollView hsServicios = (HorizontalScrollView) mLayout.findViewById(R.id.tablaServicios);
		TableLayout tlServicios = (TableLayout) mLayout.findViewById(R.id.tableLayoutServicios);
		TableLayout tlTotal = (TableLayout) mLayout.findViewById(R.id.tableLayoutTotal);
		TextView tvServicioDestinatario = (TextView) mLayout.findViewById(R.id.textViewDestinatarios);

		ImageView ivIcono = (ImageView) mLayout.findViewById(R.id.imageViewIcono);

		TextView tvTipoRegistro = (TextView) mLayout.findViewById(R.id.textViewTipoHistorico);

		TextView tvTitulo = (TextView) mLayout.findViewById(R.id.textViewTitulo);
		TextView tvEmpresa = (TextView) mLayout.findViewById(R.id.textViewEmpresa);
		TextView tvContacto = (TextView) mLayout.findViewById(R.id.textViewContacto);
		TextView tvUsuario = (TextView) mLayout.findViewById(R.id.textViewUsuario);
		TextView tvFechaCreacion = (TextView) mLayout.findViewById(R.id.textViewFechaCreacion);
		TextView tvFecha1 = (TextView) mLayout.findViewById(R.id.textViewFecha1);
		TextView tvFecha2 = (TextView) mLayout.findViewById(R.id.textViewFecha2);
		TextView tvDescripcion = (TextView) mLayout.findViewById(R.id.textViewDescripcion);

		TextView tvEmpresaNombre = (TextView) mLayout.findViewById(R.id.textViewEmpresaNombre);
		TextView tvContactoNombre = (TextView) mLayout.findViewById(R.id.textViewContactoNombre);
		TextView tvUsuarioNombre = (TextView) mLayout.findViewById(R.id.textViewUsuarioNombre);
		TextView tvFechaCreacionNombre = (TextView) mLayout.findViewById(R.id.textViewFechaCreacionNombre);
		TextView tvFecha1Nombre = (TextView) mLayout.findViewById(R.id.textViewFecha1Nombre);
		TextView tvFecha2Nombre = (TextView) mLayout.findViewById(R.id.textViewFecha2Nombre);
		TextView tvDescripcionNombre = (TextView) mLayout.findViewById(R.id.textViewDescripcionNombre);

		//OJO: aqui estamos usando  getListView() porq tenemos un solo ListView
		//pero de tener mas de uno, no debemos usar esto (Ver ClientesActivity.java)
		Cursor cursor = (Cursor) getListView().getItemAtPosition(position);

		String tipoRegistro = cursor.getString(cursor.getColumnIndex(Historico.TIPO_REGISTRO));
		String tipoRegistroNombre = null;
		String titulo = null;
		String empresa = null;
		String contacto = null;
		String usuario = null;
		String fechaCreacion = null;
		String fecha1 = null;
		String fecha2 = null;
		String descripcion = null;

		String empresaNombre = "Empresa ";
		String contactoNombre = "Contacto ";
		String fechaCreacionNombre = "Fecha Creación";
		String usuarioNombre = null;
		String fecha1Nombre = null;
		String fecha2Nombre = null;
		String descripcionNombre = null;
		String destinatario = null;


		if (tipoRegistro.equals("cotizacion")){
			ivIcono.setBackgroundResource(android.R.drawable.sym_action_email);

			tipoRegistroNombre = "Cotización";
			usuarioNombre = "Enviado por ";
			fecha1Nombre = "Enviado ";
			fecha2Nombre = "Leído ";
			descripcionNombre = "Descripción ";

			String idCotizacion = cursor.getString(cursor.getColumnIndex("cotizacionId"));

			titulo = "Número "+ idCotizacion;
			empresa = cursor.getString(cursor.getColumnIndex("nombreEmpresaCotizacion"));
			usuario = cursor.getString(cursor.getColumnIndex("loginUsuario"));
			fechaCreacion = cursor.getString(cursor.getColumnIndex("cotizacionFechaCreacion"));
			fecha1 = cursor.getString(cursor.getColumnIndex(Cotizacion.FECHA_ENVIO));
			fecha2 = cursor.getString(cursor.getColumnIndex(Cotizacion.FECHA_LEIDO));
			descripcion = cursor.getString(cursor.getColumnIndex("cotizacionDescripcion"));
			String nombreContacto = cursor.getString(cursor.getColumnIndex("nombreEmpleadoCotizacion"));
			String apellidoContacto = cursor.getString(cursor.getColumnIndex("apellidoEmpleadoCotizacion"));

			if(nombreContacto==null){
				nombreContacto = "--";
			}

			if(apellidoContacto==null){
				apellidoContacto = "--";
			}

			destinatario =  cursor.getString(cursor.getColumnIndex("emailEmpleadoCotizacion"));
			contacto = nombreContacto+ " " + apellidoContacto;
			listarServicios(tlServicios, tlTotal, idCotizacion);

			//usados en cotizacion
			tbServiciosDest.setVisibility(View.VISIBLE);
			hsServicios.setVisibility(View.VISIBLE);
			tlTotal.setVisibility(View.VISIBLE);

			tvContactoNombre.setVisibility(View.VISIBLE);
			tvContacto.setVisibility(View.VISIBLE);
			tvEmpresaNombre.setVisibility(View.VISIBLE);
			tvEmpresa.setVisibility(View.VISIBLE);
			tvDescripcionNombre.setVisibility(View.VISIBLE);
			tvDescripcion.setVisibility(View.VISIBLE);

		}else if(tipoRegistro.equals("tarea")){
			ivIcono.setBackgroundResource(android.R.drawable.ic_menu_agenda);

			tipoRegistroNombre = "Tarea";
			usuarioNombre = "Creado por ";
			fecha1Nombre = "Pautado ";
			fecha2Nombre = "Finalizado ";
			descripcionNombre = "Descripción ";

			titulo = cursor.getString(cursor.getColumnIndex("nombreTarea"));
			empresa = cursor.getString(cursor.getColumnIndex("nombreEmpresaTarea"));

			String nombreEmpl = cursor.getString(cursor.getColumnIndex("nombreEmpleadoTarea"));
			String apellidoEmpl = cursor.getString(cursor.getColumnIndex("apellidoEmpleadoTarea"));
			if(nombreEmpl==null){
				nombreEmpl = "--";
			}

			if(apellidoEmpl==null){
				apellidoEmpl = "--";
			}
			contacto = nombreEmpl+ " " + apellidoEmpl;
			usuario = cursor.getString(cursor.getColumnIndex("loginUsuarioTarea"));
			fechaCreacion = cursor.getString(cursor.getColumnIndex("tareaFechaCreacion"));
			fecha1 = cursor.getString(cursor.getColumnIndex(Tarea.FECHA)) + " " + cursor.getString(cursor.getColumnIndex(Tarea.HORA));
			fecha2 = cursor.getString(cursor.getColumnIndex(Tarea.FECHA_FINALIZACION));
			descripcion = cursor.getString(cursor.getColumnIndex("tareaDescripcion"));

			//usados en cotizacion
			tbServiciosDest.setVisibility(View.GONE);
			hsServicios.setVisibility(View.GONE);
			tlTotal.setVisibility(View.GONE);

			tvContactoNombre.setVisibility(View.VISIBLE);
			tvContacto.setVisibility(View.VISIBLE);
			tvEmpresaNombre.setVisibility(View.VISIBLE);
			tvEmpresa.setVisibility(View.VISIBLE);
			tvDescripcionNombre.setVisibility(View.VISIBLE);
			tvDescripcion.setVisibility(View.VISIBLE);

		}else if(tipoRegistro.equals("visita")){
			ivIcono.setBackgroundResource(android.R.drawable.ic_menu_compass);

			tipoRegistroNombre = "Visita";
			usuarioNombre = "Vendedor ";
			fecha1Nombre = "Fecha de llegada ";
			fecha2Nombre = "Fecha de retirada ";
			descripcionNombre = "Descripción ";

			titulo = cursor.getString(cursor.getColumnIndex("nombreEmpresaVisita"));
			usuario = cursor.getString(cursor.getColumnIndex("loginUsuarioVisita"));
			fechaCreacion = cursor.getString(cursor.getColumnIndex("historicoFechaCreacion"));
			fecha1 = cursor.getString(cursor.getColumnIndex(Checkin.CHECKIN));
			fecha2 = cursor.getString(cursor.getColumnIndex(Checkin.CHECKOUT));
			descripcion = "--";

			//usados en cotizacion
			tbServiciosDest.setVisibility(View.GONE);
			hsServicios.setVisibility(View.GONE);
			tlTotal.setVisibility(View.GONE);

			tvContactoNombre.setVisibility(View.GONE);
			tvContacto.setVisibility(View.GONE);
			tvEmpresaNombre.setVisibility(View.GONE);
			tvEmpresa.setVisibility(View.GONE);
			tvDescripcionNombre.setVisibility(View.GONE);
			tvDescripcion.setVisibility(View.GONE);
		}

		tvEmpresaNombre.setText(empresaNombre);
		tvContactoNombre.setText(contactoNombre);
		tvUsuarioNombre.setText(usuarioNombre);
		tvFecha1Nombre.setText(fecha1Nombre);
		tvFecha2Nombre.setText(fecha2Nombre);
		tvDescripcionNombre.setText(descripcionNombre);
		tvFechaCreacionNombre.setText(fechaCreacionNombre);

		if(titulo!=null){
			tvTitulo.setText(titulo);
		}else{
			tvTitulo.setText("--");
		}

		if(empresa!=null){
			tvEmpresa.setText(empresa);
		}else{
			tvEmpresa.setText("--");
		}

		if(contacto!=null){
			tvContacto.setText(contacto);
		}else{
			tvContacto.setText("--");
		}

		if(usuario!=null){
			tvUsuario.setText(usuario);
		}else{
			tvUsuario.setText("--");
		}

		if(destinatario!=null){
			tvServicioDestinatario.setText(destinatario);
		}else{
			tvServicioDestinatario.setText("--");
		}

		if(fechaCreacion!=null){

			String v_date_str=fechaCreacion;
			Date v_date=null;
			try {
				v_date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).parse(v_date_str);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			DateFormat formatter = null;

			formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.ENGLISH);

			tvFechaCreacion.setText(formatter.format(v_date));
		}else{
			tvFechaCreacion.setText("--");
		}

		if(fecha1!=null){
			tvFecha1.setText(fecha1);
		}else{
			tvFecha1.setText("--");
		}

		if(fecha2!=null){
			tvFecha2.setText(fecha2);
		}else{
			tvFecha2.setText("--");
		}


		if(descripcion.equals("")){
			tvDescripcion.setText("--");
		}else if(descripcion!=null){
			tvDescripcion.setText(descripcion);
		}


		tvTipoRegistro.setText(tipoRegistroNombre);

		// Inflate and set the layout for the dialog
		// Pass null as the parent view because its going in the dialog layout
		builder.setView(mLayout)
		.setTitle("Detalle")
		.setCancelable(false)
		.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
			}
		});
		AlertDialog alert = builder.create();
		alert.show();

	}

	/**
	 * Funcion que lista los servicios asociados a una cotizacion
	 * y calcula los valores mensual y total
	 * @param tlServicios
	 * @param idCotizacion
	 */
	private void listarServicios(TableLayout tlServicios, TableLayout tlTotal, String idCotizacion) {
		Cotizacion_ServicioSqliteDao cot_servDao = new Cotizacion_ServicioSqliteDao();
		Cursor cServicios = cot_servDao.listarCotizacion_Servicio(getActivity(), idCotizacion);
		double mensual = 0;
		double total = 0;

		while(!cServicios.isAfterLast()){
			String nombreServ = cServicios.getString(cServicios.getColumnIndex(Servicio.NOMBRE));
			String unidadMed =  cServicios.getString(cServicios.getColumnIndex(Servicio.UNIDAD_MEDICION));
			String medida = cServicios.getString(cServicios.getColumnIndex(Cotizacion_Servicio.MEDIDA));
			String descripcionServ = cServicios.getString(cServicios.getColumnIndex(Cotizacion_Servicio.DESCRIPCION));
			double inicial = cServicios.getDouble(cServicios.getColumnIndex(Cotizacion_Servicio.INICIAL));
			double precio = cServicios.getDouble(cServicios.getColumnIndex(Cotizacion_Servicio.PRECIO));

			TableRow tr = new TableRow(getActivity());
			TextView tvNombreServ=new TextView(getActivity());
			tvNombreServ.setPadding(10,10, 10, 10);
			tvNombreServ.setLayoutParams(new LayoutParams(200, LayoutParams.WRAP_CONTENT));
			TextView tvUnidadMed=new TextView(getActivity());
			tvUnidadMed.setPadding(10,10, 10, 10);
			TextView tvMedida=new TextView(getActivity());
			tvMedida.setPadding(10,10, 10, 10);
			TextView tvDescripcionServ=new TextView(getActivity());
			tvDescripcionServ.setPadding(10,10, 10, 10);
			tvDescripcionServ.setLayoutParams(new LayoutParams(200, LayoutParams.WRAP_CONTENT));
			TextView tvInicial=new TextView(getActivity());
			tvInicial.setPadding(10,10, 10, 10);
			TextView tvPrecio=new TextView(getActivity());
			tvPrecio.setPadding(10,10, 10, 10);

			tvNombreServ.setText(nombreServ);
			tvUnidadMed.setText(unidadMed);
			tvMedida.setText(medida);
			tvDescripcionServ.setText(descripcionServ);
			tvInicial.setText(""+inicial);
			tvPrecio.setText(""+precio);

			tr.addView(tvNombreServ);
			tr.addView(tvMedida);
			tr.addView(tvUnidadMed);
			tr.addView(tvDescripcionServ);
			tr.addView(tvInicial);
			tr.addView(tvPrecio);

			if(unidadMed.equals("ninguno")){

				mensual = mensual + precio;
				total = total + inicial;

			}else{
				mensual = mensual + (Double.parseDouble(medida)*precio);
				total = total + (Double.parseDouble(medida)*inicial);
			}

			tlServicios.addView(tr);

			cServicios.moveToNext();
		}

		TextView tvTotal = (TextView) tlTotal.findViewById(R.id.textViewTotal);
		TextView tvMensual = (TextView) tlTotal.findViewById(R.id.textViewMensual);
		total = total + mensual;

		tvTotal.setText(""+total);
		tvMensual.setText(""+mensual);
	}
}
