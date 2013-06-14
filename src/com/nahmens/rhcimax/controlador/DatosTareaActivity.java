package com.nahmens.rhcimax.controlador;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.regex.Pattern;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Patterns;
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
import com.nahmens.rhcimax.database.modelo.Tarea;
import com.nahmens.rhcimax.database.modelo.Usuario;
import com.nahmens.rhcimax.database.sqliteDAO.EmpleadoSqliteDao;
import com.nahmens.rhcimax.database.sqliteDAO.EmpresaSqliteDao;
import com.nahmens.rhcimax.database.sqliteDAO.TareaSqliteDao;
import com.nahmens.rhcimax.mensaje.Mensaje;
import com.nahmens.rhcimax.utils.InstantAutoComplete;

public class DatosTareaActivity extends Fragment {

	Calendar myCalendar = Calendar.getInstance();
	
	EditText etNombre;
	AutoCompleteTextView etEmpresa;
	EditText etHiddenIdEmpresa;
	InstantAutoComplete etEmpleado;
	EditText etHiddenIdEmpleado;
	Button bFecha;
	Button bHora;
	EditText etDescripcion;
	CheckBox cbFinalizada;

	@Override
	public View onCreateView(final LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		final View view = inflater.inflate(R.layout.activity_datos_tarea, container, false);
		
		//inicializamos la referencia a los campos del formulario
		setReferenciaCampos(view);

		final Bundle mArgumentos = this.getArguments();

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

		//Creacion de dialog y listener del campo fecha
		inicializarFecha();

		//Creacion de dialog y listener del campo hora
		inicializarHora();

		// Registro del evento OnClick del buttonVerEmpresa
		ImageButton bVerEmpresa = (ImageButton)view.findViewById(R.id.imageButtonVerEmpresa);
		bVerEmpresa.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				setOnclickVerEmpresa(inflater);
			}
		});

		// Registro del evento OnClick del buttonVerEmpleado
		ImageButton bVerEmpleado = (ImageButton)view.findViewById(R.id.imageButtonVerEmpleado);
		bVerEmpleado.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				setOnclickVerEmpleado(inflater);
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
		etNombre = (EditText) view.findViewById(R.id.editTextTarea);
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
	 * @param inflater
	 */
	protected void setOnclickVerEmpleado(LayoutInflater inflater) {
		String idEmpleado = etHiddenIdEmpleado.getText().toString();

		if(idEmpleado.equals("") || idEmpleado.equals(null)){
			Mensaje mToast = new Mensaje(inflater, getActivity(), "error_empleado_no_existe");

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
	 * @param inflater
	 */
	protected void setOnclickVerEmpresa(LayoutInflater inflater) {
		String idEmpresa = etHiddenIdEmpresa.getText().toString();

		if(idEmpresa.equals("") || idEmpresa.equals(null)){
			Mensaje mToast = new Mensaje(inflater, getActivity(), "error_empresa_no_existe");

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

		if(tarea.getFinalizada() == 1){
			finalizado = false;
		}

		etNombre.setText(tarea.getNombre());
		etEmpresa.setText(empresa.getNombre());
		etHiddenIdEmpresa.setText(tarea.getIdEmpresa());
		etEmpleado.setText(empleado.getNombre()+" " + empleado.getApellido());
		etHiddenIdEmpleado.setText(tarea.getIdEmpleado());
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
				// TODO Auto-generated method stub
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
		String myFormat = "dd/MM/yy"; //In which you need put here
		SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

		if(inicializar){
			myCalendar = Calendar.getInstance();
		}
		bFecha.setText(sdf.format(myCalendar.getTime()));
	}

	/*
	 * @param id Id del empleado
	 */
	public void onClickSalvar(String id){
		/*Mensaje mToast = null;
		boolean error = false;

		EditText etIdEmpresa = (EditText) getActivity().findViewById(R.id.textEditHiddenIdEmpresa);
		EditText etNombre = (EditText) getActivity().findViewById(R.id.textEditNombEmpleado);
		EditText etApellido = (EditText) getActivity().findViewById(R.id.textEditApellidoEmpleado);
		EditText etPosicion = (EditText) getActivity().findViewById(R.id.textEditPosEmpleado);
		EditText etEmail = (EditText) getActivity().findViewById(R.id.textEditEmailEmpleado);
		EditText etTelfOficina = (EditText) getActivity().findViewById(R.id.textEditTelfOficEmpleado);
		EditText etCelular = (EditText) getActivity().findViewById(R.id.textEditCelularEmpleado);
		EditText etPin = (EditText) getActivity().findViewById(R.id.textEditPinEmpleado);
		EditText etLinkedin = (EditText) getActivity().findViewById(R.id.textEditLinkedinEmpleado);
		EditText etDescripcion = (EditText) getActivity().findViewById(R.id.textEditDescripEmpleado);
		AutoCompleteTextView acNombreEmpresa = (AutoCompleteTextView) getActivity().findViewById(R.id.autocompleteEmpresa);

		int idEmpresa = 0;

		//este try catch es para evitar errores de tipo de campo
		try{
			idEmpresa = Integer.parseInt(etIdEmpresa.getText().toString());
		}catch (Exception e) {}

		String nombre = etNombre.getText().toString();
		String apellido = etApellido.getText().toString();
		String posicion = etPosicion.getText().toString();
		String email = etEmail.getText().toString();
		String telfOficina = etTelfOficina.getText().toString();
		String celular = etCelular.getText().toString();
		String linkedin = etLinkedin.getText().toString();
		String pin = etPin.getText().toString();
		String descripcion = etDescripcion.getText().toString();
		String nombreEmpresa = acNombreEmpresa.getText().toString();

		/** Verificacion de errores **/
		/*if(nombre.equals("") || nombre==null){
			etNombre.setError(Mensaje.ERROR_CAMPO_VACIO);
			error = true;
		}

		if(apellido.equals("") || apellido==null){
			etApellido.setError(Mensaje.ERROR_CAMPO_VACIO);
			error = true;
		}

		if(nombreEmpresa.equals("") || nombreEmpresa==null){
			acNombreEmpresa.setError(Mensaje.ERROR_CAMPO_VACIO);
			error = true;
		}

		if(email.equals("") || email==null){
			etEmail.setError(Mensaje.ERROR_CAMPO_VACIO);
			error = true;
		}

		Pattern pattern = Patterns.EMAIL_ADDRESS;
		if(!pattern.matcher(email).matches()){
			etEmail.setError(Mensaje.ERROR_EMAIL_INVALIDO);
			error = true;
		}

		/*pattern = Patterns.PHONE;
	    if(!pattern.matcher(celular).matches()){
	    	etCelular.setError(Mensaje.ERROR_TELF_INVALIDO);
			error = true;
	    }

	    if(!pattern.matcher(telfOficina).matches()){
	    	etCelular.setError(Mensaje.ERROR_TELF_INVALIDO);
			error = true;
	    }*/

		/*if (idEmpresa==0){
			acNombreEmpresa.setError(Mensaje.ERROR_EMPRESA_NO_VALIDA);
			error = true;
		}else{
			//Verificamos que el nombre de empresa ingresado se corresponda con alguno de la BD.

			EmpresaSqliteDao empresaDao = new EmpresaSqliteDao();
			Empresa empresa = empresaDao.buscarEmpresa(getActivity(), String.valueOf(idEmpresa));

			if(!nombreEmpresa.equals(empresa.getNombre()) && empresa.getNombre() !=null){
				acNombreEmpresa.setError(Mensaje.ERROR_EMPRESA_NO_VALIDA);
				error = true;
			}
		}

		/** FIN  Verificacion de errores **/

		/*if(!error){

			EmpleadoSqliteDao empleadoDao = new EmpleadoSqliteDao();

			SharedPreferences prefs = this.getActivity().getSharedPreferences("Usuario",Context.MODE_PRIVATE);
			int idUsuario = prefs.getInt(Usuario.ID, 0);

			if(id!=null){

				//cada vez que hagamos una modificacion, colocamos el valor de fechaSincronizacion en null 
				//para saber que este empleado esta desactualizado en la nube
				String fechaSincronizacion = null;
				Empleado empleado = new Empleado( Integer.parseInt(id),nombre, apellido, posicion, email, telfOficina, celular, pin, linkedin, descripcion, idEmpresa, idUsuario, fechaSincronizacion);

				//Estamos modificando un registro
				Boolean modificado = empleadoDao.modificarEmpleado(getActivity(), empleado);

				if(modificado){
					mToast = new Mensaje(inflater, getActivity(), "ok_modificar_empleado");

				}else{
					mToast = new Mensaje(inflater, getActivity(), "error_modificar_empleado");
				}

			}else{
				//Estamos creando un nuevo registro

				//cada vez que hagamos una modificacion, colocamos el valor de fechaSincronizacion en null 
				//para saber que este empleado esta desactualizado en la nube
				String fechaSincronizacion = null;

				Empleado empleado = new Empleado(nombre, apellido, posicion, email, telfOficina, celular, pin, linkedin, descripcion, idEmpresa, idUsuario, fechaSincronizacion);
				Boolean insertado = empleadoDao.insertarEmpleado(getActivity(), empleado, idUsuario);

				if(insertado){
					mToast = new Mensaje(inflater, getActivity(), "ok_ingreso_empleado");

				}else{
					mToast = new Mensaje(inflater, getActivity(), "error_ingreso_empleado");
				}
			}


		}else{
			mToast = new Mensaje(inflater, getActivity(), "error_formulario");
		}

		try {
			mToast.controlMensajesToast();
		} catch (Exception e) {
			e.printStackTrace();
		}*/

	}
}
