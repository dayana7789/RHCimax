package com.nahmens.rhcimax.controlador;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;

import com.nahmens.rhcimax.R;
import com.nahmens.rhcimax.adapters.AutocompleteEmpleadoCursorAdapter;
import com.nahmens.rhcimax.adapters.AutocompleteEmpresaCursorAdapter;
import com.nahmens.rhcimax.database.modelo.Empleado;
import com.nahmens.rhcimax.database.modelo.Empresa;
import com.nahmens.rhcimax.database.modelo.Historico;
import com.nahmens.rhcimax.database.modelo.Tarea;
import com.nahmens.rhcimax.database.modelo.Usuario;
import com.nahmens.rhcimax.database.sqliteDAO.EmpleadoSqliteDao;
import com.nahmens.rhcimax.database.sqliteDAO.EmpresaSqliteDao;
import com.nahmens.rhcimax.database.sqliteDAO.HistoricoSqliteDao;
import com.nahmens.rhcimax.database.sqliteDAO.TareaSqliteDao;
import com.nahmens.rhcimax.mensaje.Mensaje;
import com.nahmens.rhcimax.utils.InstantAutoComplete;

public class DatosTareaActivity extends Fragment {

	Calendar myCalendar = Calendar.getInstance();

	//Campos formulario:
	EditText etNombre;
	AutoCompleteTextView etEmpresa;
	EditText etHiddenIdEmpresa;
	InstantAutoComplete etEmpleado;
	EditText etHiddenIdEmpleado;
	Button bFecha;
	Button bHora;
	EditText etDescripcion;
	CheckBox cbFinalizada;

	/* Flag que permite saber si la tarea ya fue 
	 * insertada cuando se presiona el boton salvar dos veces.
	 * Evita que se duplique el registro.
	 */
	@SuppressWarnings("unused")
	private boolean flagGuardado;
	private Bundle mArgumentos;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		final View view = inflater.inflate(R.layout.activity_datos_tarea, container, false);

		//Nos aseguramos que no importa desde donde nos llamen, el indicador del 
		//tab es el correspondiente.
		AplicacionActivity.mTabsWidget.setCurrentTab(AplicacionActivity.posicionTagFragmentTareas);	

		flagGuardado = false;

		//inicializamos la referencia a los campos del formulario
		setReferenciaCampos(view);

		//Creacion de dialog y listener del campo fecha
		inicializarFecha();

		//Creacion de dialog y listener del campo hora
		inicializarHora();

		mArgumentos = this.getArguments();

		//Si me pasaron argumentos, relleno la vista con la informacion. 
		//De lo contrario, dejo todo vacio.
		if(mArgumentos!= null){

			String idTarea = mArgumentos.getString("idTarea");
			TareaSqliteDao tareaDao = new TareaSqliteDao();
			Tarea tarea  = tareaDao.buscarTarea(getActivity(),idTarea);

			if(tarea !=null){

				llenarCamposTarea(tarea);

			}else{
				//Esto nunca deberia llamarse
				Mensaje mToast = new Mensaje("error_general");
				try {
					mToast.controlMensajesToast();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		setAutocompleteEmpresa(view);
		setAutocompleteEmpleado(view);



		// Registro del evento OnClick del buttonVerEmpresa
		ImageButton bVerEmpresa = (ImageButton)view.findViewById(R.id.imageButtonVerEmpresa);
		bVerEmpresa.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				setOnclickVerEmpresa();
			}
		});

