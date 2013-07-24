package com.nahmens.rhcimax.controlador;

import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;

import com.nahmens.rhcimax.R;
import com.nahmens.rhcimax.adapters.ListaHistoricosCursorAdapter;
import com.nahmens.rhcimax.database.modelo.Checkin;
import com.nahmens.rhcimax.database.modelo.Cotizacion;
import com.nahmens.rhcimax.database.modelo.Cotizacion_Servicio;
import com.nahmens.rhcimax.database.modelo.Historico;
import com.nahmens.rhcimax.database.modelo.Permiso;
import com.nahmens.rhcimax.database.modelo.Servicio;
import com.nahmens.rhcimax.database.modelo.Tarea;
import com.nahmens.rhcimax.database.sqliteDAO.Cotizacion_ServicioSqliteDao;
import com.nahmens.rhcimax.database.sqliteDAO.HistoricoSqliteDao;
import com.nahmens.rhcimax.utils.FormatoFecha;
import com.nahmens.rhcimax.utils.SesionUsuario;


public class HistoricosActivity extends ListFragment{

	ListaHistoricosCursorAdapter listCursorAdapterHistoricos;
	@SuppressLint("UseSparseArrays")
	HashMap<String,Boolean> arrSincronizados = new HashMap<String, Boolean>(); //Contiene idHistorico y si esta sincronizado o no
	private ArrayList<String> permisos;
	
	//variable que se utiliza para evitar llamar al listener
	//del spinner cuando el usuario no ha seleccionado 
	//explicitamente el spinner
	private boolean mSpinnerBool;

	//Se utiliza para evitar llamar al metodo onChanged del
	//DataSetObserver dos veces. Su valor se inicializa en el 
	//metodo onResume.
	private boolean mObserverBool;

	//Se utiliza para evitar llamar al metodo OnTextChanged del
	//dos veces. Su valor se inicializa en el 
	//metodo onResume.
	private boolean mOnTextChangedBool;

	//Creamos un DataSetObserver para saber cuando el listView de tarea
	//ha sido modificado y lo registramos al adaptor con la funcion 
	//registerDataSetObserver().
	private DataSetObserver observer = new DataSetObserver() {
		public void onChanged(){
			if(mObserverBool){
				cambiarColorCuadroNotificacion(getView());

			}else{
				mObserverBool = true;
			}
		}
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.activity_historicos, container, false);
		permisos = SesionUsuario.getPermisos(getActivity());
		