		// Registro del evento OnClick del buttonVerEmpleado
		ImageButton bVerEmpleado = (ImageButton)view.findViewById(R.id.imageButtonVerEmpleado);
		bVerEmpleado.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				setOnclickVerEmpleado();
			}
		});

		// Registro del evento OnClick del buttonLimpiar
		Button bLimpiar= (Button)view.findViewById(R.id.buttonLimpiar);
		bLimpiar.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				limpiarFormulario();
			}
		});

		// Registro del evento OnClick del buttonSalvar
		Button bSalvar = (Button)view.findViewById(R.id.buttonSalvar);
		bSalvar.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String idTarea = null;

				if(mArgumentos!=null){
					idTarea = mArgumentos.getString("idTarea");
				}
				onClickSalvar(idTarea);
			}
		});

		return view;
	}


	/**
	 * Funcion que almacena la referencia de los campos de tal manera que estos
	 * sean calculados una sola vez.
	 * @param view
	 */
	private void setReferenciaCampos(View view) {
		etNombre = (EditText) view.findViewById(R.id.editTextNombre);
		etEmpresa = (AutoCompleteTextView) view.findViewById(R.id.autocompleteEmpresa);
		etHiddenIdEmpresa = (EditText) view.findViewById(R.id.textEditHiddenIdEmpresa);
		etEmpleado = (InstantAutoComplete) view.findViewById(R.id.autocompleteEmpleado);
		etHiddenIdEmpleado = (EditText) view.findViewById(R.id.textEditHiddenIdEmpleado);
		bFecha = (Button) view.findViewById(R.id.buttonFecha);
		bHora = (Button) view.findViewById(R.id.buttonHora);
		etDescripcion = (EditText) view.findViewById(R.id.editTextDescripcion);
		cbFinalizada = (CheckBox) view.findViewById(R.id.checkBoxFinalizada);
	}

	/**
	 * Funcion que prepara el campo de empleado para que sea autocomplete.
	 * @param view View de la actividad.
	 */
	private void setAutocompleteEmpleado(View view) {
		AutocompleteEmpleadoCursorAdapter mAutocompleteCursor = new AutocompleteEmpleadoCursorAdapter(view);
		etEmpleado.setAdapter(mAutocompleteCursor);

		//seteamos este listener definido en la clase AutocompleteEmpresaCursorAdapter
		etEmpleado.setOnItemClickListener(mAutocompleteCursor);
		etEmpleado.addTextChangedListener(mAutocompleteCursor);
		//modificamos el siguiente valor para que despliegue lista a partir del ingreso de un caracter
		etEmpleado.setThreshold(1);

	}

	/**
	 * Funcion que prepara el campo de empresa para que sea autocomplete.
	 * @param view View de la actividad.
	 */
	private void setAutocompleteEmpresa(View view) {
		AutocompleteEmpresaCursorAdapter mAutocompleteCursor = new AutocompleteEmpresaCursorAdapter(view);

		etEmpresa.setAdapter(mAutocompleteCursor);
		//seteamos este listener definido en la clase AutocompleteEmpresaCursorAdapter
		etEmpresa.setOnItemClickListener(mAutocompleteCursor);
		etEmpresa.addTextChangedListener(mAutocompleteCursor);
		//modificamos el siguiente valor para que despliegue lista a partir del ingreso de un caracter
		etEmpresa.setThreshold(1);

	}

	/**
	 * Funcion que se llama al hacer click en ver empleado. 
	 * Carga la pagina de datos cliente.
	 */
	protected void setOnclickVerEmpleado() {
		String idEmpleado = etHiddenIdEmpleado.getText().toString();

		if(idEmpleado.equals("") || idEmpleado.equals(null)){
			Mensaje mToast = new Mensaje(getActivity().getLayoutInflater(), getActivity(), "error_empleado_no_existe");

			try {
				mToast.controlMensajesToast();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}else{

			Bundle mArgumentos = new Bundle();
			mArgumentos.putString("id",idEmpleado);

			Fragment fragment = new DatosClienteActivity();

			fragment.setArguments(mArgumentos); 
			FragmentManager manager = getFragmentManager();
			FragmentTransaction ft = manager.beginTransaction();
			ft.addToBackStack(null);
			ft.replace(android.R.id.tabcontent, fragment, AplicacionActivity.tagFragmentDatosCliente);
			ft.commit();
		}

	}

	/**
	 * Funcion que se llama al hacer click en ver empresa. 
	 * Carga la pagina de datos empresa.
	 */
	protected void setOnclickVerEmpresa() {
		String idEmpresa = etHiddenIdEmpresa.getText().toString();

		if(idEmpresa.equals("") || idEmpresa.equals(null)){
			Mensaje mToast = new Mensaje(getActivity().getLayoutInflater(), getActivity(), "error_empresa_no_existe");

			try {
				mToast.controlMensajesToast();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}else{

			Bundle mArgumentos = new Bundle();
			mArgumentos.putString("idEmpresa",idEmpresa);
			Fragment fragment = new DatosEmpresaActivity();
			fragment.setArguments(mArgumentos); 
			FragmentManager manager = getFragmentManager();
			FragmentTransaction ft = manager.beginTransaction();
			ft.addToBackStack(null);
			ft.replace(android.R.id.tabcontent, fragment, AplicacionActivity.tagFragmentDatosEmpresa);
			ft.commit();
		}
	}

	/**
	 * Funcion que se encarga de cargar los datos de una tarea en sus respectivos campos.
	 * @param view
	 * @param tarea Tarea cuya informacion se esta cargando.
	 */
	private void llenarCamposTarea(Tarea tarea) {
		EmpleadoSqliteDao empleadoDao = new EmpleadoSqliteDao();
		Empleado empleado  = empleadoDao.buscarEmpleado(getActivity(),""+tarea.getIdEmpleado());

		EmpresaSqliteDao empresaDao = new EmpresaSqliteDao();
		Empresa empresa = empresaDao.buscarEmpresa(getActivity(), ""+tarea.getIdEmpresa());

		boolean finalizado = false;

		if(tarea.getFechaFinalizacion() == null){
			finalizado = false;
		}

		etNombre.setText(tarea.getNombre());

		if(empresa!=null){
			etEmpresa.setText(empresa.getNombre());
			etHiddenIdEmpresa.setText(tarea.getIdEmpresa()+"");
		}else{
			etEmpresa.setText("");
			etHiddenIdEmpresa.setText("0");
		}

		if(empleado!=null){
			etEmpleado.setText(empleado.getNombre()+" " + empleado.getApellido());
			etHiddenIdEmpleado.setText(tarea.getIdEmpleado()+"");
		}else{
			etEmpleado.setText("");
			etHiddenIdEmpleado.setText("0");
		}


		bFecha.setText(tarea.getFecha());
		bHora.setText(tarea.getHora());
		etDescripcion.setText(tarea.getDescripcion());
		cbFinalizada.setChecked(finalizado);

	}

	/**
	 * Funcion que limpia todos los campos del formulario
	 * @param view
	 */
	protected void limpiarFormulario() {
		etNombre.setText("");
		etEmpresa.setText("");
		etHiddenIdEmpresa.setText("");
		etEmpleado.setText("");
		etHiddenIdEmpleado.setText("");
		etDescripcion.setText("");
		cbFinalizada.setChecked(false);

		setHora(true);
		setFecha(true);
	}

	/**
	 * Funcion que crea dialog, listener y setea los valores de hora
	 * @param view
	 */
	private void inicializarHora() {
		setHora(true);

		final TimePickerDialog.OnTimeSetListener time = new TimePickerDialog.OnTimeSetListener() {

			@Override
			public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
				myCalendar.set(Calendar.HOUR, hourOfDay);
				myCalendar.set(Calendar.MINUTE, minute);
				setHora(false);
			}
		};

		bHora.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new TimePickerDialog(getActivity(), time,  myCalendar
						.get(Calendar.HOUR),  myCalendar
						.get(Calendar.MINUTE),false).show();
			}
		});
	}

	/**
	 * Funcion que crea dialog, listener y setea los valores de fecha
	 */
	private void inicializarFecha() {
		setFecha(true);

		final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear,
					int dayOfMonth) {
				myCalendar.set(Calendar.YEAR, year);
				myCalendar.set(Calendar.MONTH, monthOfYear);
				myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
				setFecha(false);
			}
		};

		bFecha.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new DatePickerDialog(getActivity(), date, myCalendar
						.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
						myCalendar.get(Calendar.DAY_OF_MONTH)).show();
			}
		});

	}


	/**
	 * Funcion que setea la hora, segun calendar.
	 * @param inicializar Indica si queremos obtener la hora actual
	 */
	private void setHora(boolean inicializar) {
		String myFormat = "hh:mm a";
		SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

		if(inicializar){
			myCalendar = Calendar.getInstance();
		}
		bHora.setText(sdf.format(myCalendar.getTime()));
	}

	/**
	 * Funcion que setea la fecha, segun calendar.
	 * @param inicializar Indica si queremos obtener la fecha actual
	 */
	private void setFecha(boolean inicializar) {
		String myFormat = "dd/MM/yyyy"; 
		SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

		if(inicializar){
			myCalendar = Calendar.getInstance();
		}
		bFecha.setText(sdf.format(myCalendar.getTime()));
	}

	/*
	 * @param id Id de la tarea
	 */
	public void onClickSalvar(String idTarea){
		Mensaje mToast = null;
		boolean error = false;
		int idEmpresa = 0;
		int idEmpleado = 0;

		//este try catch es para evitar errores de tipo de campo
		try{
			idEmpresa = Integer.parseInt(etHiddenIdEmpresa.getText().toString());
		}catch (Exception e) {}

		//este try catch es para evitar errores de tipo de campo
		try{
			idEmpleado = Integer.parseInt(etHiddenIdEmpleado.getText().toString());
		}catch (Exception e) {}

		String nombre = etNombre.getText().toString();
		String nombreEmpresa = etEmpresa.getText().toString();
		String nombreEmpleado = etEmpleado.getText().toString();
		String fecha = bFecha.getText().toString();
		String hora = bHora.getText().toString();
		String descripcion = etDescripcion.getText().toString();
		boolean finalizada = cbFinalizada.isChecked();
		String fechaFinalizacion = null;

		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm a",Locale.getDefault());
		
		//OJO: el formato de fecha, si queremos poder compararlos en el query, debe estar almacenado
		//exactamente como yyyy-MM-dd. De lo contrario nunca va hacer bien la comparacion. Nisiquiera
		//cambiando / por -.
		//Referencia: Time Strings en http://www.sqlite.org/lang_datefunc.html
		DateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd",Locale.getDefault());
		fecha = dateFormat2.format(myCalendar.getTime());

		if(finalizada){
			fechaFinalizacion = dateFormat.format(new Date());
		}

		/** Verificacion de errores **/
		//Si  el nombre de la empresa no es vacio..
		if(!nombreEmpresa.equals("") && nombreEmpresa!=null){
			if (idEmpresa==0){
				etEmpresa.setError(Mensaje.ERROR_EMPRESA_NO_VALIDA);
				error = true;
			}else{
				//Verificamos que el nombre de empresa ingresado se corresponda con alguno de la BD.

				EmpresaSqliteDao empresaDao = new EmpresaSqliteDao();
				Empresa empresa = empresaDao.buscarEmpresa(getActivity(), ""+idEmpresa);

				if(empresa==null || (!nombreEmpresa.equals(empresa.getNombre()) && 
						empresa.getNombre() !=null)){
					etEmpresa.setError(Mensaje.ERROR_EMPRESA_NO_VALIDA);
					error = true;
				}
			}
		}

		//Si  el nombre de la empresa no es vacio..
		if(!nombreEmpleado.equals("") && nombreEmpleado!=null){

			if (idEmpleado==0){
				etEmpleado.setError(Mensaje.ERROR_EMPLEADO_NO_VALIDO);
				error = true;
			}
		}

		if(nombre.equals("") || nombre==null){
			etNombre.setError(Mensaje.ERROR_CAMPO_VACIO);
			error = true;
		}

		/*	if(nombreEmpresa.equals("") || nombreEmpresa==null){
			etEmpresa.setError(Mensaje.ERROR_CAMPO_VACIO);
			error = true;
		}

		if(nombreEmpleado.equals("") || nombreEmpleado==null){
			etEmpleado.setError(Mensaje.ERROR_CAMPO_VACIO);
			error = true;
		}

		if(descripcion.equals("") || descripcion==null){
			etDescripcion.setError(Mensaje.ERROR_CAMPO_VACIO);
			error = true;
		}*/


		/** FIN  Verificacion de errores **/

		if(!error){

			TareaSqliteDao tareaoDao = new TareaSqliteDao();

			SharedPreferences prefs = this.getActivity().getSharedPreferences("Usuario",Context.MODE_PRIVATE);
			int idUsuario = prefs.getInt(Usuario.ID, 0);

			//Estamos modificando un registro..
			if(idTarea!=null){

				String fechaModificacion = dateFormat.format(new Date());
				Tarea tarea = new Tarea(Integer.parseInt(idTarea), nombre, fecha, hora, descripcion, idUsuario, idEmpresa, idEmpleado,fechaFinalizacion, fechaModificacion);

				Boolean modificado = tareaoDao.modificarTarea(getActivity(), tarea);

				if(modificado){
					mToast = new Mensaje(getActivity().getLayoutInflater(), getActivity(), "ok_modificar_tarea");

				}else{
					mToast = new Mensaje(getActivity().getLayoutInflater(), getActivity(), "error_modificar_tarea");
				}

				//Estamos creando un nuevo registro..
			}else{

				Tarea tarea = new Tarea(nombre, fecha, hora, descripcion, idUsuario, idUsuario, idEmpresa, idEmpleado, fechaFinalizacion);
				long idFilaInsertada = tareaoDao.insertarTarea(getActivity(), tarea);

				if(idFilaInsertada != -1){
					mToast = new Mensaje(getActivity().getLayoutInflater(), getActivity(), "ok_ingreso_tarea");
					mArgumentos = new Bundle();
					mArgumentos.putString("idTarea", ""+idFilaInsertada);
					flagGuardado = true;

				}else{
					mToast = new Mensaje(getActivity().getLayoutInflater(), getActivity(), "error_ingreso_tarea");
					flagGuardado = false;
				}

				idTarea = ""+idFilaInsertada;
			}


		}else{
			mToast = new Mensaje(getActivity().getLayoutInflater(), getActivity(), "error_formulario");
		}

		try {
			mToast.controlMensajesToast();
		} catch (Exception e) {
			e.printStackTrace();
		}

		if(finalizada){
			//creamos un historico de la tarea
			Historico historico = new Historico("tarea", 0 , Integer.parseInt(idTarea), 0);
			HistoricoSqliteDao historicoDao = new HistoricoSqliteDao();
			historicoDao.insertarHistorico(getActivity(), historico);

			//y lo enviamos a la pagina de historicos
			HistoricosActivity fragment = new HistoricosActivity();

			getFragmentManager().beginTransaction()
			.replace(android.R.id.tabcontent,fragment, AplicacionActivity.tagFragmentHistoricos)
			.addToBackStack(null)
			.commit();
		}
	}
}