		if (savedInstanceState==null){
			//Nos aseguramos que no importa desde donde nos llamen, el indicador del 
			//tab es el correspondiente.
			AplicacionActivity.mTabsWidget.setCurrentTab(AplicacionActivity.posicionTagFragmentHistoricos);	

			final Bundle mArgumentos = this.getArguments();
			
			//Asociamos los valores al combo box o spinner
			inicializarSpinner(view);

			HistoricoSqliteDao historicoDao = new HistoricoSqliteDao();

			Cursor mCursorHistoricos = null;
			
			if(permisos.contains(Permiso.LISTAR_TODO)){
				mCursorHistoricos = historicoDao.buscarHistoricoFilter(getActivity(), "Todos", false);
			}else if(permisos.contains(Permiso.LISTAR_PROPIOS)){
				mCursorHistoricos = historicoDao.buscarHistoricoFilter(getActivity(), "Todos", true);
			}else{
				mCursorHistoricos = historicoDao.buscarHistoricoFilter(getActivity(), "Todos", false);
			}
			
			listarHistoricos(view, mCursorHistoricos);

			//Registro del evento addTextChangedListener cuando utilizamos el buscador
			EditText etBuscar = (EditText) view.findViewById(R.id.editTextBuscar);
			etBuscar.addTextChangedListener(new TextWatcher() {

				@Override
				public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
					
					
					//Este if es para que este metodo no se llame automaticamente
					//al iniciar la actividad
					if(mOnTextChangedBool){
						if(listCursorAdapterHistoricos!=null){
							listCursorAdapterHistoricos.getFilter().filter(cs);   
						}
					}else{
						mOnTextChangedBool=true;
					}
				}

				@Override
				public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
						int arg3) {}

				@Override
				public void afterTextChanged(Editable arg0) {}
			});

			

			//Si me pasaron argumentos, filtro la lista. 
			//De lo contrario, listo todo.
			if(mArgumentos!= null){

				String nombreEmpresa = mArgumentos.getString("nombreEmpresa");
				String nombreEmpleado = mArgumentos.getString("nombreEmpleado");

				if(nombreEmpresa!=null){
					//OJO: es importante crear el listener antes de hacer el setText
					//de lo contrario no se llama al metodo onTextChanged automaticamnt
					
					//esta linea es para que se llame onTextChanged dos veces. 
					//recordar q el filtro empieza cuando al menos dos letras son ingresadas
					//en el edit text del buscador.
					etBuscar.setText(""); 
					etBuscar.setText(nombreEmpresa);

				}else if(nombreEmpleado !=null){
					etBuscar.setText("");
					etBuscar.setText(nombreEmpleado);

				}

			}

			setArrSincronizados(mCursorHistoricos);
			cambiarColorCuadroNotificacion(view);


		}
		return view;
	}


	/**
	 * Funcion que inicializa el arreglo de sincronizados
	 * a ser utilizado para determinar el color de los
	 * cuadro de notificacion principal 
	 * @param mCursorHistoricos
	 */
	private void setArrSincronizados(Cursor mCursorHistoricos) {

		int strSincronizado = 0;

		String id = null;

		if (mCursorHistoricos != null) {
			mCursorHistoricos.moveToFirst();
		}

		while(!mCursorHistoricos.isAfterLast()){
			id =  mCursorHistoricos.getString(mCursorHistoricos.getColumnIndex("historicoId"));
			strSincronizado = mCursorHistoricos.getInt(mCursorHistoricos.getColumnIndex(Historico.SINCRONIZADO));

			if(strSincronizado == 0){
				arrSincronizados.put(id,false);
			}else{
				arrSincronizados.put(id, true);
			}

			mCursorHistoricos.moveToNext();
		}

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

		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long arg3) {

				if(mSpinnerBool){
					String valor = (String)parent.getItemAtPosition(position);

					if(listCursorAdapterHistoricos!=null){

						listCursorAdapterHistoricos.getFilter().filter(valor);   
					}
				}else{
					mSpinnerBool=true;
				}

			}

			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});

	}

	//Se utiliza este metodo para que cuando le de 
	//al back button estas variables se inicialicen
	//y puedan funcionar correctamente.
	@Override
	public void onResume() {
		super.onResume();
		mSpinnerBool=false;
		mObserverBool=false;
		mOnTextChangedBool = false;
	}

	private void listarHistoricos(View view, Cursor mCursorHistoricos) {

		if(mCursorHistoricos.getCount()>0){

			final ListView lvHistoricos = (ListView) view.findViewById (android.R.id.list);
			//indicamos los campos que queremos mostrar (from) y en donde (to)
			String[] fromCotizacion = new String[] {Cotizacion.NUM_COTIZACION, "loginUsuario", "nombreEmpresaCotizacion", "nombreEmpleadoCotizacion", "apellidoEmpleadoCotizacion", Cotizacion.FECHA_ENVIO, Cotizacion.FECHA_LEIDO};
			int[] toCotizacion = new int[] { R.id.textViewTitulo,  R.id.textViewUsuario,  R.id.textViewFil1Col1, R.id.textViewFil2Col1, R.id.textViewFil2Col1, R.id.textViewFil1Col2, R.id.textViewFil2Col2};

			String[] fromTarea = new String[] { "nombreTarea", "loginUsuarioTarea", Tarea.FECHA, Tarea.HORA, "nombreEmpleadoTarea", "apellidoEmpleadoTarea", "nombreEmpresaTarea", Tarea.FECHA_FINALIZACION};
			int[] toTarea = new int[] {R.id.textViewTitulo,  R.id.textViewUsuario, R.id.textViewFil1Col2, R.id.textViewFil1Col2, R.id.textViewFil2Col1, R.id.textViewFil2Col1, R.id.textViewFil1Col1, R.id.textViewFil2Col2 };

			String[] fromVisita = new String[] { "nombreEmpresaVisita",  "loginUsuarioVisita", Checkin.CHECKIN, Checkin.CHECKOUT};
			int[] toVisita = new int[] {R.id.textViewTitulo,  R.id.textViewUsuario, R.id.textViewFil1Col1, R.id.textViewFil2Col1};


			//Creamos un array adapter para desplegar cada una de las filas
			listCursorAdapterHistoricos = new ListaHistoricosCursorAdapter(getActivity(), R.layout.activity_fila_historico, mCursorHistoricos, fromCotizacion, toCotizacion, 0, fromTarea, toTarea, fromVisita, toVisita, arrSincronizados);
			lvHistoricos.setAdapter(listCursorAdapterHistoricos);

			//registramos el DataSetObserver al adaptador
			listCursorAdapterHistoricos.registerDataSetObserver(observer);


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

			String numCotizacion  = cursor.getString(cursor.getColumnIndex(Cotizacion.NUM_COTIZACION));
			String idCotizacion =  cursor.getString(cursor.getColumnIndex("cotizacionId"));
			titulo = "Número "+ numCotizacion;
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
			fecha1 = FormatoFecha.darFormatoDateES(cursor.getString(cursor.getColumnIndex(Tarea.FECHA))) + " " + cursor.getString(cursor.getColumnIndex(Tarea.HORA));
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

			tvFechaCreacion.setText(FormatoFecha.darFormatoDateTimeES(fechaCreacion));
		}else{
			tvFechaCreacion.setText("--");
		}

		if(fecha1!=null){
			//este try catch es para capturar la excepcion q se produce
			//por el formato de fecha de la tarea que tiene la hora
			//concatenada
			try{
				tvFecha1.setText(FormatoFecha.darFormatoDateTimeES(fecha1));
			}catch (Exception e) {
				tvFecha1.setText(fecha1);
			}
		}else{
			tvFecha1.setText("--");
		}

		if(fecha2!=null){
			tvFecha2.setText(FormatoFecha.darFormatoDateTimeES(fecha2));
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

	/**
	 * Funcion encargada de modificar los colores de los cuadros de notificacion principal.
	 * @param v
	 */
	private void cambiarColorCuadroNotificacion(View v) {

		TextView tvVerde = (TextView) v.findViewById(R.id.avisoVerde);
		TextView tvRojo = (TextView) v.findViewById(R.id.avisoRojo);


		//pintamos..
		if(arrSincronizados.containsValue(true) && arrSincronizados.containsValue(false)){
			tvRojo.setBackgroundResource(R.drawable.borde_rojo);
			tvVerde.setBackgroundResource(R.drawable.borde_blanco);

		}else if(arrSincronizados.containsValue(true)){
			tvRojo.setBackgroundResource(R.drawable.borde_blanco);
			tvVerde.setBackgroundResource(R.drawable.borde_verde);

		}else if(arrSincronizados.containsValue(false)){
			tvRojo.setBackgroundResource(R.drawable.borde_rojo);
			tvVerde.setBackgroundResource(R.drawable.borde_blanco);
		}
	}

}
